package code.name.monkey.retromusic.mvp.presenter;

import java.lang.System;

/**
 * * Created by hemanths on 20/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0006H\u0016J\b\u0010\u000b\u001a\u00020\tH\u0016J\b\u0010\f\u001a\u00020\tH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcode/name/monkey/retromusic/mvp/presenter/PlaylistSongsPresenter;", "Lcode/name/monkey/retromusic/mvp/Presenter;", "Lcode/name/monkey/retromusic/mvp/contract/PlaylistSongsContract$Presenter;", "view", "Lcode/name/monkey/retromusic/mvp/contract/PlaylistSongsContract$PlaylistSongsView;", "mPlaylist", "Lcode/name/monkey/retromusic/model/Playlist;", "(Lcode/name/monkey/retromusic/mvp/contract/PlaylistSongsContract$PlaylistSongsView;Lcode/name/monkey/retromusic/model/Playlist;)V", "loadSongs", "", "playlist", "subscribe", "unsubscribe", "app_normalDebug"})
public final class PlaylistSongsPresenter extends code.name.monkey.retromusic.mvp.Presenter implements code.name.monkey.retromusic.mvp.contract.PlaylistSongsContract.Presenter {
    private final code.name.monkey.retromusic.mvp.contract.PlaylistSongsContract.PlaylistSongsView view = null;
    private final code.name.monkey.retromusic.model.Playlist mPlaylist = null;
    
    @java.lang.Override()
    public void subscribe() {
    }
    
    @java.lang.Override()
    public void unsubscribe() {
    }
    
    @java.lang.Override()
    public void loadSongs(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist playlist) {
    }
    
    public PlaylistSongsPresenter(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.mvp.contract.PlaylistSongsContract.PlaylistSongsView view, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist mPlaylist) {
        super();
    }
}