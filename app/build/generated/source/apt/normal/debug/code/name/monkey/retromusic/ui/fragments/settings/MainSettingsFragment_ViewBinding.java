// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.settings;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.CircularImageView;
import code.name.monkey.retromusic.views.IconImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainSettingsFragment_ViewBinding implements Unbinder {
  private MainSettingsFragment target;

  private View view7f0a0111;

  private View view7f0a0093;

  private View view7f0a01c3;

  private View view7f0a027d;

  private View view7f0a0133;

  private View view7f0a01d2;

  private View view7f0a01be;

  private View view7f0a01ca;

  @UiThread
  public MainSettingsFragment_ViewBinding(final MainSettingsFragment target, View source) {
    this.target = target;

    View view;
    target.container = Utils.findRequiredViewAsType(source, R.id.container, "field 'container'", ViewGroup.class);
    target.userImageBottom = Utils.findRequiredViewAsType(source, R.id.user_image_bottom, "field 'userImageBottom'", CircularImageView.class);
    target.titleWelcome = Utils.findRequiredViewAsType(source, R.id.title_welcome, "field 'titleWelcome'", AppCompatTextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", AppCompatTextView.class);
    view = Utils.findRequiredView(source, R.id.general_settings, "method 'onViewClicked'");
    view7f0a0111 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.audio_settings, "method 'onViewClicked'");
    view7f0a0093 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.now_playing_settings, "method 'onViewClicked'");
    view7f0a01c3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.user_info_container, "method 'onViewClicked'");
    view7f0a027d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.image_settings, "method 'onViewClicked'");
    view7f0a0133 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.personalize_settings, "method 'onViewClicked'");
    view7f0a01d2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.notification_settings, "method 'onViewClicked'");
    view7f0a01be = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.other_settings, "method 'onViewClicked'");
    view7f0a01ca = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.icons = Utils.listFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.general_settings_icon, "field 'icons'", IconImageView.class), 
        Utils.findRequiredViewAsType(source, R.id.audio_settings_icon, "field 'icons'", IconImageView.class), 
        Utils.findRequiredViewAsType(source, R.id.now_playing_settings_icon, "field 'icons'", IconImageView.class), 
        Utils.findRequiredViewAsType(source, R.id.personalize_settings_icon, "field 'icons'", IconImageView.class), 
        Utils.findRequiredViewAsType(source, R.id.image_settings_icon, "field 'icons'", IconImageView.class), 
        Utils.findRequiredViewAsType(source, R.id.notification_settings_icon, "field 'icons'", IconImageView.class), 
        Utils.findRequiredViewAsType(source, R.id.other_settings_icon, "field 'icons'", IconImageView.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    MainSettingsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.container = null;
    target.userImageBottom = null;
    target.titleWelcome = null;
    target.text = null;
    target.icons = null;

    view7f0a0111.setOnClickListener(null);
    view7f0a0111 = null;
    view7f0a0093.setOnClickListener(null);
    view7f0a0093 = null;
    view7f0a01c3.setOnClickListener(null);
    view7f0a01c3 = null;
    view7f0a027d.setOnClickListener(null);
    view7f0a027d = null;
    view7f0a0133.setOnClickListener(null);
    view7f0a0133 = null;
    view7f0a01d2.setOnClickListener(null);
    view7f0a01d2 = null;
    view7f0a01be.setOnClickListener(null);
    view7f0a01be = null;
    view7f0a01ca.setOnClickListener(null);
    view7f0a01ca = null;
  }
}
