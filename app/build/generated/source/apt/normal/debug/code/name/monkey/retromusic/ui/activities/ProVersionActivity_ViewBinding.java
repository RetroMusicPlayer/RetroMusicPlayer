// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProVersionActivity_ViewBinding implements Unbinder {
  private ProVersionActivity target;

  private View view7f0a01fa;

  private View view7f0a01f4;

  @UiThread
  public ProVersionActivity_ViewBinding(ProVersionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProVersionActivity_ViewBinding(final ProVersionActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.restore_button, "field 'restoreButton' and method 'onViewClicked'");
    target.restoreButton = Utils.castView(view, R.id.restore_button, "field 'restoreButton'", MaterialButton.class);
    view7f0a01fa = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.purchase_button, "field 'purchaseButton' and method 'onViewClicked'");
    target.purchaseButton = Utils.castView(view, R.id.purchase_button, "field 'purchaseButton'", MaterialButton.class);
    view7f0a01f4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.appBar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBar'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProVersionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.restoreButton = null;
    target.purchaseButton = null;
    target.appBar = null;
    target.title = null;

    view7f0a01fa.setOnClickListener(null);
    view7f0a01fa = null;
    view7f0a01f4.setOnClickListener(null);
    view7f0a01f4 = null;
  }
}
