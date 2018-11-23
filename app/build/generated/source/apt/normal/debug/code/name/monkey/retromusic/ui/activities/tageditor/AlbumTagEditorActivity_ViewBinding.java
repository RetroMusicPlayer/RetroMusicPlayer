// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities.tageditor;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AlbumTagEditorActivity_ViewBinding extends AbsTagEditorActivity_ViewBinding {
  private AlbumTagEditorActivity target;

  @UiThread
  public AlbumTagEditorActivity_ViewBinding(AlbumTagEditorActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AlbumTagEditorActivity_ViewBinding(AlbumTagEditorActivity target, View source) {
    super(target, source);

    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.appBarLayout = Utils.findOptionalViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.albumTitle = Utils.findRequiredViewAsType(source, R.id.album_title, "field 'albumTitle'", TextInputEditText.class);
    target.albumArtist = Utils.findRequiredViewAsType(source, R.id.album_artist, "field 'albumArtist'", TextInputEditText.class);
    target.genre = Utils.findRequiredViewAsType(source, R.id.genre, "field 'genre'", TextInputEditText.class);
    target.year = Utils.findRequiredViewAsType(source, R.id.year, "field 'year'", TextInputEditText.class);
    target.background = Utils.findRequiredView(source, R.id.gradient_background, "field 'background'");
    target.content = Utils.findRequiredView(source, R.id.content, "field 'content'");
    target.textInputLayouts = Utils.listFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.album_title_container, "field 'textInputLayouts'", TextInputLayout.class), 
        Utils.findRequiredViewAsType(source, R.id.album_artist_container, "field 'textInputLayouts'", TextInputLayout.class), 
        Utils.findRequiredViewAsType(source, R.id.genre_container, "field 'textInputLayouts'", TextInputLayout.class), 
        Utils.findRequiredViewAsType(source, R.id.year_container, "field 'textInputLayouts'", TextInputLayout.class));
  }

  @Override
  public void unbind() {
    AlbumTagEditorActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.title = null;
    target.appBarLayout = null;
    target.albumTitle = null;
    target.albumArtist = null;
    target.genre = null;
    target.year = null;
    target.background = null;
    target.content = null;
    target.textInputLayouts = null;

    super.unbind();
  }
}
