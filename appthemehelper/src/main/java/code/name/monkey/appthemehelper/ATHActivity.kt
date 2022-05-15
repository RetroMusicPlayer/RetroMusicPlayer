package code.name.monkey.appthemehelper

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Aidan Follestad (afollestad), Karim Abou Zeid (kabouzeid)
 */
open class ATHActivity : AppCompatActivity() {

    private var updateTime: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTime = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        if (ATH.didThemeValuesChange(this, updateTime)) {
            onThemeChanged()
        }
    }

    private fun onThemeChanged() {
        postRecreate()
    }

    fun postRecreate() {
        // hack to prevent java.lang.RuntimeException: Performing pause of activity that is not resumed
        // makes sure recreate() is called right after and not in onResume()
        Handler(Looper.getMainLooper()).post { recreate() }
    }
}