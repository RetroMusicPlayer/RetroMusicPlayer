package code.name.monkey.retromusic.activities.base

import android.os.Bundle
import code.name.monkey.appthemehelper.ATHActivity
import code.name.monkey.retromusic.helper.TopExceptionHandler

abstract class AbsCrashCollector : ATHActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(TopExceptionHandler())
    }
}