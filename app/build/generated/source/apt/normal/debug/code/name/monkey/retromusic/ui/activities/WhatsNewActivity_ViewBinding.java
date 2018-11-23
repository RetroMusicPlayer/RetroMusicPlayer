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

public class WhatsNewActivity_ViewBinding implements Unbinder {
  private WhatsNewActivity target;

  @UiThread
  public WhatsNewActivity_ViewBinding(WhatsNewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WhatsNewActivity_ViewBinding(WhatsNewActivity target, View source) {
    this.target = target;

    target.webView = Utils.findRequiredViewAsType(source, R.id.web_view, "field 'webView'", WebView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WhatsNewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.webView = null;
    target.title = null;
    target.toolbar = null;
    target.appBarLayout = null;
  }
}
