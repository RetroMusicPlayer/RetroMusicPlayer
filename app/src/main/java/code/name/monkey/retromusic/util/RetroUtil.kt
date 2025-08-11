package code.name.monkey.retromusic.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import code.name.monkey.retromusic.App.Companion.getContext
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.util.*
import android.util.Log

object RetroUtil {
    fun formatValue(numValue: Float): String {
        var value = numValue
        val arr = arrayOf("", "K", "M", "B", "T", "P", "E")
        var index = 0
        while (value / 1000 >= 1) {
            value /= 1000
            index++
        }
        val decimalFormat = DecimalFormat("#.##")
        return String.format("%s %s", decimalFormat.format(value.toDouble()), arr[index])
    }

    fun frequencyCount(frequency: Int): Float {
        return (frequency / 1000.0).toFloat()
    }

    fun getScreenSize(context: Context): Point {
        val x: Int = context.resources.displayMetrics.widthPixels
        val y: Int = context.resources.displayMetrics.heightPixels
        return Point(x, y)
    }

    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = getContext()
                .resources
                .getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = getContext().resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    val navigationBarHeight: Int
        get() {
            var result = 0
            val resourceId = getContext()
                .resources
                .getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = getContext().resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    val isLandscape: Boolean
        get() = (getContext().resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)
    val isTablet: Boolean
        get() = (getContext().resources.configuration.smallestScreenWidthDp
                >= 600)

    fun getIpAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                // Skip loopback and inactive interfaces
                if (intf.isLoopback || !intf.isUp) continue
                
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        if (sAddr != null) {
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                // Skip local and link-local addresses
                                if (isIPv4 && !addr.isLinkLocalAddress && !addr.isSiteLocalAddress) continue
                                if (isIPv4) {
                                    Log.d("RetroUtil", "Using IP address: $sAddr")
                                    return sAddr
                                }
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%')
                                    val processedAddr = if (delim < 0) {
                                        sAddr.uppercase()
                                    } else {
                                        sAddr.substring(0, delim).uppercase()
                                    }
                                    Log.d("RetroUtil", "Using IPv6 address: $processedAddr")
                                    return processedAddr
                                }
                            }
                        }
                    }
                }
            }
            Log.e("RetroUtil", "No suitable network interface found")
        } catch (e: Exception) {
            Log.e("RetroUtil", "Error getting IP address: ${e.message}")
        }
        return null
    }
}