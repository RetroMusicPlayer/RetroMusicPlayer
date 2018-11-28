// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.full;

import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FullPlaybackControlsFragment_ViewBinding implements Unbinder {
  private FullPlaybackControlsFragment target;

  @UiThread
  public FullPlaybackControlsFragment_ViewBinding(FullPlaybackControlsFragment target,
      View source) {
    this.target = target;

    target.mPlayerSongCurrentProgress = Utils.findRequiredViewAsType(source, R.id.player_song_current_progress, "field 'mPlayerSongCurrentProgress'", TextView.class);
    target.songTotalTime = Utils.findRequiredViewAsType(source, R.id.player_song_total_time, "field 'songTotalTime'", TextView.class);
    target.progressSlider = Utils.findRequiredViewAsType(source, R.id.player_progress_slider, "field 'progressSlider'", SeekBar.class);
    target.playerPrevButton = Utils.findRequiredViewAsType(source, R.id.player_prev_button, "field 'playerPrevButton'", ImageButton.class);
    target.playerNextButton = Utils.findRequiredViewAsType(source, R.id.player_next_button, "field 'playerNextButton'", ImageButton.class);
    target.playerRepeatButton = Utils.findRequiredViewAsType(source, R.id.player_repeat_button, "field 'playerRepeatButton'", ImageButton.class);
    target.playerShuffleButton = Utils.findRequiredViewAsType(source, R.id.player_shuffle_button, "field 'playerShuffleButton'", ImageButton.class);
    target.playerPlayPauseFab = Utils.findRequiredViewAsType(source, R.id.player_play_pause_button, "field 'playerPlayPauseFab'", ImageButton.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
    target.mText = Utils.findRequiredViewAsType(source, R.id.text, "field 'mText'", TextView.class);
    target.mVolumeContainer = Utils.findRequiredView(source, R.id.volume_fragment_container, "field 'mVolumeContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    FullPlaybackControlsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPlayerSongCurrentProgress = null;
    target.songTotalTime = null;
    target.progressSlider = null;
    target.playerPrevButton = null;
    target.playerNextButton = null;
    target.playerRepeatButton = null;
    target.playerShuffleButton = null;
    target.playerPlayPauseFab = null;
    target.mTitle = null;
    target.mText = null;
    target.mVolumeContainer = null;
  }
}
