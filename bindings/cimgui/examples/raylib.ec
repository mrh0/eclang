// Dear ImGui in a raylib window: raylib gives the input and draws ImGui's triangles (through rlgl, raylib's
// OpenGL layer), and the user interface changes a scene raylib draws. Also shows ImGui's demo window.
//   java -jar target/eclang-0.1.0.jar run bindings/cimgui/examples/raylib.ec
// Needs raylib 5.5 and cimgui (see ../README.md); rlgl.h comes with raylib.
use "../cimgui.ec"
use "../../raylib/raylib.ec"
use "rlgl.h"

// The few functions of rlgl.h a renderer needs: raylib's immediate mode, with a texture per batch.
declare val RL_TRIANGLES as Int
declare fn rlBegin as (mode: Int)
declare fn rlEnd as ()
declare fn rlVertex2f as (x: Float, y: Float)
declare fn rlTexCoord2f as (x: Float, y: Float)
declare fn rlColor4ub as (r: UChar, g: UChar, b: UChar, a: UChar)
declare fn rlSetTexture as (id: UInt)
declare fn rlDrawRenderBatchActive as ()
declare fn rlCheckRenderBatchLimit as (vCount: Int): Bool
declare fn rlEnableBackfaceCulling as ()
declare fn rlDisableBackfaceCulling as ()

/** What the user interface changes. */
mut rec Scene as
    radius: Float = 40f
    speed: Float = 200f
    color: Float[3] = [0.9f, 0.3f, 0.2f]
    trail: Bool = true
    paused: Bool = false
    demo: Bool = false
    x: Float = 200f
    y: Float = 150f
    dx: Float = 1f
    dy: Float = 0.7f

// ---------------------------------------------------------------------------------------------------------------------
// The platform side: raylib's input to ImGui
// ---------------------------------------------------------------------------------------------------------------------

fn key(inputs: @ImGuiIO, k: ImGuiKey, down: Bool) = ImGuiIO_AddKeyEvent(inputs, k, down)

/** Gives ImGui this frame's input: mouse, keys (those ImGui's widgets use) and typed characters. */
fn feedInput(inputs: @ImGuiIO) do
    inputs.DisplaySize = ImVec2 { GetScreenWidth() as Float, GetScreenHeight() as Float }
    val dt = GetFrameTime()
    inputs.DeltaTime = if dt > 0f do dt else 1f / 60f
    val mouse = GetMousePosition()
    ImGuiIO_AddMousePosEvent(inputs, mouse.x, mouse.y)
    ImGuiIO_AddMouseButtonEvent(inputs, ImGuiMouseButton_Left, IsMouseButtonDown(:left))
    ImGuiIO_AddMouseButtonEvent(inputs, ImGuiMouseButton_Right, IsMouseButtonDown(:right))
    ImGuiIO_AddMouseButtonEvent(inputs, ImGuiMouseButton_Middle, IsMouseButtonDown(:middle))
    val wheel = GetMouseWheelMoveV()
    ImGuiIO_AddMouseWheelEvent(inputs, wheel.x, wheel.y)

    key(inputs, ImGuiMod_Ctrl, IsKeyDown(:left_control) or IsKeyDown(:right_control))
    key(inputs, ImGuiMod_Shift, IsKeyDown(:left_shift) or IsKeyDown(:right_shift))
    key(inputs, ImGuiMod_Alt, IsKeyDown(:left_alt) or IsKeyDown(:right_alt))
    key(inputs, ImGuiMod_Super, IsKeyDown(:left_super) or IsKeyDown(:right_super))
    key(inputs, ImGuiKey_Tab, IsKeyDown(:tab))
    key(inputs, ImGuiKey_LeftArrow, IsKeyDown(:left))
    key(inputs, ImGuiKey_RightArrow, IsKeyDown(:right))
    key(inputs, ImGuiKey_UpArrow, IsKeyDown(:up))
    key(inputs, ImGuiKey_DownArrow, IsKeyDown(:down))
    key(inputs, ImGuiKey_PageUp, IsKeyDown(:page_up))
    key(inputs, ImGuiKey_PageDown, IsKeyDown(:page_down))
    key(inputs, ImGuiKey_Home, IsKeyDown(:home))
    key(inputs, ImGuiKey_End, IsKeyDown(:end))
    key(inputs, ImGuiKey_Insert, IsKeyDown(:insert))
    key(inputs, ImGuiKey_Delete, IsKeyDown(:delete))
    key(inputs, ImGuiKey_Backspace, IsKeyDown(:backspace))
    key(inputs, ImGuiKey_Space, IsKeyDown(:space))
    key(inputs, ImGuiKey_Enter, IsKeyDown(:enter))
    key(inputs, ImGuiKey_KeypadEnter, IsKeyDown(:kp_enter))
    key(inputs, ImGuiKey_Escape, IsKeyDown(:escape))
    key(inputs, ImGuiKey_A, IsKeyDown(:a))
    key(inputs, ImGuiKey_C, IsKeyDown(:c))
    key(inputs, ImGuiKey_V, IsKeyDown(:v))
    key(inputs, ImGuiKey_X, IsKeyDown(:x))
    key(inputs, ImGuiKey_Y, IsKeyDown(:y))
    key(inputs, ImGuiKey_Z, IsKeyDown(:z))
    while true do
        val c = GetCharPressed()
        break if c == 0
        ImGuiIO_AddInputCharacter(inputs, c as UInt)

