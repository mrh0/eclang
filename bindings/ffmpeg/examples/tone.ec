// Writes two seconds of a chord to an audio file: .wav, .flac, .mp3, .ogg, ... The codec comes from the
// container, or the second argument (an encoder's name: aac, libopus, pcm_s16le, ...).
//   java -jar target/eclang-0.1.0.jar run bindings/ffmpeg/examples/tone.ec -- out.wav [codec]
//
// The tone is made as mono Floats at 48000 Hz. libswresample converts it to what the encoder takes (stereo, 44100
// Hz if it can, its sample format), and an AVAudioFifo cuts that into frames of the size the encoder wants.
use "../ffmpeg.ec"
use "core:io.ec"
use "core:math.ec"
use "core:array.ec"

const SOURCE_RATE = 48000
const PREFERRED_RATE = 44100
const CHUNK = 960
const SECONDS = 2

/** Fills a mono FLT frame with the next samples of an A major chord, starting at sample `start`. */
fn synthesize(src: @AVFrame, start: Int) throws AVError do
    try avCheck(av_frame_make_writable(src), "can't write to the frame")
    val samples = unsafe src.data[0]! !as Float[]c
    for i in 0..<src.nb_samples do
        val t = (start + i) as Double / SOURCE_RATE
        val v = math.sin(2.0 * math.PI * 440.0 * t) + math.sin(2.0 * math.PI * 554.37 * t) + math.sin(2.0 * math.PI * 659.25 * t)
        unsafe samples[i] = (v * 0.2) as Float

/** Everything the steps of writing share. */
mut rec Writer as
    enc: @AVCodecContext
    out: @AVFormatContext
    stream: @AVStream
    fifo: @AVAudioFifo
    frame: @AVFrame
    pkt: @AVPacket
    /** Samples per frame for the encoder. */
    frameSize: Int
    /** Samples given to the encoder so far: the pts of the next frame, in 1/sample rate. */
    written: Long

/** Gives the encoder a frame (null: there are no more) and writes the packets it gives back. */
fn encode(w: @Writer, frame: @AVFrame?) throws AVError do
    try avCheck(avcodec_send_frame(w.enc, frame), "can't encode")
    while true do
        val r = avcodec_receive_packet(w.enc, w.pkt)
        ret if r == AVERROR(EAGAIN) or r == AVERROR_EOF
        try avCheck(r, "can't encode")
        av_packet_rescale_ts(w.pkt, w.enc.time_base, w.stream.time_base)
        w.pkt.stream_index = w.stream.index
        try avCheck(av_interleaved_write_frame(w.out, w.pkt), "can't write a packet")

