package code.name.monkey.retromusic.ui.activities

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import butterknife.ButterKnife
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.internal.ThemeSingleton
import kotlinx.android.synthetic.main.activity_whats_new.*
import java.io.BufferedReader
import java.io.InputStreamReader

class WhatsNewActivity : AbsBaseActivity() {


    private fun setChangelogRead(context: Context) {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val currentVersion = pInfo.versionCode
            PreferenceUtil.getInstance().setLastChangeLogVersion(currentVersion)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun colorToHex(color: Int): String {
        return Integer.toHexString(color).substring(2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_new)
        ButterKnife.bind(this)

        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()

        toolbar.setBackgroundColor(ThemeStore.primaryColor(this))
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this))
        setSupportActionBar(toolbar)
        title = null
        toolbar.setNavigationOnClickListener { onBackPressed() }
        whatNewtitle.setTextColor(ThemeStore.textColorPrimary(this))
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this))

        try {
            val buf = StringBuilder()
            val json = assets.open("retro-changelog.html")
            val inputStream = BufferedReader(InputStreamReader(json, "UTF-8"))
            while (inputStream.readLine() != null) {
                buf.append(inputStream.readLine())
            }
            inputStream.close()
            // Inject color values for WebView body background and links
            val backgroundColor = colorToHex(ThemeStore.primaryColor(this))
            val contentColor = if (ThemeSingleton.get().darkTheme) "#ffffff" else "#000000"
            webView.loadData(buf.toString()
                    .replace("{style-placeholder}",
                            String.format("body { background-color: %s; color: %s; }", backgroundColor, contentColor))
                    .replace("{link-color}", colorToHex(ThemeSingleton.get().positiveColor.defaultColor))
                    .replace("{link-color-active}", colorToHex(ColorUtil.lightenColor(ThemeSingleton.get().positiveColor.defaultColor))), "text/html", "UTF-8")
        } catch (e: Throwable) {
            webView.loadData("<h1>Unable to load</h1><p>" + e.localizedMessage + "</p>", "text/html", "UTF-8")
        }

        setChangelogRead(this)
    }
}
