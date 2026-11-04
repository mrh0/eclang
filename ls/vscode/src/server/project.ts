import { existsSync, readFileSync, statSync } from "node:fs";
import { dirname, isAbsolute, join, posix, sep } from "node:path";
import { pathToFileURL } from "node:url";
import { FileDocs, Use } from "./docs";
import { parse, Parsed } from "./parse";

/** A loaded file. [docs] is from the last time it parsed, so hovers keep working while it's being typed in. */
interface Entry { stamp: string; parsed: Parsed; docs: FileDocs | null; }

/**
 * The `.ec` files the server knows of. A file is named by its path, `core:name.ec` for the compiler's library (a copy of
 * it is in [coreDir]), or its URI when it isn't saved anywhere. An open file's text comes from [openText], others' from disk.
 */
export class Project {
    private readonly files = new Map<string, Entry>();

    constructor(private readonly coreDir: string, private readonly roots: () => string[], private readonly openText: (file: string) => string | undefined) {}

    /** The text of [file], with a stamp that changes with it, or null when there's no such file. */
    private read(file: string): { text: string; stamp: string } | null {
        const open = this.openText(file);
        if (open !== undefined) return { text: open, stamp: open };
        const path = file.startsWith("core:") ? join(this.coreDir, file.slice("core:".length)) : file;
        try {
            const stat = statSync(path);
            const stamp = `${stat.mtimeMs}:${stat.size}`;
            const known = this.files.get(file);
            return { text: known?.stamp === stamp ? "" : readFileSync(path, "utf8"), stamp };
        } catch {
            return null;
        }
    }

    private entry(file: string): Entry | null {
        const source = this.read(file);
        if (source === null) {
            this.files.delete(file);
            return null;
        }
        const known = this.files.get(file);
        if (known?.stamp === source.stamp) return known;
        const parsed = parse(source.text);
        const entry = { stamp: source.stamp, parsed, docs: parsed.docs ?? known?.docs ?? null };
        this.files.set(file, entry);
        return entry;
    }

    /** What [file] parses to now. */
    parsed(file: string): Parsed | null { return this.entry(file)?.parsed ?? null; }

    /** The declarations of [file], from the last time it parsed. */
    docs(file: string): FileDocs | null { return this.entry(file)?.docs ?? null; }

    /** Forgets a file that was closed: its last good parse says nothing about the file on disk. */
    close(file: string): void { this.files.delete(file); }

    /** The URI of [file], to open it. A `core:` file opens from the copy that comes with the extension. */
    uriOf(file: string): string {
        if (file.startsWith("core:")) return pathToFileURL(join(this.coreDir, file.slice("core:".length))).toString();
        return isAbsolute(file) ? pathToFileURL(file).toString() : file;
    }

    /**
     * What the compiler calls [file]: its `core:` name when it's a file of the library, here or in the compiler's sources, as
     * other library files load it by that name. Otherwise [file].
     */
    compilerName(file: string): string {
        const inCopy = file.startsWith(this.coreDir + sep) ? file.slice(this.coreDir.length + 1) : null;
        const relative = inCopy ?? /[\\/]src[\\/]main[\\/]resources[\\/](.+\.ec)$/.exec(file)?.[1];
        if (relative === undefined) return file;
        const name = relative.split(sep).join("/");
        return existsSync(join(this.coreDir, name)) ? `core:${name}` : file;
    }

    /** The file that [use] in [from] loads, as `Util.resolveUse` finds it, or null. */
    resolve(from: string, use: Use): string | null {
        const path = use.path;
        if (path.startsWith("core:")) return existsSync(join(this.coreDir, path.slice("core:".length))) ? path : null;
        if (isAbsolute(path)) return existsSync(path) ? path : null;
        if (path.includes(":")) return null;
        // Inside a bundled file, a plain path is another bundled file: `use "pools.ec"` in core:apr/env.ec.
        if (from.startsWith("core:")) return this.resolve(from, { ...use, path: "core:" + posix.join(posix.dirname(from.slice("core:".length)), path) });
        const places = [...(isAbsolute(from) ? [dirname(from)] : []), ...this.roots()];
        return places.map(p => join(p, path)).find(p => existsSync(p) && statSync(p).isFile()) ?? null;
    }

    /** [file] and the `.ec` files it uses, directly or not, with their declarations. */
    closure(file: string): { file: string; docs: FileDocs }[] {
        const seen = new Set<string>([file]);
        const found: { file: string; docs: FileDocs }[] = [];
        for (const current of seen) { // a Set iterates over what is added while it runs
            const docs = this.docs(current);
            if (docs === null) continue;
            found.push({ file: current, docs });
            for (const use of docs.uses) {
                if (!use.path.endsWith(".ec")) continue; // a C header
                const target = this.resolve(current, use);
                if (target !== null) seen.add(target);
            }
        }
        return found;
    }
}