// ---------------------------------------------------------------------------------------------------------------------
// The renderer side: ImGui's textures and triangles, drawn by raylib
// ---------------------------------------------------------------------------------------------------------------------

/** The raylib texture ImGui knows by its TexID (raylib's texture id). */
fn textureOf(tex: @ImTextureData): Texture2D = Texture { tex.TexID as UInt, tex.Width, tex.Height, 1, PIXELFORMAT_UNCOMPRESSED_R8G8B8A8 }

/** Makes, updates and frees the textures ImGui asks for (its font atlas), as raylib textures. */
fn updateTexture(tex: @ImTextureData) do
    if tex.Status == ImTextureStatus_WantCreate do
        val pixels = ImTextureData_GetPixels(tex) ?? ret
        val image = Image { pixels, tex.Width, tex.Height, 1, PIXELFORMAT_UNCOMPRESSED_R8G8B8A8 }
        val texture = LoadTextureFromImage(image)
        ImTextureData_SetTexID(tex, texture.id as ImTextureID)
        ImTextureData_SetStatus(tex, ImTextureStatus_OK)
    else if tex.Status == ImTextureStatus_WantUpdates do
        // The whole texture again; a renderer may copy only tex.Updates, the rectangles that changed.
        if val pixels = ImTextureData_GetPixels(tex) do
            UpdateTexture(textureOf(tex), pixels)
        ImTextureData_SetStatus(tex, ImTextureStatus_OK)
    else if tex.Status == ImTextureStatus_WantDestroy and tex.UnusedFrames > 0 do
        UnloadTexture(textureOf(tex))
        ImTextureData_SetTexID(tex, 0)
        ImTextureData_SetStatus(tex, ImTextureStatus_Destroyed)

fn updateTextures(textures: @ImVector_ImTextureDataPtr) do
    val list = textures.Data ?? ret
    for i in 0..<textures.Size do
        updateTexture(unsafe list[i])

/** One vertex of ImGui's: its color (ImU32 is 0xAABBGGRR), texture coordinate and position. */
fn vertex(v: ImDrawVert) do
    rlColor4ub((v.col & 0xff) as UChar, (v.col >> 8 & 0xff) as UChar, (v.col >> 16 & 0xff) as UChar, (v.col >> 24) as UChar)
    rlTexCoord2f(v.uv.x, v.uv.y)
    rlVertex2f(v.pos.x, v.pos.y)

/** Draws one command: its triangles, with its texture, clipped to its rectangle. */
fn drawCommand(data: @ImDrawData, cmd: @ImDrawCmd, vertices: ImDrawVert[]c, indices: ImDrawIdx[]c) do
    if cmd.UserCallback != null do
        ret     // A callback the program set with ImDrawList_AddCallback: this one sets none.
    val clip = cmd.ClipRect
    val x = clip.x - data.DisplayPos.x
    val y = clip.y - data.DisplayPos.y
    BeginScissorMode(x as Int, y as Int, (clip.z - clip.x) as Int, (clip.w - clip.y) as Int)
    val texture = ImDrawCmd_GetTexID(cmd) as UInt
    rlBegin(RL_TRIANGLES)
    rlSetTexture(texture)
    val first = cmd.IdxOffset as Int
    val base = cmd.VtxOffset as Int
    for i in 0..<(cmd.ElemCount / 3) as Int do
        // Starts a new batch when the current one is full.
        if rlCheckRenderBatchLimit(3) do
            rlBegin(RL_TRIANGLES)
            rlSetTexture(texture)
        for k in 0..<3 do
            val index = unsafe indices[first + i * 3 + k]
            vertex(unsafe vertices[base + index as Int])
    rlEnd()
    rlDrawRenderBatchActive()
    EndScissorMode()

