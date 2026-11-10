/**
 * Bindings for raylib 5.5 (raylib.h), a library for making games: https://www.raylib.com
 *
 * Generated from raylib.h by bindings/raylib/generate.py; change that script, not this file. The names are
 * raylib's own, so its cheatsheet (https://www.raylib.com/cheatsheet/cheatsheet.html) and examples apply. See
 * bindings/raylib/README.md for how C's types map to eclang's.
 */
use "raylib.h"
use "lib:raylib"

// ---------------------------------------------------------------------------------------------------------------------
// Structures
// ---------------------------------------------------------------------------------------------------------------------

/** Opaque: defined inside raylib */
declare rec rAudioBuffer
/** Opaque: defined inside raylib */
declare rec rAudioProcessor

/** Vector2, 2 components */
declare rec Vector2 as
    /** Vector x component */
    x: Float
    /** Vector y component */
    y: Float

/** Vector3, 3 components */
declare rec Vector3 as
    /** Vector x component */
    x: Float
    /** Vector y component */
    y: Float
    /** Vector z component */
    z: Float

/** Vector4, 4 components */
declare rec Vector4 as
    /** Vector x component */
    x: Float
    /** Vector y component */
    y: Float
    /** Vector z component */
    z: Float
    /** Vector w component */
    w: Float
/** Matrix, 4x4 components, column major, OpenGL style, right-handed */
declare type Quaternion as Vector4

/** Matrix, 4x4 components, column major, OpenGL style, right-handed */
declare rec Matrix as
    /** Matrix first row (4 components) */
    m0: Float
    m4: Float
    m8: Float
    m12: Float
    /** Matrix second row (4 components) */
    m1: Float
    m5: Float
    m9: Float
    m13: Float
    /** Matrix third row (4 components) */
    m2: Float
    m6: Float
    m10: Float
    m14: Float
    /** Matrix fourth row (4 components) */
    m3: Float
    m7: Float
    m11: Float
    m15: Float

/** Color, 4 components, R8G8B8A8 (32bit) */
declare rec Color as
    /** Color red value */
    r: UChar
    /** Color green value */
    g: UChar
    /** Color blue value */
    b: UChar
    /** Color alpha value */
    a: UChar

/** Rectangle, 4 components */
declare rec Rectangle as
    /** Rectangle top-left corner position x */
    x: Float
    /** Rectangle top-left corner position y */
    y: Float
    /** Rectangle width */
    width: Float
    /** Rectangle height */
    height: Float

/** Image, pixel data stored in CPU memory (RAM) */
declare rec Image as
    /** Image raw data */
    data: @Any?
    /** Image base width */
    width: Int
    /** Image base height */
    height: Int
    /** Mipmap levels, 1 by default */
    mipmaps: Int
    /** Data format (PixelFormat type) */
    format: Int

/** Texture, tex data stored in GPU memory (VRAM) */
declare rec Texture as
    /** OpenGL texture id */
    id: UInt
    /** Texture base width */
    width: Int
    /** Texture base height */
    height: Int
    /** Mipmap levels, 1 by default */
    mipmaps: Int
    /** Data format (PixelFormat type) */
    format: Int
/** TextureCubemap, same as Texture */
declare type Texture2D as Texture
/** RenderTexture, fbo for texture rendering */
declare type TextureCubemap as Texture

/** RenderTexture, fbo for texture rendering */
declare rec RenderTexture as
    /** OpenGL framebuffer object id */
    id: UInt
    /** Color buffer attachment texture */
    texture: Texture
    /** Depth buffer attachment texture */
    depth: Texture
/** NPatchInfo, n-patch layout info */
declare type RenderTexture2D as RenderTexture

/** NPatchInfo, n-patch layout info */
declare rec NPatchInfo as
    /** Texture source rectangle */
    source: Rectangle
    /** Left border offset */
    left: Int
    /** Top border offset */
    top: Int
    /** Right border offset */
    right: Int
    /** Bottom border offset */
    bottom: Int
    /** Layout of the n-patch: 3x3, 1x3 or 3x1 */
    layout: Int

/** GlyphInfo, font characters glyphs info */
declare rec GlyphInfo as
    /** Character value (Unicode) */
    value: Int
    /** Character offset X when drawing */
    offsetX: Int
    /** Character offset Y when drawing */
    offsetY: Int
    /** Character advance position X */
    advanceX: Int
    /** Character image data */
    image: Image

/** Font, font texture and GlyphInfo array data */
declare rec Font as
    /** Base size (default chars height) */
    baseSize: Int
    /** Number of glyph characters */
    glyphCount: Int
    /** Padding around the glyph characters */
    glyphPadding: Int
    /** Texture atlas containing the glyphs */
    texture: Texture2D
    /** Rectangles in texture for the glyphs */
    recs: Rectangle[]c
    /** Glyphs info data */
    glyphs: GlyphInfo[]c

/** Camera, defines position/orientation in 3d space */
declare rec Camera3D as
    /** Camera position */
    position: Vector3
    /** Camera target it looks-at */
    target: Vector3
    /** Camera up vector (rotation over its axis) */
    up: Vector3
    /** Camera field-of-view aperture in Y (degrees) in perspective, used as near plane width in orthographic */
    fovy: Float
    /** Camera projection: CAMERA_PERSPECTIVE or CAMERA_ORTHOGRAPHIC */
    projection: Int
/** Camera type fallback, defaults to Camera3D */
declare type Camera as Camera3D

/** Camera2D, defines position/orientation in 2d space */
declare rec Camera2D as
    /** Camera offset (displacement from target) */
    offset: Vector2
    /** Camera target (rotation and zoom origin) */
    target: Vector2
    /** Camera rotation in degrees */
    rotation: Float
    /** Camera zoom (scaling), should be 1.0f by default */
    zoom: Float

/** Mesh, vertex data and vao/vbo */
declare rec Mesh as
    /** Number of vertices stored in arrays */
    vertexCount: Int
    /** Number of triangles stored (indexed or not) */
    triangleCount: Int
    /** Vertex position (XYZ - 3 components per vertex) (shader-location = 0) */
    vertices: Float[]c?
    /** Vertex texture coordinates (UV - 2 components per vertex) (shader-location = 1) */
    texcoords: Float[]c?
    /** Vertex texture second coordinates (UV - 2 components per vertex) (shader-location = 5) */
    texcoords2: Float[]c?
    /** Vertex normals (XYZ - 3 components per vertex) (shader-location = 2) */
    normals: Float[]c?
    /** Vertex tangents (XYZW - 4 components per vertex) (shader-location = 4) */
    tangents: Float[]c?
    /** Vertex colors (RGBA - 4 components per vertex) (shader-location = 3) */
    colors: UChar[]c?
    /** Vertex indices (in case vertex data comes indexed) */
    indices: UShort[]c?
    /** Animated vertex positions (after bones transformations) */
    animVertices: Float[]c?
    /** Animated normals (after bones transformations) */
    animNormals: Float[]c?
    /** Vertex bone ids, max 255 bone ids, up to 4 bones influence by vertex (skinning) (shader-location = 6) */
    boneIds: UChar[]c?
    /** Vertex bone weight, up to 4 bones influence by vertex (skinning) (shader-location = 7) */
    boneWeights: Float[]c?
    /** Bones animated transformation matrices */
    boneMatrices: Matrix[]c?
    /** Number of bones */
    boneCount: Int
    /** OpenGL Vertex Array Object id */
    vaoId: UInt
    /** OpenGL Vertex Buffer Objects id (default vertex data) */
    vboId: UInt[]c?

/** Shader */
declare rec Shader as
    /** Shader program id */
    id: UInt
    /** Shader locations array (RL_MAX_SHADER_LOCATIONS) */
    locs: Int[]c

/** MaterialMap */
declare rec MaterialMap as
    /** Material map texture */
    texture: Texture2D
    /** Material map color */
    color: Color
    /** Material map value */
    value: Float

/** Material, includes shader and maps */
declare rec Material as
    /** Material shader */
    shader: Shader
    /** Material maps array (MAX_MATERIAL_MAPS) */
    maps: MaterialMap[]c
    /** Material generic parameters (if required) */
    params: Float[4]

/** Transform, vertex transformation data */
declare rec Transform as
    /** Translation */
    translation: Vector3
    /** Rotation */
    rotation: Quaternion
    /** Scale */
    scale: Vector3

/** Bone, skeletal animation bone */
declare rec BoneInfo as
    /** Bone name */
    name: Char[32]
    /** Bone parent */
    parent: Int

/** Model, meshes, materials and animation data */
declare rec Model as
    /** Local transform matrix */
    transform: Matrix
    /** Number of meshes */
    meshCount: Int
    /** Number of materials */
    materialCount: Int
    /** Meshes array */
    meshes: Mesh[]c
    /** Materials array */
    materials: Material[]c
    /** Mesh material number */
    meshMaterial: Int[]c
    /** Number of bones */
    boneCount: Int
    /** Bones information (skeleton) */
    bones: BoneInfo[]c?
    /** Bones base transformation (pose) */
    bindPose: Transform[]c?

/** ModelAnimation */
declare rec ModelAnimation as
    /** Number of bones */
    boneCount: Int
    /** Number of animation frames */
    frameCount: Int
    /** Bones information (skeleton) */
    bones: BoneInfo[]c
    /** Poses array by frame */
    framePoses: Transform[]c[]c
    /** Animation name */
    name: Char[32]

/** Ray, ray for raycasting */
declare rec Ray as
    /** Ray position (origin) */
    position: Vector3
    /** Ray direction (normalized) */
    direction: Vector3

/** RayCollision, ray hit information */
declare rec RayCollision as
    /** Did the ray hit something? */
    hit: Bool
    /** Distance to the nearest hit */
    distance: Float
    /** Point of the nearest hit */
    point: Vector3
    /** Surface normal of hit */
    normal: Vector3

/** BoundingBox */
declare rec BoundingBox as
    /** Minimum vertex box-corner */
    min: Vector3
    /** Maximum vertex box-corner */
    max: Vector3

/** Wave, audio wave data */
declare rec Wave as
    /** Total number of frames (considering channels) */
    frameCount: UInt
    /** Frequency (samples per second) */
    sampleRate: UInt
    /** Bit depth (bits per sample): 8, 16, 32 (24 not supported) */
    sampleSize: UInt
    /** Number of channels (1-mono, 2-stereo, ...) */
    channels: UInt
    /** Buffer data pointer */
    data: @Any?

/** AudioStream, custom audio stream */
declare rec AudioStream as
    /** Pointer to internal data used by the audio system */
    buffer: @rAudioBuffer?
    /** Pointer to internal data processor, useful for audio effects */
    processor: @rAudioProcessor?
    /** Frequency (samples per second) */
    sampleRate: UInt
    /** Bit depth (bits per sample): 8, 16, 32 (24 not supported) */
    sampleSize: UInt
    /** Number of channels (1-mono, 2-stereo, ...) */
    channels: UInt

/** Sound */
declare rec Sound as
    /** Audio stream */
    stream: AudioStream
    /** Total number of frames (considering channels) */
    frameCount: UInt

/** Music, audio stream, anything longer than ~10 seconds should be streamed */
declare rec Music as
    /** Audio stream */
    stream: AudioStream
    /** Total number of frames (considering channels) */
    frameCount: UInt
    /** Music looping enable */
    looping: Bool
    /** Type of music context (audio filetype) */
    ctxType: Int
    /** Audio context data, depends on type */
    ctxData: @Any?

/** VrDeviceInfo, Head-Mounted-Display device parameters */
declare rec VrDeviceInfo as
    /** Horizontal resolution in pixels */
    hResolution: Int
    /** Vertical resolution in pixels */
    vResolution: Int
    /** Horizontal size in meters */
    hScreenSize: Float
    /** Vertical size in meters */
    vScreenSize: Float
    /** Distance between eye and display in meters */
    eyeToScreenDistance: Float
    /** Lens separation distance in meters */
    lensSeparationDistance: Float
    /** IPD (distance between pupils) in meters */
    interpupillaryDistance: Float
    /** Lens distortion constant parameters */
    lensDistortionValues: Float[4]
    /** Chromatic aberration correction parameters */
    chromaAbCorrection: Float[4]

/** VrStereoConfig, VR stereo rendering configuration for simulator */
declare rec VrStereoConfig as
    /** VR projection matrices (per eye) */
    projection: Matrix[2]
    /** VR view offset matrices (per eye) */
    viewOffset: Matrix[2]
    /** VR left lens center */
    leftLensCenter: Float[2]
    /** VR right lens center */
    rightLensCenter: Float[2]
    /** VR left screen center */
    leftScreenCenter: Float[2]
    /** VR right screen center */
    rightScreenCenter: Float[2]
    /** VR distortion scale */
    scale: Float[2]
    /** VR distortion scale in */
    scaleIn: Float[2]

/** File path list */
declare rec FilePathList as
    /** Filepaths max entries */
    capacity: UInt
    /** Filepaths entries count */
    count: UInt
    /** Filepaths entries */
    paths: CString[]c

/** Automation event */
declare rec AutomationEvent as
    /** Event frame */
    frame: UInt
    /** Event type (AutomationEventType) */
    type_ extern "type": UInt
    /** Event parameters (if required) */
    params: Int[4]

/** Automation event list */
declare rec AutomationEventList as
    /** Events max entries (MAX_AUTOMATION_EVENTS) */
    capacity: UInt
    /** Events entries count */
    count: UInt
    /** Events entries */
    events: AutomationEvent[]c

// Constants
declare val RAYLIB_VERSION_MAJOR as Int
declare val RAYLIB_VERSION_MINOR as Int
declare val RAYLIB_VERSION_PATCH as Int
declare val RAYLIB_VERSION as CString
declare val PI as Float
/** Multiply degrees by it to get radians */
declare val DEG2RAD as Float
/** Multiply radians by it to get degrees */
declare val RAD2DEG as Float

// Colors (raylib's palette)
/** Light Gray */
declare val LIGHTGRAY as Color
/** Gray */
declare val GRAY as Color
/** Dark Gray */
declare val DARKGRAY as Color
/** Yellow */
declare val YELLOW as Color
/** Gold */
declare val GOLD as Color
/** Orange */
declare val ORANGE as Color
/** Pink */
declare val PINK as Color
/** Red */
declare val RED as Color
/** Maroon */
declare val MAROON as Color
/** Green */
declare val GREEN as Color
/** Lime */
declare val LIME as Color
/** Dark Green */
declare val DARKGREEN as Color
/** Sky Blue */
declare val SKYBLUE as Color
/** Blue */
declare val BLUE as Color
/** Dark Blue */
declare val DARKBLUE as Color
/** Purple */
declare val PURPLE as Color
/** Violet */
declare val VIOLET as Color
/** Dark Purple */
declare val DARKPURPLE as Color
/** Beige */
declare val BEIGE as Color
/** Brown */
declare val BROWN as Color
/** Dark Brown */
declare val DARKBROWN as Color
/** White */
declare val WHITE as Color
/** Black */
declare val BLACK as Color
/** Blank (Transparent) */
declare val BLANK as Color
/** Magenta */
declare val MAGENTA as Color
/** My own White (raylib logo) */
declare val RAYWHITE as Color

// ---------------------------------------------------------------------------------------------------------------------
// Enumerators
//
// Functions take most enums as atoms: IsKeyDown(:space), SetTraceLogLevel(:warning), UpdateCamera(mut camera, :orbital).
// Flags, which combine with |, and values that structs store or index with, are numbers named as in C:
// SetConfigFlags(FLAG_VSYNC_HINT | FLAG_WINDOW_RESIZABLE), camera.projection = CAMERA_PERSPECTIVE.
// ---------------------------------------------------------------------------------------------------------------------

