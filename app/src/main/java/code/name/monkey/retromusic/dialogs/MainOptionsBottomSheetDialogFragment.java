package code.name.monkey.retromusic.dialogs;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.ui.activities.MainActivity;
import code.name.monkey.retromusic.ui.fragments.mainactivity.folders.FoldersFragment;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

public class MainOptionsBottomSheetDialogFragment extends RoundedBottomSheetDialogFragment {

    private static final String TAG = "MainOptionsBottomSheetD";
    private static ButterKnife.Setter<TextView, Integer> textColor = (view, value, index) -> view.setTextColor(ColorStateList.valueOf(value));


    @BindViews({R.id.action_folders, R.id.action_settings, R.id.action_about, R.id.action_buy_pro, R.id.action_rate,
            R.id.action_sleep_timer})
    List<MaterialButton> materialButtons;


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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_options, container, false);
        ButterKnife.bind(this, layout);
        layout.findViewById(R.id.action_buy_pro).setVisibility(RetroApplication.Companion.isProVersion() ? View.GONE : View.VISIBLE);
        ButterKnife.apply(materialButtons, textColor, ThemeStore.textColorPrimary(getContext()));
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick({R.id.action_folders, R.id.action_settings, R.id.action_sleep_timer, R.id.action_rate,
            R.id.action_buy_pro, R.id.action_about})
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
                NavigationUtil.goToAbout(mainActivity);
                break;
            case R.id.action_buy_pro:
                NavigationUtil.goToProVersion(mainActivity);
                break;
            case R.id.action_sleep_timer:
                if (getFragmentManager() != null) {
                    new SleepTimerDialog().show(getFragmentManager(), TAG);
                }
                break;
            case R.id.action_rate:
                NavigationUtil.goToPlayStore(mainActivity);
                break;
        }
        dismiss();
    }


}