/** Draws what igRender made, over what raylib drew this frame. */
fn renderImGui(data: @ImDrawData) do
    if val textures = data.Textures do
        updateTextures(textures)
    rlDrawRenderBatchActive()
    rlDisableBackfaceCulling()
    if val lists = data.CmdLists.Data do
        for n in 0..<data.CmdLists.Size do
            val list = unsafe lists[n]
            val vertices = list.VtxBuffer.Data ?? continue
            val indices = list.IdxBuffer.Data ?? continue
            val commands = list.CmdBuffer.Data ?? continue
            for c in 0..<list.CmdBuffer.Size do
                drawCommand(data, unsafe addrof commands[c], vertices, indices)
    // Back to raylib's state: a new batch, on its default texture (rlSetTexture(0) isn't enough for the shapes after it).
    rlDrawRenderBatchActive()
    rlEnableBackfaceCulling()

// ---------------------------------------------------------------------------------------------------------------------
// The program
// ---------------------------------------------------------------------------------------------------------------------

fn controls(mut s: Scene) do
    igSetNextWindowPos(ImVec2 { 16f, 16f }, ImGuiCond_FirstUseEver)
    igSetNextWindowSize(ImVec2 { 300f, 0f }, ImGuiCond_FirstUseEver)
    if igBegin("Ball"c) do
        igText("%d FPS"c, GetFPS())
        igSliderFloat("Radius"c, mut s.radius, 5f, 120f, "%.0f px"c)
        igSliderFloat("Speed"c, mut s.speed, 0f, 800f, "%.0f px/s"c)
        igColorEdit3("Color"c, s.color.data)
        igCheckbox("Trail"c, mut s.trail)
        igCheckbox("Paused"c, mut s.paused)
        if igButton("Center"c) do
            s.x = GetScreenWidth() as Float / 2f
            s.y = GetScreenHeight() as Float / 2f
        igSameLine()
        igCheckbox("Demo window"c, mut s.demo)
    igEnd()
    if s.demo do
        igShowDemoWindow(addrof s.demo)

fn move(mut s: Scene, dt: Float) do
    if s.paused do
        ret
    s.x += s.dx * s.speed * dt
    s.y += s.dy * s.speed * dt
    val w = GetScreenWidth() as Float
    val h = GetScreenHeight() as Float
    if s.x < s.radius or s.x > w - s.radius do
        s.dx = -s.dx
        s.x = if s.x < s.radius do s.radius else w - s.radius
    if s.y < s.radius or s.y > h - s.radius do
        s.dy = -s.dy
        s.y = if s.y < s.radius do s.radius else h - s.radius

fn draw(s: Scene) do
    val color = ColorFromNormalized(Vector4 { s.color[0], s.color[1], s.color[2], 1f })
    if s.trail do
        DrawCircleV(Vector2 { s.x - s.dx * s.radius, s.y - s.dy * s.radius }, s.radius * 0.6f, Color { color.r, color.g, color.b, 80 })
    DrawCircleV(Vector2 { s.x, s.y }, s.radius, color)
    DrawText("raylib draws the ball; Dear ImGui the window"c, 16, GetScreenHeight() - 30, 20, DARKGRAY)

fn main() do
    SetConfigFlags(FLAG_WINDOW_RESIZABLE | FLAG_VSYNC_HINT | FLAG_MSAA_4X_HINT)
    InitWindow(1100, 700, "eclang + Dear ImGui + raylib"c)
    defer CloseWindow()
    SetTargetFPS(60)
    SetExitKey(0)       // Escape is for ImGui (closing popups); close the window to quit.

    val ctx = igCreateContext()
    val inputs = igGetIO()
    inputs.IniFilename = null
    inputs.BackendFlags = inputs.BackendFlags | ImGuiBackendFlags_RendererHasTextures
    igStyleColorsDark()

    var scene = Scene {}
    while not WindowShouldClose() do
        feedInput(inputs)
        igNewFrame()
        controls(mut scene)
        igRender()
        // Clicks and keys ImGui uses aren't the scene's.
        move(mut scene, GetFrameTime())

        BeginDrawing()
        ClearBackground(RAYWHITE)
        draw(scene)
        if val data = igGetDrawData() do
            renderImGui(data)
        EndDrawing()

    // Free ImGui's textures before the window (and its OpenGL context) closes.
    val platform = igGetPlatformIO()
    if val list = platform.Textures.Data do
        for i in 0..<platform.Textures.Size do
            val tex = unsafe list[i]
            if tex.TexID != 0 do
                UnloadTexture(textureOf(tex))
    igDestroyContext(ctx)
