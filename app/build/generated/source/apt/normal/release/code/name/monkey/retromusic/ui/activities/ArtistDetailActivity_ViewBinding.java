// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity_ViewBinding;
import code.name.monkey.retromusic.views.CollapsingFAB;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ArtistDetailActivity_ViewBinding extends AbsSlidingMusicPanelActivity_ViewBinding {
  private ArtistDetailActivity target;

  private View view7f0a00a0;

  private View view7f0a0058;

  @UiThread
  public ArtistDetailActivity_ViewBinding(ArtistDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ArtistDetailActivity_ViewBinding(final ArtistDetailActivity target, View source) {
    super(target, source);

    this.target = target;

    View view;
    target.appBarLayout = Utils.findOptionalViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.collapsingToolbarLayout = Utils.findOptionalViewAsType(source, R.id.collapsing_toolbar, "field 'collapsingToolbarLayout'", CollapsingToolbarLayout.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.biography, "field 'biographyTextView' and method 'toggleArtistBiography'");
    target.biographyTextView = Utils.castView(view, R.id.biography, "field 'biographyTextView'", TextView.class);
    view7f0a00a0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toggleArtistBiography();
      }
    });
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.albumRecyclerView = Utils.findRequiredViewAsType(source, R.id.album_recycler_view, "field 'albumRecyclerView'", RecyclerView.class);
    target.albumTitle = Utils.findRequiredViewAsType(source, R.id.album_title, "field 'albumTitle'", AppCompatTextView.class);
    target.songTitle = Utils.findRequiredViewAsType(source, R.id.song_title, "field 'songTitle'", AppCompatTextView.class);
    target.biographyTitle = Utils.findRequiredViewAsType(source, R.id.biography_title, "field 'biographyTitle'", AppCompatTextView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_shuffle_all, "field 'shuffleButton' and method 'onViewClicked'");
    target.shuffleButton = Utils.castView(view, R.id.action_shuffle_all, "field 'shuffleButton'", CollapsingFAB.class);
    view7f0a0058 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.background = source.findViewById(R.id.gradient_background);
    target.imageContainer = source.findViewById(R.id.image_container);
    target.contentContainer = Utils.findRequiredViewAsType(source, R.id.content, "field 'contentContainer'", NestedScrollView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  public void unbind() {
    ArtistDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.appBarLayout = null;
    target.collapsingToolbarLayout = null;
    target.image = null;
    target.biographyTextView = null;
    target.recyclerView = null;
    target.albumRecyclerView = null;
    target.albumTitle = null;
    target.songTitle = null;
    target.biographyTitle = null;
    target.title = null;
    target.text = null;
    target.shuffleButton = null;
    target.background = null;
    target.imageContainer = null;
    target.contentContainer = null;
    target.toolbar = null;

    view7f0a00a0.setOnClickListener(null);
    view7f0a00a0 = null;
    view7f0a0058.setOnClickListener(null);
    view7f0a0058 = null;

    super.unbind();
  }
}
