package code.name.monkey.appthemehelper

import android.os.Bundle
import android.os.Handler
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Aidan Follestad (afollestad), Karim Abou Zeid (kabouzeid)
 */
open class ATHActivity : AppCompatActivity() {

    private var updateTime: Long = -1

    protected val themeRes: Int
        @StyleRes
        get() = ThemeStore.activityTheme(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(themeRes)
        super.onCreate(savedInstanceState)
        updateTime = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        if (ATH.didThemeValuesChange(this, updateTime)) {
            onThemeChanged()
        }
    }

    open fun onThemeChanged() {
        postRecreate()
    }

    fun postRecreate() {
        // hack to prevent java.lang.RuntimeException: Performing pause of activity that is not resumed
        // makes sure recreate() is called right after and not in onResume()
        Handler().post { recreate() }
    }
}