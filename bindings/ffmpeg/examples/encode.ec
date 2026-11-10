// Encodes three seconds of a moving test picture into a video file. The container comes from the file name, and
// the codec from the container, or the second argument (an encoder's name: libx264, mpeg4, ffv1, ...).
//   java -jar target/eclang-0.1.0.jar run bindings/ffmpeg/examples/encode.ec -- out.mp4 [codec]
//
// It draws each picture in RGB and converts it with libswscale to a pixel format the encoder takes.
use "../ffmpeg.ec"
use "core:io.ec"

const WIDTH = 320
const HEIGHT = 240
const FPS = 25
const FRAMES = 75

/** Draws picture i of the test pattern into an RGB24 frame. */
fn draw(rgb: @AVFrame, i: Int) throws AVError do
    try avCheck(av_frame_make_writable(rgb), "can't write to the picture")
    val pixels = rgb.data[0]!
    val stride = rgb.linesize[0]
    for y in 0..<HEIGHT do
        for x in 0..<WIDTH do
            val at = y * stride + x * 3
            unsafe pixels[at] = ((x + i * 4) % 256) as UChar
            unsafe pixels[at + 1] = ((y + i * 2) % 256) as UChar
            unsafe pixels[at + 2] = (((x + y) / 2 + i * 8) % 256) as UChar

/** Gives the encoder a frame (null: there are no more) and writes the packets it gives back. */
fn encode(enc: @AVCodecContext, frame: @AVFrame?, pkt: @AVPacket, out: @AVFormatContext, stream: @AVStream) throws AVError do
    try avCheck(avcodec_send_frame(enc, frame), "can't encode")
    while true do
        val r = avcodec_receive_packet(enc, pkt)
        ret if r == AVERROR(EAGAIN) or r == AVERROR_EOF
        try avCheck(r, "can't encode")
        // The encoder counts in its time base, the file in the stream's.
        av_packet_rescale_ts(pkt, enc.time_base, stream.time_base)
        pkt.stream_index = stream.index
        try avCheck(av_interleaved_write_frame(out, pkt), "can't write a packet")

fn write(path: String, codecName: String?) throws AVError do
    val file = cstr(path)
    var made: @AVFormatContext? = null
    try avCheck(avformat_alloc_output_context2(mut made, null, null, file), "can't tell the format of ${path}")
    val out = made!
    defer avformat_free_context(out)
    val format = out.oformat!

    var found: @AVCodec? = null
    if val name = codecName do
        found = avcodec_find_encoder_by_name(cstr(name))
    else
        found = avcodec_find_encoder(format.video_codec)
    val codec = found ?? throw avError(AVERROR_ENCODER_NOT_FOUND, "no encoder for ${path}")

    val stream = avformat_new_stream(out, null) ?? throw avError(AVERROR(ENOMEM), "can't add a stream")
    val enc = avcodec_alloc_context3(codec) ?? throw avError(AVERROR(ENOMEM), "can't make an encoder")
    defer avcodec_free_context(enc)
    enc.width = WIDTH
    enc.height = HEIGHT
    enc.time_base = AVRational { 1, FPS }
    enc.framerate = AVRational { FPS, 1 }
    enc.gop_size = 12
    val formats = avSupportedPixelFormats(codec)
    enc.pix_fmt = if formats.len > 0 do formats[0] else AV_PIX_FMT_YUV420P
    if format.flags & AVFMT_GLOBALHEADER != 0 do
        enc.flags |= AV_CODEC_FLAG_GLOBAL_HEADER
    try avCheck(avcodec_open2(enc, codec, null), "can't open the encoder ${fromC(codec.name)}")
    try avCheck(avcodec_parameters_from_context(stream.codecpar, enc), "can't describe the stream")
    stream.time_base = enc.time_base

    // avio_closep does nothing when pb is null, so this works for formats without a file too.
    defer avio_closep(mut out.pb)
    if format.flags & AVFMT_NOFILE == 0 do
        try avCheck(avio_open(mut out.pb, file, AVIO_FLAG_WRITE), "can't open ${path}")
    try avCheck(avformat_write_header(out, null), "can't write the header")

    val rgb = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
    defer av_frame_free(rgb)
    rgb.format = AV_PIX_FMT_RGB24
    rgb.width = WIDTH
    rgb.height = HEIGHT
    try avCheck(av_frame_get_buffer(rgb, 0), "can't make a picture")
    val frame = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
    defer av_frame_free(frame)
    frame.format = enc.pix_fmt
    frame.width = WIDTH
    frame.height = HEIGHT
    try avCheck(av_frame_get_buffer(frame, 0), "can't make a picture")
    val sws = sws_alloc_context() ?? throw avError(AVERROR(ENOMEM), "can't make a converter")
    defer sws_free_context(sws)
    val pkt = av_packet_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a packet")
    defer av_packet_free(pkt)

    for i in 0..<FRAMES do
        try draw(rgb, i)
        // The encoder may still hold the last frame's buffers.
        try avCheck(av_frame_make_writable(frame), "can't write to the frame")
        try avCheck(sws_scale_frame(sws, frame, rgb), "can't convert the picture")
        frame.pts = i
        try encode(enc, frame, pkt, out, stream)
    try encode(enc, null, pkt, out, stream)
    try avCheck(av_write_trailer(out), "can't finish ${path}")
    val pixels = fromC(av_get_pix_fmt_name(enc.pix_fmt) ?? "?"c)
    io.println("wrote ${FRAMES} frames of ${WIDTH}x${HEIGHT} ${pixels} (${fromC(codec.name)}) to ${path}")

fn main() throws Error do
    val words = io.args()
    if words.len < 1 do
        io.eprintln("usage: encode OUTPUT [CODEC]")
        ret
    try write(words[0], if words.len > 1 do words[1] else null)
