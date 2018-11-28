// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.color;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ColorPlaybackControlsFragment_ViewBinding implements Unbinder {
  private ColorPlaybackControlsFragment target;

  @UiThread
  public ColorPlaybackControlsFragment_ViewBinding(ColorPlaybackControlsFragment target,
      View source) {
    this.target = target;

    target.playPauseFab = Utils.findRequiredViewAsType(source, R.id.player_play_pause_button, "field 'playPauseFab'", ImageButton.class);
    target.prevButton = Utils.findRequiredViewAsType(source, R.id.player_prev_button, "field 'prevButton'", ImageButton.class);
    target.nextButton = Utils.findRequiredViewAsType(source, R.id.player_next_button, "field 'nextButton'", ImageButton.class);
    target.repeatButton = Utils.findRequiredViewAsType(source, R.id.player_repeat_button, "field 'repeatButton'", ImageButton.class);
    target.shuffleButton = Utils.findRequiredViewAsType(source, R.id.player_shuffle_button, "field 'shuffleButton'", ImageButton.class);
    target.progressSlider = Utils.findRequiredViewAsType(source, R.id.player_progress_slider, "field 'progressSlider'", AppCompatSeekBar.class);
    target.songTotalTime = Utils.findRequiredViewAsType(source, R.id.player_song_total_time, "field 'songTotalTime'", TextView.class);
    target.songCurrentProgress = Utils.findRequiredViewAsType(source, R.id.player_song_current_progress, "field 'songCurrentProgress'", TextView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", AppCompatTextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.volumeContainer = Utils.findRequiredView(source, R.id.volume_fragment_container, "field 'volumeContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ColorPlaybackControlsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.playPauseFab = null;
    target.prevButton = null;
    target.nextButton = null;
    target.repeatButton = null;
    target.shuffleButton = null;
    target.progressSlider = null;
    target.songTotalTime = null;
    target.songCurrentProgress = null;
    target.title = null;
    target.text = null;
    target.volumeContainer = null;
  }
}
