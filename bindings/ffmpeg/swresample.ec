/**
 * Bindings for libswresample, FFmpeg's audio resampling and sample format and channel layout conversion.
 * https://ffmpeg.org/doxygen/8.0/group__lswr.html
 *
 * Written for FFmpeg 8.0 (libswresample 6). The simplest use converts whole frames: swr_alloc, then
 * swr_convert_frame(swr, out, in) with out's format, sample_rate and ch_layout set (it allocates the buffers),
 * and swr_convert_frame(swr, out, null) at the end for what it still holds.
 */
use "libswresample/swresample.h"
use "lib:swresample"
use "avutil.ec"

/** The state of a conversion. Opaque: set it up with swr_alloc_set_opts2, av_opt_set_* or the frames. */
declare rec SwrContext

/** The version of libswresample: major << 16 | minor << 8 | micro. */
declare fn swresample_version as (): UInt
/** A new conversion; free it with swr_free. Null when out of memory. */
declare fn swr_alloc as (): @SwrContext?
/**
 * Sets up a conversion between two layouts, sample formats and rates, making it when the variable is null;
 * swr_init must follow. log_offset is usually 0 and log_ctx null.
 */
declare fn swr_alloc_set_opts2 as (mut ps: @SwrContext?, out_ch_layout: @AVChannelLayout, out_sample_fmt: AVSampleFormat, out_sample_rate: Int, in_ch_layout: @AVChannelLayout, in_sample_fmt: AVSampleFormat, in_sample_rate: Int, log_offset: Int, log_ctx: @Any?): Int
/** Starts the conversion after its options are set. */
declare fn swr_init as (s: @SwrContext): Int
/** Non-zero if swr_init has run. */
declare fn swr_is_initialized as (s: @SwrContext): Int
/** Undoes swr_init, so the options can be changed. */
declare fn swr_close as (s: @SwrContext)
/** Frees a conversion and sets the variable to null. */
declare fn swr_free as (mut s: @SwrContext?)
/** Frees a conversion. */
fn swr_free(s: @SwrContext) do
    var c: @SwrContext? = s
    swr_free(mut c)
/**
 * Converts in_count samples (per channel) of the planes input to at most out_count into the planes output; the
 * samples written per channel. Null input with 0 gives what it still holds. A frame's planes are
 * `frame.extended_data`; swr_convert_frame works on frames.
 */
declare fn swr_convert as (s: @SwrContext, output: UChar[]c?[]c?, out_count: Int, input: UChar[]c?[]c?, in_count: Int): Int
/**
 * Converts the audio of input into output, whose format, sample_rate and ch_layout must be set; its buffers are
 * allocated when it has none. The context sets itself up from the frames when it isn't. Null input gives what it
 * still holds.
 */
declare fn swr_convert_frame as (swr: @SwrContext, output: @AVFrame?, input: @AVFrame?): Int
/** Sets the context up from the formats of two frames (it doesn't call swr_init). */
declare fn swr_config_frame as (swr: @SwrContext, output: @AVFrame?, input: @AVFrame?): Int
/** How long the samples it holds last, in 1/base seconds (base: the input or output sample rate, for samples). */
declare fn swr_get_delay as (s: @SwrContext, base: Long): Long
/** At most how many samples the next swr_convert of in_samples samples gives. */
declare fn swr_get_out_samples as (s: @SwrContext, in_samples: Int): Int
