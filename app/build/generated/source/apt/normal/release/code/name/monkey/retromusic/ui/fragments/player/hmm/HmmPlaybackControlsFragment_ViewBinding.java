// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.hmm;

import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HmmPlaybackControlsFragment_ViewBinding implements Unbinder {
  private HmmPlaybackControlsFragment target;

  @UiThread
  public HmmPlaybackControlsFragment_ViewBinding(HmmPlaybackControlsFragment target, View source) {
    this.target = target;

    target.repeatButton = Utils.findRequiredViewAsType(source, R.id.player_repeat_button, "field 'repeatButton'", ImageButton.class);
    target.shuffleButton = Utils.findRequiredViewAsType(source, R.id.player_shuffle_button, "field 'shuffleButton'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HmmPlaybackControlsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.repeatButton = null;
    target.shuffleButton = null;
  }
}
