// A 3D scene with a camera orbiting a cube; the mouse wheel zooms. Clicking the cube changes its color.
//   java -jar target/eclang-0.1.0.jar run bindings/raylib/examples/camera3d.ec
use "../raylib.ec"

fn main() do
    InitWindow(800, 450, "eclang + raylib: 3D camera"c)
    defer CloseWindow()
    SetTargetFPS(60)

    var camera = Camera {
        position = Vector3 { 10f, 10f, 10f },
        target = Vector3 { 0f, 0f, 0f },
        up = Vector3 { 0f, 1f, 0f },
        fovy = 45f,
        projection = CAMERA_PERSPECTIVE
    }
    val cubePosition = Vector3 { 0f, 1f, 0f }
    val cubeSize = Vector3 { 2f, 2f, 2f }
    var selected = false

    while not WindowShouldClose() do
        UpdateCamera(mut camera, :orbital)

        if IsMouseButtonPressed(:left) do
            val ray = GetScreenToWorldRay(GetMousePosition(), camera)
            val box = BoundingBox {
                min = Vector3 { cubePosition.x - 1f, cubePosition.y - 1f, cubePosition.z - 1f },
                max = Vector3 { cubePosition.x + 1f, cubePosition.y + 1f, cubePosition.z + 1f }
            }
            if GetRayCollisionBox(ray, box).hit do
                selected = not selected

        BeginDrawing()
        ClearBackground(RAYWHITE)
        BeginMode3D(camera)
        DrawCubeV(cubePosition, cubeSize, if selected do GOLD else RED)
        DrawCubeWiresV(cubePosition, cubeSize, MAROON)
        DrawGrid(10, 1f)
        EndMode3D()
        DrawText("click the cube"c, 10, 10, 20, DARKGRAY)
        EndDrawing()
