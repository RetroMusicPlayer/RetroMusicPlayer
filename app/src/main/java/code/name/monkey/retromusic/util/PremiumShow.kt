package code.name.monkey.retromusic.util

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.activities.PurchaseActivity

object PremiumShow {
    private const val PREF_NAME = "premium_show"
    private const val LAUNCH_COUNT = "launch_count"
    private const val DATE_FIRST_LAUNCH = "date_first_launch"

    @JvmStatic
    fun launch(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (App.isProVersion()) {
            return
        }
        val prefEditor = pref.edit()
        val launchCount = pref.getLong(LAUNCH_COUNT, 0) + 1
        prefEditor.putLong(LAUNCH_COUNT, launchCount)

        var dateLaunched = pref.getLong(DATE_FIRST_LAUNCH, 0)
        if (dateLaunched == 0L) {
            dateLaunched = System.currentTimeMillis()
            prefEditor.putLong(DATE_FIRST_LAUNCH, dateLaunched)
        }
        if (System.currentTimeMillis() >= dateLaunched + 2 * 24 * 60 * 60 * 1000) {
            ActivityCompat.startActivity(
                context,
                Intent(context, PurchaseActivity::class.java),
                null
            )
        }
        prefEditor.apply()
    }
}