// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.service.daydream;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RetroMusicAlbums_ViewBinding implements Unbinder {
  private RetroMusicAlbums target;

  @UiThread
  public RetroMusicAlbums_ViewBinding(RetroMusicAlbums target, View source) {
    this.target = target;

    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'mRecyclerView'", RecyclerView.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
    target.mText = Utils.findRequiredViewAsType(source, R.id.text, "field 'mText'", TextView.class);
    target.mViewGroup = Utils.findRequiredViewAsType(source, R.id.title_container, "field 'mViewGroup'", ViewGroup.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RetroMusicAlbums target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
    target.mTitle = null;
    target.mText = null;
    target.mViewGroup = null;
  }
}
