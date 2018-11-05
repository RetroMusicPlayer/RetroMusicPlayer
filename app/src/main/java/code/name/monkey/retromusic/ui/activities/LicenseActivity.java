package code.name.monkey.retromusic.ui.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;

public class LicenseActivity extends AbsBaseActivity {
    @BindView(R.id.license)
    WebView mLicense;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar)
    AppBarLayout mAppbar;

    @BindView(R.id.title)
    TextView title;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        mLicense.loadUrl("file:///android_asset/index.html");

        title.setTextColor(ThemeStore.textColorPrimary(this));
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        mAppbar.setBackgroundColor(ThemeStore.primaryColor(this));
        setTitle(null);
        setSupportActionBar(toolbar);
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this));
    }
}