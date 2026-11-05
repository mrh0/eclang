#!/usr/bin/env python3
"""Runs core:term.ec in a real pseudo-terminal: raw mode, keys, resizing, prompts, and that the terminal comes back
after the program ends in any way (return, exit, a panic, a signal) and while it is stopped.

Usage: tests/term-pty.py COMPILER_JAR OUT_DIR"""
import fcntl, os, pty, select, signal, struct, subprocess, sys, termios, time

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
failures = []


class Run:
    """The driver program in a new pseudo-terminal, with everything it wrote in `seen`."""

    def __init__(self, binary, mode, rows=24, cols=80):
        master, slave = pty.openpty()
        fcntl.ioctl(slave, termios.TIOCSWINSZ, struct.pack("HHHH", rows, cols, 0, 0))
        self.master, self.seen, self.status = master, b"", None
        self.before = termios.tcgetattr(slave)
        self.slave = slave
        self.pid = os.fork()
        if self.pid == 0:
            os.setsid()
            fcntl.ioctl(slave, termios.TIOCSCTTY, 0)
            for fd in (0, 1, 2):
                os.dup2(slave, fd)
            os.environ.update(TERM="xterm-256color")
            os.execv(binary, [binary, mode])

    def pump(self, timeout=0.05):
        while select.select([self.master], [], [], timeout)[0]:
            try:
                data = os.read(self.master, 65536)
            except OSError:
                return
            if not data:
                return
            self.seen += data

    def wait_for(self, text, timeout=5):
        """Reads until `text` was written after what the last wait_for found."""
        end = time.time() + timeout
        while time.time() < end:
            self.pump()
            if text.encode() in self.seen:
                return True
        raise AssertionError(f"never saw {text!r}; got {self.seen[-300:]!r}")

    def send(self, data):
        os.write(self.master, data if isinstance(data, bytes) else data.encode())

    def modes(self):
        return termios.tcgetattr(self.slave)

    def restored(self):
        return self.modes()[:4] == self.before[:4]

    def finish(self, timeout=5):
        end = time.time() + timeout
        while time.time() < end:
            self.pump()
            pid, status = os.waitpid(self.pid, os.WNOHANG)
            if pid:
                self.status = status
                self.pump()
                return status
        os.kill(self.pid, signal.SIGKILL)
        os.waitpid(self.pid, 0)
        raise AssertionError(f"still running; got {self.seen[-300:]!r}")

    def close(self):
        os.close(self.master)
        os.close(self.slave)


def check(condition, what):
    if not condition:
        raise AssertionError(what)


def left_cleanly(r):
    check(r.restored(), "the terminal settings weren't put back")
    check(b"\x1b[?1049l" in r.seen, "the alternate screen wasn't left")
    check(r.seen.rfind(b"\x1b[?25h") > r.seen.rfind(b"\x1b[?25l"), "the cursor wasn't shown again")


def t_keys(b):
    r = Run(b, "events")
    r.wait_for("<ready>")
    check(not r.modes()[3] & termios.ICANON and not r.modes()[3] & termios.ECHO, "not in raw mode")
    check(r.modes()[3] & termios.ISIG, "signals should stay on by default")
    check(b"\x1b[?1049h" in r.seen and b"\x1b[?1002h" in r.seen, "no alternate screen or mouse")
    r.send("a"); r.wait_for("<key :char 97 false false>")
    r.send("\x1b[A"); r.wait_for("<key :up 0 false false>")
    r.send("\x1b"); r.wait_for("<key :escape 0 false false>")          # alone: after a short wait
    r.send("\x1bx"); r.wait_for("<key :char 120 false true>")
    r.send("\x1b["); time.sleep(0.01); r.send("B"); r.wait_for("<key :down 0")  # split across reads
    r.send("é"); r.wait_for("<key :char 233")
    r.send("\x1b[<0;3;4M"); r.wait_for("<mouse :press 2 3>")
    r.send("q"); r.wait_for("<quit>")
    check(r.finish() == 0, "bad exit status")
    left_cleanly(r)
    check(b"\x1b[?1002l" in r.seen, "mouse reporting left on")


def t_resize(b):
    r = Run(b, "events")
    r.wait_for("<ready>")
    fcntl.ioctl(r.master, termios.TIOCSWINSZ, struct.pack("HHHH", 30, 100, 0, 0))
    r.wait_for("<resize 100 30>")
    r.send("q"); r.finish()


def t_ctrl_c(b):
    r = Run(b, "events")
    r.wait_for("<ready>")
    r.send("\x03")
    status = r.finish()
    check(os.WIFSIGNALED(status) and os.WTERMSIG(status) == signal.SIGINT, f"status {status}")
    left_cleanly(r)


def t_ctrl_c_as_key(b):
    r = Run(b, "keysonly")
    r.wait_for("<ready>")
    r.send("\x03"); r.wait_for("<key :char 99 true false>")
    r.send("q"); r.finish()
    left_cleanly(r)


