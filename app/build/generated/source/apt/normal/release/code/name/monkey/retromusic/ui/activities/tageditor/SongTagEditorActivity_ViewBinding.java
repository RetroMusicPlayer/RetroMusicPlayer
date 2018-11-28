// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities.tageditor;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SongTagEditorActivity_ViewBinding extends AbsTagEditorActivity_ViewBinding {
  private SongTagEditorActivity target;

  @UiThread
  public SongTagEditorActivity_ViewBinding(SongTagEditorActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SongTagEditorActivity_ViewBinding(SongTagEditorActivity target, View source) {
    super(target, source);

    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.songTitle = Utils.findRequiredViewAsType(source, R.id.title1, "field 'songTitle'", EditText.class);
    target.albumTitle = Utils.findRequiredViewAsType(source, R.id.title2, "field 'albumTitle'", EditText.class);
    target.artist = Utils.findRequiredViewAsType(source, R.id.artist, "field 'artist'", EditText.class);
    target.genre = Utils.findRequiredViewAsType(source, R.id.genre, "field 'genre'", EditText.class);
    target.year = Utils.findRequiredViewAsType(source, R.id.year, "field 'year'", EditText.class);
    target.trackNumber = Utils.findRequiredViewAsType(source, R.id.image_text, "field 'trackNumber'", EditText.class);
    target.lyrics = Utils.findRequiredViewAsType(source, R.id.lyrics, "field 'lyrics'", EditText.class);
    target.albumArtist = Utils.findRequiredViewAsType(source, R.id.album_artist, "field 'albumArtist'", EditText.class);
  }

  @Override
  public void unbind() {
    SongTagEditorActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.appBarLayout = null;
    target.title = null;
    target.songTitle = null;
    target.albumTitle = null;
    target.artist = null;
    target.genre = null;
    target.year = null;
    target.trackNumber = null;
    target.lyrics = null;
    target.albumArtist = null;

    super.unbind();
  }
}
