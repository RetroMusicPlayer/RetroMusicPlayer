package code.name.monkey.retromusic.mvp.presenter;

import java.lang.System;

/**
 * * Created by hemanths on 19/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\b\u001a\u00020\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\b\u0010\f\u001a\u00020\u0007H\u0016J\b\u0010\r\u001a\u00020\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcode/name/monkey/retromusic/mvp/presenter/PlaylistPresenter;", "Lcode/name/monkey/retromusic/mvp/Presenter;", "Lcode/name/monkey/retromusic/mvp/contract/PlaylistContract$Presenter;", "view", "Lcode/name/monkey/retromusic/mvp/contract/PlaylistContract$PlaylistView;", "(Lcode/name/monkey/retromusic/mvp/contract/PlaylistContract$PlaylistView;)V", "loadPlaylists", "", "showList", "songs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Playlist;", "subscribe", "unsubscribe", "app_normalDebug"})
public final class PlaylistPresenter extends code.name.monkey.retromusic.mvp.Presenter implements code.name.monkey.retromusic.mvp.contract.PlaylistContract.Presenter {
    private final code.name.monkey.retromusic.mvp.contract.PlaylistContract.PlaylistView view = null;
    
    @java.lang.Override()
    public void subscribe() {
    }
    
    @java.lang.Override()
    public void unsubscribe() {
    }
    
    @java.lang.Override()
    public void loadPlaylists() {
    }
    
    private final void showList(java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> songs) {
    }
    
    public PlaylistPresenter(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.mvp.contract.PlaylistContract.PlaylistView view) {
        super();
    }
}