def t_sigterm(b):
    r = Run(b, "events")
    r.wait_for("<ready>")
    os.kill(r.pid, signal.SIGTERM)
    status = r.finish()
    check(os.WIFSIGNALED(status) and os.WTERMSIG(status) == signal.SIGTERM, f"status {status}")
    left_cleanly(r)


def t_panic(b):
    r = Run(b, "panic")
    status = r.finish()
    check(os.WIFSIGNALED(status) and os.WTERMSIG(status) == signal.SIGABRT, f"status {status}")
    left_cleanly(r)
    at = r.seen.find(b"out of bounds")
    check(at > r.seen.rfind(b"\x1b[?1049l"), f"the panic's message must come after the alternate screen: {r.seen[-200:]!r}")


def t_exit(b):
    r = Run(b, "exit")
    check(os.WEXITSTATUS(r.finish()) == 3, "bad exit status")
    left_cleanly(r)
    check(r.seen.find(b"held back") > r.seen.rfind(b"\x1b[?1049l"), "stderr wasn't held back until the end")


def t_return(b):
    r = Run(b, "return")
    check(os.WEXITSTATUS(r.finish()) == 4, "bad exit status")
    check(r.restored(), "the terminal settings weren't put back")


def t_stop(b):
    r = Run(b, "events")
    r.wait_for("<ready>")
    r.send("\x1a")                                                       # Ctrl-Z
    end = time.time() + 5
    while time.time() < end:
        pid, status = os.waitpid(r.pid, os.WNOHANG | os.WUNTRACED)
        if pid and os.WIFSTOPPED(status):
            break
        time.sleep(0.01)
    else:
        raise AssertionError("it didn't stop")
    r.pump()
    left_cleanly(r)
    r.seen = b""
    os.kill(r.pid, signal.SIGCONT)
    r.wait_for("<resize 80 24>")
    check(b"\x1b[?1049h" in r.seen, "the alternate screen didn't come back")
    check(not r.modes()[3] & termios.ICANON, "raw mode didn't come back")
    r.send("q"); r.finish()
    left_cleanly(r)


def t_cursor(b):
    r = Run(b, "cursor")
    r.wait_for("<ready>")
    os.kill(r.pid, signal.SIGTERM)
    r.finish()
    check(r.seen.rfind(b"\x1b[?25h") > r.seen.rfind(b"\x1b[?25l"), "the cursor wasn't shown again")


def t_timeout(b):
    r = Run(b, "timeout")
    r.wait_for("<timed out>")
    r.finish()
    check(r.restored(), "the terminal settings weren't put back")


def t_prompts(b):
    r = Run(b, "prompts")
    r.wait_for("name: ")
    r.send("ab\x7fc\r"); r.wait_for("ab\x08 \x08c")                      # a b Backspace c
    r.wait_for("secret: ")
    mark = len(r.seen)
    r.send("hush\r")
    r.wait_for("sure?")
    check(b"hush" not in r.seen[mark:], "the password was shown")
    r.send("y"); r.wait_for("yes")
    r.wait_for("> one")
    r.send("\x1b[B"); r.wait_for("> two")
    r.send("\r")
    r.wait_for("<ac|hush|true|1>")
    r.finish()
    check(r.restored(), "the terminal settings weren't put back")


def t_screen(b):
    r = Run(b, "screen", rows=5, cols=20)
    r.wait_for("<size 20 5>")
    first = r.seen.find(b"<size")
    r.wait_for("<done>")
    r.finish()
    update = r.seen[first:]
    check("┌".encode() in r.seen[:first], "the box wasn't drawn")
    check(b"\x1b[2;2H" in update and "┌".encode() not in update, f"the second frame should be small: {update!r}")
    left_cleanly(r)


def main():
    jar, out = sys.argv[1], sys.argv[2]
    os.makedirs(out, exist_ok=True)
    source = os.path.join(ROOT, "tests/pty/term-driver.ec")
    c, binary = os.path.join(out, "term-driver.c"), os.path.join(out, "term-driver")
    java = os.environ.get("ECLANG", f"java -jar {jar}").split()
    subprocess.run(java + ["-c", source, "-o", c], check=True)
    subprocess.run(["gcc", "-Wall", "-Wextra", "-Werror", "-Wno-unused-variable", "-Wno-unused-but-set-variable", c, "-o", binary,
                    "-I/usr/include/apr-1.0", "-lapr-1", "-lm"], check=True)
    tests = [(k[2:], v) for k, v in globals().items() if k.startswith("t_")]
    for name, test in tests:
        try:
            test(binary)
            print(f"ok {name}")
        except AssertionError as e:
            failures.append(name)
            print(f"FAIL {name}: {e}")
    sys.exit(1 if failures else 0)


main()
