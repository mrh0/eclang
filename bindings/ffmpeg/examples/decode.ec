// Decodes the video of a file, counts its frames, and saves the first one as a picture (PPM, RGB).
//   java -jar target/eclang-0.1.0.jar run bindings/ffmpeg/examples/decode.ec -- input.mp4 [first.ppm]
use "../ffmpeg.ec"
use "core:io.ec"

/** Saves an RGB24 frame as a binary PPM file, through FFmpeg's I/O. */
fn savePpm(rgb: @AVFrame, path: String) throws AVError do
    var made: @AVIOContext? = null
    try avCheck(avio_open(mut made, cstr(path), AVIO_FLAG_WRITE), "can't open ${path}")
    val file = made!
    defer avio_closep(mut made)
    avio_put_str(file, cstr("P6\n${rgb.width} ${rgb.height}\n255\n"))
    // avio_put_str also wrote the string's 0 byte; go back over it.
    avio_seek(file, -1, 1)
    val pixels = rgb.data[0]!
    for y in 0..<rgb.height do
        // Each line of the picture starts linesize bytes after the one before.
        avio_write(file, unsafe (addrof pixels[y * rgb.linesize[0]]) !as UChar[]c, rgb.width * 3)

/** Counts of what the decoder gave. */
mut rec Counts as
    frames: Int
    keyFrames: Int

/** Takes the frames the decoder has ready; the first one is saved to `picture`. */
fn receive(dec: @AVCodecContext, frame: @AVFrame, counts: @Counts, picture: String) throws AVError do
    while true do
        val r = avcodec_receive_frame(dec, frame)
        ret if r == AVERROR(EAGAIN) or r == AVERROR_EOF
        try avCheck(r, "can't decode")
        if counts.frames == 0 do
            val rgb = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
            defer av_frame_free(rgb)
            rgb.format = AV_PIX_FMT_RGB24
            rgb.width = frame.width
            rgb.height = frame.height
            val sws = sws_alloc_context() ?? throw avError(AVERROR(ENOMEM), "can't make a converter")
            defer sws_free_context(sws)
            try avCheck(sws_scale_frame(sws, rgb, frame), "can't convert the picture")
            try savePpm(rgb, picture)
        counts.frames += 1
        if frame.flags & AV_FRAME_FLAG_KEY != 0 do
            counts.keyFrames += 1
        av_frame_unref(frame)

fn decode(path: String, picture: String) throws AVError do
    var opened: @AVFormatContext? = null
    try avCheck(avformat_open_input(mut opened, cstr(path), null, null), "can't open ${path}")
    val input = opened!
    defer avformat_close_input(input)
    try avCheck(avformat_find_stream_info(input, null), "can't read the streams of ${path}")

    var found: @AVCodec? = null
    val index = try avCheck(av_find_best_stream(input, AVMEDIA_TYPE_VIDEO, -1, -1, mut found, 0), "no video in ${path}")
    val codec = found!
    val stream = avStream(input, index)
    val dec = avcodec_alloc_context3(codec) ?? throw avError(AVERROR(ENOMEM), "can't make a decoder")
    defer avcodec_free_context(dec)
    try avCheck(avcodec_parameters_to_context(dec, stream.codecpar), "can't set up the decoder")
    dec.pkt_timebase = stream.time_base
    try avCheck(avcodec_open2(dec, codec, null), "can't open the decoder ${fromC(codec.name)}")

    val pkt = av_packet_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a packet")
    defer av_packet_free(pkt)
    val frame = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
    defer av_frame_free(frame)
    var counts = Counts { 0, 0 }
    while true do
        val r = av_read_frame(input, pkt)
        break if r == AVERROR_EOF
        try avCheck(r, "can't read ${path}")
        if pkt.stream_index == index do
            val sent = avcodec_send_packet(dec, pkt)
            av_packet_unref(pkt)
            try avCheck(sent, "can't decode")
            try receive(dec, frame, addrof counts, picture)
        else
            av_packet_unref(pkt)
    // No more packets: take the frames the decoder still holds.
    try avCheck(avcodec_send_packet(dec, null), "can't decode")
    try receive(dec, frame, addrof counts, picture)

    val pixels = fromC(av_get_pix_fmt_name(dec.pix_fmt) ?? "?"c)
    io.println("decoded ${counts.frames} frames (${counts.keyFrames} key frames) of ${dec.width}x${dec.height} ${pixels} (${fromC(codec.name)}); the first is in ${picture}")

fn main() throws Error do
    val words = io.args()
    if words.len < 1 do
        io.eprintln("usage: decode INPUT [PICTURE.ppm]")
        ret
    try decode(words[0], if words.len > 1 do words[1] else "first.ppm")
