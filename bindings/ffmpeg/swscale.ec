/**
 * Bindings for libswscale, FFmpeg's scaling and pixel format conversion. https://ffmpeg.org/doxygen/8.0/group__libsws.html
 *
 * Written for FFmpeg 8.0 (libswscale 9). The simplest use converts whole frames: sws_alloc_context, then
 * sws_scale_frame(sws, dst, src) with dst's format, width and height set (and no buffers: it allocates them).
 * The older sws_getContext and sws_scale work on planes and need the sizes up front.
 */
use "libswscale/swscale.h"
use "lib:swscale"
use "avutil.ec"

/**
 * The state of a conversion. With sws_scale_frame its fields are options (set them before the first frame): the
 * formats and sizes come from the frames.
 */
declare rec SwsContext as
    /** SWS_* flags: the scaling algorithm (SWS_BILINEAR, SWS_BICUBIC, SWS_LANCZOS, ...) and more. */
    flags: SwsFlags
    /** The number of threads; 0 picks one per CPU. */
    threads: Int
    /** An SwsDither. */
    dither: SwsDither
    /** An SwsAlphaBlend. */
    alpha_blend: SwsAlphaBlend
    /** 1 to scale in linear light. */
    gamma_flag: Int
    /** sws_getContext: the width of the input. */
    src_w: Int
    /** sws_getContext: the height of the input. */
    src_h: Int
    /** sws_getContext: the width of the output. */
    dst_w: Int
    /** sws_getContext: the height of the output. */
    dst_h: Int
    /** sws_getContext: the AVPixelFormat of the input. */
    src_format: Int
    /** sws_getContext: the AVPixelFormat of the output. */
    dst_format: Int
    /** An AVColorRange of the input. */
    src_range: Int
    /** An AVColorRange of the output. */
    dst_range: Int
    /** An SwsIntent. */
    intent: Int

/** A filter applied while scaling (sws_getContext); usually null. */
declare rec SwsFilter

/** The version of libswscale: major << 16 | minor << 8 | micro. */
declare fn swscale_version as (): UInt
/** A new conversion with default options; free it with sws_free_context. Null when out of memory. */
declare fn sws_alloc_context as (): @SwsContext?
/** Frees a conversion and sets the variable to null. */
declare fn sws_free_context as (mut ctx: @SwsContext?)
/** Frees a conversion. */
fn sws_free_context(ctx: @SwsContext) do
    var c: @SwsContext? = ctx
    sws_free_context(mut c)
/**
 * Converts (scales, changes the pixel format of) src into dst. dst's format, width and height must be set; if it
 * has no buffers, they are allocated. The context sets itself up again when the frames change.
 */
declare fn sws_scale_frame as (c: @SwsContext, dst: @AVFrame, src: @AVFrame): Int
/** Sets the context up for frames like these ahead of time; sws_scale_frame does it when needed. */
declare fn sws_frame_setup as (ctx: @SwsContext, dst: @AVFrame, src: @AVFrame): Int
/** 1 if converting src to dst would just copy it (same format and size). */
declare fn sws_is_noop as (dst: @AVFrame, src: @AVFrame): Int
/** 1 if libswscale can read (output 0) or write (output 1) the pixel format. */
declare fn sws_test_format as (format: AVPixelFormat, output: Int): Int
/** Non-zero if libswscale can read the pixel format. */
declare fn sws_isSupportedInput as (pix_fmt: AVPixelFormat): Int
/** Non-zero if libswscale can write the pixel format. */
declare fn sws_isSupportedOutput as (pix_fmt: AVPixelFormat): Int

/**
 * A conversion from one size and pixel format to another, for sws_scale; null if they aren't supported. flags:
 * SWS_* (an algorithm such as SWS_BILINEAR). Filters and param are usually null. Free it with sws_freeContext.
 */
declare fn sws_getContext as (srcW: Int, srcH: Int, srcFormat: AVPixelFormat, dstW: Int, dstH: Int, dstFormat: AVPixelFormat, flags: SwsFlags, srcFilter: @SwsFilter?, dstFilter: @SwsFilter?, param: Double[]c?): @SwsContext?
/** sws_getContext, reusing context if it is set up the same way (or freeing it and making a new one). */
declare fn sws_getCachedContext as (context: @SwsContext?, srcW: Int, srcH: Int, srcFormat: AVPixelFormat, dstW: Int, dstH: Int, dstFormat: AVPixelFormat, flags: SwsFlags, srcFilter: @SwsFilter?, dstFilter: @SwsFilter?, param: Double[]c?): @SwsContext?
/** Sets up a context made with sws_alloc_context whose src_w, ..., dst_format are set, for sws_scale. */
declare fn sws_init_context as (sws_context: @SwsContext, srcFilter: @SwsFilter?, dstFilter: @SwsFilter?): Int
/**
 * Converts the lines srcSliceY to srcSliceY + srcSliceH of a picture (planes and line sizes) into dst; the height
 * of the output written. C takes the input planes as `const uint8_t* const*`, which a `UChar[]c?[]c` doesn't
 * convert to, so they are a pointer here: the overload taking frames does that for you.
 */
declare fn sws_scale as (c: @SwsContext, srcSlice: UChar[]c?[]c, srcStride: Int[]c, srcSliceY: Int, srcSliceH: Int, dst: UChar[]c?[]c, dstStride: Int[]c): Int
/** Frees a context of sws_getContext (null does nothing). */
declare fn sws_freeContext as (swsContext: @SwsContext?)

/** sws_scale of the planes of two frames: lines srcSliceY to srcSliceY + srcSliceH of src into dst. */
fn sws_scale(c: @SwsContext, src: @AVFrame, srcSliceY: Int, srcSliceH: Int, dst: @AVFrame): Int = sws_scale(c, src.data.data, src.linesize.data, srcSliceY, srcSliceH, dst.data.data, dst.linesize.data)
