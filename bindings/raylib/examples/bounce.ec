// A ball bouncing around the window. Space pauses, the left mouse button moves the ball, R changes its color.
//   java -jar target/eclang-0.1.0.jar run bindings/raylib/examples/bounce.ec
use "../raylib.ec"

val colors = [MAROON, ORANGE, DARKGREEN, DARKBLUE, PURPLE]

fn main() do
    SetConfigFlags(FLAG_VSYNC_HINT | FLAG_WINDOW_RESIZABLE)
    InitWindow(800, 450, "eclang + raylib: bounce"c)
    defer CloseWindow()
    SetTargetFPS(60)

    var position = Vector2 { 400f, 225f }
    var speed = Vector2 { 300f, 240f }
    val radius = 20f
    var paused = false
    var color = 0
    var bounces = 0

    while not WindowShouldClose() do
        if IsKeyPressed(:space) do
            paused = not paused
        if IsKeyPressed(:r) do
            color = (color + 1) % colors.len as Int
        if IsMouseButtonDown(:left) do
            position = GetMousePosition()

        if not paused do
            val dt = GetFrameTime()
            // Raylib's vectors are values: a change makes a new one.
            position = { ...position, x = position.x + speed.x * dt, y = position.y + speed.y * dt }
            val width = GetScreenWidth() as Float
            val height = GetScreenHeight() as Float
            if position.x >= width - radius or position.x <= radius do
                speed = { ...speed, x = -speed.x }
                bounces += 1
            if position.y >= height - radius or position.y <= radius do
                speed = { ...speed, y = -speed.y }
                bounces += 1

        BeginDrawing()
        ClearBackground(RAYWHITE)
        DrawCircleV(position, radius, colors[color])
        // Each frame's text is allocated in a pool of its own, freed at the end of the frame.
        pool do
            DrawText(cstr("bounces: ${bounces}"), 10, 10, 20, DARKGRAY)
        DrawText("space: pause, mouse: move, R: color"c, 10, 40, 10, GRAY)
        if paused do
            DrawText("PAUSED"c, 350, 200, 30, GRAY)
        DrawFPS(GetScreenWidth() - 90, 10)
        EndDrawing()
