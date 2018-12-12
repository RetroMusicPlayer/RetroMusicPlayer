package code.name.monkey.retromusic.mvp.contract;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/HomeContract;", "", "HomePresenter", "HomeView", "app_normalDebug"})
public abstract interface HomeContract {
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001J\u0016\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0002H&J\u0016\u0010\b\u001a\u00020\u00052\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0002H&J\u0016\u0010\n\u001a\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0002H&J\u0016\u0010\r\u001a\u00020\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0002H&J\u0016\u0010\u0010\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00110\u0002H&J\u0016\u0010\u0012\u001a\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0002H&J\u0016\u0010\u0013\u001a\u00020\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0002H&\u00a8\u0006\u0014"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/HomeContract$HomeView;", "Lcode/name/monkey/retromusic/mvp/BaseView;", "Ljava/util/ArrayList;", "", "geners", "", "songs", "Lcode/name/monkey/retromusic/model/Genre;", "playlists", "Lcode/name/monkey/retromusic/model/Playlist;", "recentAlbum", "albums", "Lcode/name/monkey/retromusic/model/Album;", "recentArtist", "artists", "Lcode/name/monkey/retromusic/model/Artist;", "suggestions", "Lcode/name/monkey/retromusic/model/Song;", "topAlbums", "topArtists", "app_normalDebug"})
    public static abstract interface HomeView extends code.name.monkey.retromusic.mvp.BaseView<java.util.ArrayList<java.lang.Object>> {
        
        public abstract void recentArtist(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Artist> artists);
        
        public abstract void recentAlbum(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Album> albums);
        
        public abstract void topArtists(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Artist> artists);
        
        public abstract void topAlbums(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Album> albums);
        
        public abstract void suggestions(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs);
        
        public abstract void playlists(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> playlists);
        
        public abstract void geners(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<code.name.monkey.retromusic.model.Genre> songs);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0004H&J\b\u0010\u0007\u001a\u00020\u0004H&J\b\u0010\b\u001a\u00020\u0004H&J\b\u0010\t\u001a\u00020\u0004H&\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/HomeContract$HomePresenter;", "Lcode/name/monkey/retromusic/mvp/BasePresenter;", "Lcode/name/monkey/retromusic/mvp/contract/HomeContract$HomeView;", "loadGenres", "", "loadRecentAlbums", "loadRecentArtists", "loadSuggestions", "loadTopAlbums", "loadTopArtists", "app_normalDebug"})
    public static abstract interface HomePresenter extends code.name.monkey.retromusic.mvp.BasePresenter<code.name.monkey.retromusic.mvp.contract.HomeContract.HomeView> {
        
        public abstract void loadRecentAlbums();
        
        public abstract void loadTopAlbums();
        
        public abstract void loadRecentArtists();
        
        public abstract void loadTopArtists();
        
        public abstract void loadSuggestions();
        
        public abstract void loadGenres();
    }
}