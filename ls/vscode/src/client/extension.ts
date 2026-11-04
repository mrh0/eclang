import { ExtensionContext, workspace } from "vscode";
import { LanguageClient, ServerOptions, TransportKind } from "vscode-languageclient/node";

let client: LanguageClient | undefined;

export function activate(context: ExtensionContext): Promise<void> {
    const module = context.asAbsolutePath("dist/server.js");
    const server: ServerOptions = {
        run: { module, transport: TransportKind.ipc },
        debug: { module, transport: TransportKind.ipc, options: { execArgv: ["--nolazy", "--inspect=6009"] } },
    };
    const options = () => {
        const config = workspace.getConfiguration("eclang");
        return { typeCheck: config.get("typeCheck.enable"), javaPath: config.get("java.path"), compilerJar: config.get("compiler.jar") };
    };
    const started = new LanguageClient("eclang", "eclang", server, { documentSelector: [{ language: "eclang" }], initializationOptions: options });
    client = started;
    context.subscriptions.push(workspace.onDidChangeConfiguration(e => {
        if (e.affectsConfiguration("eclang")) void started.restart();
    }));
    return started.start();
}

export function deactivate(): Promise<void> | undefined {
    return client?.stop();
}
