package code.name.monkey.retromusic.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;

/**
 * Created by yu on 2016/11/10.
 */
@SuppressLint("RestrictedApi")
public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

   /* @Override
    public int getTheme() {
        //noinspection ConstantConditions
        if (PreferenceUtil.getInstance().getGeneralTheme() == R.style.Theme_RetroMusic_Light) {
            return R.style.BottomSheetDialogTheme;
        } else if (PreferenceUtil.getInstance().getGeneralTheme() == R.style.Theme_RetroMusic_Color) {
            int color = ThemeStore.primaryColor(getContext());
            if (ColorUtil.isColorLight(color)) {
                return R.style.BottomSheetDialogTheme;
            } else {
                return R.style.BottomSheetDialogThemeDark;
            }
        } else {
            return R.style.BottomSheetDialogTheme;
        }
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bg_bottom_sheet_dialog_fragment));
        view.setBackgroundTintList(ColorStateList.valueOf(ThemeStore.primaryColor(view.getContext())));
        ((AbsBaseActivity) Objects.requireNonNull(getActivity())).setNavigationbarColorAuto();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //noinspection ConstantConditions
        return new BottomSheetDialog(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setNavigationBarColor(ThemeStore.primaryColor(getContext()));
            window.findViewById(com.google.android.material.R.id.container).setFitsSystemWindows(true);
        }
    }
}