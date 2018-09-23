package code.name.monkey.retromusic.ui.fragments.settings;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.SettingsActivity;
import code.name.monkey.retromusic.util.Compressor;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.CircularImageView;
import code.name.monkey.retromusic.views.IconImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainSettingsFragment extends Fragment {

    @BindViews({R.id.general_settings_icon, R.id.audio_settings_icon,
            R.id.now_playing_settings_icon, R.id.personalize_settings_icon,
            R.id.image_settings_icon, R.id.notification_settings_icon, R.id.other_settings_icon})
    List<IconImageView> icons;

    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.user_image_bottom)
    CircularImageView userImageBottom;
    @BindView(R.id.title_welcome)
    AppCompatTextView titleWelcome;
    @BindView(R.id.text)
    AppCompatTextView text;
    private Unbinder unbinder;
    private ButterKnife.Action<View> apply = (view, index) -> {
        //noinspection ConstantConditions
        ((IconImageView) view).setColorFilter(ThemeStore.accentColor(getContext()), PorterDuff.Mode.SRC_IN);
    };
    private CompositeDisposable disposable = new CompositeDisposable();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text.setTextColor(ThemeStore.textColorSecondary(getContext()));
        titleWelcome.setTextColor(ThemeStore.textColorPrimary(getContext()));
        titleWelcome.setText(String.format("%s %s!", getTimeOfTheDay(), PreferenceUtil.getInstance(getContext()).getUserName()));
        loadImageFromStorage();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        unbinder.unbind();
    }

    @OnClick({R.id.general_settings, R.id.audio_settings, R.id.now_playing_settings,
            R.id.user_info_container, R.id.image_settings, R.id.personalize_settings, R.id.notification_settings, R.id.other_settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.general_settings:
                inflateFragment(new ThemeSettingsFragment(), R.string.general_settings_title);
                break;
            case R.id.audio_settings:
                inflateFragment(new AudioSettings(), R.string.pref_header_audio);
                break;
            case R.id.user_info_container:
                NavigationUtil.goToUserInfo(getActivity());
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

    private String getTimeOfTheDay() {
        String message = getString(R.string.title_good_day);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 6) {
            message = getString(R.string.title_good_night);
        } else if (timeOfDay >= 6 && timeOfDay < 12) {
            message = getString(R.string.title_good_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            message = getString(R.string.title_good_afternoon);
        } else if (timeOfDay >= 16 && timeOfDay < 20) {
            message = getString(R.string.title_good_evening);
        } else if (timeOfDay >= 20 && timeOfDay < 24) {
            message = getString(R.string.title_good_night);
        }
        return message;
    }


    private void loadImageFromStorage() {
        //noinspection ConstantConditions
        disposable.add(new Compressor(getContext())
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(
                        new File(PreferenceUtil.getInstance(getContext()).getProfileImage(), Constants.USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userImageBottom::setImageBitmap,
                        throwable -> userImageBottom.setImageDrawable(ContextCompat
                                .getDrawable(getContext(), R.drawable.ic_person_flat))));
    }

}