// ConfigFlags
/** Set to try enabling V-Sync on GPU */
declare val FLAG_VSYNC_HINT as UInt
/** Set to run program in fullscreen */
declare val FLAG_FULLSCREEN_MODE as UInt
/** Set to allow resizable window */
declare val FLAG_WINDOW_RESIZABLE as UInt
/** Set to disable window decoration (frame and buttons) */
declare val FLAG_WINDOW_UNDECORATED as UInt
/** Set to hide window */
declare val FLAG_WINDOW_HIDDEN as UInt
/** Set to minimize window (iconify) */
declare val FLAG_WINDOW_MINIMIZED as UInt
/** Set to maximize window (expanded to monitor) */
declare val FLAG_WINDOW_MAXIMIZED as UInt
/** Set to window non focused */
declare val FLAG_WINDOW_UNFOCUSED as UInt
/** Set to window always on top */
declare val FLAG_WINDOW_TOPMOST as UInt
/** Set to allow windows running while minimized */
declare val FLAG_WINDOW_ALWAYS_RUN as UInt
/** Set to allow transparent framebuffer */
declare val FLAG_WINDOW_TRANSPARENT as UInt
/** Set to support HighDPI */
declare val FLAG_WINDOW_HIGHDPI as UInt
/** Set to support mouse passthrough, only supported when FLAG_WINDOW_UNDECORATED */
declare val FLAG_WINDOW_MOUSE_PASSTHROUGH as UInt
/** Set to run program in borderless windowed mode */
declare val FLAG_BORDERLESS_WINDOWED_MODE as UInt
/** Set to try enabling MSAA 4X */
declare val FLAG_MSAA_4X_HINT as UInt
/** Set to try enabling interlaced video format (for V3D) */
declare val FLAG_INTERLACED_HINT as UInt

/** Trace log level */
declare enum TraceLogLevel as
    /** Display all logs */
    :all = LOG_ALL
    /** Trace logging, intended for internal use only */
    :trace = LOG_TRACE
    /** Debug logging, used for internal debugging, it should be disabled on release builds */
    :debug = LOG_DEBUG
    /** Info logging, used for program execution info */
    :info = LOG_INFO
    /** Warning logging, used on recoverable failures */
    :warning = LOG_WARNING
    /** Error logging, used on unrecoverable failures */
    :error = LOG_ERROR
    /** Fatal logging, used to abort program: exit(EXIT_FAILURE) */
    :fatal = LOG_FATAL
    /** Disable logging */
    :none = LOG_NONE

/** Keyboard keys (US keyboard layout) */
declare enum KeyboardKey as
    /** Key: NULL, used for no key pressed */
    :null = KEY_NULL
    /** Key: ' */
    :apostrophe = KEY_APOSTROPHE
    /** Key: , */
    :comma = KEY_COMMA
    /** Key: - */
    :minus = KEY_MINUS
    /** Key:  */
    :period = KEY_PERIOD
    /** Key: / */
    :slash = KEY_SLASH
    /** Key: 0 */
    :zero = KEY_ZERO
    /** Key: 1 */
    :one = KEY_ONE
    /** Key: 2 */
    :two = KEY_TWO
    /** Key: 3 */
    :three = KEY_THREE
    /** Key: 4 */
    :four = KEY_FOUR
    /** Key: 5 */
    :five = KEY_FIVE
    /** Key: 6 */
    :six = KEY_SIX
    /** Key: 7 */
    :seven = KEY_SEVEN
    /** Key: 8 */
    :eight = KEY_EIGHT
    /** Key: 9 */
    :nine = KEY_NINE
    /** Key: ; */
    :semicolon = KEY_SEMICOLON
    /** Key: = */
    :equal = KEY_EQUAL
    /** Key: A | a */
    :a = KEY_A
    /** Key: B | b */
    :b = KEY_B
    /** Key: C | c */
    :c = KEY_C
    /** Key: D | d */
    :d = KEY_D
    /** Key: E | e */
    :e = KEY_E
    /** Key: F | f */
    :f = KEY_F
    /** Key: G | g */
    :g = KEY_G
    /** Key: H | h */
    :h = KEY_H
    /** Key: I | i */
    :i = KEY_I
    /** Key: J | j */
    :j = KEY_J
    /** Key: K | k */
    :k = KEY_K
    /** Key: L | l */
    :l = KEY_L
    /** Key: M | m */
    :m = KEY_M
    /** Key: N | n */
    :n = KEY_N
    /** Key: O | o */
    :o = KEY_O
    /** Key: P | p */
    :p = KEY_P
    /** Key: Q | q */
    :q = KEY_Q
    /** Key: R | r */
    :r = KEY_R
    /** Key: S | s */
    :s = KEY_S
    /** Key: T | t */
    :t = KEY_T
    /** Key: U | u */
    :u = KEY_U
    /** Key: V | v */
    :v = KEY_V
    /** Key: W | w */
    :w = KEY_W
    /** Key: X | x */
    :x = KEY_X
    /** Key: Y | y */
    :y = KEY_Y
    /** Key: Z | z */
    :z = KEY_Z
    /** Key: [ */
    :left_bracket = KEY_LEFT_BRACKET
    /** Key: '\' */
    :backslash = KEY_BACKSLASH
    /** Key: ] */
    :right_bracket = KEY_RIGHT_BRACKET
    /** Key: ` */
    :grave = KEY_GRAVE
    /** Key: Space */
    :space = KEY_SPACE
    /** Key: Esc */
    :escape = KEY_ESCAPE
    /** Key: Enter */
    :enter = KEY_ENTER
    /** Key: Tab */
    :tab = KEY_TAB
    /** Key: Backspace */
    :backspace = KEY_BACKSPACE
    /** Key: Ins */
    :insert = KEY_INSERT
    /** Key: Del */
    :delete = KEY_DELETE
    /** Key: Cursor right */
    :right = KEY_RIGHT
    /** Key: Cursor left */
    :left = KEY_LEFT
    /** Key: Cursor down */
    :down = KEY_DOWN
    /** Key: Cursor up */
    :up = KEY_UP
    /** Key: Page up */
    :page_up = KEY_PAGE_UP
    /** Key: Page down */
    :page_down = KEY_PAGE_DOWN
    /** Key: Home */
    :home = KEY_HOME
    /** Key: End */
    :end = KEY_END
    /** Key: Caps lock */
    :caps_lock = KEY_CAPS_LOCK
    /** Key: Scroll down */
    :scroll_lock = KEY_SCROLL_LOCK
    /** Key: Num lock */
    :num_lock = KEY_NUM_LOCK
    /** Key: Print screen */
    :print_screen = KEY_PRINT_SCREEN
    /** Key: Pause */
    :pause = KEY_PAUSE
    /** Key: F1 */
    :f1 = KEY_F1
    /** Key: F2 */
    :f2 = KEY_F2
    /** Key: F3 */
    :f3 = KEY_F3
    /** Key: F4 */
    :f4 = KEY_F4
    /** Key: F5 */
    :f5 = KEY_F5
    /** Key: F6 */
    :f6 = KEY_F6
    /** Key: F7 */
    :f7 = KEY_F7
    /** Key: F8 */
    :f8 = KEY_F8
    /** Key: F9 */
    :f9 = KEY_F9
    /** Key: F10 */
    :f10 = KEY_F10
    /** Key: F11 */
    :f11 = KEY_F11
    /** Key: F12 */
    :f12 = KEY_F12
    /** Key: Shift left */
    :left_shift = KEY_LEFT_SHIFT
    /** Key: Control left */
    :left_control = KEY_LEFT_CONTROL
    /** Key: Alt left */
    :left_alt = KEY_LEFT_ALT
    /** Key: Super left */
    :left_super = KEY_LEFT_SUPER
    /** Key: Shift right */
    :right_shift = KEY_RIGHT_SHIFT
    /** Key: Control right */
    :right_control = KEY_RIGHT_CONTROL
    /** Key: Alt right */
    :right_alt = KEY_RIGHT_ALT
    /** Key: Super right */
    :right_super = KEY_RIGHT_SUPER
    /** Key: KB menu */
    :kb_menu = KEY_KB_MENU
    /** Key: Keypad 0 */
    :kp_0 = KEY_KP_0
    /** Key: Keypad 1 */
    :kp_1 = KEY_KP_1
    /** Key: Keypad 2 */
    :kp_2 = KEY_KP_2
    /** Key: Keypad 3 */
    :kp_3 = KEY_KP_3
    /** Key: Keypad 4 */
    :kp_4 = KEY_KP_4
    /** Key: Keypad 5 */
    :kp_5 = KEY_KP_5
    /** Key: Keypad 6 */
    :kp_6 = KEY_KP_6
    /** Key: Keypad 7 */
    :kp_7 = KEY_KP_7
    /** Key: Keypad 8 */
    :kp_8 = KEY_KP_8
    /** Key: Keypad 9 */
    :kp_9 = KEY_KP_9
    /** Key: Keypad  */
    :kp_decimal = KEY_KP_DECIMAL
    /** Key: Keypad / */
    :kp_divide = KEY_KP_DIVIDE
    /** Key: Keypad * */
    :kp_multiply = KEY_KP_MULTIPLY
    /** Key: Keypad - */
    :kp_subtract = KEY_KP_SUBTRACT
    /** Key: Keypad + */
    :kp_add = KEY_KP_ADD
    /** Key: Keypad Enter */
    :kp_enter = KEY_KP_ENTER
    /** Key: Keypad = */
    :kp_equal = KEY_KP_EQUAL
    /** Key: Android back button */
    :back = KEY_BACK
    /** Key: Android menu button */
    :menu = KEY_MENU
    /** Key: Android volume up button */
    :volume_up = KEY_VOLUME_UP
    /** Key: Android volume down button */
    :volume_down = KEY_VOLUME_DOWN

/** Mouse buttons */
declare enum MouseButton as
    /** Mouse button left */
    :left = MOUSE_BUTTON_LEFT
    /** Mouse button right */
    :right = MOUSE_BUTTON_RIGHT
    /** Mouse button middle (pressed wheel) */
    :middle = MOUSE_BUTTON_MIDDLE
    /** Mouse button side (advanced mouse device) */
    :side = MOUSE_BUTTON_SIDE
    /** Mouse button extra (advanced mouse device) */
    :extra = MOUSE_BUTTON_EXTRA
    /** Mouse button forward (advanced mouse device) */
    :forward = MOUSE_BUTTON_FORWARD
    /** Mouse button back (advanced mouse device) */
    :back = MOUSE_BUTTON_BACK

/** Mouse cursor */
declare enum MouseCursor as
    /** Default pointer shape */
    :default = MOUSE_CURSOR_DEFAULT
    /** Arrow shape */
    :arrow = MOUSE_CURSOR_ARROW
    /** Text writing cursor shape */
    :ibeam = MOUSE_CURSOR_IBEAM
    /** Cross shape */
    :crosshair = MOUSE_CURSOR_CROSSHAIR
    /** Pointing hand cursor */
    :pointing_hand = MOUSE_CURSOR_POINTING_HAND
    /** Horizontal resize/move arrow shape */
    :resize_ew = MOUSE_CURSOR_RESIZE_EW
    /** Vertical resize/move arrow shape */
    :resize_ns = MOUSE_CURSOR_RESIZE_NS
    /** Top-left to bottom-right diagonal resize/move arrow shape */
    :resize_nwse = MOUSE_CURSOR_RESIZE_NWSE
    /** The top-right to bottom-left diagonal resize/move arrow shape */
    :resize_nesw = MOUSE_CURSOR_RESIZE_NESW
    /** The omnidirectional resize/move cursor shape */
    :resize_all = MOUSE_CURSOR_RESIZE_ALL
    /** The operation-not-allowed shape */
    :not_allowed = MOUSE_CURSOR_NOT_ALLOWED

/** Gamepad buttons */
declare enum GamepadButton as
    /** Unknown button, just for error checking */
    :unknown = GAMEPAD_BUTTON_UNKNOWN
    /** Gamepad left DPAD up button */
    :left_face_up = GAMEPAD_BUTTON_LEFT_FACE_UP
    /** Gamepad left DPAD right button */
    :left_face_right = GAMEPAD_BUTTON_LEFT_FACE_RIGHT
    /** Gamepad left DPAD down button */
    :left_face_down = GAMEPAD_BUTTON_LEFT_FACE_DOWN
    /** Gamepad left DPAD left button */
    :left_face_left = GAMEPAD_BUTTON_LEFT_FACE_LEFT
    /** Gamepad right button up (i.e. PS3: Triangle, Xbox: Y) */
    :right_face_up = GAMEPAD_BUTTON_RIGHT_FACE_UP
    /** Gamepad right button right (i.e. PS3: Circle, Xbox: B) */
    :right_face_right = GAMEPAD_BUTTON_RIGHT_FACE_RIGHT
    /** Gamepad right button down (i.e. PS3: Cross, Xbox: A) */
    :right_face_down = GAMEPAD_BUTTON_RIGHT_FACE_DOWN
    /** Gamepad right button left (i.e. PS3: Square, Xbox: X) */
    :right_face_left = GAMEPAD_BUTTON_RIGHT_FACE_LEFT
    /** Gamepad top/back trigger left (first), it could be a trailing button */
    :left_trigger_1 = GAMEPAD_BUTTON_LEFT_TRIGGER_1
    /** Gamepad top/back trigger left (second), it could be a trailing button */
    :left_trigger_2 = GAMEPAD_BUTTON_LEFT_TRIGGER_2
    /** Gamepad top/back trigger right (first), it could be a trailing button */
    :right_trigger_1 = GAMEPAD_BUTTON_RIGHT_TRIGGER_1
    /** Gamepad top/back trigger right (second), it could be a trailing button */
    :right_trigger_2 = GAMEPAD_BUTTON_RIGHT_TRIGGER_2
    /** Gamepad center buttons, left one (i.e. PS3: Select) */
    :middle_left = GAMEPAD_BUTTON_MIDDLE_LEFT
    /** Gamepad center buttons, middle one (i.e. PS3: PS, Xbox: XBOX) */
    :middle = GAMEPAD_BUTTON_MIDDLE
    /** Gamepad center buttons, right one (i.e. PS3: Start) */
    :middle_right = GAMEPAD_BUTTON_MIDDLE_RIGHT
    /** Gamepad joystick pressed button left */
    :left_thumb = GAMEPAD_BUTTON_LEFT_THUMB
    /** Gamepad joystick pressed button right */
    :right_thumb = GAMEPAD_BUTTON_RIGHT_THUMB

/** Gamepad axis */
declare enum GamepadAxis as
    /** Gamepad left stick X axis */
    :left_x = GAMEPAD_AXIS_LEFT_X
    /** Gamepad left stick Y axis */
    :left_y = GAMEPAD_AXIS_LEFT_Y
    /** Gamepad right stick X axis */
    :right_x = GAMEPAD_AXIS_RIGHT_X
    /** Gamepad right stick Y axis */
    :right_y = GAMEPAD_AXIS_RIGHT_Y
    /** Gamepad back trigger left, pressure level: [1..-1] */
    :left_trigger = GAMEPAD_AXIS_LEFT_TRIGGER
    /** Gamepad back trigger right, pressure level: [1..-1] */
    :right_trigger = GAMEPAD_AXIS_RIGHT_TRIGGER

// MaterialMapIndex: Material map index
/** Albedo material (same as: MATERIAL_MAP_DIFFUSE) */
declare val MATERIAL_MAP_ALBEDO as Int
/** Metalness material (same as: MATERIAL_MAP_SPECULAR) */
declare val MATERIAL_MAP_METALNESS as Int
/** Normal material */
declare val MATERIAL_MAP_NORMAL as Int
/** Roughness material */
declare val MATERIAL_MAP_ROUGHNESS as Int
/** Ambient occlusion material */
declare val MATERIAL_MAP_OCCLUSION as Int
/** Emission material */
declare val MATERIAL_MAP_EMISSION as Int
/** Heightmap material */
declare val MATERIAL_MAP_HEIGHT as Int
/** Cubemap material (NOTE: Uses GL_TEXTURE_CUBE_MAP) */
declare val MATERIAL_MAP_CUBEMAP as Int
/** Irradiance material (NOTE: Uses GL_TEXTURE_CUBE_MAP) */
declare val MATERIAL_MAP_IRRADIANCE as Int
/** Prefilter material (NOTE: Uses GL_TEXTURE_CUBE_MAP) */
declare val MATERIAL_MAP_PREFILTER as Int
/** Brdf material */
declare val MATERIAL_MAP_BRDF as Int
/** Same as MATERIAL_MAP_ALBEDO */
declare val MATERIAL_MAP_DIFFUSE as Int
/** Same as MATERIAL_MAP_METALNESS */
declare val MATERIAL_MAP_SPECULAR as Int

