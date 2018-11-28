// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.card;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardPlaybackControlsFragment_ViewBinding implements Unbinder {
  private CardPlaybackControlsFragment target;

  private View view7f0a01e1;

  @UiThread
  public CardPlaybackControlsFragment_ViewBinding(final CardPlaybackControlsFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.player_play_pause_button, "field 'playPauseFab' and method 'showAnimation'");
    target.playPauseFab = Utils.castView(view, R.id.player_play_pause_button, "field 'playPauseFab'", ImageButton.class);
    view7f0a01e1 = view;
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
    target.progressSlider = Utils.findRequiredViewAsType(source, R.id.player_progress_slider, "field 'progressSlider'", AppCompatSeekBar.class);
    target.songTotalTime = Utils.findRequiredViewAsType(source, R.id.player_song_total_time, "field 'songTotalTime'", TextView.class);
    target.songCurrentProgress = Utils.findRequiredViewAsType(source, R.id.player_song_current_progress, "field 'songCurrentProgress'", TextView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", AppCompatTextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.volumeContainer = Utils.findRequiredView(source, R.id.volume_fragment_container, "field 'volumeContainer'");
    target.menuView = Utils.findRequiredView(source, R.id.menu, "field 'menuView'");
    target.colorContainer = Utils.findRequiredViewAsType(source, R.id.image_text_container, "field 'colorContainer'", CardView.class);
    target.playImageView = Utils.findRequiredViewAsType(source, R.id.image, "field 'playImageView'", ImageView.class);
    target.playbackControls = Utils.findRequiredView(source, R.id.playback_controls, "field 'playbackControls'");
  }

  @Override
  @CallSuper
  public void unbind() {
    CardPlaybackControlsFragment target = this.target;
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
    target.menuView = null;
    target.colorContainer = null;
    target.playImageView = null;
    target.playbackControls = null;

    view7f0a01e1.setOnClickListener(null);
    view7f0a01e1 = null;
  }
}
