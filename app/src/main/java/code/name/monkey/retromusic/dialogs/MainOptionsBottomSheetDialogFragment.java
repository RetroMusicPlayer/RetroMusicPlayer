package code.name.monkey.retromusic.dialogs;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.ui.activities.MainActivity;
import code.name.monkey.retromusic.ui.fragments.mainactivity.folders.FoldersFragment;
import code.name.monkey.retromusic.util.Compressor;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.CircularImageView;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static code.name.monkey.retromusic.Constants.USER_PROFILE;

public class MainOptionsBottomSheetDialogFragment extends RoundedBottomSheetDialogFragment {

    private static final String TAG = "MainOptionsBottomSheetD";
    private static ButterKnife.Setter<TextView, Integer> textColor = (view, value, index) -> view.setTextColor(ColorStateList.valueOf(value));

    @BindView(R.id.user_image_bottom)
    CircularImageView userImageBottom;

    @BindView(R.id.title_welcome)
    AppCompatTextView titleWelcome;

    @BindView(R.id.text)
    AppCompatTextView text;

    @BindViews({R.id.action_folders, R.id.action_about, R.id.action_buy_pro, R.id.action_rate, R.id.action_sleep_timer})
    List<MaterialButton> materialButtons;

    private CompositeDisposable disposable = new CompositeDisposable();

    public static MainOptionsBottomSheetDialogFragment newInstance(int selected_id) {
        Bundle bundle = new Bundle();
        bundle.putInt("selected_id", selected_id);
        MainOptionsBottomSheetDialogFragment fragment = new MainOptionsBottomSheetDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainOptionsBottomSheetDialogFragment newInstance() {
        return new MainOptionsBottomSheetDialogFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_options, container, false);
        ButterKnife.bind(this, layout);
        layout.findViewById(R.id.action_buy_pro).setVisibility(RetroApplication.isProVersion() ? View.GONE : View.VISIBLE);
        ButterKnife.apply(materialButtons, textColor, ThemeStore.textColorPrimary(getContext()));
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

    @OnClick({R.id.action_folders, R.id.action_settings, R.id.action_sleep_timer, R.id.action_rate,
            R.id.user_info_container, R.id.action_buy_pro, R.id.action_about})
    void onClick(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.action_folders:
                mainActivity.setCurrentFragment(FoldersFragment.newInstance(getContext()), true, FoldersFragment.TAG);
                break;
            case R.id.action_settings:
                NavigationUtil.goToSettings(mainActivity);
                break;
            case R.id.action_about:
                NavigationUtil.goToAbout(getActivity());
                break;
            case R.id.action_buy_pro:
                NavigationUtil.goToProVersion(getActivity());
                break;
            case R.id.action_sleep_timer:
                if (getFragmentManager() != null) {
                    new SleepTimerDialog().show(getFragmentManager(), TAG);
                }
                break;
            case R.id.user_info_container:
                NavigationUtil.goToUserInfo(getActivity());
                break;
            case R.id.action_rate:
                NavigationUtil.goToPlayStore(getActivity());
                break;
        }
        dismiss();
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
                        new File(PreferenceUtil.getInstance(getContext()).getProfileImage(), USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userImageBottom::setImageBitmap,
                        throwable -> userImageBottom.setImageDrawable(ContextCompat
                                .getDrawable(getContext(), R.drawable.ic_person_flat))));
    }
}