// ShaderLocationIndex: Shader location index
/** Shader location: vertex attribute: position */
declare val SHADER_LOC_VERTEX_POSITION as Int
/** Shader location: vertex attribute: texcoord01 */
declare val SHADER_LOC_VERTEX_TEXCOORD01 as Int
/** Shader location: vertex attribute: texcoord02 */
declare val SHADER_LOC_VERTEX_TEXCOORD02 as Int
/** Shader location: vertex attribute: normal */
declare val SHADER_LOC_VERTEX_NORMAL as Int
/** Shader location: vertex attribute: tangent */
declare val SHADER_LOC_VERTEX_TANGENT as Int
/** Shader location: vertex attribute: color */
declare val SHADER_LOC_VERTEX_COLOR as Int
/** Shader location: matrix uniform: model-view-projection */
declare val SHADER_LOC_MATRIX_MVP as Int
/** Shader location: matrix uniform: view (camera transform) */
declare val SHADER_LOC_MATRIX_VIEW as Int
/** Shader location: matrix uniform: projection */
declare val SHADER_LOC_MATRIX_PROJECTION as Int
/** Shader location: matrix uniform: model (transform) */
declare val SHADER_LOC_MATRIX_MODEL as Int
/** Shader location: matrix uniform: normal */
declare val SHADER_LOC_MATRIX_NORMAL as Int
/** Shader location: vector uniform: view */
declare val SHADER_LOC_VECTOR_VIEW as Int
/** Shader location: vector uniform: diffuse color */
declare val SHADER_LOC_COLOR_DIFFUSE as Int
/** Shader location: vector uniform: specular color */
declare val SHADER_LOC_COLOR_SPECULAR as Int
/** Shader location: vector uniform: ambient color */
declare val SHADER_LOC_COLOR_AMBIENT as Int
/** Shader location: sampler2d texture: albedo (same as: SHADER_LOC_MAP_DIFFUSE) */
declare val SHADER_LOC_MAP_ALBEDO as Int
/** Shader location: sampler2d texture: metalness (same as: SHADER_LOC_MAP_SPECULAR) */
declare val SHADER_LOC_MAP_METALNESS as Int
/** Shader location: sampler2d texture: normal */
declare val SHADER_LOC_MAP_NORMAL as Int
/** Shader location: sampler2d texture: roughness */
declare val SHADER_LOC_MAP_ROUGHNESS as Int
/** Shader location: sampler2d texture: occlusion */
declare val SHADER_LOC_MAP_OCCLUSION as Int
/** Shader location: sampler2d texture: emission */
declare val SHADER_LOC_MAP_EMISSION as Int
/** Shader location: sampler2d texture: height */
declare val SHADER_LOC_MAP_HEIGHT as Int
/** Shader location: samplerCube texture: cubemap */
declare val SHADER_LOC_MAP_CUBEMAP as Int
/** Shader location: samplerCube texture: irradiance */
declare val SHADER_LOC_MAP_IRRADIANCE as Int
/** Shader location: samplerCube texture: prefilter */
declare val SHADER_LOC_MAP_PREFILTER as Int
/** Shader location: sampler2d texture: brdf */
declare val SHADER_LOC_MAP_BRDF as Int
/** Shader location: vertex attribute: boneIds */
declare val SHADER_LOC_VERTEX_BONEIDS as Int
/** Shader location: vertex attribute: boneWeights */
declare val SHADER_LOC_VERTEX_BONEWEIGHTS as Int
/** Shader location: array of matrices uniform: boneMatrices */
declare val SHADER_LOC_BONE_MATRICES as Int
/** Same as SHADER_LOC_MAP_ALBEDO */
declare val SHADER_LOC_MAP_DIFFUSE as Int
/** Same as SHADER_LOC_MAP_METALNESS */
declare val SHADER_LOC_MAP_SPECULAR as Int

/** Shader uniform data type */
declare enum ShaderUniformDataType as
    /** Shader uniform type: float */
    :float = SHADER_UNIFORM_FLOAT
    /** Shader uniform type: vec2 (2 float) */
    :vec2 = SHADER_UNIFORM_VEC2
    /** Shader uniform type: vec3 (3 float) */
    :vec3 = SHADER_UNIFORM_VEC3
    /** Shader uniform type: vec4 (4 float) */
    :vec4 = SHADER_UNIFORM_VEC4
    /** Shader uniform type: int */
    :int = SHADER_UNIFORM_INT
    /** Shader uniform type: ivec2 (2 int) */
    :ivec2 = SHADER_UNIFORM_IVEC2
    /** Shader uniform type: ivec3 (3 int) */
    :ivec3 = SHADER_UNIFORM_IVEC3
    /** Shader uniform type: ivec4 (4 int) */
    :ivec4 = SHADER_UNIFORM_IVEC4
    /** Shader uniform type: sampler2d */
    :sampler2d = SHADER_UNIFORM_SAMPLER2D

// ShaderAttributeDataType: Shader attribute data types
/** Shader attribute type: float */
declare val SHADER_ATTRIB_FLOAT as Int
/** Shader attribute type: vec2 (2 float) */
declare val SHADER_ATTRIB_VEC2 as Int
/** Shader attribute type: vec3 (3 float) */
declare val SHADER_ATTRIB_VEC3 as Int
/** Shader attribute type: vec4 (4 float) */
declare val SHADER_ATTRIB_VEC4 as Int

/** Pixel formats */
declare enum PixelFormat as
    /** 8 bit per pixel (no alpha) */
    :uncompressed_grayscale = PIXELFORMAT_UNCOMPRESSED_GRAYSCALE
    /** 8*2 bpp (2 channels) */
    :uncompressed_gray_alpha = PIXELFORMAT_UNCOMPRESSED_GRAY_ALPHA
    /** 16 bpp */
    :uncompressed_r5g6b5 = PIXELFORMAT_UNCOMPRESSED_R5G6B5
    /** 24 bpp */
    :uncompressed_r8g8b8 = PIXELFORMAT_UNCOMPRESSED_R8G8B8
    /** 16 bpp (1 bit alpha) */
    :uncompressed_r5g5b5a1 = PIXELFORMAT_UNCOMPRESSED_R5G5B5A1
    /** 16 bpp (4 bit alpha) */
    :uncompressed_r4g4b4a4 = PIXELFORMAT_UNCOMPRESSED_R4G4B4A4
    /** 32 bpp */
    :uncompressed_r8g8b8a8 = PIXELFORMAT_UNCOMPRESSED_R8G8B8A8
    /** 32 bpp (1 channel - float) */
    :uncompressed_r32 = PIXELFORMAT_UNCOMPRESSED_R32
    /** 32*3 bpp (3 channels - float) */
    :uncompressed_r32g32b32 = PIXELFORMAT_UNCOMPRESSED_R32G32B32
    /** 32*4 bpp (4 channels - float) */
    :uncompressed_r32g32b32a32 = PIXELFORMAT_UNCOMPRESSED_R32G32B32A32
    /** 16 bpp (1 channel - half float) */
    :uncompressed_r16 = PIXELFORMAT_UNCOMPRESSED_R16
    /** 16*3 bpp (3 channels - half float) */
    :uncompressed_r16g16b16 = PIXELFORMAT_UNCOMPRESSED_R16G16B16
    /** 16*4 bpp (4 channels - half float) */
    :uncompressed_r16g16b16a16 = PIXELFORMAT_UNCOMPRESSED_R16G16B16A16
    /** 4 bpp (no alpha) */
    :compressed_dxt1_rgb = PIXELFORMAT_COMPRESSED_DXT1_RGB
    /** 4 bpp (1 bit alpha) */
    :compressed_dxt1_rgba = PIXELFORMAT_COMPRESSED_DXT1_RGBA
    /** 8 bpp */
    :compressed_dxt3_rgba = PIXELFORMAT_COMPRESSED_DXT3_RGBA
    /** 8 bpp */
    :compressed_dxt5_rgba = PIXELFORMAT_COMPRESSED_DXT5_RGBA
    /** 4 bpp */
    :compressed_etc1_rgb = PIXELFORMAT_COMPRESSED_ETC1_RGB
    /** 4 bpp */
    :compressed_etc2_rgb = PIXELFORMAT_COMPRESSED_ETC2_RGB
    /** 8 bpp */
    :compressed_etc2_eac_rgba = PIXELFORMAT_COMPRESSED_ETC2_EAC_RGBA
    /** 4 bpp */
    :compressed_pvrt_rgb = PIXELFORMAT_COMPRESSED_PVRT_RGB
    /** 4 bpp */
    :compressed_pvrt_rgba = PIXELFORMAT_COMPRESSED_PVRT_RGBA
    /** 8 bpp */
    :compressed_astc_4x4_rgba = PIXELFORMAT_COMPRESSED_ASTC_4x4_RGBA
    /** 2 bpp */
    :compressed_astc_8x8_rgba = PIXELFORMAT_COMPRESSED_ASTC_8x8_RGBA

// PixelFormat: Pixel formats
/** 8 bit per pixel (no alpha) */
declare val PIXELFORMAT_UNCOMPRESSED_GRAYSCALE as Int
/** 8*2 bpp (2 channels) */
declare val PIXELFORMAT_UNCOMPRESSED_GRAY_ALPHA as Int
/** 16 bpp */
declare val PIXELFORMAT_UNCOMPRESSED_R5G6B5 as Int
/** 24 bpp */
declare val PIXELFORMAT_UNCOMPRESSED_R8G8B8 as Int
/** 16 bpp (1 bit alpha) */
declare val PIXELFORMAT_UNCOMPRESSED_R5G5B5A1 as Int
/** 16 bpp (4 bit alpha) */
declare val PIXELFORMAT_UNCOMPRESSED_R4G4B4A4 as Int
/** 32 bpp */
declare val PIXELFORMAT_UNCOMPRESSED_R8G8B8A8 as Int
/** 32 bpp (1 channel - float) */
declare val PIXELFORMAT_UNCOMPRESSED_R32 as Int
/** 32*3 bpp (3 channels - float) */
declare val PIXELFORMAT_UNCOMPRESSED_R32G32B32 as Int
/** 32*4 bpp (4 channels - float) */
declare val PIXELFORMAT_UNCOMPRESSED_R32G32B32A32 as Int
/** 16 bpp (1 channel - half float) */
declare val PIXELFORMAT_UNCOMPRESSED_R16 as Int
/** 16*3 bpp (3 channels - half float) */
declare val PIXELFORMAT_UNCOMPRESSED_R16G16B16 as Int
/** 16*4 bpp (4 channels - half float) */
declare val PIXELFORMAT_UNCOMPRESSED_R16G16B16A16 as Int
/** 4 bpp (no alpha) */
declare val PIXELFORMAT_COMPRESSED_DXT1_RGB as Int
/** 4 bpp (1 bit alpha) */
declare val PIXELFORMAT_COMPRESSED_DXT1_RGBA as Int
/** 8 bpp */
declare val PIXELFORMAT_COMPRESSED_DXT3_RGBA as Int
/** 8 bpp */
declare val PIXELFORMAT_COMPRESSED_DXT5_RGBA as Int
/** 4 bpp */
declare val PIXELFORMAT_COMPRESSED_ETC1_RGB as Int
/** 4 bpp */
declare val PIXELFORMAT_COMPRESSED_ETC2_RGB as Int
/** 8 bpp */
declare val PIXELFORMAT_COMPRESSED_ETC2_EAC_RGBA as Int
/** 4 bpp */
declare val PIXELFORMAT_COMPRESSED_PVRT_RGB as Int
/** 4 bpp */
declare val PIXELFORMAT_COMPRESSED_PVRT_RGBA as Int
/** 8 bpp */
declare val PIXELFORMAT_COMPRESSED_ASTC_4x4_RGBA as Int
/** 2 bpp */
declare val PIXELFORMAT_COMPRESSED_ASTC_8x8_RGBA as Int

/** Texture parameters: filter mode */
declare enum TextureFilter as
    /** No filter, just pixel approximation */
    :point = TEXTURE_FILTER_POINT
    /** Linear filtering */
    :bilinear = TEXTURE_FILTER_BILINEAR
    /** Trilinear filtering (linear with mipmaps) */
    :trilinear = TEXTURE_FILTER_TRILINEAR
    /** Anisotropic filtering 4x */
    :anisotropic_4x = TEXTURE_FILTER_ANISOTROPIC_4X
    /** Anisotropic filtering 8x */
    :anisotropic_8x = TEXTURE_FILTER_ANISOTROPIC_8X
    /** Anisotropic filtering 16x */
    :anisotropic_16x = TEXTURE_FILTER_ANISOTROPIC_16X

/** Texture parameters: wrap mode */
declare enum TextureWrap as
    /** Repeats texture in tiled mode */
    :repeat = TEXTURE_WRAP_REPEAT
    /** Clamps texture to edge pixel in tiled mode */
    :clamp = TEXTURE_WRAP_CLAMP
    /** Mirrors and repeats the texture in tiled mode */
    :mirror_repeat = TEXTURE_WRAP_MIRROR_REPEAT
    /** Mirrors and clamps to border the texture in tiled mode */
    :mirror_clamp = TEXTURE_WRAP_MIRROR_CLAMP

/** Cubemap layouts */
declare enum CubemapLayout as
    /** Automatically detect layout type */
    :auto_detect = CUBEMAP_LAYOUT_AUTO_DETECT
    /** Layout is defined by a vertical line with faces */
    :line_vertical = CUBEMAP_LAYOUT_LINE_VERTICAL
    /** Layout is defined by a horizontal line with faces */
    :line_horizontal = CUBEMAP_LAYOUT_LINE_HORIZONTAL
    /** Layout is defined by a 3x4 cross with cubemap faces */
    :cross_three_by_four = CUBEMAP_LAYOUT_CROSS_THREE_BY_FOUR
    /** Layout is defined by a 4x3 cross with cubemap faces */
    :cross_four_by_three = CUBEMAP_LAYOUT_CROSS_FOUR_BY_THREE

/** Font type, defines generation method */
declare enum FontType as
    /** Default font generation, anti-aliased */
    :default = FONT_DEFAULT
    /** Bitmap font generation, no anti-aliasing */
    :bitmap = FONT_BITMAP
    /** SDF font generation, requires external shader */
    :sdf = FONT_SDF

/** Color blending modes (pre-defined) */
declare enum BlendMode as
    /** Blend textures considering alpha (default) */
    :alpha = BLEND_ALPHA
    /** Blend textures adding colors */
    :additive = BLEND_ADDITIVE
    /** Blend textures multiplying colors */
    :multiplied = BLEND_MULTIPLIED
    /** Blend textures adding colors (alternative) */
    :add_colors = BLEND_ADD_COLORS
    /** Blend textures subtracting colors (alternative) */
    :subtract_colors = BLEND_SUBTRACT_COLORS
    /** Blend premultiplied textures considering alpha */
    :alpha_premultiply = BLEND_ALPHA_PREMULTIPLY
    /** Blend textures using custom src/dst factors (use rlSetBlendFactors()) */
    :custom = BLEND_CUSTOM
    /** Blend textures using custom rgb/alpha separate src/dst factors (use rlSetBlendFactorsSeparate()) */
    :custom_separate = BLEND_CUSTOM_SEPARATE

// Gesture: Gesture
/** No gesture */
declare val GESTURE_NONE as UInt
/** Tap gesture */
declare val GESTURE_TAP as UInt
/** Double tap gesture */
declare val GESTURE_DOUBLETAP as UInt
/** Hold gesture */
declare val GESTURE_HOLD as UInt
/** Drag gesture */
declare val GESTURE_DRAG as UInt
/** Swipe right gesture */
declare val GESTURE_SWIPE_RIGHT as UInt
/** Swipe left gesture */
declare val GESTURE_SWIPE_LEFT as UInt
/** Swipe up gesture */
declare val GESTURE_SWIPE_UP as UInt
/** Swipe down gesture */
declare val GESTURE_SWIPE_DOWN as UInt
/** Pinch in gesture */
declare val GESTURE_PINCH_IN as UInt
/** Pinch out gesture */
declare val GESTURE_PINCH_OUT as UInt

