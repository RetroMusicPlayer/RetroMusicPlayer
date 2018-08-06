package code.name.monkey.retromusic.dialogs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

/**
 * @author Hemanth S (h4h13).
 */
public class HomeOptionDialog extends RoundedBottomSheetDialogFragment {

    private static final String TAG = "HomeOptionDialog";
    Unbinder mUnbinder;

    @BindView(R.id.user_image_bottom)
    CircularImageView userImageBottom;

    @BindView(R.id.title_welcome)
    AppCompatTextView titleWelcome;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.user_action_details, container, false);
        mUnbinder = ButterKnife.bind(this, layout);
        layout.findViewById(R.id.action_buy_pro).setVisibility(RetroApplication.isProVersion() ? View
                .GONE : View.VISIBLE);
        return layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadImageFromStorage();
        //noinspection ConstantConditions
        titleWelcome.setText(String.format("%s, %s!", getTimeOfTheDay(),
                PreferenceUtil.getInstance(getContext()).getUserName()));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        mUnbinder.unbind();
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick({R.id.action_about, R.id.user_info_container, R.id.action_buy_pro, R.id.action_folder,
            R.id.action_settings, R.id.action_sleep_timer,R.id.action_rate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_info_container:
                NavigationUtil.goToUserInfo(getActivity());
                break; case R.id.action_rate:
                NavigationUtil.goToPlayStore(getActivity());
                break;
            case R.id.action_folder:
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity == null) {
                    return;
                }
                mainActivity.setCurrentFragment(FoldersFragment.newInstance(getContext()), true);
                break;
            case R.id.action_settings:
                NavigationUtil.goToSettings(getActivity());
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
        }
        dismiss();
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
