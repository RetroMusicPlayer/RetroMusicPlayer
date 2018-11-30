package code.name.monkey.retromusic.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;
import code.name.monkey.retromusic.ui.fragments.settings.MainSettingsFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class SettingsActivity extends AbsBaseActivity implements ColorChooserDialog.ColorCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.detail_content_frame)
    @Nullable
    FrameLayout detailsFrame;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        switch (dialog.getTitle()) {
            case R.string.primary_color:
                int theme = ColorUtil.isColorLight(selectedColor) ?
                        PreferenceUtil.getThemeResFromPrefValue("light") :
                        PreferenceUtil.getThemeResFromPrefValue("dark");

                ThemeStore.editTheme(this).activityTheme(theme).primaryColor(selectedColor).commit();
                break;
            case R.string.accent_color:
                ThemeStore.editTheme(this).accentColor(selectedColor).commit();
                break;
        }

        recreate();
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();

        setLightNavigationBar(true);

        setupToolbar();

        if (bundle == null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainSettingsFragment())
                    .commit();
        }
    }

    private void setupToolbar() {
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this));
        setSupportActionBar(toolbar);
        setTitle(null);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        title.setTextColor(ThemeStore.textColorPrimary(this));
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this));
    }

    public void setupFragment(Fragment fragment, @StringRes int titleName) {
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.sliding_in_left, R.anim.sliding_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        title.setText(titleName);

        if (detailsFrame == null) {
            fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.detail_content_frame, fragment, fragment.getTag());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            title.setText(R.string.action_settings);
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addAppbarLayoutElevation(float v) {
        TransitionManager.beginDelayedTransition(appBarLayout);
        appBarLayout.setElevation(v);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "onSharedPreferenceChanged: ");
        if (key.equals(PreferenceUtil.PROFILE_IMAGE_PATH)) {
            recreate();
        }
    }
}
