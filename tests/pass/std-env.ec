use "core:io.ec"
use "core:string.ec"
use "core:os.ec"
use "core:env.ec"

fn show(text: String) do
    val values = try env.parse(text) catch e do
        io.log("error: ${e.message}")
        ret
    for key, value in values do
        io.log("${key} = ${quote(value)}")
    io.log("--")

fn main(): Int throws Error do
    show("A=1\nB = two words  \n# comment\n\n  export C=3 # trailing\nD=\nCOLOR=#fff\n")
    show("S='no \${A} \\n here'\nD=\"tab\\tquote\\\" dollar\\$ back\\\\ odd\\q\"\n")
    show("M=\"line one\nline two\"\r\nN='a\r\nb'\n")
    show("HOME_X=\${ECLANG_ENV_UNSET}\nA=1\nP=\${A}/bin:\${A}\nQ=\"\${P}!\"\nR=$A\n")
    show("\xEF\xBB\xBFX=1\nX=2")
    show("A.b_c9=ok")

    show("=1\n")
    show("1A=1\n")
    show("A\n")
    show("A=\"open\nstill\n")
    show("A=\"x\" y\n")
    show("A=\${\n")
    show("A=\${B\n")

    val dir = try io.tempDir()
    val path = "${dir}/eclang-std-env-${os.pid()}.env"
    try io.writeFile(path, "ECLANG_ENV_A=from file\nECLANG_ENV_B=\${ECLANG_ENV_A}!\nECLANG_ENV_KEEP=file\n")
    try os.setEnv("ECLANG_ENV_KEEP", "shell")
    io.log(try env.load(path))
    io.log(env.get("ECLANG_ENV_A") ?? "?")
    io.log(env.get("ECLANG_ENV_B") ?? "?")
    io.log(env.get("ECLANG_ENV_KEEP") ?? "?")
    io.log(try env.load(path, override = true))
    io.log(env.get("ECLANG_ENV_KEEP") ?? "?")
    io.log(try env.loadIfExists("/nonexistent/.env"))
    try env.load("/nonexistent/.env") catch e do
        io.log("load: ${describe(e)}")
    io.log(try env.require("ECLANG_ENV_A"))
    try env.require("ECLANG_ENV_MISSING") catch e2 do
        io.log(e2.message)
    try io.remove(path)
    ret 0
