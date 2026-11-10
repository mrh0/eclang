/**
 * Bindings for FFmpeg 8.0: https://ffmpeg.org/doxygen/8.0/
 *
 * `use` this file for all of them, or one of the files it uses for one library (each also links it):
 * avutil.ec (frames, errors, dictionaries, ...), avcodec.ec (encoders and decoders), avformat.ec (containers and
 * I/O), swscale.ec (pictures) and swresample.ec (audio). The constants are in constants.ec. See
 * bindings/ffmpeg/README.md.
 */
use "avutil.ec"
use "avcodec.ec"
use "avformat.ec"
use "swscale.ec"
use "swresample.ec"
