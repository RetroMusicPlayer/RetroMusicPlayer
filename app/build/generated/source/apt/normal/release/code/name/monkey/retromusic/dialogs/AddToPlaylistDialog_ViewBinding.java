// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.dialogs;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddToPlaylistDialog_ViewBinding implements Unbinder {
  private AddToPlaylistDialog target;

  private View view7f0a0008;

  @UiThread
  public AddToPlaylistDialog_ViewBinding(final AddToPlaylistDialog target, View source) {
    this.target = target;

    View view;
    target.playlist = Utils.findRequiredViewAsType(source, R.id.playlists, "field 'playlist'", RecyclerView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_add_playlist, "method 'newPlaylist'");
    view7f0a0008 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.newPlaylist();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddToPlaylistDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.playlist = null;
    target.title = null;

    view7f0a0008.setOnClickListener(null);
    view7f0a0008 = null;
  }
}
