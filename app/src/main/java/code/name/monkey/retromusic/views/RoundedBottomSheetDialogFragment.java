package code.name.monkey.retromusic.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
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
    return
        PreferenceUtil.getInstance(getContext()).getGeneralTheme() == R.style.Theme_RetroMusic_Light
            ? R.style.BottomSheetDialogTheme : R.style.BottomSheetDialogThemeDark;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AbsThemeActivity absThemeActivity = (AbsThemeActivity) getActivity();
    if (absThemeActivity != null) {
      absThemeActivity.setLightNavigationBar(true);
      Dialog dialog = new BottomSheetDialog(getContext(), getTheme());

    }
    //noinspection ConstantConditions
    return new BottomSheetDialog(getContext(), getTheme());
  }
}