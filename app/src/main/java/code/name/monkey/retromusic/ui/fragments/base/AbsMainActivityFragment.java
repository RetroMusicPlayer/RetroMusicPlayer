package code.name.monkey.retromusic.ui.fragments.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.MainActivity;
import code.name.monkey.retromusic.util.RetroUtil;


public abstract class AbsMainActivityFragment extends AbsMusicServiceFragment {

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getMainActivity().setNavigationbarColorAuto();
        getMainActivity().setLightNavigationBar(true);
        getMainActivity().setTaskDescriptionColorAuto();
        getMainActivity().hideStatusBar();
    }


    // WORKAROUND
    public void setStatusbarColor(View view, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final View statusBar = view.findViewById(R.id.status_bar);
            if (statusBar != null) {
                RetroUtil.statusBarHeight(statusBar);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    statusBar.setBackgroundColor(color);
                    getMainActivity().setLightStatusbarAuto(color);
                } else {
                    statusBar.setBackgroundColor(color);
                }
            }
        }
    }

    public void setStatusbarColorAuto(View view) {
        // we don't want to use statusbar color because we are doing the color darkening on our own to support KitKat
        //noinspection ConstantConditions
        setStatusbarColor(view, ColorUtil.darkenColor(ThemeStore.primaryColor(getContext())));
    }
}
