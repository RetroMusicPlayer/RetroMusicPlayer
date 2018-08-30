package code.name.monkey.retromusic.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsThemeActivity;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * Created by yu on 2016/11/10.
 */
@SuppressLint("RestrictedApi")
public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @Override
    public int getTheme() {
        //noinspection ConstantConditions
        if (PreferenceUtil.getInstance(getContext()).getGeneralTheme() == R.style.Theme_RetroMusic_Light) {
            return R.style.BottomSheetDialogTheme;
        } else if (PreferenceUtil.getInstance(getContext()).getGeneralTheme() == R.style.Theme_RetroMusic_Color) {
            int color = ThemeStore.primaryColor(getContext());
            if (ColorUtil.isColorLight(color)) {
                return R.style.BottomSheetDialogTheme;
            } else {
                return R.style.BottomSheetDialogThemeDark;
            }
        } else {
            return R.style.BottomSheetDialogTheme;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AbsThemeActivity absThemeActivity = (AbsThemeActivity) getActivity();
        if (absThemeActivity != null) {
            absThemeActivity.setLightNavigationBar(true);
        }
        //noinspection ConstantConditions
        return new BottomSheetDialog(getContext(), getTheme());
    }
}