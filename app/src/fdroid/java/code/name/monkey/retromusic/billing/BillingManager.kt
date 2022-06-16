package code.name.monkey.retromusic.billing

import android.content.Context

@Suppress("UNUSED_PARAMETER")
class BillingManager(context: Context) {

    fun release() {}

    val isProVersion: Boolean
        get() = true
}