/** Camera system modes */
declare enum CameraMode as
    /** Camera custom, controlled by user (UpdateCamera() does nothing) */
    :custom = CAMERA_CUSTOM
    /** Camera free mode */
    :free = CAMERA_FREE
    /** Camera orbital, around target, zoom supported */
    :orbital = CAMERA_ORBITAL
    /** Camera first person */
    :first_person = CAMERA_FIRST_PERSON
    /** Camera third person */
    :third_person = CAMERA_THIRD_PERSON

// CameraProjection: Camera projection
/** Perspective projection */
declare val CAMERA_PERSPECTIVE as Int
/** Orthographic projection */
declare val CAMERA_ORTHOGRAPHIC as Int

// NPatchLayout: N-patch layout
/** Npatch layout: 3x3 tiles */
declare val NPATCH_NINE_PATCH as Int
/** Npatch layout: 1x3 tiles */
declare val NPATCH_THREE_PATCH_VERTICAL as Int
/** Npatch layout: 3x1 tiles */
declare val NPATCH_THREE_PATCH_HORIZONTAL as Int

// Callbacks. An eclang function given to C is a plain C function pointer: `fn name`, or a lambda that uses no
// local variables. AudioCallback runs on raylib's audio thread.
declare type LoadFileDataCallback as (fileName: CString, dataSize: @Int) => UChar[]c?
declare type SaveFileDataCallback as (fileName: CString, data: @Any, dataSize: Int) => Bool
declare type LoadFileTextCallback as (fileName: CString) => CString?
declare type SaveFileTextCallback as (fileName: CString, text: CString) => Bool
declare type AudioCallback as (bufferData: @Any, frames: UInt) => None

// ---------------------------------------------------------------------------------------------------------------------
// Window and Graphics Device Functions (Module: core)
// ---------------------------------------------------------------------------------------------------------------------

// Window-related functions
/** Initialize window and OpenGL context */
declare fn InitWindow as (width: Int, height: Int, title: CString)
/** Close window and unload OpenGL context */
declare fn CloseWindow as ()
/** Check if application should close (KEY_ESCAPE pressed or windows close icon clicked) */
declare fn WindowShouldClose as (): Bool
/** Check if window has been initialized successfully */
declare fn IsWindowReady as (): Bool
/** Check if window is currently fullscreen */
declare fn IsWindowFullscreen as (): Bool
/** Check if window is currently hidden */
declare fn IsWindowHidden as (): Bool
/** Check if window is currently minimized */
declare fn IsWindowMinimized as (): Bool
/** Check if window is currently maximized */
declare fn IsWindowMaximized as (): Bool
/** Check if window is currently focused */
declare fn IsWindowFocused as (): Bool
/** Check if window has been resized last frame */
declare fn IsWindowResized as (): Bool
/** Check if one specific window flag is enabled */
declare fn IsWindowState as (flag: UInt): Bool
/** Set window configuration state using flags */
declare fn SetWindowState as (flags: UInt)
/** Clear window configuration state flags */
declare fn ClearWindowState as (flags: UInt)
/** Toggle window state: fullscreen/windowed, resizes monitor to match window resolution */
declare fn ToggleFullscreen as ()
/** Toggle window state: borderless windowed, resizes window to match monitor resolution */
declare fn ToggleBorderlessWindowed as ()
/** Set window state: maximized, if resizable */
declare fn MaximizeWindow as ()
/** Set window state: minimized, if resizable */
declare fn MinimizeWindow as ()
/** Set window state: not minimized/maximized */
declare fn RestoreWindow as ()
/** Set icon for window (single image, RGBA 32bit) */
declare fn SetWindowIcon as (image: Image)
/** Set icon for window (multiple images, RGBA 32bit) */
declare fn SetWindowIcons as (images: Image[]c, count: Int)
/** Set title for window */
declare fn SetWindowTitle as (title: CString)
/** Set window position on screen */
declare fn SetWindowPosition as (x: Int, y: Int)
/** Set monitor for the current window */
declare fn SetWindowMonitor as (monitor: Int)
/** Set window minimum dimensions (for FLAG_WINDOW_RESIZABLE) */
declare fn SetWindowMinSize as (width: Int, height: Int)
/** Set window maximum dimensions (for FLAG_WINDOW_RESIZABLE) */
declare fn SetWindowMaxSize as (width: Int, height: Int)
/** Set window dimensions */
declare fn SetWindowSize as (width: Int, height: Int)
/** Set window opacity [0.0f..1.0f] */
declare fn SetWindowOpacity as (opacity: Float)
/** Set window focused */
declare fn SetWindowFocused as ()
/** Get native window handle */
declare fn GetWindowHandle as (): @Any?
/** Get current screen width */
declare fn GetScreenWidth as (): Int
/** Get current screen height */
declare fn GetScreenHeight as (): Int
/** Get current render width (it considers HiDPI) */
declare fn GetRenderWidth as (): Int
/** Get current render height (it considers HiDPI) */
declare fn GetRenderHeight as (): Int
/** Get number of connected monitors */
declare fn GetMonitorCount as (): Int
/** Get current monitor where window is placed */
declare fn GetCurrentMonitor as (): Int
/** Get specified monitor position */
declare fn GetMonitorPosition as (monitor: Int): Vector2
/** Get specified monitor width (current video mode used by monitor) */
declare fn GetMonitorWidth as (monitor: Int): Int
/** Get specified monitor height (current video mode used by monitor) */
declare fn GetMonitorHeight as (monitor: Int): Int
/** Get specified monitor physical width in millimetres */
declare fn GetMonitorPhysicalWidth as (monitor: Int): Int
/** Get specified monitor physical height in millimetres */
declare fn GetMonitorPhysicalHeight as (monitor: Int): Int
/** Get specified monitor refresh rate */
declare fn GetMonitorRefreshRate as (monitor: Int): Int
/** Get window position XY on monitor */
declare fn GetWindowPosition as (): Vector2
/** Get window scale DPI factor */
declare fn GetWindowScaleDPI as (): Vector2
/** Get the human-readable, UTF-8 encoded name of the specified monitor */
declare fn GetMonitorName as (monitor: Int): CString
/** Set clipboard text content */
declare fn SetClipboardText as (text: CString)
/** Get clipboard text content */
declare fn GetClipboardText as (): CString?
/** Get clipboard image content */
declare fn GetClipboardImage as (): Image
/** Enable waiting for events on EndDrawing(), no automatic event polling */
declare fn EnableEventWaiting as ()
/** Disable waiting for events on EndDrawing(), automatic events polling */
declare fn DisableEventWaiting as ()

// Cursor-related functions
/** Shows cursor */
declare fn ShowCursor as ()
/** Hides cursor */
declare fn HideCursor as ()
/** Check if cursor is not visible */
declare fn IsCursorHidden as (): Bool
/** Enables cursor (unlock cursor) */
declare fn EnableCursor as ()
/** Disables cursor (lock cursor) */
declare fn DisableCursor as ()
/** Check if cursor is on the screen */
declare fn IsCursorOnScreen as (): Bool

// Drawing-related functions
/** Set background color (framebuffer clear color) */
declare fn ClearBackground as (color: Color)
/** Setup canvas (framebuffer) to start drawing */
declare fn BeginDrawing as ()
/** End canvas drawing and swap buffers (double buffering) */
declare fn EndDrawing as ()
/** Begin 2D mode with custom camera (2D) */
declare fn BeginMode2D as (camera: Camera2D)
/** Ends 2D mode with custom camera */
declare fn EndMode2D as ()
/** Begin 3D mode with custom camera (3D) */
declare fn BeginMode3D as (camera: Camera3D)
/** Ends 3D mode and returns to default 2D orthographic mode */
declare fn EndMode3D as ()
/** Begin drawing to render texture */
declare fn BeginTextureMode as (target: RenderTexture2D)
/** Ends drawing to render texture */
declare fn EndTextureMode as ()
/** Begin custom shader drawing */
declare fn BeginShaderMode as (shader: Shader)
/** End custom shader drawing (use default shader) */
declare fn EndShaderMode as ()
/** Begin blending mode (alpha, additive, multiplied, subtract, custom) */
declare fn BeginBlendMode as (mode: BlendMode)
/** End blending mode (reset to default: alpha blending) */
declare fn EndBlendMode as ()
/** Begin scissor mode (define screen area for following drawing) */
declare fn BeginScissorMode as (x: Int, y: Int, width: Int, height: Int)
/** End scissor mode */
declare fn EndScissorMode as ()
/** Begin stereo rendering (requires VR simulator) */
declare fn BeginVrStereoMode as (config: VrStereoConfig)
/** End stereo rendering (requires VR simulator) */
declare fn EndVrStereoMode as ()

// VR stereo config functions for VR simulator
/** Load VR stereo config for VR simulator device parameters */
declare fn LoadVrStereoConfig as (device: VrDeviceInfo): VrStereoConfig
/** Unload VR stereo config */
declare fn UnloadVrStereoConfig as (config: VrStereoConfig)

// Shader management functions

// NOTE: Shader functionality is not available on OpenGL 1.1
/** Load shader from files and bind default locations */
declare fn LoadShader as (vsFileName: CString?, fsFileName: CString?): Shader
/** Load shader from code strings and bind default locations */
declare fn LoadShaderFromMemory as (vsCode: CString?, fsCode: CString?): Shader
/** Check if a shader is valid (loaded on GPU) */
declare fn IsShaderValid as (shader: Shader): Bool
/** Get shader uniform location */
declare fn GetShaderLocation as (shader: Shader, uniformName: CString): Int
/** Get shader attribute location */
declare fn GetShaderLocationAttrib as (shader: Shader, attribName: CString): Int
/** Set shader uniform value */
declare fn SetShaderValue as (shader: Shader, locIndex: Int, value: @Any, uniformType: ShaderUniformDataType)
/** Set shader uniform value vector */
declare fn SetShaderValueV as (shader: Shader, locIndex: Int, value: @Any, uniformType: ShaderUniformDataType, count: Int)
/** Set shader uniform value (matrix 4x4) */
declare fn SetShaderValueMatrix as (shader: Shader, locIndex: Int, mat: Matrix)
/** Set shader uniform value for texture (sampler2d) */
declare fn SetShaderValueTexture as (shader: Shader, locIndex: Int, texture: Texture2D)
/** Unload shader from GPU memory (VRAM) */
declare fn UnloadShader as (shader: Shader)

// Screen-space-related functions
/** Get a ray trace from screen position (i.e mouse) */
declare fn GetScreenToWorldRay as (position: Vector2, camera: Camera): Ray
/** Get a ray trace from screen position (i.e mouse) in a viewport */
declare fn GetScreenToWorldRayEx as (position: Vector2, camera: Camera, width: Int, height: Int): Ray
/** Get the screen space position for a 3d world space position */
declare fn GetWorldToScreen as (position: Vector3, camera: Camera): Vector2
/** Get size position for a 3d world space position */
declare fn GetWorldToScreenEx as (position: Vector3, camera: Camera, width: Int, height: Int): Vector2
/** Get the screen space position for a 2d camera world space position */
declare fn GetWorldToScreen2D as (position: Vector2, camera: Camera2D): Vector2
/** Get the world space position for a 2d camera screen space position */
declare fn GetScreenToWorld2D as (position: Vector2, camera: Camera2D): Vector2
/** Get camera transform matrix (view matrix) */
declare fn GetCameraMatrix as (camera: Camera): Matrix
/** Get camera 2d transform matrix */
declare fn GetCameraMatrix2D as (camera: Camera2D): Matrix

// Timing-related functions
/** Set target FPS (maximum) */
declare fn SetTargetFPS as (fps: Int)
/** Get time in seconds for last frame drawn (delta time) */
declare fn GetFrameTime as (): Float
/** Get elapsed time in seconds since InitWindow() */
declare fn GetTime as (): Double
/** Get current FPS */
declare fn GetFPS as (): Int

// Custom frame control functions

// NOTE: Those functions are intended for advanced users that want full control over the frame processing

// By default EndDrawing() does this job: draws everything + SwapScreenBuffer() + manage frame timing + PollInputEvents()

// To avoid that behaviour and control frame processes manually, enable in config.h: SUPPORT_CUSTOM_FRAME_CONTROL
/** Swap back buffer with front buffer (screen drawing) */
declare fn SwapScreenBuffer as ()
/** Register all input events */
declare fn PollInputEvents as ()
/** Wait for some time (halt program execution) */
declare fn WaitTime as (seconds: Double)

// Random values generation functions
/** Set the seed for the random number generator */
declare fn SetRandomSeed as (seed: UInt)
/** Get a random value between min and max (both included) */
declare fn GetRandomValue as (min: Int, max: Int): Int
/** Load random values sequence, no values repeated */
declare fn LoadRandomSequence as (count: UInt, min: Int, max: Int): Int[]c?
/** Unload random values sequence */
declare fn UnloadRandomSequence as (sequence: Int[]c)

// Misc. functions
/** Takes a screenshot of current screen (filename extension defines format) */
declare fn TakeScreenshot as (fileName: CString)
/** Setup init configuration flags (view FLAGS) */
declare fn SetConfigFlags as (flags: UInt)
/** Open URL with default system browser (if available) */
declare fn OpenURL as (url: CString)

// NOTE: Following functions implemented in module [utils]
/** Show trace log messages (LOG_DEBUG, LOG_INFO, LOG_WARNING, LOG_ERROR...) */
declare fn TraceLog as (logLevel: TraceLogLevel, text: CString, ...)
/** Set the current threshold (minimum) log level */
declare fn SetTraceLogLevel as (logLevel: TraceLogLevel)
/** Internal memory allocator */
declare fn MemAlloc as (size: UInt): @Any?
/** Internal memory reallocator */
declare fn MemRealloc as (ptr: @Any?, size: UInt): @Any?
/** Internal memory free */
declare fn MemFree as (ptr: @Any?)

// Set custom callbacks

// WARNING: Callbacks setup is intended for advanced users
// SetTraceLogCallback is left out: its callback takes a C va_list.
/** Set custom file binary data loader */
declare fn SetLoadFileDataCallback as (callback: LoadFileDataCallback)
/** Set custom file binary data saver */
declare fn SetSaveFileDataCallback as (callback: SaveFileDataCallback)
/** Set custom file text data loader */
declare fn SetLoadFileTextCallback as (callback: LoadFileTextCallback)
/** Set custom file text data saver */
declare fn SetSaveFileTextCallback as (callback: SaveFileTextCallback)

// Files management functions
/** Load file data as byte array (read) */
declare fn LoadFileData as (fileName: CString, out dataSize: Int): UChar[]c?
/** Unload file data allocated by LoadFileData() */
declare fn UnloadFileData as (data: UChar[]c)
/** Save data to file from byte array (write), returns true on success */
declare fn SaveFileData as (fileName: CString, data: @Any, dataSize: Int): Bool
/** Export data to code (.h), returns true on success */
declare fn ExportDataAsCode as (data: UChar[]c, dataSize: Int, fileName: CString): Bool
/** Load text data from file (read), returns a '\0' terminated string */
declare fn LoadFileText as (fileName: CString): CString?
/** Unload file text data allocated by LoadFileText() */
declare fn UnloadFileText as (text: CString)
/** Save text data to file (write), string must be '\0' terminated, returns true on success */
declare fn SaveFileText as (fileName: CString, text: CString): Bool

