import github.mrh0.eclang.Compiler;
import github.mrh0.eclang.antlr.EclangLexer;
import github.mrh0.eclang.doc.*;
import github.mrh0.eclang.error.EcError;
import github.mrh0.eclang.ast.Loc;
import org.antlr.v4.runtime.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * What the real compiler makes of each file listed on stdin, as a JSON line each, for test/run.ts to compare with
 * the TypeScript port: its token stream, and its syntax error or its documentation.
 * Run with: java -cp target/eclang-0.1.0.jar ls/vscode/test/oracle/Oracle.java < list-of-files
 */
public class Oracle {
    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), false, "UTF-8");
        BufferedReader paths = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        for (String path = paths.readLine(); path != null; path = paths.readLine()) {
            StringBuilder s = new StringBuilder("{\"file\":" + str(path));
            byte[] bytes = Files.readAllBytes(Path.of(path));
            s.append(",\"tokens\":").append(tokens(new String(bytes, "UTF-8")));
            try {
                Compiler.Parsed parsed = Compiler.INSTANCE.parse(new ByteArrayInputStream(bytes), path);
                s.append(",\"docs\":").append(docs(parsed.getDocs()));
            } catch (Exception thrown) { // Kotlin declares no checked exceptions, so `catch (EcError e)` doesn't compile.
                if (!(thrown instanceof EcError)) throw thrown;
                EcError e = (EcError) thrown;
                s.append(",\"error\":{\"line\":" + e.getLocation().getLine() + ",\"column\":" + e.getLocation().getCharPos() + ",\"message\":" + str(e.getReason()) + "}");
            }
            out.println(s.append("}"));
        }
        out.flush();
    }

    static String tokens(String text) {
        EclangLexer lexer = new EclangLexer(new ANTLRInputStream(text));
        lexer.removeErrorListeners();
        List<String> all = new ArrayList<>();
        for (Token t = lexer.nextToken(); ; t = lexer.nextToken()) {
            all.add("[" + t.getType() + "," + t.getLine() + "," + t.getCharPositionInLine() + "," + str(t.getText()) + "]");
            if (t.getType() == Token.EOF) break;
        }
        return "[" + String.join(",", all) + "]";
    }

    static String docs(FileDocs d) {
        List<String> items = new ArrayList<>(), problems = new ArrayList<>();
        for (DocItem i : d.getItems()) items.add(item(i));
        for (DocProblem p : d.getProblems()) problems.add("{\"location\":" + loc(p.getLocation()) + ",\"message\":" + str(p.getMessage()) + "}");
        return "{\"module\":" + str(d.getModule()) + ",\"overview\":" + ecdoc(d.getOverview()) + ",\"items\":[" + String.join(",", items) + "],\"problems\":[" + String.join(",", problems) + "]}";
    }

    static String item(DocItem i) {
        List<String> members = new ArrayList<>();
        for (DocItem m : i.getMembers()) members.add(item(m));
        return "{\"kind\":" + str(i.getKind().name()) + ",\"name\":" + str(i.getName()) + ",\"signature\":" + str(i.getSignature()) + ",\"location\":" + loc(i.getLocation())
            + ",\"doc\":" + ecdoc(i.getDoc()) + ",\"isPrivate\":" + i.isPrivate() + ",\"members\":[" + String.join(",", members) + "],\"cName\":" + str(i.getCName()) + "}";
    }

    static String ecdoc(Ecdoc d) {
        if (d == null) return "null";
        List<String> tags = new ArrayList<>();
        for (DocTag t : d.getTags()) tags.add("{\"name\":" + str(t.getName()) + ",\"location\":" + loc(t.getLocation()) + ",\"arg\":" + str(t.getArg()) + ",\"text\":" + str(t.getText()) + "}");
        return "{\"location\":" + loc(d.getLocation()) + ",\"description\":" + str(d.getDescription()) + ",\"tags\":[" + String.join(",", tags) + "]}";
    }

    static String loc(Loc l) { return "{\"line\":" + l.getLine() + ",\"column\":" + l.getCharPos() + "}"; }

    static String str(String s) {
        if (s == null) return "null";
        StringBuilder b = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            if (c == '"' || c == '\\') b.append('\\').append(c);
            else if (c < 0x20 || c > 0x7e) b.append(String.format("\\u%04x", (int) c));
            else b.append(c);
        }
        return b.append('"').toString();
    }
}
