package code.name.monkey.retromusic.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.ui.activities.SettingsActivity
import kotlinx.android.synthetic.main.fragment_main_settings.*

class MainSettingsFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.generalSettings -> inflateFragment(ThemeSettingsFragment(), R.string.general_settings_title)
            R.id.audioSettings -> inflateFragment(AudioSettings(), R.string.pref_header_audio)
            R.id.nowPlayingSettings -> inflateFragment(NowPlayingSettingsFragment(), R.string.now_playing)
            R.id.personalizeSettings -> inflateFragment(PersonaizeSettingsFragment(), R.string.personalize)
            R.id.imageSettings -> inflateFragment(ImageSettingFragment(), R.string.pref_header_images)
            R.id.notificationSettings -> inflateFragment(NotificationSettingsFragment(), R.string.notification)
            R.id.otherSettings -> inflateFragment(OtherSettingsFragment(), R.string.others)
        }
    }

    private val settingsIcons = arrayOf(R.id.general_settings_icon, R.id.audio_settings_icon, R.id.now_playing_settings_icon, R.id.personalize_settings_icon, R.id.image_settings_icon, R.id.notification_settings_icon, R.id.other_settings_icon)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsIcons.forEach {
            view.findViewById<ImageView>(it).setColorFilter(ThemeStore.accentColor(context!!))
        }
        generalSettings.setOnClickListener(this)
        audioSettings.setOnClickListener(this)
        nowPlayingSettings.setOnClickListener(this)
        personalizeSettings.setOnClickListener(this)
        imageSettings.setOnClickListener(this)
        notificationSettings.setOnClickListener(this)
        otherSettings.setOnClickListener(this)
    }

    private fun inflateFragment(fragment: Fragment, @StringRes title: Int) {
        if (activity != null) {
            (activity as SettingsActivity).setupFragment(fragment, title)
        }
    }
}