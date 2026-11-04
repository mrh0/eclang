// Kotlin's string functions, where JavaScript's differ. The ported compiler code relies on their exact behaviour.

/** `String.lines()`: splits at \r\n, \n and \r. */
export const lines = (s: string): string[] => s.split(/\r\n|\n|\r/);

export const isBlank = (s: string): boolean => s.trim().length === 0;

/** `String.trimIndent()`: removes the common indentation, and a blank first and last line. */
export function trimIndent(s: string): string {
    const all = lines(s);
    const indent = (line: string) => { const i = line.search(/\S/); return i < 0 ? line.length : i; };
    const widths = all.filter(l => !isBlank(l)).map(indent);
    const common = widths.length > 0 ? Math.min(...widths) : 0;
    const last = all.length - 1;
    return all.flatMap((l, i) => (i === 0 || i === last) && isBlank(l) ? [] : [l.slice(common)]).join("\n");
}

/** `replaceFirst` with a literal replacement (`String.replace` treats `$` in it specially). */
export const replaceFirst = (s: string, from: string, to: string): string => s.replace(from, () => to);