// File system functions
/** Check if file exists */
declare fn FileExists as (fileName: CString): Bool
/** Check if a directory path exists */
declare fn DirectoryExists as (dirPath: CString): Bool
/** Check file extension (including point: .png, .wav) */
declare fn IsFileExtension as (fileName: CString, ext: CString): Bool
/** Get file length in bytes (NOTE: GetFileSize() conflicts with windows.h) */
declare fn GetFileLength as (fileName: CString): Int
/** Get pointer to extension for a filename string (includes dot: '.png') */
declare fn GetFileExtension as (fileName: CString): CString?
/** Get pointer to filename for a path string */
declare fn GetFileName as (filePath: CString): CString
/** Get filename string without extension (uses static string) */
declare fn GetFileNameWithoutExt as (filePath: CString): CString
/** Get full path for a given fileName with path (uses static string) */
declare fn GetDirectoryPath as (filePath: CString): CString
/** Get previous directory path for a given path (uses static string) */
declare fn GetPrevDirectoryPath as (dirPath: CString): CString
/** Get current working directory (uses static string) */
declare fn GetWorkingDirectory as (): CString
/** Get the directory of the running application (uses static string) */
declare fn GetApplicationDirectory as (): CString
/** Create directories (including full path requested), returns 0 on success */
declare fn MakeDirectory as (dirPath: CString): Int
/** Change working directory, return true on success */
declare fn ChangeDirectory as (dir: CString): Bool
/** Check if a given path is a file or a directory */
declare fn IsPathFile as (path: CString): Bool
/** Check if fileName is valid for the platform/OS */
declare fn IsFileNameValid as (fileName: CString): Bool
/** Load directory filepaths */
declare fn LoadDirectoryFiles as (dirPath: CString): FilePathList
/** Load directory filepaths with extension filtering and recursive directory scan. Use 'DIR' in the filter string to include directories in the result */
declare fn LoadDirectoryFilesEx as (basePath: CString, filter: CString?, scanSubdirs: Bool): FilePathList
/** Unload filepaths */
declare fn UnloadDirectoryFiles as (files: FilePathList)
/** Check if a file has been dropped into window */
declare fn IsFileDropped as (): Bool
/** Load dropped filepaths */
declare fn LoadDroppedFiles as (): FilePathList
/** Unload dropped filepaths */
declare fn UnloadDroppedFiles as (files: FilePathList)
/** Get file modification time (last write time) */
declare fn GetFileModTime as (fileName: CString): Long

// Compression/Encoding functionality
/** Compress data (DEFLATE algorithm), memory must be MemFree() */
declare fn CompressData as (data: UChar[]c, dataSize: Int, out compDataSize: Int): UChar[]c?
/** Decompress data (DEFLATE algorithm), memory must be MemFree() */
declare fn DecompressData as (compData: UChar[]c, compDataSize: Int, out dataSize: Int): UChar[]c?
/** Encode data to Base64 string, memory must be MemFree() */
declare fn EncodeDataBase64 as (data: UChar[]c, dataSize: Int, out outputSize: Int): CString?
/** Decode Base64 string data, memory must be MemFree() */
declare fn DecodeDataBase64 as (data: UChar[]c, out outputSize: Int): UChar[]c?
/** Compute CRC32 hash code */
declare fn ComputeCRC32 as (data: UChar[]c, dataSize: Int): UInt
/** Compute MD5 hash code, returns static int[4] (16 bytes) */
declare fn ComputeMD5 as (data: UChar[]c, dataSize: Int): UInt[]c
/** Compute SHA1 hash code, returns static int[5] (20 bytes) */
declare fn ComputeSHA1 as (data: UChar[]c, dataSize: Int): UInt[]c

// Automation events functionality
/** Load automation events list from file, NULL for empty list, capacity = MAX_AUTOMATION_EVENTS */
declare fn LoadAutomationEventList as (fileName: CString): AutomationEventList
/** Unload automation events list from file */
declare fn UnloadAutomationEventList as (list: AutomationEventList)
/** Export automation events list as text file */
declare fn ExportAutomationEventList as (list: AutomationEventList, fileName: CString): Bool
/** Set automation event list to record to */
declare fn SetAutomationEventList as (escaping list: @AutomationEventList)
/** Set automation event internal base frame to start recording */
declare fn SetAutomationEventBaseFrame as (frame: Int)
/** Start recording automation events (AutomationEventList must be set) */
declare fn StartAutomationEventRecording as ()
/** Stop recording automation events */
declare fn StopAutomationEventRecording as ()
/** Play a recorded automation event */
declare fn PlayAutomationEvent as (event: AutomationEvent)

// ---------------------------------------------------------------------------------------------------------------------
// Input Handling Functions (Module: core)
// ---------------------------------------------------------------------------------------------------------------------

// Input-related functions: keyboard
/** Check if a key has been pressed once */
declare fn IsKeyPressed as (key: KeyboardKey): Bool
/** IsKeyPressed with the key's number, as GetKeyPressed returns it */
declare fn IsKeyPressed as (key: Int): Bool
/** Check if a key has been pressed again */
declare fn IsKeyPressedRepeat as (key: KeyboardKey): Bool
/** IsKeyPressedRepeat with the key's number, as GetKeyPressed returns it */
declare fn IsKeyPressedRepeat as (key: Int): Bool
/** Check if a key is being pressed */
declare fn IsKeyDown as (key: KeyboardKey): Bool
/** IsKeyDown with the key's number, as GetKeyPressed returns it */
declare fn IsKeyDown as (key: Int): Bool
/** Check if a key has been released once */
declare fn IsKeyReleased as (key: KeyboardKey): Bool
/** IsKeyReleased with the key's number, as GetKeyPressed returns it */
declare fn IsKeyReleased as (key: Int): Bool
/** Check if a key is NOT being pressed */
declare fn IsKeyUp as (key: KeyboardKey): Bool
/** IsKeyUp with the key's number, as GetKeyPressed returns it */
declare fn IsKeyUp as (key: Int): Bool
/** Get key pressed (keycode), call it multiple times for keys queued, returns 0 when the queue is empty */
declare fn GetKeyPressed as (): Int
/** Get char pressed (unicode), call it multiple times for chars queued, returns 0 when the queue is empty */
declare fn GetCharPressed as (): Int
/** The number of a key, to compare with what GetKeyPressed returns: GetKeyPressed() == KeyCode(:enter). (C's abs, which
 * returns a key's number unchanged, is just the way to get it: the call converts the atom to its C constant.) */
declare fn KeyCode extern "abs" as (key: KeyboardKey): Int
/** Set a custom key to exit program (default is ESC) */
declare fn SetExitKey as (key: KeyboardKey)
/** SetExitKey with the key's number, as GetKeyPressed returns it */
declare fn SetExitKey as (key: Int)

// Input-related functions: gamepads
/** Check if a gamepad is available */
declare fn IsGamepadAvailable as (gamepad: Int): Bool
/** Get gamepad internal name id */
declare fn GetGamepadName as (gamepad: Int): CString
/** Check if a gamepad button has been pressed once */
declare fn IsGamepadButtonPressed as (gamepad: Int, button: GamepadButton): Bool
/** Check if a gamepad button is being pressed */
declare fn IsGamepadButtonDown as (gamepad: Int, button: GamepadButton): Bool
/** Check if a gamepad button has been released once */
declare fn IsGamepadButtonReleased as (gamepad: Int, button: GamepadButton): Bool
/** Check if a gamepad button is NOT being pressed */
declare fn IsGamepadButtonUp as (gamepad: Int, button: GamepadButton): Bool
/** Get the last gamepad button pressed */
declare fn GetGamepadButtonPressed as (): Int
/** Get gamepad axis count for a gamepad */
declare fn GetGamepadAxisCount as (gamepad: Int): Int
/** Get axis movement value for a gamepad axis */
declare fn GetGamepadAxisMovement as (gamepad: Int, axis: GamepadAxis): Float
/** Set internal gamepad mappings (SDL_GameControllerDB) */
declare fn SetGamepadMappings as (mappings: CString): Int
/** Set gamepad vibration for both motors (duration in seconds) */
declare fn SetGamepadVibration as (gamepad: Int, leftMotor: Float, rightMotor: Float, duration: Float)

// Input-related functions: mouse
/** Check if a mouse button has been pressed once */
declare fn IsMouseButtonPressed as (button: MouseButton): Bool
/** Check if a mouse button is being pressed */
declare fn IsMouseButtonDown as (button: MouseButton): Bool
/** Check if a mouse button has been released once */
declare fn IsMouseButtonReleased as (button: MouseButton): Bool
/** Check if a mouse button is NOT being pressed */
declare fn IsMouseButtonUp as (button: MouseButton): Bool
/** Get mouse position X */
declare fn GetMouseX as (): Int
/** Get mouse position Y */
declare fn GetMouseY as (): Int
/** Get mouse position XY */
declare fn GetMousePosition as (): Vector2
/** Get mouse delta between frames */
declare fn GetMouseDelta as (): Vector2
/** Set mouse position XY */
declare fn SetMousePosition as (x: Int, y: Int)
/** Set mouse offset */
declare fn SetMouseOffset as (offsetX: Int, offsetY: Int)
/** Set mouse scaling */
declare fn SetMouseScale as (scaleX: Float, scaleY: Float)
/** Get mouse wheel movement for X or Y, whichever is larger */
declare fn GetMouseWheelMove as (): Float
/** Get mouse wheel movement for both X and Y */
declare fn GetMouseWheelMoveV as (): Vector2
/** Set mouse cursor */
declare fn SetMouseCursor as (cursor: MouseCursor)

// Input-related functions: touch
/** Get touch position X for touch point 0 (relative to screen size) */
declare fn GetTouchX as (): Int
/** Get touch position Y for touch point 0 (relative to screen size) */
declare fn GetTouchY as (): Int
/** Get touch position XY for a touch point index (relative to screen size) */
declare fn GetTouchPosition as (index: Int): Vector2
/** Get touch point identifier for given index */
declare fn GetTouchPointId as (index: Int): Int
/** Get number of touch points */
declare fn GetTouchPointCount as (): Int

// ---------------------------------------------------------------------------------------------------------------------
// Gestures and Touch Handling Functions (Module: rgestures)
// ---------------------------------------------------------------------------------------------------------------------
/** Enable a set of gestures using flags */
declare fn SetGesturesEnabled as (flags: UInt)
/** Check if a gesture have been detected */
declare fn IsGestureDetected as (gesture: UInt): Bool
/** Get latest detected gesture */
declare fn GetGestureDetected as (): Int
/** Get gesture hold time in seconds */
declare fn GetGestureHoldDuration as (): Float
/** Get gesture drag vector */
declare fn GetGestureDragVector as (): Vector2
/** Get gesture drag angle */
declare fn GetGestureDragAngle as (): Float
/** Get gesture pinch delta */
declare fn GetGesturePinchVector as (): Vector2
/** Get gesture pinch angle */
declare fn GetGesturePinchAngle as (): Float

// ---------------------------------------------------------------------------------------------------------------------
// Camera System Functions (Module: rcamera)
// ---------------------------------------------------------------------------------------------------------------------
/** Update camera position for selected mode */
declare fn UpdateCamera as (mut camera: Camera, mode: CameraMode)
/** Update camera movement/rotation */
declare fn UpdateCameraPro as (mut camera: Camera, movement: Vector3, rotation: Vector3, zoom: Float)

// ---------------------------------------------------------------------------------------------------------------------
// Basic Shapes Drawing Functions (Module: shapes)
// ---------------------------------------------------------------------------------------------------------------------

// Set texture and rectangle to be used on shapes drawing

// NOTE: It can be useful when using basic shapes and one single font,

// defining a font char white rectangle would allow drawing everything in a single draw call
/** Set texture and rectangle to be used on shapes drawing */
declare fn SetShapesTexture as (texture: Texture2D, source: Rectangle)
/** Get texture that is used for shapes drawing */
declare fn GetShapesTexture as (): Texture2D
/** Get texture source rectangle that is used for shapes drawing */
declare fn GetShapesTextureRectangle as (): Rectangle

// Basic shapes drawing functions
/** Draw a pixel using geometry [Can be slow, use with care] */
declare fn DrawPixel as (posX: Int, posY: Int, color: Color)
/** Draw a pixel using geometry (Vector version) [Can be slow, use with care] */
declare fn DrawPixelV as (position: Vector2, color: Color)
/** Draw a line */
declare fn DrawLine as (startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: Color)
/** Draw a line (using gl lines) */
declare fn DrawLineV as (startPos: Vector2, endPos: Vector2, color: Color)
/** Draw a line (using triangles/quads) */
declare fn DrawLineEx as (startPos: Vector2, endPos: Vector2, thick: Float, color: Color)
/** Draw lines sequence (using gl lines) */
declare fn DrawLineStrip as (points: Vector2[]c, pointCount: Int, color: Color)
/** Draw line segment cubic-bezier in-out interpolation */
declare fn DrawLineBezier as (startPos: Vector2, endPos: Vector2, thick: Float, color: Color)
/** Draw a color-filled circle */
declare fn DrawCircle as (centerX: Int, centerY: Int, radius: Float, color: Color)
/** Draw a piece of a circle */
declare fn DrawCircleSector as (center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color)
/** Draw circle sector outline */
declare fn DrawCircleSectorLines as (center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color)
/** Draw a gradient-filled circle */
declare fn DrawCircleGradient as (centerX: Int, centerY: Int, radius: Float, inner: Color, outer: Color)
/** Draw a color-filled circle (Vector version) */
declare fn DrawCircleV as (center: Vector2, radius: Float, color: Color)
/** Draw circle outline */
declare fn DrawCircleLines as (centerX: Int, centerY: Int, radius: Float, color: Color)
/** Draw circle outline (Vector version) */
declare fn DrawCircleLinesV as (center: Vector2, radius: Float, color: Color)
/** Draw ellipse */
declare fn DrawEllipse as (centerX: Int, centerY: Int, radiusH: Float, radiusV: Float, color: Color)
/** Draw ellipse outline */
declare fn DrawEllipseLines as (centerX: Int, centerY: Int, radiusH: Float, radiusV: Float, color: Color)
/** Draw ring */
declare fn DrawRing as (center: Vector2, innerRadius: Float, outerRadius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color)
/** Draw ring outline */
declare fn DrawRingLines as (center: Vector2, innerRadius: Float, outerRadius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color)
/** Draw a color-filled rectangle */
declare fn DrawRectangle as (posX: Int, posY: Int, width: Int, height: Int, color: Color)
/** Draw a color-filled rectangle (Vector version) */
declare fn DrawRectangleV as (position: Vector2, size: Vector2, color: Color)
/** Draw a color-filled rectangle */
declare fn DrawRectangleRec as (rec_: Rectangle, color: Color)
/** Draw a color-filled rectangle with pro parameters */
declare fn DrawRectanglePro as (rec_: Rectangle, origin: Vector2, rotation: Float, color: Color)
/** Draw a vertical-gradient-filled rectangle */
declare fn DrawRectangleGradientV as (posX: Int, posY: Int, width: Int, height: Int, top: Color, bottom: Color)
/** Draw a horizontal-gradient-filled rectangle */
declare fn DrawRectangleGradientH as (posX: Int, posY: Int, width: Int, height: Int, left: Color, right: Color)
/** Draw a gradient-filled rectangle with custom vertex colors */
declare fn DrawRectangleGradientEx as (rec_: Rectangle, topLeft: Color, bottomLeft: Color, topRight: Color, bottomRight: Color)
/** Draw rectangle outline */
declare fn DrawRectangleLines as (posX: Int, posY: Int, width: Int, height: Int, color: Color)
/** Draw rectangle outline with extended parameters */
declare fn DrawRectangleLinesEx as (rec_: Rectangle, lineThick: Float, color: Color)
/** Draw rectangle with rounded edges */
declare fn DrawRectangleRounded as (rec_: Rectangle, roundness: Float, segments: Int, color: Color)
/** Draw rectangle lines with rounded edges */
declare fn DrawRectangleRoundedLines as (rec_: Rectangle, roundness: Float, segments: Int, color: Color)
/** Draw rectangle with rounded edges outline */
declare fn DrawRectangleRoundedLinesEx as (rec_: Rectangle, roundness: Float, segments: Int, lineThick: Float, color: Color)
/** Draw a color-filled triangle (vertex in counter-clockwise order!) */
declare fn DrawTriangle as (v1: Vector2, v2: Vector2, v3: Vector2, color: Color)
/** Draw triangle outline (vertex in counter-clockwise order!) */
declare fn DrawTriangleLines as (v1: Vector2, v2: Vector2, v3: Vector2, color: Color)
/** Draw a triangle fan defined by points (first vertex is the center) */
declare fn DrawTriangleFan as (points: Vector2[]c, pointCount: Int, color: Color)
/** Draw a triangle strip defined by points */
declare fn DrawTriangleStrip as (points: Vector2[]c, pointCount: Int, color: Color)
/** Draw a regular polygon (Vector version) */
declare fn DrawPoly as (center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color)
/** Draw a polygon outline of n sides */
declare fn DrawPolyLines as (center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color)
/** Draw a polygon outline of n sides with extended parameters */
declare fn DrawPolyLinesEx as (center: Vector2, sides: Int, radius: Float, rotation: Float, lineThick: Float, color: Color)

