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

public class AlbumDetailsActivity_ViewBinding extends AbsSlidingMusicPanelActivity_ViewBinding {
  private AlbumDetailsActivity target;

  private View view7f0a0058;

  private View view7f0a0090;

  @UiThread
  public AlbumDetailsActivity_ViewBinding(AlbumDetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AlbumDetailsActivity_ViewBinding(final AlbumDetailsActivity target, View source) {
    super(target, source);

    this.target = target;

    View view;
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.songTitle = Utils.findRequiredViewAsType(source, R.id.song_title, "field 'songTitle'", AppCompatTextView.class);
    view = Utils.findRequiredView(source, R.id.action_shuffle_all, "field 'shuffleButton' and method 'onViewClicked'");
    target.shuffleButton = Utils.castView(view, R.id.action_shuffle_all, "field 'shuffleButton'", CollapsingFAB.class);
    view7f0a0058 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.collapsingToolbarLayout = Utils.findOptionalViewAsType(source, R.id.collapsing_toolbar, "field 'collapsingToolbarLayout'", CollapsingToolbarLayout.class);
    target.appBarLayout = Utils.findOptionalViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.contentContainer = Utils.findRequiredViewAsType(source, R.id.content, "field 'contentContainer'", NestedScrollView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.moreRecyclerView = Utils.findRequiredViewAsType(source, R.id.more_recycler_view, "field 'moreRecyclerView'", RecyclerView.class);
    target.moreTitle = Utils.findRequiredViewAsType(source, R.id.more_title, "field 'moreTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.artist_image, "method 'onViewClicked'");
    target.artistImage = Utils.castView(view, R.id.artist_image, "field 'artistImage'", ImageView.class);
    view7f0a0090 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  public void unbind() {
    AlbumDetailsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.image = null;
    target.recyclerView = null;
    target.title = null;
    target.text = null;
    target.songTitle = null;
    target.shuffleButton = null;
    target.collapsingToolbarLayout = null;
    target.appBarLayout = null;
    target.contentContainer = null;
    target.toolbar = null;
    target.moreRecyclerView = null;
    target.moreTitle = null;
    target.artistImage = null;

    view7f0a0058.setOnClickListener(null);
    view7f0a0058 = null;
    view7f0a0090.setOnClickListener(null);
    view7f0a0090 = null;

    super.unbind();
  }
}
