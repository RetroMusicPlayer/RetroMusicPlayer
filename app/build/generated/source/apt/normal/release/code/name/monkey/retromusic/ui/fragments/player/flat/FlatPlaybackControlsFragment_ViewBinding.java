// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.flat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FlatPlaybackControlsFragment_ViewBinding implements Unbinder {
  private FlatPlaybackControlsFragment target;

  @UiThread
  public FlatPlaybackControlsFragment_ViewBinding(FlatPlaybackControlsFragment target,
      View source) {
    this.target = target;

    target.mText = Utils.findRequiredViewAsType(source, R.id.text, "field 'mText'", TextView.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
    target.viewGroup = Utils.findRequiredViewAsType(source, R.id.playback_controls, "field 'viewGroup'", ViewGroup.class);
    target.mSongTotalTime = Utils.findRequiredViewAsType(source, R.id.player_song_total_time, "field 'mSongTotalTime'", TextView.class);
    target.mPlayerSongCurrentProgress = Utils.findRequiredViewAsType(source, R.id.player_song_current_progress, "field 'mPlayerSongCurrentProgress'", TextView.class);
    target.mPlayerRepeatButton = Utils.findRequiredViewAsType(source, R.id.player_repeat_button, "field 'mPlayerRepeatButton'", ImageButton.class);
    target.mPlayerShuffleButton = Utils.findRequiredViewAsType(source, R.id.player_shuffle_button, "field 'mPlayerShuffleButton'", ImageButton.class);
    target.playPauseFab = Utils.findRequiredViewAsType(source, R.id.player_play_pause_button, "field 'playPauseFab'", ImageView.class);
    target.progressSlider = Utils.findRequiredViewAsType(source, R.id.player_progress_slider, "field 'progressSlider'", SeekBar.class);
    target.mVolumeContainer = Utils.findRequiredView(source, R.id.volume_fragment_container, "field 'mVolumeContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    FlatPlaybackControlsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mText = null;
    target.mTitle = null;
    target.viewGroup = null;
    target.mSongTotalTime = null;
    target.mPlayerSongCurrentProgress = null;
    target.mPlayerRepeatButton = null;
    target.mPlayerShuffleButton = null;
    target.playPauseFab = null;
    target.progressSlider = null;
    target.mVolumeContainer = null;
  }
}