// Splines drawing functions
/** Draw spline: Linear, minimum 2 points */
declare fn DrawSplineLinear as (points: Vector2[]c, pointCount: Int, thick: Float, color: Color)
/** Draw spline: B-Spline, minimum 4 points */
declare fn DrawSplineBasis as (points: Vector2[]c, pointCount: Int, thick: Float, color: Color)
/** Draw spline: Catmull-Rom, minimum 4 points */
declare fn DrawSplineCatmullRom as (points: Vector2[]c, pointCount: Int, thick: Float, color: Color)
/** Draw spline: Quadratic Bezier, minimum 3 points (1 control point): [p1, c2, p3, c4...] */
declare fn DrawSplineBezierQuadratic as (points: Vector2[]c, pointCount: Int, thick: Float, color: Color)
/** Draw spline: Cubic Bezier, minimum 4 points (2 control points): [p1, c2, c3, p4, c5, c6...] */
declare fn DrawSplineBezierCubic as (points: Vector2[]c, pointCount: Int, thick: Float, color: Color)
/** Draw spline segment: Linear, 2 points */
declare fn DrawSplineSegmentLinear as (p1: Vector2, p2: Vector2, thick: Float, color: Color)
/** Draw spline segment: B-Spline, 4 points */
declare fn DrawSplineSegmentBasis as (p1: Vector2, p2: Vector2, p3: Vector2, p4: Vector2, thick: Float, color: Color)
/** Draw spline segment: Catmull-Rom, 4 points */
declare fn DrawSplineSegmentCatmullRom as (p1: Vector2, p2: Vector2, p3: Vector2, p4: Vector2, thick: Float, color: Color)
/** Draw spline segment: Quadratic Bezier, 2 points, 1 control point */
declare fn DrawSplineSegmentBezierQuadratic as (p1: Vector2, c2: Vector2, p3: Vector2, thick: Float, color: Color)
/** Draw spline segment: Cubic Bezier, 2 points, 2 control points */
declare fn DrawSplineSegmentBezierCubic as (p1: Vector2, c2: Vector2, c3: Vector2, p4: Vector2, thick: Float, color: Color)

// Spline segment point evaluation functions, for a given t [0.0f .. 1.0f]
/** Get (evaluate) spline point: Linear */
declare fn GetSplinePointLinear as (startPos: Vector2, endPos: Vector2, t: Float): Vector2
/** Get (evaluate) spline point: B-Spline */
declare fn GetSplinePointBasis as (p1: Vector2, p2: Vector2, p3: Vector2, p4: Vector2, t: Float): Vector2
/** Get (evaluate) spline point: Catmull-Rom */
declare fn GetSplinePointCatmullRom as (p1: Vector2, p2: Vector2, p3: Vector2, p4: Vector2, t: Float): Vector2
/** Get (evaluate) spline point: Quadratic Bezier */
declare fn GetSplinePointBezierQuad as (p1: Vector2, c2: Vector2, p3: Vector2, t: Float): Vector2
/** Get (evaluate) spline point: Cubic Bezier */
declare fn GetSplinePointBezierCubic as (p1: Vector2, c2: Vector2, c3: Vector2, p4: Vector2, t: Float): Vector2

// Basic shapes collision detection functions
/** Check collision between two rectangles */
declare fn CheckCollisionRecs as (rec1: Rectangle, rec2: Rectangle): Bool
/** Check collision between two circles */
declare fn CheckCollisionCircles as (center1: Vector2, radius1: Float, center2: Vector2, radius2: Float): Bool
/** Check collision between circle and rectangle */
declare fn CheckCollisionCircleRec as (center: Vector2, radius: Float, rec_: Rectangle): Bool
/** Check if circle collides with a line created betweeen two points [p1] and [p2] */
declare fn CheckCollisionCircleLine as (center: Vector2, radius: Float, p1: Vector2, p2: Vector2): Bool
/** Check if point is inside rectangle */
declare fn CheckCollisionPointRec as (point: Vector2, rec_: Rectangle): Bool
/** Check if point is inside circle */
declare fn CheckCollisionPointCircle as (point: Vector2, center: Vector2, radius: Float): Bool
/** Check if point is inside a triangle */
declare fn CheckCollisionPointTriangle as (point: Vector2, p1: Vector2, p2: Vector2, p3: Vector2): Bool
/** Check if point belongs to line created between two points [p1] and [p2] with defined margin in pixels [threshold] */
declare fn CheckCollisionPointLine as (point: Vector2, p1: Vector2, p2: Vector2, threshold: Int): Bool
/** Check if point is within a polygon described by array of vertices */
declare fn CheckCollisionPointPoly as (point: Vector2, points: Vector2[]c, pointCount: Int): Bool
/** Check the collision between two lines defined by two points each, returns collision point by reference */
declare fn CheckCollisionLines as (startPos1: Vector2, endPos1: Vector2, startPos2: Vector2, endPos2: Vector2, mut collisionPoint: Vector2): Bool
/** Get collision rectangle for two rectangles collision */
declare fn GetCollisionRec as (rec1: Rectangle, rec2: Rectangle): Rectangle

// ---------------------------------------------------------------------------------------------------------------------
// Texture Loading and Drawing Functions (Module: textures)
// ---------------------------------------------------------------------------------------------------------------------

// Image loading functions

// NOTE: These functions do not require GPU access
/** Load image from file into CPU memory (RAM) */
declare fn LoadImage as (fileName: CString): Image
/** Load image from RAW file data */
declare fn LoadImageRaw as (fileName: CString, width: Int, height: Int, format: PixelFormat, headerSize: Int): Image
/** Load image sequence from file (frames appended to image.data) */
declare fn LoadImageAnim as (fileName: CString, out frames: Int): Image
/** Load image sequence from memory buffer */
declare fn LoadImageAnimFromMemory as (fileType: CString, fileData: UChar[]c, dataSize: Int, out frames: Int): Image
/** Load image from memory buffer, fileType refers to extension: i.e. '.png' */
declare fn LoadImageFromMemory as (fileType: CString, fileData: UChar[]c, dataSize: Int): Image
/** Load image from GPU texture data */
declare fn LoadImageFromTexture as (texture: Texture2D): Image
/** Load image from screen buffer and (screenshot) */
declare fn LoadImageFromScreen as (): Image
/** Check if an image is valid (data and parameters) */
declare fn IsImageValid as (image: Image): Bool
/** Unload image from CPU memory (RAM) */
declare fn UnloadImage as (image: Image)
/** Export image data to file, returns true on success */
declare fn ExportImage as (image: Image, fileName: CString): Bool
/** Export image to memory buffer */
declare fn ExportImageToMemory as (image: Image, fileType: CString, out fileSize: Int): UChar[]c?
/** Export image as code file defining an array of bytes, returns true on success */
declare fn ExportImageAsCode as (image: Image, fileName: CString): Bool

// Image generation functions
/** Generate image: plain color */
declare fn GenImageColor as (width: Int, height: Int, color: Color): Image
/** Generate image: linear gradient, direction in degrees [0..360], 0=Vertical gradient */
declare fn GenImageGradientLinear as (width: Int, height: Int, direction: Int, start: Color, end: Color): Image
/** Generate image: radial gradient */
declare fn GenImageGradientRadial as (width: Int, height: Int, density: Float, inner: Color, outer: Color): Image
/** Generate image: square gradient */
declare fn GenImageGradientSquare as (width: Int, height: Int, density: Float, inner: Color, outer: Color): Image
/** Generate image: checked */
declare fn GenImageChecked as (width: Int, height: Int, checksX: Int, checksY: Int, col1: Color, col2: Color): Image
/** Generate image: white noise */
declare fn GenImageWhiteNoise as (width: Int, height: Int, factor: Float): Image
/** Generate image: perlin noise */
declare fn GenImagePerlinNoise as (width: Int, height: Int, offsetX: Int, offsetY: Int, scale: Float): Image
/** Generate image: cellular algorithm, bigger tileSize means bigger cells */
declare fn GenImageCellular as (width: Int, height: Int, tileSize: Int): Image
/** Generate image: grayscale image from text data */
declare fn GenImageText as (width: Int, height: Int, text: CString): Image

// Image manipulation functions
/** Create an image duplicate (useful for transformations) */
declare fn ImageCopy as (image: Image): Image
/** Create an image from another image piece */
declare fn ImageFromImage as (image: Image, rec_: Rectangle): Image
/** Create an image from a selected channel of another image (GRAYSCALE) */
declare fn ImageFromChannel as (image: Image, selectedChannel: Int): Image
/** Create an image from text (default font) */
declare fn ImageText as (text: CString, fontSize: Int, color: Color): Image
/** Create an image from text (custom sprite font) */
declare fn ImageTextEx as (font: Font, text: CString, fontSize: Float, spacing: Float, tint: Color): Image
/** Convert image data to desired format */
declare fn ImageFormat as (mut image: Image, newFormat: PixelFormat)
/** Convert image to POT (power-of-two) */
declare fn ImageToPOT as (mut image: Image, fill: Color)
/** Crop an image to a defined rectangle */
declare fn ImageCrop as (mut image: Image, crop: Rectangle)
/** Crop image depending on alpha value */
declare fn ImageAlphaCrop as (mut image: Image, threshold: Float)
/** Clear alpha channel to desired color */
declare fn ImageAlphaClear as (mut image: Image, color: Color, threshold: Float)
/** Apply alpha mask to image */
declare fn ImageAlphaMask as (mut image: Image, alphaMask: Image)
/** Premultiply alpha channel */
declare fn ImageAlphaPremultiply as (mut image: Image)
/** Apply Gaussian blur using a box blur approximation */
declare fn ImageBlurGaussian as (mut image: Image, blurSize: Int)
/** Apply custom square convolution kernel to image */
declare fn ImageKernelConvolution as (mut image: Image, kernel: Float[]c, kernelSize: Int)
/** Resize image (Bicubic scaling algorithm) */
declare fn ImageResize as (mut image: Image, newWidth: Int, newHeight: Int)
/** Resize image (Nearest-Neighbor scaling algorithm) */
declare fn ImageResizeNN as (mut image: Image, newWidth: Int, newHeight: Int)
/** Resize canvas and fill with color */
declare fn ImageResizeCanvas as (mut image: Image, newWidth: Int, newHeight: Int, offsetX: Int, offsetY: Int, fill: Color)
/** Compute all mipmap levels for a provided image */
declare fn ImageMipmaps as (mut image: Image)
/** Dither image data to 16bpp or lower (Floyd-Steinberg dithering) */
declare fn ImageDither as (mut image: Image, rBpp: Int, gBpp: Int, bBpp: Int, aBpp: Int)
/** Flip image vertically */
declare fn ImageFlipVertical as (mut image: Image)
/** Flip image horizontally */
declare fn ImageFlipHorizontal as (mut image: Image)
/** Rotate image by input angle in degrees (-359 to 359) */
declare fn ImageRotate as (mut image: Image, degrees: Int)
/** Rotate image clockwise 90deg */
declare fn ImageRotateCW as (mut image: Image)
/** Rotate image counter-clockwise 90deg */
declare fn ImageRotateCCW as (mut image: Image)
/** Modify image color: tint */
declare fn ImageColorTint as (mut image: Image, color: Color)
/** Modify image color: invert */
declare fn ImageColorInvert as (mut image: Image)
/** Modify image color: grayscale */
declare fn ImageColorGrayscale as (mut image: Image)
/** Modify image color: contrast (-100 to 100) */
declare fn ImageColorContrast as (mut image: Image, contrast: Float)
/** Modify image color: brightness (-255 to 255) */
declare fn ImageColorBrightness as (mut image: Image, brightness: Int)
/** Modify image color: replace color */
declare fn ImageColorReplace as (mut image: Image, color: Color, replace: Color)
/** Load color data from image as a Color array (RGBA - 32bit) */
declare fn LoadImageColors as (image: Image): Color[]c?
/** Load colors palette from image as a Color array (RGBA - 32bit) */
declare fn LoadImagePalette as (image: Image, maxPaletteSize: Int, out colorCount: Int): Color[]c?
/** Unload color data loaded with LoadImageColors() */
declare fn UnloadImageColors as (colors: Color[]c)
/** Unload colors palette loaded with LoadImagePalette() */
declare fn UnloadImagePalette as (colors: Color[]c)
/** Get image alpha border rectangle */
declare fn GetImageAlphaBorder as (image: Image, threshold: Float): Rectangle
/** Get image pixel color at (x, y) position */
declare fn GetImageColor as (image: Image, x: Int, y: Int): Color

// Image drawing functions

// NOTE: Image software-rendering functions (CPU)
/** Clear image background with given color */
declare fn ImageClearBackground as (mut dst: Image, color: Color)
/** Draw pixel within an image */
declare fn ImageDrawPixel as (mut dst: Image, posX: Int, posY: Int, color: Color)
/** Draw pixel within an image (Vector version) */
declare fn ImageDrawPixelV as (mut dst: Image, position: Vector2, color: Color)
/** Draw line within an image */
declare fn ImageDrawLine as (mut dst: Image, startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: Color)
/** Draw line within an image (Vector version) */
declare fn ImageDrawLineV as (mut dst: Image, start: Vector2, end: Vector2, color: Color)
/** Draw a line defining thickness within an image */
declare fn ImageDrawLineEx as (mut dst: Image, start: Vector2, end: Vector2, thick: Int, color: Color)
/** Draw a filled circle within an image */
declare fn ImageDrawCircle as (mut dst: Image, centerX: Int, centerY: Int, radius: Int, color: Color)
/** Draw a filled circle within an image (Vector version) */
declare fn ImageDrawCircleV as (mut dst: Image, center: Vector2, radius: Int, color: Color)
/** Draw circle outline within an image */
declare fn ImageDrawCircleLines as (mut dst: Image, centerX: Int, centerY: Int, radius: Int, color: Color)
/** Draw circle outline within an image (Vector version) */
declare fn ImageDrawCircleLinesV as (mut dst: Image, center: Vector2, radius: Int, color: Color)
/** Draw rectangle within an image */
declare fn ImageDrawRectangle as (mut dst: Image, posX: Int, posY: Int, width: Int, height: Int, color: Color)
/** Draw rectangle within an image (Vector version) */
declare fn ImageDrawRectangleV as (mut dst: Image, position: Vector2, size: Vector2, color: Color)
/** Draw rectangle within an image */
declare fn ImageDrawRectangleRec as (mut dst: Image, rec_: Rectangle, color: Color)
/** Draw rectangle lines within an image */
declare fn ImageDrawRectangleLines as (mut dst: Image, rec_: Rectangle, thick: Int, color: Color)
/** Draw triangle within an image */
declare fn ImageDrawTriangle as (mut dst: Image, v1: Vector2, v2: Vector2, v3: Vector2, color: Color)
/** Draw triangle with interpolated colors within an image */
declare fn ImageDrawTriangleEx as (mut dst: Image, v1: Vector2, v2: Vector2, v3: Vector2, c1: Color, c2: Color, c3: Color)
/** Draw triangle outline within an image */
declare fn ImageDrawTriangleLines as (mut dst: Image, v1: Vector2, v2: Vector2, v3: Vector2, color: Color)
/** Draw a triangle fan defined by points within an image (first vertex is the center) */
declare fn ImageDrawTriangleFan as (mut dst: Image, points: Vector2[]c, pointCount: Int, color: Color)
/** Draw a triangle strip defined by points within an image */
declare fn ImageDrawTriangleStrip as (mut dst: Image, points: Vector2[]c, pointCount: Int, color: Color)
/** Draw a source image within a destination image (tint applied to source) */
declare fn ImageDraw as (mut dst: Image, src: Image, srcRec: Rectangle, dstRec: Rectangle, tint: Color)
/** Draw text (using default font) within an image (destination) */
declare fn ImageDrawText as (mut dst: Image, text: CString, posX: Int, posY: Int, fontSize: Int, color: Color)
/** Draw text (custom sprite font) within an image (destination) */
declare fn ImageDrawTextEx as (mut dst: Image, font: Font, text: CString, position: Vector2, fontSize: Float, spacing: Float, tint: Color)

