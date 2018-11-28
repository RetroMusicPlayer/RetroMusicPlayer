// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SupportDevelopmentActivity_ViewBinding implements Unbinder {
  private SupportDevelopmentActivity target;

  private View view7f0a00eb;

  private View view7f0a0117;

  @UiThread
  public SupportDevelopmentActivity_ViewBinding(SupportDevelopmentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SupportDevelopmentActivity_ViewBinding(final SupportDevelopmentActivity target,
      View source) {
    this.target = target;

    View view;
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress, "field 'progressBar'", ProgressBar.class);
    target.progressContainer = Utils.findRequiredView(source, R.id.progress_container, "field 'progressContainer'");
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list, "field 'recyclerView'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.viewGroup = Utils.findRequiredViewAsType(source, R.id.root, "field 'viewGroup'", ViewGroup.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    view = Utils.findRequiredView(source, R.id.donate, "field 'materialButton' and method 'donate'");
    target.materialButton = Utils.castView(view, R.id.donate, "field 'materialButton'", MaterialButton.class);
    view7f0a00eb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.donate();
      }
    });
    view = Utils.findRequiredView(source, R.id.google_pay, "method 'googlePay'");
    view7f0a0117 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.googlePay();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SupportDevelopmentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progressBar = null;
    target.progressContainer = null;
    target.recyclerView = null;
    target.toolbar = null;
    target.appBarLayout = null;
    target.viewGroup = null;
    target.title = null;
    target.materialButton = null;

    view7f0a00eb.setOnClickListener(null);
    view7f0a00eb = null;
    view7f0a0117.setOnClickListener(null);
    view7f0a0117 = null;
  }
}
