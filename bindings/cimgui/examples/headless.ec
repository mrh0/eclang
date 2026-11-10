// Dear ImGui without a window: a program gives ImGui its input (here made up: the mouse moves to a button and clicks
// it) and gets back what to draw, as triangles. A real program draws them with its renderer; raylib.ec shows one.
// It also shows ImGui's texture protocol, which a renderer must follow: ImGui asks for its font texture to be made.
//   java -jar target/eclang-0.1.0.jar run bindings/cimgui/examples/headless.ec
use "../cimgui.ec"
use "core:io.ec"

mut rec State as
    clicks: Int
    enabled: Bool
    speed: Float
    name: Char[32]
    /** Where the button and the checkbox were drawn, to aim the made-up mouse at. */
    button: ImVec2 = ImVec2 { 0f, 0f }
    checkbox: ImVec2 = ImVec2 { 0f, 0f }

/** The middle of the last widget drawn. */
fn lastItemCenter(): ImVec2 do
    val min = igGetItemRectMin()
    val max = igGetItemRectMax()
    ret ImVec2 { (min.x + max.x) / 2f, (min.y + max.y) / 2f }

/** One frame of the user interface: the same calls every frame, as ImGui is immediate-mode. */
fn ui(mut s: State) do
    igSetNextWindowPos(ImVec2 { 10f, 10f })
    igSetNextWindowSize(ImVec2 { 380f, 200f })
    if igBegin("Demo"c) do
        igText("Hello from %s, ImGui %s"c, "eclang"c, igGetVersion())
        if igButton("Click me"c) do
            s.clicks += 1
        s.button = lastItemCenter()
        igSameLine()
        igText("%d click(s)"c, s.clicks)
        igCheckbox("Enabled"c, mut s.enabled)
        s.checkbox = lastItemCenter()
        igSliderFloat("Speed"c, mut s.speed, 0f, 10f)
        igInputText("Name"c, s.name.data, s.name.len)
    // igEnd even when igBegin returns false (a collapsed window): unlike the other Begin functions.
    igEnd()

/**
 * What a renderer does with ImGui's textures after each frame: make those it asks for (WantCreate), update those
 * that changed (WantUpdates), and free those it no longer needs (WantDestroy). Here nothing is drawn, so making a
 * texture is giving it an ID.
 */
fn updateTextures(data: @ImDrawData, mut made: Int) do
    val textures = data.Textures ?? ret
    val list = textures.Data ?? ret
    for i in 0..<textures.Size do
        val tex = unsafe list[i]
        if tex.Status == ImTextureStatus_WantCreate do
            made += 1
            io.println("ImGui asks for a ${tex.Width}x${tex.Height} texture (${tex.BytesPerPixel} bytes a pixel)")
            ImTextureData_SetTexID(tex, made as ImTextureID)
            ImTextureData_SetStatus(tex, ImTextureStatus_OK)
        else if tex.Status == ImTextureStatus_WantUpdates do
            ImTextureData_SetStatus(tex, ImTextureStatus_OK)
        else if tex.Status == ImTextureStatus_WantDestroy and tex.UnusedFrames > 0 do
            ImTextureData_SetTexID(tex, 0)
            ImTextureData_SetStatus(tex, ImTextureStatus_Destroyed)

fn main() do
    val ctx = igCreateContext()
    defer igDestroyContext(ctx)
    val inputs = igGetIO()
    inputs.DisplaySize = ImVec2 { 400f, 300f }
    inputs.DeltaTime = 1f / 60f
    inputs.IniFilename = null       // no imgui.ini
    inputs.BackendFlags = inputs.BackendFlags | ImGuiBackendFlags_RendererHasTextures

    var s = State { 0, true, 2.5f, "" }
    var textures = 0
    // The made-up input of each frame: where the mouse is, and whether its left button is down.
    val steps = [(0, false), (1, false), (1, true), (1, false), (2, false), (2, true), (2, false), (0, false)]
    for step in steps do
        val target = if step.first == 1 do s.button else if step.first == 2 do s.checkbox else ImVec2 { 0f, 0f }
        ImGuiIO_AddMousePosEvent(inputs, target.x, target.y)
        ImGuiIO_AddMouseButtonEvent(inputs, ImGuiMouseButton_Left, step.second)
        igNewFrame()
        ui(mut s)
        igRender()
        val data = igGetDrawData() ?? continue
        updateTextures(data, mut textures)

    io.println("clicks: ${s.clicks}, enabled: ${s.enabled}, speed: ${s.speed:.1f}")
    val data = igGetDrawData()!
    io.println("the last frame: ${data.CmdLists.Size} draw list(s), with vertices: ${data.TotalVtxCount > 0}")
