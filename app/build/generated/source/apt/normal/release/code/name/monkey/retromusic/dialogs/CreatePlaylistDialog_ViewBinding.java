// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.dialogs;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CreatePlaylistDialog_ViewBinding implements Unbinder {
  private CreatePlaylistDialog target;

  private View view7f0a001b;

  private View view7f0a0020;

  @UiThread
  public CreatePlaylistDialog_ViewBinding(final CreatePlaylistDialog target, View source) {
    this.target = target;

    View view;
    target.playlistName = Utils.findRequiredViewAsType(source, R.id.option_1, "field 'playlistName'", TextInputEditText.class);
    target.textInputLayout = Utils.findRequiredViewAsType(source, R.id.action_new_playlist, "field 'textInputLayout'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.action_cancel, "field 'actionCancel' and method 'actions'");
    target.actionCancel = Utils.castView(view, R.id.action_cancel, "field 'actionCancel'", MaterialButton.class);
    view7f0a001b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.actions(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_create, "field 'actionCreate' and method 'actions'");
    target.actionCreate = Utils.castView(view, R.id.action_create, "field 'actionCreate'", MaterialButton.class);
    view7f0a0020 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.actions(p0);
      }
    });
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreatePlaylistDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.playlistName = null;
    target.textInputLayout = null;
    target.actionCancel = null;
    target.actionCreate = null;
    target.title = null;

    view7f0a001b.setOnClickListener(null);
    view7f0a001b = null;
    view7f0a0020.setOnClickListener(null);
    view7f0a0020 = null;
  }
}
