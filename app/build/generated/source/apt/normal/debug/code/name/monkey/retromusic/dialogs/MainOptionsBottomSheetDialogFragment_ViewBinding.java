// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.dialogs;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.CircularImageView;
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainOptionsBottomSheetDialogFragment_ViewBinding implements Unbinder {
  private MainOptionsBottomSheetDialogFragment target;

  private View view7f0a0027;

  private View view7f0a027d;

  private View view7f0a0054;

  private View view7f0a005a;

  private View view7f0a0046;

  private View view7f0a001a;

  private View view7f0a0007;

  @UiThread
  public MainOptionsBottomSheetDialogFragment_ViewBinding(
      final MainOptionsBottomSheetDialogFragment target, View source) {
    this.target = target;

    View view;
    target.userImageBottom = Utils.findRequiredViewAsType(source, R.id.user_image_bottom, "field 'userImageBottom'", CircularImageView.class);
    target.titleWelcome = Utils.findRequiredViewAsType(source, R.id.title_welcome, "field 'titleWelcome'", AppCompatTextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", AppCompatTextView.class);
    view = Utils.findRequiredView(source, R.id.action_folders, "method 'onClick'");
    view7f0a0027 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.user_info_container, "method 'onClick'");
    view7f0a027d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_settings, "method 'onClick'");
    view7f0a0054 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_sleep_timer, "method 'onClick'");
    view7f0a005a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_rate, "method 'onClick'");
    view7f0a0046 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_buy_pro, "method 'onClick'");
    view7f0a001a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_about, "method 'onClick'");
    view7f0a0007 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.materialButtons = Utils.listFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.action_folders, "field 'materialButtons'", MaterialButton.class), 
        Utils.findRequiredViewAsType(source, R.id.action_about, "field 'materialButtons'", MaterialButton.class), 
        Utils.findRequiredViewAsType(source, R.id.action_buy_pro, "field 'materialButtons'", MaterialButton.class), 
        Utils.findRequiredViewAsType(source, R.id.action_rate, "field 'materialButtons'", MaterialButton.class), 
        Utils.findRequiredViewAsType(source, R.id.action_sleep_timer, "field 'materialButtons'", MaterialButton.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    MainOptionsBottomSheetDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.userImageBottom = null;
    target.titleWelcome = null;
    target.text = null;
    target.materialButtons = null;

    view7f0a0027.setOnClickListener(null);
    view7f0a0027 = null;
    view7f0a027d.setOnClickListener(null);
    view7f0a027d = null;
    view7f0a0054.setOnClickListener(null);
    view7f0a0054 = null;
    view7f0a005a.setOnClickListener(null);
    view7f0a005a = null;
    view7f0a0046.setOnClickListener(null);
    view7f0a0046 = null;
    view7f0a001a.setOnClickListener(null);
    view7f0a001a = null;
    view7f0a0007.setOnClickListener(null);
    view7f0a0007 = null;
  }
}
