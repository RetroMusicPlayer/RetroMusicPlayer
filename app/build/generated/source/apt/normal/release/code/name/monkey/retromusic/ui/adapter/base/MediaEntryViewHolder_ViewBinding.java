// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MediaEntryViewHolder_ViewBinding implements Unbinder {
  private MediaEntryViewHolder target;

  @UiThread
  public MediaEntryViewHolder_ViewBinding(MediaEntryViewHolder target, View source) {
    this.target = target;

    target.image = Utils.findOptionalViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.imageText = Utils.findOptionalViewAsType(source, R.id.image_text, "field 'imageText'", TextView.class);
    target.title = Utils.findOptionalViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findOptionalViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.imageContainer = Utils.findOptionalViewAsType(source, R.id.image_container, "field 'imageContainer'", ViewGroup.class);
    target.imageContainerCard = Utils.findOptionalViewAsType(source, R.id.image_container_card, "field 'imageContainerCard'", CardView.class);
    target.menu = source.findViewById(R.id.menu);
    target.separator = source.findViewById(R.id.separator);
    target.shortSeparator = source.findViewById(R.id.short_separator);
    target.dragView = source.findViewById(R.id.drag_view);
    target.paletteColorContainer = source.findViewById(R.id.palette_color_container);
    target.time = Utils.findOptionalViewAsType(source, R.id.time, "field 'time'", TextView.class);
    target.recyclerView = Utils.findOptionalViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.playSongs = Utils.findOptionalViewAsType(source, R.id.play_songs, "field 'playSongs'", ImageButton.class);
    target.mask = source.findViewById(R.id.mask);
    target.imageTextContainer = Utils.findOptionalViewAsType(source, R.id.image_text_container, "field 'imageTextContainer'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MediaEntryViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.image = null;
    target.imageText = null;
    target.title = null;
    target.text = null;
    target.imageContainer = null;
    target.imageContainerCard = null;
    target.menu = null;
    target.separator = null;
    target.shortSeparator = null;
    target.dragView = null;
    target.paletteColorContainer = null;
    target.time = null;
    target.recyclerView = null;
    target.playSongs = null;
    target.mask = null;
    target.imageTextContainer = null;
  }
}