/** Encodes frames of frameSize samples from the FIFO while it has that many (or, at the end, what is left). */
fn drain(w: @Writer, end: Bool) throws AVError do
    while av_audio_fifo_size(w.fifo) >= w.frameSize or (end and av_audio_fifo_size(w.fifo) > 0) do
        try avCheck(av_frame_make_writable(w.frame), "can't write to the frame")
        val n = try avCheck(av_audio_fifo_read(w.fifo, w.frame, w.frameSize), "can't take samples")
        w.frame.pts = w.written
        w.written += n
        try encode(w, w.frame)

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
        found = avcodec_find_encoder(format.audio_codec)
    val codec = found ?? throw avError(AVERROR_ENCODER_NOT_FOUND, "no encoder for ${path}")

    val stream = avformat_new_stream(out, null) ?? throw avError(AVERROR(ENOMEM), "can't add a stream")
    val enc = avcodec_alloc_context3(codec) ?? throw avError(AVERROR(ENOMEM), "can't make an encoder")
    defer avcodec_free_context(enc)
    val formats = avSupportedSampleFormats(codec)
    enc.sample_fmt = if formats.len > 0 do formats[0] else AV_SAMPLE_FMT_FLTP
    // 44100 Hz, unless the encoder can't (Opus takes only 48000).
    val rates = avSupportedSampleRates(codec)
    var rate = PREFERRED_RATE
    if rates.len > 0 and not rates.contains(PREFERRED_RATE) do
        rate = rates[0]
    enc.sample_rate = rate
    av_channel_layout_default(mut enc.ch_layout, 2)
    enc.time_base = AVRational { 1, rate }
    if format.flags & AVFMT_GLOBALHEADER != 0 do
        enc.flags |= AV_CODEC_FLAG_GLOBAL_HEADER
    try avCheck(avcodec_open2(enc, codec, null), "can't open the encoder ${fromC(codec.name)}")
    try avCheck(avcodec_parameters_from_context(stream.codecpar, enc), "can't describe the stream")
    stream.time_base = enc.time_base

    defer avio_closep(mut out.pb)
    if format.flags & AVFMT_NOFILE == 0 do
        try avCheck(avio_open(mut out.pb, file, AVIO_FLAG_WRITE), "can't open ${path}")
    try avCheck(avformat_write_header(out, null), "can't write the header")

    // The encoder's frames: frame_size samples, unless it takes any number (PCM).
    val variable = codec.capabilities & AV_CODEC_CAP_VARIABLE_FRAME_SIZE != 0
    val frameSize = if enc.frame_size > 0 and not variable do enc.frame_size else 1024
    val frame = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
    defer av_frame_free(frame)
    frame.format = enc.sample_fmt
    frame.sample_rate = rate
    frame.nb_samples = frameSize
    try avCheck(av_channel_layout_copy(mut frame.ch_layout, addrof enc.ch_layout), "can't set the channels")
    try avCheck(av_frame_get_buffer(frame, 0), "can't make a frame")

    // The tone, and the same converted to the encoder's format.
    val src = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
    defer av_frame_free(src)
    src.format = AV_SAMPLE_FMT_FLT
    src.sample_rate = SOURCE_RATE
    src.nb_samples = CHUNK
    av_channel_layout_default(mut src.ch_layout, 1)
    try avCheck(av_frame_get_buffer(src, 0), "can't make a frame")
    val converted = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")
    defer av_frame_free(converted)

    val swr = swr_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a resampler")
    defer swr_free(swr)
    val fifo = av_audio_fifo_alloc(enc.sample_fmt, 2, frameSize) ?? throw avError(AVERROR(ENOMEM), "can't make a FIFO")
    defer av_audio_fifo_free(fifo)
    val pkt = av_packet_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a packet")
    defer av_packet_free(pkt)
    var w = Writer { enc, out, stream, fifo, frame, pkt, frameSize, 0 }

    var made_samples = 0
    var flushed = false
    while not flushed do
        // swr_convert_frame allocates the converted frame's buffers, for as many samples as it gives.
        av_frame_unref(converted)
        converted.format = enc.sample_fmt
        converted.sample_rate = rate
        try avCheck(av_channel_layout_copy(mut converted.ch_layout, addrof enc.ch_layout), "can't set the channels")
        if made_samples < SOURCE_RATE * SECONDS do
            try synthesize(src, made_samples)
            made_samples += CHUNK
            try avCheck(swr_convert_frame(swr, converted, src), "can't convert the samples")
        else
            // No more input: take what the resampler still holds.
            try avCheck(swr_convert_frame(swr, converted, null), "can't convert the samples")
            flushed = true
        try avCheck(av_audio_fifo_write(fifo, converted), "can't queue samples")
        try drain(addrof w, flushed)
    try encode(addrof w, null)
    try avCheck(av_write_trailer(out), "can't finish ${path}")
    val sampleFormat = fromC(av_get_sample_fmt_name(enc.sample_fmt) ?? "?"c)
    io.println("wrote ${w.written} samples of ${rate} Hz ${av_channel_layout_name(addrof enc.ch_layout)} ${sampleFormat} (${fromC(codec.name)}) to ${path}")

fn main() throws Error do
    val words = io.args()
    if words.len < 1 do
        io.eprintln("usage: tone OUTPUT [CODEC]")
        ret
    try write(words[0], if words.len > 1 do words[1] else null)
