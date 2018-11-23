// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LicenseActivity_ViewBinding implements Unbinder {
  private LicenseActivity target;

  @UiThread
  public LicenseActivity_ViewBinding(LicenseActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LicenseActivity_ViewBinding(LicenseActivity target, View source) {
    this.target = target;

    target.mLicense = Utils.findRequiredViewAsType(source, R.id.license, "field 'mLicense'", WebView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.mAppbar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'mAppbar'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LicenseActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mLicense = null;
    target.toolbar = null;
    target.mAppbar = null;
    target.title = null;
  }
}
