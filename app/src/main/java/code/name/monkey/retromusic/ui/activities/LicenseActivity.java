package code.name.monkey.retromusic.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;

public class LicenseActivity extends AbsBaseActivity {
  @BindView(R.id.license)
  WebView mLicense;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.appbar)
  AppBarLayout mAppbar;

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

    mToolbar.setTitle(R.string.licenses);
    mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
    mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    mToolbar.setBackgroundColor(ThemeStore.primaryColor(this));
    mAppbar.setBackgroundColor(ThemeStore.primaryColor(this));
    setSupportActionBar(mToolbar);
  }
}