// Says what a media file holds, as ffprobe does: its format, duration, metadata and streams.
//   java -jar target/eclang-0.1.0.jar run bindings/ffmpeg/examples/probe.ec -- input.mp4 [more files...]
use "../ffmpeg.ec"
use "core:io.ec"

/** "1:02.50" for a time in seconds. */
fn clock(seconds: Double): String do
    val minutes = (seconds / 60.0) as Int
    ret "${minutes}:${seconds - minutes as Double * 60.0:05.2f}"

/** Prints the entries of a dictionary, indented. */
fn printMetadata(m: @AVDictionary?, indent: String) do
    var previous: @AVDictionaryEntry? = null
    while true do
        val e = av_dict_iterate(m, previous) ?? break
        io.println("${indent}${e.key}: ${e.value}")
        previous = e

/** A line about a stream: its type and codec, then what matters for that type. */
fn describe(input: @AVFormatContext, s: @AVStream): String do
    val par = s.codecpar
    val kind = fromC(av_get_media_type_string(par.codec_type) ?? "unknown"c)
    val codec = fromC(avcodec_get_name(par.codec_id))
    if par.codec_type == AVMEDIA_TYPE_VIDEO do
        val pixels = fromC(av_get_pix_fmt_name(par.format) ?? "?"c)
        val fps = av_q2d(av_guess_frame_rate(input, s, null))
        ret "${kind}: ${codec}, ${par.width}x${par.height}, ${pixels}, ${fps:.2f} fps"
    if par.codec_type == AVMEDIA_TYPE_AUDIO do
        val samples = fromC(av_get_sample_fmt_name(par.format) ?? "?"c)
        ret "${kind}: ${codec}, ${par.sample_rate} Hz, ${av_channel_layout_name(addrof par.ch_layout)}, ${samples}"
    ret "${kind}: ${codec}"

fn probe(path: String) throws AVError do
    var opened: @AVFormatContext? = null
    try avCheck(avformat_open_input(mut opened, cstr(path), null, null), "can't open ${path}")
    val input = opened!
    defer avformat_close_input(input)
    try avCheck(avformat_find_stream_info(input, null), "can't read the streams of ${path}")

    val format = input.iformat!
    val description = format.long_name ?? format.name
    io.println("${path}: ${format.name} (${description})")
    if input.duration != AV_NOPTS_VALUE do
        io.println("  duration: ${clock(input.duration as Double / AV_TIME_BASE)}")
    if input.bit_rate > 0 do
        io.println("  bit rate: ${input.bit_rate / 1000} kb/s")
    printMetadata(input.metadata, "  ")
    for i in 0..<input.nb_streams as Int do
        val s = avStream(input, i)
        io.println("  stream ${i}: ${describe(input, s)}")
        if s.duration != AV_NOPTS_VALUE do
            io.println("    duration: ${clock(s.duration as Double * av_q2d(s.time_base))}")
        printMetadata(s.metadata, "    ")

fn main() throws Error do
    av_log_set_level(AV_LOG_ERROR)
    val words = io.args()
    if words.len < 1 do
        io.eprintln("usage: probe FILE...")
        ret
    for path in words do
        try probe(path)
