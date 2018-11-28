// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.simple;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SimplePlaybackControlsFragment_ViewBinding implements Unbinder {
  private SimplePlaybackControlsFragment target;

  private View view7f0a01df;

  @UiThread
  public SimplePlaybackControlsFragment_ViewBinding(final SimplePlaybackControlsFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.player_play_pause_button, "field 'playPauseFab' and method 'showAnimation'");
    target.playPauseFab = Utils.castView(view, R.id.player_play_pause_button, "field 'playPauseFab'", ImageButton.class);
    view7f0a01df = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showAnimation();
      }
    });
    target.prevButton = Utils.findRequiredViewAsType(source, R.id.player_prev_button, "field 'prevButton'", ImageButton.class);
    target.nextButton = Utils.findRequiredViewAsType(source, R.id.player_next_button, "field 'nextButton'", ImageButton.class);
    target.repeatButton = Utils.findRequiredViewAsType(source, R.id.player_repeat_button, "field 'repeatButton'", ImageButton.class);
    target.shuffleButton = Utils.findRequiredViewAsType(source, R.id.player_shuffle_button, "field 'shuffleButton'", ImageButton.class);
    target.songCurrentProgress = Utils.findRequiredViewAsType(source, R.id.player_song_current_progress, "field 'songCurrentProgress'", TextView.class);
    target.volumeContainer = Utils.findRequiredView(source, R.id.volume_fragment_container, "field 'volumeContainer'");
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SimplePlaybackControlsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.playPauseFab = null;
    target.prevButton = null;
    target.nextButton = null;
    target.repeatButton = null;
    target.shuffleButton = null;
    target.songCurrentProgress = null;
    target.volumeContainer = null;
    target.title = null;
    target.text = null;

    view7f0a01df.setOnClickListener(null);
    view7f0a01df = null;
  }
}
