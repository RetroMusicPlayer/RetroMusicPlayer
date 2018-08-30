package code.name.monkey.retromusic.ui.fragments.settings;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.SettingsActivity;
import code.name.monkey.retromusic.views.IconImageView;

public class MainSettingsFragment extends Fragment {

    @BindViews({R.id.general_settings_icon, R.id.audio_settings_icon,
            R.id.now_playing_settings_icon, R.id.personalize_settings_icon,
            R.id.image_settings_icon, R.id.notification_settings_icon, R.id.other_settings_icon})
    List<IconImageView> icons;

    @BindView(R.id.container)
    ViewGroup container;

    private Unbinder unbinder;
    private ButterKnife.Action<View> apply = (view, index) -> {
        //noinspection ConstantConditions
        ((IconImageView) view).setColorFilter(ThemeStore.accentColor(getContext()), PorterDuff.Mode.SRC_IN);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_settings, container, false);
        unbinder = ButterKnife.bind(this, layout);
        ButterKnife.apply(icons, apply);
        return layout;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.general_settings, R.id.audio_settings, R.id.now_playing_settings,
            R.id.image_settings, R.id.personalize_settings, R.id.notification_settings, R.id.other_settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.general_settings:
                inflateFragment(new ThemeSettingsFragment(), R.string.general_settings_title);
                break;
            case R.id.audio_settings:
                inflateFragment(new AudioSettings(), R.string.pref_header_audio);
                break;
            case R.id.now_playing_settings:
                inflateFragment(new NowPlayingSettingsFragment(), R.string.now_playing);
                break;
            case R.id.personalize_settings:
                inflateFragment(new PersonaizeSettingsFragment(), R.string.personalize);
                break;
            case R.id.image_settings:
                inflateFragment(new ImageSettingFragment(), R.string.pref_header_images);
                break;
            case R.id.notification_settings:
                inflateFragment(new NotificationSettingsFragment(), R.string.notification);
                break;
            case R.id.other_settings:
                inflateFragment(new OtherSettingsFragment(), R.string.others);
                break;
        }
    }

    private void inflateFragment(Fragment fragment, @StringRes int title) {
        if (getActivity() != null) {
            ((SettingsActivity) getActivity()).setupFragment(fragment, title);
        }
    }
}