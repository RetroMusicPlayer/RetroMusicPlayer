// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.color;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ColorFragment_ViewBinding implements Unbinder {
  private ColorFragment target;

  private View view7f0a0127;

  private View view7f0a0149;

  private View view7f0a00fd;

  @UiThread
  public ColorFragment_ViewBinding(final ColorFragment target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.colorBackground = Utils.findRequiredView(source, R.id.gradient_background, "field 'colorBackground'");
    view = Utils.findRequiredView(source, R.id.image, "field 'imageView' and method 'toggleLyrics'");
    target.imageView = Utils.castView(view, R.id.image, "field 'imageView'", ImageView.class);
    view7f0a0127 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toggleLyrics(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.lyrics, "field 'lyricsView' and method 'toggleLyrics'");
    target.lyricsView = Utils.castView(view, R.id.lyrics, "field 'lyricsView'", TextView.class);
    view7f0a0149 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toggleLyrics(p0);
      }
    });
    target.lyricsViewContainer = Utils.findRequiredView(source, R.id.lyrics_container, "field 'lyricsViewContainer'");
    target.imageViewContainer = Utils.findRequiredView(source, R.id.album_cover_container, "field 'imageViewContainer'");
    view = Utils.findRequiredView(source, R.id.expand, "method 'expand'");
    view7f0a00fd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.expand();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ColorFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.colorBackground = null;
    target.imageView = null;
    target.lyricsView = null;
    target.lyricsViewContainer = null;
    target.imageViewContainer = null;

    view7f0a0127.setOnClickListener(null);
    view7f0a0127 = null;
    view7f0a0149.setOnClickListener(null);
    view7f0a0149 = null;
    view7f0a00fd.setOnClickListener(null);
    view7f0a00fd = null;
  }
}
