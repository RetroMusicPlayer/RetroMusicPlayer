package code.name.monkey.retromusic.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //behavior.setPeekHeight(0);
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //noinspection ConstantConditions
        return new BottomSheetDialog(getContext(), getTheme());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = getDialog().getWindow();
            window.findViewById(com.google.android.material.R.id.container).setFitsSystemWindows(false);
            // dark navigation bar icons
            View decorView = window.getDecorView();
            if (!ATHUtil.isWindowBackgroundDark(getContext()))
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}