// Texture loading functions

// NOTE: These functions require GPU access
/** Load texture from file into GPU memory (VRAM) */
declare fn LoadTexture as (fileName: CString): Texture2D
/** Load texture from image data */
declare fn LoadTextureFromImage as (image: Image): Texture2D
/** Load cubemap from image, multiple image cubemap layouts supported */
declare fn LoadTextureCubemap as (image: Image, layout: CubemapLayout): TextureCubemap
/** Load texture for rendering (framebuffer) */
declare fn LoadRenderTexture as (width: Int, height: Int): RenderTexture2D
/** Check if a texture is valid (loaded in GPU) */
declare fn IsTextureValid as (texture: Texture2D): Bool
/** Unload texture from GPU memory (VRAM) */
declare fn UnloadTexture as (texture: Texture2D)
/** Check if a render texture is valid (loaded in GPU) */
declare fn IsRenderTextureValid as (target: RenderTexture2D): Bool
/** Unload render texture from GPU memory (VRAM) */
declare fn UnloadRenderTexture as (target: RenderTexture2D)
/** Update GPU texture with new data */
declare fn UpdateTexture as (texture: Texture2D, pixels: @Any)
/** Update GPU texture rectangle with new data */
declare fn UpdateTextureRec as (texture: Texture2D, rec_: Rectangle, pixels: @Any)

// Texture configuration functions
/** Generate GPU mipmaps for a texture */
declare fn GenTextureMipmaps as (mut texture: Texture2D)
/** Set texture scaling filter mode */
declare fn SetTextureFilter as (texture: Texture2D, filter: TextureFilter)
/** Set texture wrapping mode */
declare fn SetTextureWrap as (texture: Texture2D, wrap: TextureWrap)

// Texture drawing functions
/** Draw a Texture2D */
declare fn DrawTexture as (texture: Texture2D, posX: Int, posY: Int, tint: Color)
/** Draw a Texture2D with position defined as Vector2 */
declare fn DrawTextureV as (texture: Texture2D, position: Vector2, tint: Color)
/** Draw a Texture2D with extended parameters */
declare fn DrawTextureEx as (texture: Texture2D, position: Vector2, rotation: Float, scale: Float, tint: Color)
/** Draw a part of a texture defined by a rectangle */
declare fn DrawTextureRec as (texture: Texture2D, source: Rectangle, position: Vector2, tint: Color)
/** Draw a part of a texture defined by a rectangle with 'pro' parameters */
declare fn DrawTexturePro as (texture: Texture2D, source: Rectangle, dest: Rectangle, origin: Vector2, rotation: Float, tint: Color)
/** Draws a texture (or part of it) that stretches or shrinks nicely */
declare fn DrawTextureNPatch as (texture: Texture2D, nPatchInfo: NPatchInfo, dest: Rectangle, origin: Vector2, rotation: Float, tint: Color)

// Color/pixel related functions
/** Check if two colors are equal */
declare fn ColorIsEqual as (col1: Color, col2: Color): Bool
/** Get color with alpha applied, alpha goes from 0.0f to 1.0f */
declare fn Fade as (color: Color, alpha: Float): Color
/** Get hexadecimal value for a Color (0xRRGGBBAA) */
declare fn ColorToInt as (color: Color): Int
/** Get Color normalized as float [0..1] */
declare fn ColorNormalize as (color: Color): Vector4
/** Get Color from normalized values [0..1] */
declare fn ColorFromNormalized as (normalized: Vector4): Color
/** Get HSV values for a Color, hue [0..360], saturation/value [0..1] */
declare fn ColorToHSV as (color: Color): Vector3
/** Get a Color from HSV values, hue [0..360], saturation/value [0..1] */
declare fn ColorFromHSV as (hue: Float, saturation: Float, value: Float): Color
/** Get color multiplied with another color */
declare fn ColorTint as (color: Color, tint: Color): Color
/** Get color with brightness correction, brightness factor goes from -1.0f to 1.0f */
declare fn ColorBrightness as (color: Color, factor: Float): Color
/** Get color with contrast correction, contrast values between -1.0f and 1.0f */
declare fn ColorContrast as (color: Color, contrast: Float): Color
/** Get color with alpha applied, alpha goes from 0.0f to 1.0f */
declare fn ColorAlpha as (color: Color, alpha: Float): Color
/** Get src alpha-blended into dst color with tint */
declare fn ColorAlphaBlend as (dst: Color, src: Color, tint: Color): Color
/** Get color lerp interpolation between two colors, factor [0.0f..1.0f] */
declare fn ColorLerp as (color1: Color, color2: Color, factor: Float): Color
/** Get Color structure from hexadecimal value */
declare fn GetColor as (hexValue: UInt): Color
/** Get Color from a source pixel pointer of certain format */
declare fn GetPixelColor as (srcPtr: @Any, format: PixelFormat): Color
/** Set color formatted into destination pixel pointer */
declare fn SetPixelColor as (dstPtr: @Any, color: Color, format: PixelFormat)
/** Get pixel data size in bytes for certain format */
declare fn GetPixelDataSize as (width: Int, height: Int, format: PixelFormat): Int

// ---------------------------------------------------------------------------------------------------------------------
// Font Loading and Text Drawing Functions (Module: text)
// ---------------------------------------------------------------------------------------------------------------------

// Font loading/unloading functions
/** Get the default Font */
declare fn GetFontDefault as (): Font
/** Load font from file into GPU memory (VRAM) */
declare fn LoadFont as (fileName: CString): Font
/** Load font from file with extended parameters, use NULL for codepoints and 0 for codepointCount to load the default character set, font size is provided in pixels height */
declare fn LoadFontEx as (fileName: CString, fontSize: Int, codepoints: Int[]c?, codepointCount: Int): Font
/** Load font from Image (XNA style) */
declare fn LoadFontFromImage as (image: Image, key: Color, firstChar: Int): Font
/** Load font from memory buffer, fileType refers to extension: i.e. '.ttf' */
declare fn LoadFontFromMemory as (fileType: CString, fileData: UChar[]c, dataSize: Int, fontSize: Int, codepoints: Int[]c?, codepointCount: Int): Font
/** Check if a font is valid (font data loaded, WARNING: GPU texture not checked) */
declare fn IsFontValid as (font: Font): Bool
/** Load font data for further use */
declare fn LoadFontData as (fileData: UChar[]c, dataSize: Int, fontSize: Int, codepoints: Int[]c?, codepointCount: Int, type_: FontType): GlyphInfo[]c?
/** Generate image font atlas using chars info */
declare fn GenImageFontAtlas as (glyphs: GlyphInfo[]c, out glyphRecs: Rectangle[]c, glyphCount: Int, fontSize: Int, padding: Int, packMethod: Int): Image
/** Unload font chars info data (RAM) */
declare fn UnloadFontData as (glyphs: GlyphInfo[]c, glyphCount: Int)
/** Unload font from GPU memory (VRAM) */
declare fn UnloadFont as (font: Font)
/** Export font as code file, returns true on success */
declare fn ExportFontAsCode as (font: Font, fileName: CString): Bool

// Text drawing functions
/** Draw current FPS */
declare fn DrawFPS as (posX: Int, posY: Int)
/** Draw text (using default font) */
declare fn DrawText as (text: CString, posX: Int, posY: Int, fontSize: Int, color: Color)
/** Draw text using font and additional parameters */
declare fn DrawTextEx as (font: Font, text: CString, position: Vector2, fontSize: Float, spacing: Float, tint: Color)
/** Draw text using Font and pro parameters (rotation) */
declare fn DrawTextPro as (font: Font, text: CString, position: Vector2, origin: Vector2, rotation: Float, fontSize: Float, spacing: Float, tint: Color)
/** Draw one character (codepoint) */
declare fn DrawTextCodepoint as (font: Font, codepoint: Int, position: Vector2, fontSize: Float, tint: Color)
/** Draw multiple character (codepoint) */
declare fn DrawTextCodepoints as (font: Font, codepoints: Int[]c, codepointCount: Int, position: Vector2, fontSize: Float, spacing: Float, tint: Color)

// Text font info functions
/** Set vertical line spacing when drawing with line-breaks */
declare fn SetTextLineSpacing as (spacing: Int)
/** Measure string width for default font */
declare fn MeasureText as (text: CString, fontSize: Int): Int
/** Measure string size for Font */
declare fn MeasureTextEx as (font: Font, text: CString, fontSize: Float, spacing: Float): Vector2
/** Get glyph index position in font for a codepoint (unicode character), fallback to '?' if not found */
declare fn GetGlyphIndex as (font: Font, codepoint: Int): Int
/** Get glyph font info data for a codepoint (unicode character), fallback to '?' if not found */
declare fn GetGlyphInfo as (font: Font, codepoint: Int): GlyphInfo
/** Get glyph rectangle in font atlas for a codepoint (unicode character), fallback to '?' if not found */
declare fn GetGlyphAtlasRec as (font: Font, codepoint: Int): Rectangle

// Text codepoints management functions (unicode characters)
/** Load UTF-8 text encoded from codepoints array */
declare fn LoadUTF8 as (codepoints: Int[]c, length: Int): CString
/** Unload UTF-8 text encoded from codepoints array */
declare fn UnloadUTF8 as (text: CString)
/** Load all codepoints from a UTF-8 text string, codepoints count returned by parameter */
declare fn LoadCodepoints as (text: CString, out count: Int): Int[]c
/** Unload codepoints data from memory */
declare fn UnloadCodepoints as (codepoints: Int[]c)
/** Get total number of codepoints in a UTF-8 encoded string */
declare fn GetCodepointCount as (text: CString): Int
/** Get next codepoint in a UTF-8 encoded string, 0x3f('?') is returned on failure */
declare fn GetCodepoint as (text: CString, out codepointSize: Int): Int
/** Get next codepoint in a UTF-8 encoded string, 0x3f('?') is returned on failure */
declare fn GetCodepointNext as (text: CString, out codepointSize: Int): Int
/** Get previous codepoint in a UTF-8 encoded string, 0x3f('?') is returned on failure */
declare fn GetCodepointPrevious as (text: CString, out codepointSize: Int): Int
/** Encode one codepoint into UTF-8 byte array (array length returned as parameter) */
declare fn CodepointToUTF8 as (codepoint: Int, out utf8Size: Int): CString

// Text strings management functions (no UTF-8 strings, only byte chars)

// NOTE: Some strings allocate memory internally for returned strings, just be careful!
/** Copy one string to another, returns bytes copied */
declare fn TextCopy as (dst: Char[]c, src: CString): Int
/** Check if two text string are equal */
declare fn TextIsEqual as (text1: CString, text2: CString): Bool
/** Get text length, checks for '\0' ending */
declare fn TextLength as (text: CString): UInt
/** Text formatting with variables (sprintf() style) */
declare fn TextFormat as (text: CString, ...): CString
/** Get a piece of a text string */
declare fn TextSubtext as (text: CString, position: Int, length: Int): CString
/** Replace text string (WARNING: memory must be freed!) */
declare fn TextReplace as (text: CString, replace: CString, by: CString): CString?
/** Insert text in a position (WARNING: memory must be freed!) */
declare fn TextInsert as (text: CString, insert: CString, position: Int): CString
/** Join text strings with delimiter */
declare fn TextJoin as (textList: CString[]c, count: Int, delimiter: CString): CString
/** Split text into multiple strings */
declare fn TextSplit as (text: CString, delimiter: Char, out count: Int): CString[]c
/** Append text at specific position and move cursor! */
declare fn TextAppend as (text: Char[]c, append: CString, mut position: Int)
/** Find first text occurrence within a string */
declare fn TextFindIndex as (text: CString, find: CString): Int
/** Get upper case version of provided string */
declare fn TextToUpper as (text: CString): CString
/** Get lower case version of provided string */
declare fn TextToLower as (text: CString): CString
/** Get Pascal case notation version of provided string */
declare fn TextToPascal as (text: CString): CString
/** Get Snake case notation version of provided string */
declare fn TextToSnake as (text: CString): CString
/** Get Camel case notation version of provided string */
declare fn TextToCamel as (text: CString): CString
/** Get integer value from text (negative values not supported) */
declare fn TextToInteger as (text: CString): Int
/** Get float value from text (negative values not supported) */
declare fn TextToFloat as (text: CString): Float

// ---------------------------------------------------------------------------------------------------------------------
// Basic 3d Shapes Drawing Functions (Module: models)
// ---------------------------------------------------------------------------------------------------------------------

// Basic geometric 3D shapes drawing functions
/** Draw a line in 3D world space */
declare fn DrawLine3D as (startPos: Vector3, endPos: Vector3, color: Color)
/** Draw a point in 3D space, actually a small line */
declare fn DrawPoint3D as (position: Vector3, color: Color)
/** Draw a circle in 3D world space */
declare fn DrawCircle3D as (center: Vector3, radius: Float, rotationAxis: Vector3, rotationAngle: Float, color: Color)
/** Draw a color-filled triangle (vertex in counter-clockwise order!) */
declare fn DrawTriangle3D as (v1: Vector3, v2: Vector3, v3: Vector3, color: Color)
/** Draw a triangle strip defined by points */
declare fn DrawTriangleStrip3D as (points: Vector3[]c, pointCount: Int, color: Color)
/** Draw cube */
declare fn DrawCube as (position: Vector3, width: Float, height: Float, length: Float, color: Color)
/** Draw cube (Vector version) */
declare fn DrawCubeV as (position: Vector3, size: Vector3, color: Color)
/** Draw cube wires */
declare fn DrawCubeWires as (position: Vector3, width: Float, height: Float, length: Float, color: Color)
/** Draw cube wires (Vector version) */
declare fn DrawCubeWiresV as (position: Vector3, size: Vector3, color: Color)
/** Draw sphere */
declare fn DrawSphere as (centerPos: Vector3, radius: Float, color: Color)
/** Draw sphere with extended parameters */
declare fn DrawSphereEx as (centerPos: Vector3, radius: Float, rings: Int, slices: Int, color: Color)
/** Draw sphere wires */
declare fn DrawSphereWires as (centerPos: Vector3, radius: Float, rings: Int, slices: Int, color: Color)
/** Draw a cylinder/cone */
declare fn DrawCylinder as (position: Vector3, radiusTop: Float, radiusBottom: Float, height: Float, slices: Int, color: Color)
/** Draw a cylinder with base at startPos and top at endPos */
declare fn DrawCylinderEx as (startPos: Vector3, endPos: Vector3, startRadius: Float, endRadius: Float, sides: Int, color: Color)
/** Draw a cylinder/cone wires */
declare fn DrawCylinderWires as (position: Vector3, radiusTop: Float, radiusBottom: Float, height: Float, slices: Int, color: Color)
/** Draw a cylinder wires with base at startPos and top at endPos */
declare fn DrawCylinderWiresEx as (startPos: Vector3, endPos: Vector3, startRadius: Float, endRadius: Float, sides: Int, color: Color)
/** Draw a capsule with the center of its sphere caps at startPos and endPos */
declare fn DrawCapsule as (startPos: Vector3, endPos: Vector3, radius: Float, slices: Int, rings: Int, color: Color)
/** Draw capsule wireframe with the center of its sphere caps at startPos and endPos */
declare fn DrawCapsuleWires as (startPos: Vector3, endPos: Vector3, radius: Float, slices: Int, rings: Int, color: Color)
/** Draw a plane XZ */
declare fn DrawPlane as (centerPos: Vector3, size: Vector2, color: Color)
/** Draw a ray line */
declare fn DrawRay as (ray: Ray, color: Color)
/** Draw a grid (centered at (0, 0, 0)) */
declare fn DrawGrid as (slices: Int, spacing: Float)

