package code.name.monkey.retromusic.cast

import android.content.Context
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroUtil
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status
import java.io.*

const val DEFAULT_SERVER_PORT = 9090
private var currentServerPort = DEFAULT_SERVER_PORT

class RetroWebServer(val context: Context) : NanoHTTPD(findAndInitializePort()) {
    companion object {
        private const val MIME_TYPE_IMAGE = "image/jpg"
        const val MIME_TYPE_AUDIO = "audio/mp3"

        const val PART_COVER_ART = "coverart"
        const val PART_SONG = "song"
        const val PARAM_ID = "id"
        
        private fun findAndInitializePort(): Int {
            return if (CastServerUtils.isPortAvailable(DEFAULT_SERVER_PORT)) {
                CastServerUtils.logInfo("Using default port: $DEFAULT_SERVER_PORT")
                DEFAULT_SERVER_PORT
            } else {
                val port = CastServerUtils.findAvailablePort(DEFAULT_SERVER_PORT)
                if (port != -1) {
                    CastServerUtils.logInfo("Using alternative port: $port")
                    currentServerPort = port
                    port
                } else {
                    CastServerUtils.logError("Failed to find available port, using default")
                    DEFAULT_SERVER_PORT
                }
            }
        }
    }

    override fun start() {
        try {
            super.start()
            val ipAddress = RetroUtil.getIpAddress(true)
            CastServerUtils.logInfo("Server started successfully on port $currentServerPort with IP: $ipAddress")
        } catch (e: Exception) {
            CastServerUtils.logError("Failed to start server: ${e.message}")
            throw e
        }
    }

    override fun stop() {
        try {
            super.stop()
            CastServerUtils.logInfo("Server stopped")
        } catch (e: Exception) {
            CastServerUtils.logError("Error stopping server: ${e.message}")
        }
    }

    override fun serve(session: IHTTPSession?): Response {
        try {
            CastServerUtils.logInfo("Received request: ${session?.uri} from ${session?.remoteIpAddress}")
            CastServerUtils.logInfo("Headers: ${session?.headers}")
            
            if (session?.uri?.contains(PART_COVER_ART) == true) {
                val albumId = session.parameters?.get(PARAM_ID)?.get(0) ?: return errorResponse("Missing album ID")
                val albumArtUri = MusicUtil.getMediaStoreAlbumCoverUri(albumId.toLong())
                val fis: InputStream?
                try {
                    fis = context.contentResolver.openInputStream(albumArtUri)
                    CastServerUtils.logInfo("Serving album art for ID: $albumId")
                } catch (e: FileNotFoundException) {
                    CastServerUtils.logError("Album art not found for ID: $albumId - ${e.message}")
                    return errorResponse("Album art not found")
                }
                return newChunkedResponse(Status.OK, MIME_TYPE_IMAGE, fis)
            } else if (session?.uri?.contains(PART_SONG) == true) {
                val songId = session.parameters?.get(PARAM_ID)?.get(0) ?: return errorResponse("Missing song ID")
                val songUri = MusicUtil.getSongFileUri(songId.toLong())
                val songPath = MusicUtil.getSongFilePath(context, songUri)
                val song = File(songPath)
                
                if (!song.exists()) {
                    CastServerUtils.logError("Song file not found: $songPath")
                    return errorResponse("Song file not found")
                }
                
                CastServerUtils.logInfo("Serving song: $songPath (${song.length()} bytes)")
                return serveFile(session.headers!!, song, MIME_TYPE_AUDIO)
            }
            
            CastServerUtils.logError("Invalid request URI: ${session?.uri}")
            return newFixedLengthResponse(Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        } catch (e: Exception) {
            CastServerUtils.logError("Error serving request: ${e.message}")
            return errorResponse("Internal server error")
        }
    }

    private fun serveFile(
        header: MutableMap<String, String>,
        file: File,
        mime: String
    ): Response {
        var res: Response
        try {
            CastServerUtils.logInfo("Serving file: ${file.path} with MIME type: $mime")
            CastServerUtils.logInfo("Request headers: $header")
            
            var startFrom: Long = 0
            var endAt: Long = -1
            var range = header["range"]
            if (range != null) {
                CastServerUtils.logInfo("Range request: $range")
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length)
                    val minus = range.indexOf('-')
                    try {
                        if (minus > 0) {
                            startFrom = range.substring(0, minus).toLong()
                            endAt = range.substring(minus + 1).toLong()
                            CastServerUtils.logInfo("Parsed range - start: $startFrom, end: $endAt")
                        }
                    } catch (e: NumberFormatException) {
                        CastServerUtils.logError("Failed to parse range: ${e.message}")
                    }
                }
            }

            val fileLen = file.length()
            if (range != null && startFrom >= 0) {
                if (startFrom >= fileLen) {
                    CastServerUtils.logError("Requested range start ($startFrom) exceeds file length ($fileLen)")
                    res = newFixedLengthResponse(Status.RANGE_NOT_SATISFIABLE, MIME_PLAINTEXT, "")
                    res.addHeader("Content-Range", "bytes 0-0/$fileLen")
                } else {
                    if (endAt < 0) {
                        endAt = fileLen - 1
                    }
                    var newLen = endAt - startFrom + 1
                    if (newLen < 0) {
                        newLen = 0
                    }
                    val dataLen = newLen
                    val fis: FileInputStream = object : FileInputStream(file) {
                        @Throws(IOException::class)
                        override fun available(): Int {
                            return dataLen.toInt()
                        }
                    }
                    fis.skip(startFrom)
                    res = newChunkedResponse(Status.PARTIAL_CONTENT, mime, fis)
                    res.addHeader("Content-Length", "" + dataLen)
                    res.addHeader("Content-Range", "bytes $startFrom-$endAt/$fileLen")
                    CastServerUtils.logInfo("Serving partial content: bytes $startFrom-$endAt/$fileLen")
                }
            } else {
                res = newFixedLengthResponse(Status.OK, mime, file.inputStream(), file.length())
                res.addHeader("Accept-Ranges", "bytes")
                res.addHeader("Content-Length", "" + fileLen)
                CastServerUtils.logInfo("Serving full file: $fileLen bytes")
            }
            
            // Add CORS headers to allow Cast device access
            res.addHeader("Access-Control-Allow-Origin", "*")
            res.addHeader("Access-Control-Allow-Methods", "GET, HEAD")
            res.addHeader("Access-Control-Allow-Headers", "Range")
            
            return res
        } catch (ioe: IOException) {
            val errorMsg = "Failed to read file: ${ioe.message}"
            CastServerUtils.logError(errorMsg)
            res = newFixedLengthResponse(Status.FORBIDDEN, MIME_PLAINTEXT, "FORBIDDEN: $errorMsg")
        }
        return res
    }

    private fun errorResponse(message: String = "Error Occurred"): Response {
        CastServerUtils.logError("Returning error response: $message")
        return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, message)
    }
}

fun getCurrentServerPort(): Int = currentServerPort