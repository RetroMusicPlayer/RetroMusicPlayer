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

  private View view7f0a0110;

  private View view7f0a0093;

  private View view7f0a01c1;

  private View view7f0a027a;

  private View view7f0a0131;

  private View view7f0a01d0;

  private View view7f0a01bc;

  private View view7f0a01c8;

  @UiThread
  public MainSettingsFragment_ViewBinding(final MainSettingsFragment target, View source) {
    this.target = target;

    View view;
    target.container = Utils.findRequiredViewAsType(source, R.id.container, "field 'container'", ViewGroup.class);
    target.userImageBottom = Utils.findRequiredViewAsType(source, R.id.user_image_bottom, "field 'userImageBottom'", CircularImageView.class);
    target.titleWelcome = Utils.findRequiredViewAsType(source, R.id.title_welcome, "field 'titleWelcome'", AppCompatTextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", AppCompatTextView.class);
    view = Utils.findRequiredView(source, R.id.general_settings, "method 'onViewClicked'");
    view7f0a0110 = view;
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
    view7f0a01c1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.user_info_container, "method 'onViewClicked'");
    view7f0a027a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.image_settings, "method 'onViewClicked'");
    view7f0a0131 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.personalize_settings, "method 'onViewClicked'");
    view7f0a01d0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.notification_settings, "method 'onViewClicked'");
    view7f0a01bc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.other_settings, "method 'onViewClicked'");
    view7f0a01c8 = view;
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

    view7f0a0110.setOnClickListener(null);
    view7f0a0110 = null;
    view7f0a0093.setOnClickListener(null);
    view7f0a0093 = null;
    view7f0a01c1.setOnClickListener(null);
    view7f0a01c1 = null;
    view7f0a027a.setOnClickListener(null);
    view7f0a027a = null;
    view7f0a0131.setOnClickListener(null);
    view7f0a0131 = null;
    view7f0a01d0.setOnClickListener(null);
    view7f0a01d0 = null;
    view7f0a01bc.setOnClickListener(null);
    view7f0a01bc = null;
    view7f0a01c8.setOnClickListener(null);
    view7f0a01c8 = null;
  }
}