// ---------------------------------------------------------------------------------------------------------------------
// Model 3d Loading and Drawing Functions (Module: models)
// ---------------------------------------------------------------------------------------------------------------------

// Model management functions
/** Load model from files (meshes and materials) */
declare fn LoadModel as (fileName: CString): Model
/** Load model from generated mesh (default material) */
declare fn LoadModelFromMesh as (mesh: Mesh): Model
/** Check if a model is valid (loaded in GPU, VAO/VBOs) */
declare fn IsModelValid as (model: Model): Bool
/** Unload model (including meshes) from memory (RAM and/or VRAM) */
declare fn UnloadModel as (model: Model)
/** Compute model bounding box limits (considers all meshes) */
declare fn GetModelBoundingBox as (model: Model): BoundingBox

// Model drawing functions
/** Draw a model (with texture if set) */
declare fn DrawModel as (model: Model, position: Vector3, scale: Float, tint: Color)
/** Draw a model with extended parameters */
declare fn DrawModelEx as (model: Model, position: Vector3, rotationAxis: Vector3, rotationAngle: Float, scale: Vector3, tint: Color)
/** Draw a model wires (with texture if set) */
declare fn DrawModelWires as (model: Model, position: Vector3, scale: Float, tint: Color)
/** Draw a model wires (with texture if set) with extended parameters */
declare fn DrawModelWiresEx as (model: Model, position: Vector3, rotationAxis: Vector3, rotationAngle: Float, scale: Vector3, tint: Color)
/** Draw a model as points */
declare fn DrawModelPoints as (model: Model, position: Vector3, scale: Float, tint: Color)
/** Draw a model as points with extended parameters */
declare fn DrawModelPointsEx as (model: Model, position: Vector3, rotationAxis: Vector3, rotationAngle: Float, scale: Vector3, tint: Color)
/** Draw bounding box (wires) */
declare fn DrawBoundingBox as (box: BoundingBox, color: Color)
/** Draw a billboard texture */
declare fn DrawBillboard as (camera: Camera, texture: Texture2D, position: Vector3, scale: Float, tint: Color)
/** Draw a billboard texture defined by source */
declare fn DrawBillboardRec as (camera: Camera, texture: Texture2D, source: Rectangle, position: Vector3, size: Vector2, tint: Color)
/** Draw a billboard texture defined by source and rotation */
declare fn DrawBillboardPro as (camera: Camera, texture: Texture2D, source: Rectangle, position: Vector3, up: Vector3, size: Vector2, origin: Vector2, rotation: Float, tint: Color)

// Mesh management functions
/** Upload mesh vertex data in GPU and provide VAO/VBO ids */
declare fn UploadMesh as (mut mesh: Mesh, dynamic: Bool)
/** Update mesh vertex data in GPU for a specific buffer index */
declare fn UpdateMeshBuffer as (mesh: Mesh, index: Int, data: @Any, dataSize: Int, offset: Int)
/** Unload mesh data from CPU and GPU */
declare fn UnloadMesh as (mesh: Mesh)
/** Draw a 3d mesh with material and transform */
declare fn DrawMesh as (mesh: Mesh, material: Material, transform: Matrix)
/** Draw multiple mesh instances with material and different transforms */
declare fn DrawMeshInstanced as (mesh: Mesh, material: Material, transforms: Matrix[]c, instances: Int)
/** Compute mesh bounding box limits */
declare fn GetMeshBoundingBox as (mesh: Mesh): BoundingBox
/** Compute mesh tangents */
declare fn GenMeshTangents as (mut mesh: Mesh)
/** Export mesh data to file, returns true on success */
declare fn ExportMesh as (mesh: Mesh, fileName: CString): Bool
/** Export mesh as code file (.h) defining multiple arrays of vertex attributes */
declare fn ExportMeshAsCode as (mesh: Mesh, fileName: CString): Bool

// Mesh generation functions
/** Generate polygonal mesh */
declare fn GenMeshPoly as (sides: Int, radius: Float): Mesh
/** Generate plane mesh (with subdivisions) */
declare fn GenMeshPlane as (width: Float, length: Float, resX: Int, resZ: Int): Mesh
/** Generate cuboid mesh */
declare fn GenMeshCube as (width: Float, height: Float, length: Float): Mesh
/** Generate sphere mesh (standard sphere) */
declare fn GenMeshSphere as (radius: Float, rings: Int, slices: Int): Mesh
/** Generate half-sphere mesh (no bottom cap) */
declare fn GenMeshHemiSphere as (radius: Float, rings: Int, slices: Int): Mesh
/** Generate cylinder mesh */
declare fn GenMeshCylinder as (radius: Float, height: Float, slices: Int): Mesh
/** Generate cone/pyramid mesh */
declare fn GenMeshCone as (radius: Float, height: Float, slices: Int): Mesh
/** Generate torus mesh */
declare fn GenMeshTorus as (radius: Float, size: Float, radSeg: Int, sides: Int): Mesh
/** Generate trefoil knot mesh */
declare fn GenMeshKnot as (radius: Float, size: Float, radSeg: Int, sides: Int): Mesh
/** Generate heightmap mesh from image data */
declare fn GenMeshHeightmap as (heightmap: Image, size: Vector3): Mesh
/** Generate cubes-based map mesh from image data */
declare fn GenMeshCubicmap as (cubicmap: Image, cubeSize: Vector3): Mesh

// Material loading/unloading functions
/** Load materials from model file */
declare fn LoadMaterials as (fileName: CString, out materialCount: Int): Material[]c?
/** Load default material (Supports: DIFFUSE, SPECULAR, NORMAL maps) */
declare fn LoadMaterialDefault as (): Material
/** Check if a material is valid (shader assigned, map textures loaded in GPU) */
declare fn IsMaterialValid as (material: Material): Bool
/** Unload material from GPU memory (VRAM) */
declare fn UnloadMaterial as (material: Material)
/** Set texture for a material map type (MATERIAL_MAP_DIFFUSE, MATERIAL_MAP_SPECULAR...) */
declare fn SetMaterialTexture as (mut material: Material, mapType: Int, texture: Texture2D)
/** Set material for a mesh */
declare fn SetModelMeshMaterial as (mut model: Model, meshId: Int, materialId: Int)

// Model animations loading/unloading functions
/** Load model animations from file */
declare fn LoadModelAnimations as (fileName: CString, out animCount: Int): ModelAnimation[]c?
/** Update model animation pose (CPU) */
declare fn UpdateModelAnimation as (model: Model, anim: ModelAnimation, frame: Int)
/** Update model animation mesh bone matrices (GPU skinning) */
declare fn UpdateModelAnimationBones as (model: Model, anim: ModelAnimation, frame: Int)
/** Unload animation data */
declare fn UnloadModelAnimation as (anim: ModelAnimation)
/** Unload animation array data */
declare fn UnloadModelAnimations as (animations: ModelAnimation[]c, animCount: Int)
/** Check model animation skeleton match */
declare fn IsModelAnimationValid as (model: Model, anim: ModelAnimation): Bool

// Collision detection functions
/** Check collision between two spheres */
declare fn CheckCollisionSpheres as (center1: Vector3, radius1: Float, center2: Vector3, radius2: Float): Bool
/** Check collision between two bounding boxes */
declare fn CheckCollisionBoxes as (box1: BoundingBox, box2: BoundingBox): Bool
/** Check collision between box and sphere */
declare fn CheckCollisionBoxSphere as (box: BoundingBox, center: Vector3, radius: Float): Bool
/** Get collision info between ray and sphere */
declare fn GetRayCollisionSphere as (ray: Ray, center: Vector3, radius: Float): RayCollision
/** Get collision info between ray and box */
declare fn GetRayCollisionBox as (ray: Ray, box: BoundingBox): RayCollision
/** Get collision info between ray and mesh */
declare fn GetRayCollisionMesh as (ray: Ray, mesh: Mesh, transform: Matrix): RayCollision
/** Get collision info between ray and triangle */
declare fn GetRayCollisionTriangle as (ray: Ray, p1: Vector3, p2: Vector3, p3: Vector3): RayCollision
/** Get collision info between ray and quad */
declare fn GetRayCollisionQuad as (ray: Ray, p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3): RayCollision

// ---------------------------------------------------------------------------------------------------------------------
// Audio Loading and Playing Functions (Module: audio)
// ---------------------------------------------------------------------------------------------------------------------

// Audio device management functions
/** Initialize audio device and context */
declare fn InitAudioDevice as ()
/** Close the audio device and context */
declare fn CloseAudioDevice as ()
/** Check if audio device has been initialized successfully */
declare fn IsAudioDeviceReady as (): Bool
/** Set master volume (listener) */
declare fn SetMasterVolume as (volume: Float)
/** Get master volume (listener) */
declare fn GetMasterVolume as (): Float

// Wave/Sound loading/unloading functions
/** Load wave data from file */
declare fn LoadWave as (fileName: CString): Wave
/** Load wave from memory buffer, fileType refers to extension: i.e. '.wav' */
declare fn LoadWaveFromMemory as (fileType: CString, fileData: UChar[]c, dataSize: Int): Wave
/** Checks if wave data is valid (data loaded and parameters) */
declare fn IsWaveValid as (wave: Wave): Bool
/** Load sound from file */
declare fn LoadSound as (fileName: CString): Sound
/** Load sound from wave data */
declare fn LoadSoundFromWave as (wave: Wave): Sound
/** Create a new sound that shares the same sample data as the source sound, does not own the sound data */
declare fn LoadSoundAlias as (source: Sound): Sound
/** Checks if a sound is valid (data loaded and buffers initialized) */
declare fn IsSoundValid as (sound: Sound): Bool
/** Update sound buffer with new data */
declare fn UpdateSound as (sound: Sound, data: @Any, sampleCount: Int)
/** Unload wave data */
declare fn UnloadWave as (wave: Wave)
/** Unload sound */
declare fn UnloadSound as (sound: Sound)
/** Unload a sound alias (does not deallocate sample data) */
declare fn UnloadSoundAlias as (alias: Sound)
/** Export wave data to file, returns true on success */
declare fn ExportWave as (wave: Wave, fileName: CString): Bool
/** Export wave sample data to code (.h), returns true on success */
declare fn ExportWaveAsCode as (wave: Wave, fileName: CString): Bool

// Wave/Sound management functions
/** Play a sound */
declare fn PlaySound as (sound: Sound)
/** Stop playing a sound */
declare fn StopSound as (sound: Sound)
/** Pause a sound */
declare fn PauseSound as (sound: Sound)
/** Resume a paused sound */
declare fn ResumeSound as (sound: Sound)
/** Check if a sound is currently playing */
declare fn IsSoundPlaying as (sound: Sound): Bool
/** Set volume for a sound (1.0 is max level) */
declare fn SetSoundVolume as (sound: Sound, volume: Float)
/** Set pitch for a sound (1.0 is base level) */
declare fn SetSoundPitch as (sound: Sound, pitch: Float)
/** Set pan for a sound (0.5 is center) */
declare fn SetSoundPan as (sound: Sound, pan: Float)
/** Copy a wave to a new wave */
declare fn WaveCopy as (wave: Wave): Wave
/** Crop a wave to defined frames range */
declare fn WaveCrop as (mut wave: Wave, initFrame: Int, finalFrame: Int)
/** Convert wave data to desired format */
declare fn WaveFormat as (mut wave: Wave, sampleRate: Int, sampleSize: Int, channels: Int)
/** Load samples data from wave as a 32bit float data array */
declare fn LoadWaveSamples as (wave: Wave): Float[]c
/** Unload samples data loaded with LoadWaveSamples() */
declare fn UnloadWaveSamples as (samples: Float[]c)

// Music management functions
/** Load music stream from file */
declare fn LoadMusicStream as (fileName: CString): Music
/** Load music stream from data */
declare fn LoadMusicStreamFromMemory as (fileType: CString, data: UChar[]c, dataSize: Int): Music
/** Checks if a music stream is valid (context and buffers initialized) */
declare fn IsMusicValid as (music: Music): Bool
/** Unload music stream */
declare fn UnloadMusicStream as (music: Music)
/** Start music playing */
declare fn PlayMusicStream as (music: Music)
/** Check if music is playing */
declare fn IsMusicStreamPlaying as (music: Music): Bool
/** Updates buffers for music streaming */
declare fn UpdateMusicStream as (music: Music)
/** Stop music playing */
declare fn StopMusicStream as (music: Music)
/** Pause music playing */
declare fn PauseMusicStream as (music: Music)
/** Resume playing paused music */
declare fn ResumeMusicStream as (music: Music)
/** Seek music to a position (in seconds) */
declare fn SeekMusicStream as (music: Music, position: Float)
/** Set volume for music (1.0 is max level) */
declare fn SetMusicVolume as (music: Music, volume: Float)
/** Set pitch for a music (1.0 is base level) */
declare fn SetMusicPitch as (music: Music, pitch: Float)
/** Set pan for a music (0.5 is center) */
declare fn SetMusicPan as (music: Music, pan: Float)
/** Get music time length (in seconds) */
declare fn GetMusicTimeLength as (music: Music): Float
/** Get current music time played (in seconds) */
declare fn GetMusicTimePlayed as (music: Music): Float

// AudioStream management functions
/** Load audio stream (to stream raw audio pcm data) */
declare fn LoadAudioStream as (sampleRate: UInt, sampleSize: UInt, channels: UInt): AudioStream
/** Checks if an audio stream is valid (buffers initialized) */
declare fn IsAudioStreamValid as (stream: AudioStream): Bool
/** Unload audio stream and free memory */
declare fn UnloadAudioStream as (stream: AudioStream)
/** Update audio stream buffers with data */
declare fn UpdateAudioStream as (stream: AudioStream, data: @Any, frameCount: Int)
/** Check if any audio stream buffers requires refill */
declare fn IsAudioStreamProcessed as (stream: AudioStream): Bool
/** Play audio stream */
declare fn PlayAudioStream as (stream: AudioStream)
/** Pause audio stream */
declare fn PauseAudioStream as (stream: AudioStream)
/** Resume audio stream */
declare fn ResumeAudioStream as (stream: AudioStream)
/** Check if audio stream is playing */
declare fn IsAudioStreamPlaying as (stream: AudioStream): Bool
/** Stop audio stream */
declare fn StopAudioStream as (stream: AudioStream)
/** Set volume for audio stream (1.0 is max level) */
declare fn SetAudioStreamVolume as (stream: AudioStream, volume: Float)
/** Set pitch for audio stream (1.0 is base level) */
declare fn SetAudioStreamPitch as (stream: AudioStream, pitch: Float)
/** Set pan for audio stream (0.5 is centered) */
declare fn SetAudioStreamPan as (stream: AudioStream, pan: Float)
/** Default size for new audio streams */
declare fn SetAudioStreamBufferSizeDefault as (size: Int)
/** Audio thread callback to request new data */
declare fn SetAudioStreamCallback as (stream: AudioStream, callback: AudioCallback)
/** Attach audio stream processor to stream, receives the samples as 'float' */
declare fn AttachAudioStreamProcessor as (stream: AudioStream, processor: AudioCallback)
/** Detach audio stream processor from stream */
declare fn DetachAudioStreamProcessor as (stream: AudioStream, processor: AudioCallback)
/** Attach audio stream processor to the entire audio pipeline, receives the samples as 'float' */
declare fn AttachAudioMixedProcessor as (processor: AudioCallback)
/** Detach audio stream processor from the entire audio pipeline */
declare fn DetachAudioMixedProcessor as (processor: AudioCallback)
