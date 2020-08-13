package code.name.monkey.retromusic.fragments.about

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.ContributorAdapter
import code.name.monkey.retromusic.model.Contributor
import code.name.monkey.retromusic.util.NavigationUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.card_credit.*
import kotlinx.android.synthetic.main.card_other.*
import kotlinx.android.synthetic.main.card_retro_info.*
import kotlinx.android.synthetic.main.card_social.*
import java.io.IOException
import java.nio.charset.StandardCharsets

class AboutFragment : Fragment(R.layout.fragment_about), View.OnClickListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        version.setSummary(getAppVersion())
        setUpView()
        loadContributors()
    }

    private val contributorsJson: String?
        get() {
            val json: String
            try {
                val inputStream = requireActivity().assets.open("contributors.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, StandardCharsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }


    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun setUpView() {
        appGithub.setOnClickListener(this)
        faqLink.setOnClickListener(this)
        telegramLink.setOnClickListener(this)
        appRate.setOnClickListener(this)
        appTranslation.setOnClickListener(this)
        appShare.setOnClickListener(this)
        donateLink.setOnClickListener(this)
        instagramLink.setOnClickListener(this)
        twitterLink.setOnClickListener(this)
        changelog.setOnClickListener(this)
        openSource.setOnClickListener(this)
        pinterestLink.setOnClickListener(this)
        bugReportLink.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.pinterestLink -> openUrl(Constants.PINTEREST)
            R.id.faqLink -> openUrl(Constants.FAQ_LINK)
            R.id.telegramLink -> openUrl(Constants.APP_TELEGRAM_LINK)
            R.id.appGithub -> openUrl(Constants.GITHUB_PROJECT)
            R.id.appTranslation -> openUrl(Constants.TRANSLATE)
            R.id.appRate -> openUrl(Constants.RATE_ON_GOOGLE_PLAY)
            R.id.appShare -> shareApp()
            R.id.donateLink -> NavigationUtil.goToSupportDevelopment(requireActivity())
            R.id.instagramLink -> openUrl(Constants.APP_INSTAGRAM_LINK)
            R.id.twitterLink -> openUrl(Constants.APP_TWITTER_LINK)
            R.id.changelog -> openUrl(Constants.TELEGRAM_CHANGE_LOG)
            R.id.openSource -> NavigationUtil.goToOpenSource(requireActivity())
            R.id.bugReportLink -> NavigationUtil.bugReport(requireActivity())
        }
    }

    private fun getAppVersion(): String {
        return try {
            val isPro = if (App.isProVersion()) "Pro" else "Free"
            val packageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            "${packageInfo.versionName} $isPro"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "0.0.0"
        }
    }

    private fun shareApp() {
        ShareCompat.IntentBuilder.from(requireActivity()).setType("text/plain")
            .setChooserTitle(R.string.share_app)
            .setText(String.format(getString(R.string.app_share), requireActivity().packageName))
            .startChooser()
    }

    private fun loadContributors() {
        val type = object : TypeToken<List<Contributor>>() {

        }.type
        val contributors = Gson().fromJson<List<Contributor>>(contributorsJson, type)

        val contributorAdapter = ContributorAdapter(contributors)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = contributorAdapter
        }
    }
}
