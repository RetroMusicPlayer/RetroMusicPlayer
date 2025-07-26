package code.name.monkey.retromusic.cast

import android.util.Log
import java.net.ServerSocket

object CastServerUtils {
    private const val TAG = "CastServer"
    
    fun isPortAvailable(port: Int): Boolean {
        return try {
            ServerSocket(port).use { true }
        } catch (e: Exception) {
            logError("Port $port is not available: ${e.message}")
            false
        }
    }
    
    fun findAvailablePort(startPort: Int, endPort: Int = startPort + 10): Int {
        for (port in startPort..endPort) {
            if (isPortAvailable(port)) {
                logInfo("Found available port: $port")
                return port
            }
        }
        logError("No available ports found in range $startPort-$endPort")
        return -1
    }
    
    fun logInfo(message: String) {
        Log.i(TAG, message)
    }
    
    fun logError(message: String) {
        Log.e(TAG, message)
    }
} 