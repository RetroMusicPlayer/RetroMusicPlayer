package code.name.monkey.retromusic.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.OnClick
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.ChangelogDialog
import code.name.monkey.retromusic.model.Contributor
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.ui.adapter.ContributorAdapter
import code.name.monkey.retromusic.util.NavigationUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.card_credit.*
import kotlinx.android.synthetic.main.card_other.*
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * @author Hemanth S (h4h13)
 */

class AboutActivity : AbsBaseActivity() {


    private val assetJsonData: String?
        get() {
            val json: String
            try {
                val inputStreams = assets.open("contributors.json")
                val size = inputStreams.available()
                val buffer = ByteArray(size)
                inputStreams.read(buffer)
                inputStreams.close()
                json = String(buffer, StandardCharsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        loadContributors()
        setUpToolbar()

        appVersion.text = getAppVersion()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar() {
        aboutTitle.setTextColor(ThemeStore.textColorPrimary(this))
        val primaryColor = ThemeStore.primaryColor(this)
        toolbar.setBackgroundColor(primaryColor)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setTitle(null)
        setSupportActionBar(toolbar)
    }


    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }


    @OnClick(R.id.app_github, R.id.faq_link, R.id.app_google_plus, R.id.app_translation,
            R.id.app_rate, R.id.app_share, R.id.instagram_link, R.id.twitter_link, R.id.changelog,
            R.id.open_source, R.id.discord_link, R.id.telegram_link, R.id.donate_link)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.faq_link -> openUrl(Constants.FAQ_LINK)
            R.id.telegram_link -> openUrl(Constants.APP_TELEGRAM_LINK)
            R.id.discord_link -> openUrl(Constants.DISCORD_LINK)
            R.id.app_github -> openUrl(Constants.GITHUB_PROJECT)
            R.id.app_google_plus -> openUrl(Constants.GOOGLE_PLUS_COMMUNITY)
            R.id.app_translation -> openUrl(Constants.TRANSLATE)
            R.id.app_rate -> openUrl(Constants.RATE_ON_GOOGLE_PLAY)
            R.id.app_share -> shareApp()
            R.id.donate_link -> NavigationUtil.goToSupportDevelopment(this)
            R.id.instagram_link -> openUrl(Constants.APP_INSTAGRAM_LINK)
            R.id.twitter_link -> openUrl(Constants.APP_TWITTER_LINK)
            R.id.changelog -> showChangeLogOptions()
            R.id.open_source -> NavigationUtil.goToOpenSource(this)
        }
    }

    private fun showChangeLogOptions() {
        MaterialDialog.Builder(this)
                .items(*arrayOf("Telegram Channel", "App"))
                .itemsCallback { _, _, position, _ ->
                    if (position == 0) {
                        openUrl(Constants.TELEGRAM_CHANGE_LOG)
                    } else {
                        ChangelogDialog.create().show(supportFragmentManager, "change-log")
                    }
                }
                .build()
                .show()
    }

    private fun getAppVersion(): String {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "0.0.0"
        }

    }

    private fun shareApp() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("songText/plain")
                .setText(String.format(getString(R.string.app_share), packageName))
                .intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(
                    Intent.createChooser(shareIntent, resources.getText(R.string.action_share)))
        }
    }

    fun loadContributors() {
        val data = assetJsonData
        val type = object : TypeToken<List<Contributor>>() {

        }.type
        val contributors = Gson().fromJson<List<Contributor>>(data, type)

        val contributorAdapter = ContributorAdapter(contributors)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = contributorAdapter
    }
}
