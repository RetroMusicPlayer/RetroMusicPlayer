// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.mainactivity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.CircularImageView;
import code.name.monkey.retromusic.views.MetalRecyclerViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BannerHomeFragment_ViewBinding implements Unbinder {
  private BannerHomeFragment target;

  private View view7f0a027b;

  private View view7f0a020c;

  private View view7f0a013e;

  private View view7f0a026e;

  private View view7f0a0057;

  private View view7f0a011c;

  @UiThread
  public BannerHomeFragment_ViewBinding(final BannerHomeFragment target, View source) {
    this.target = target;

    View view;
    target.imageView = Utils.findOptionalViewAsType(source, R.id.image, "field 'imageView'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.user_image, "field 'userImage' and method 'startUserInfo'");
    target.userImage = Utils.castView(view, R.id.user_image, "field 'userImage'", CircularImageView.class);
    view7f0a027b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startUserInfo(p0);
      }
    });
    target.recentArtistRV = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recentArtistRV'", RecyclerView.class);
    target.recentAlbumRV = Utils.findRequiredViewAsType(source, R.id.recent_album, "field 'recentAlbumRV'", RecyclerView.class);
    target.topArtistRV = Utils.findRequiredViewAsType(source, R.id.top_artist, "field 'topArtistRV'", RecyclerView.class);
    target.topAlbumRV = Utils.findRequiredViewAsType(source, R.id.top_album, "field 'topAlbumRV'", MetalRecyclerViewPager.class);
    target.recentArtistContainer = Utils.findRequiredView(source, R.id.recent_artist_container, "field 'recentArtistContainer'");
    target.recentAlbumsContainer = Utils.findRequiredView(source, R.id.recent_albums_container, "field 'recentAlbumsContainer'");
    target.topArtistContainer = Utils.findRequiredView(source, R.id.top_artist_container, "field 'topArtistContainer'");
    target.topAlbumContainer = Utils.findRequiredView(source, R.id.top_albums_container, "field 'topAlbumContainer'");
    target.genresRecyclerView = Utils.findRequiredViewAsType(source, R.id.genres, "field 'genresRecyclerView'", RecyclerView.class);
    target.genreContainer = Utils.findRequiredViewAsType(source, R.id.genre_container, "field 'genreContainer'", LinearLayout.class);
    target.container = Utils.findRequiredView(source, R.id.container, "field 'container'");
    target.contentContainer = Utils.findRequiredView(source, R.id.content_container, "field 'contentContainer'");
    target.suggestionsSongs = Utils.findRequiredViewAsType(source, R.id.suggestion_songs, "field 'suggestionsSongs'", RecyclerView.class);
    target.suggestionsContainer = Utils.findRequiredViewAsType(source, R.id.suggestion_container, "field 'suggestionsContainer'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.searchIcon, "method 'search'");
    view7f0a020c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.search();
      }
    });
    view = Utils.findRequiredView(source, R.id.last_added, "method 'startUserInfo'");
    view7f0a013e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startUserInfo(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.top_played, "method 'startUserInfo'");
    view7f0a026e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startUserInfo(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_shuffle, "method 'startUserInfo'");
    view7f0a0057 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startUserInfo(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.history, "method 'startUserInfo'");
    view7f0a011c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startUserInfo(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    BannerHomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.userImage = null;
    target.recentArtistRV = null;
    target.recentAlbumRV = null;
    target.topArtistRV = null;
    target.topAlbumRV = null;
    target.recentArtistContainer = null;
    target.recentAlbumsContainer = null;
    target.topArtistContainer = null;
    target.topAlbumContainer = null;
    target.genresRecyclerView = null;
    target.genreContainer = null;
    target.container = null;
    target.contentContainer = null;
    target.suggestionsSongs = null;
    target.suggestionsContainer = null;

    view7f0a027b.setOnClickListener(null);
    view7f0a027b = null;
    view7f0a020c.setOnClickListener(null);
    view7f0a020c = null;
    view7f0a013e.setOnClickListener(null);
    view7f0a013e = null;
    view7f0a026e.setOnClickListener(null);
    view7f0a026e = null;
    view7f0a0057.setOnClickListener(null);
    view7f0a0057 = null;
    view7f0a011c.setOnClickListener(null);
    view7f0a011c = null;
  }
}
