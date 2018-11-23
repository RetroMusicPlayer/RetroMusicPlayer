// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.UiThread;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder_ViewBinding;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CollageSongAdapter$CollageSongViewHolder_ViewBinding extends MediaEntryViewHolder_ViewBinding {
  private CollageSongAdapter.CollageSongViewHolder target;

  @UiThread
  public CollageSongAdapter$CollageSongViewHolder_ViewBinding(
      CollageSongAdapter.CollageSongViewHolder target, View source) {
    super(target, source);

    this.target = target;

    target.view = Utils.findRequiredViewAsType(source, R.id.image_1, "field 'view'", TextView.class);
    target.imageViews = Utils.listFilteringNull(
        Utils.findOptionalViewAsType(source, R.id.image_2, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_3, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_4, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_5, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_6, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_7, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_8, "field 'imageViews'", ImageView.class), 
        Utils.findOptionalViewAsType(source, R.id.image_9, "field 'imageViews'", ImageView.class));
  }

  @Override
  public void unbind() {
    CollageSongAdapter.CollageSongViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.view = null;
    target.imageViews = null;

    super.unbind();
  }
}
