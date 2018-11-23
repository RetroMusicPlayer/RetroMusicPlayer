// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.adapter.album;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AlbumCoverPagerAdapter$AlbumCoverFragment_ViewBinding implements Unbinder {
  private AlbumCoverPagerAdapter.AlbumCoverFragment target;

  private View view7f0a01dc;

  @UiThread
  public AlbumCoverPagerAdapter$AlbumCoverFragment_ViewBinding(
      final AlbumCoverPagerAdapter.AlbumCoverFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.player_image, "field 'albumCover' and method 'showLyrics'");
    target.albumCover = Utils.castView(view, R.id.player_image, "field 'albumCover'", ImageView.class);
    view7f0a01dc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showLyrics();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AlbumCoverPagerAdapter.AlbumCoverFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.albumCover = null;

    view7f0a01dc.setOnClickListener(null);
    view7f0a01dc = null;
  }
}
