// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
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
import java.lang.IllegalStateException;
import java.lang.Override;

public class AboutActivity_ViewBinding implements Unbinder {
  private AboutActivity target;

  private View view7f0a0087;

  private View view7f0a0102;

  private View view7f0a0088;

  private View view7f0a008c;

  private View view7f0a0089;

  private View view7f0a008a;

  private View view7f0a0136;

  private View view7f0a0273;

  private View view7f0a00c6;

  private View view7f0a01c5;

  private View view7f0a00e9;

  private View view7f0a024c;

  private View view7f0a00eb;

  @UiThread
  public AboutActivity_ViewBinding(AboutActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AboutActivity_ViewBinding(final AboutActivity target, View source) {
    this.target = target;

    View view;
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appVersion = Utils.findRequiredViewAsType(source, R.id.app_version, "field 'appVersion'", TextView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.app_github, "method 'onViewClicked'");
    view7f0a0087 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.faq_link, "method 'onViewClicked'");
    view7f0a0102 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_google_plus, "method 'onViewClicked'");
    view7f0a0088 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_translation, "method 'onViewClicked'");
    view7f0a008c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_rate, "method 'onViewClicked'");
    view7f0a0089 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_share, "method 'onViewClicked'");
    view7f0a008a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.instagram_link, "method 'onViewClicked'");
    view7f0a0136 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.twitter_link, "method 'onViewClicked'");
    view7f0a0273 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.changelog, "method 'onViewClicked'");
    view7f0a00c6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.open_source, "method 'onViewClicked'");
    view7f0a01c5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.discord_link, "method 'onViewClicked'");
    view7f0a00e9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.telegram_link, "method 'onViewClicked'");
    view7f0a024c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.donate_link, "method 'onViewClicked'");
    view7f0a00eb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.appBarLayout = null;
    target.toolbar = null;
    target.appVersion = null;
    target.title = null;
    target.recyclerView = null;

    view7f0a0087.setOnClickListener(null);
    view7f0a0087 = null;
    view7f0a0102.setOnClickListener(null);
    view7f0a0102 = null;
    view7f0a0088.setOnClickListener(null);
    view7f0a0088 = null;
    view7f0a008c.setOnClickListener(null);
    view7f0a008c = null;
    view7f0a0089.setOnClickListener(null);
    view7f0a0089 = null;
    view7f0a008a.setOnClickListener(null);
    view7f0a008a = null;
    view7f0a0136.setOnClickListener(null);
    view7f0a0136 = null;
    view7f0a0273.setOnClickListener(null);
    view7f0a0273 = null;
    view7f0a00c6.setOnClickListener(null);
    view7f0a00c6 = null;
    view7f0a01c5.setOnClickListener(null);
    view7f0a01c5 = null;
    view7f0a00e9.setOnClickListener(null);
    view7f0a00e9 = null;
    view7f0a024c.setOnClickListener(null);
    view7f0a024c = null;
    view7f0a00eb.setOnClickListener(null);
    view7f0a00eb = null;
  }
}
