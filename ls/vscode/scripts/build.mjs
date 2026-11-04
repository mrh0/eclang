#!/usr/bin/env node
// Bundles the client and the server into dist/, with the compiler jar and a copy of its `core:` library (hovers show its ecdocs).
// --watch rebuilds on changes; --tests also builds test/run.ts.
import { build, context } from "esbuild";
import { cpSync, existsSync, mkdirSync, readdirSync, rmSync, statSync } from "node:fs";
import { dirname, join, resolve } from "node:path";
import { fileURLToPath } from "node:url";

const root = resolve(dirname(fileURLToPath(import.meta.url)), "..");
const resources = resolve(root, "../../src/main/resources");
const args = process.argv.slice(2);

rmSync(join(root, "dist/core"), { recursive: true, force: true });
function copyCore(dir) {
    for (const entry of readdirSync(join(resources, dir))) {
        const path = join(dir, entry);
        if (statSync(join(resources, path)).isDirectory()) copyCore(path);
        else if (entry.endsWith(".ec")) {
            mkdirSync(dirname(join(root, "dist/core", path)), { recursive: true });
            cpSync(join(resources, path), join(root, "dist/core", path));
        }
    }
}
copyCore("");

// The compiler, for type errors. Without it the extension still works, with syntax errors only.
const jar = resolve(root, "../../target/eclang-0.1.0.jar");
if (existsSync(jar)) cpSync(jar, join(root, "dist/eclang.jar"));
else console.warn(`${jar} not found, so the extension won't show type errors. Run 'mvn package' in the repository root.`);

const entryPoints = { extension: "src/client/extension.ts", server: "src/server/server.ts" };
if (args.includes("--tests")) entryPoints["test/run"] = "test/run.ts";
const options = {
    absWorkingDir: root, entryPoints, outdir: "dist", bundle: true, platform: "node", format: "cjs", target: "node20",
    external: ["vscode"], sourcemap: true, logLevel: "warning",
    // The package's ES module build needs import.meta, which a CommonJS bundle doesn't have.
    alias: { antlr4: join(root, "node_modules/antlr4/dist/antlr4.node.cjs") },
};
if (args.includes("--watch")) await (await context(options)).watch();
else await build(options);
