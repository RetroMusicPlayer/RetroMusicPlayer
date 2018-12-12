package code.name.monkey.retromusic.mvp.presenter;

import java.lang.System;

/**
 * * Created by hemanths on 20/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0012\u0010\n\u001a\u00020\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0002J\b\u0010\r\u001a\u00020\tH\u0016J\b\u0010\u000e\u001a\u00020\tH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcode/name/monkey/retromusic/mvp/presenter/AlbumDetailsPresenter;", "Lcode/name/monkey/retromusic/mvp/Presenter;", "Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$Presenter;", "view", "Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$AlbumDetailsView;", "albumId", "", "(Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$AlbumDetailsView;I)V", "loadAlbumSongs", "", "showAlbum", "album", "Lcode/name/monkey/retromusic/model/Album;", "subscribe", "unsubscribe", "app_normalDebug"})
public final class AlbumDetailsPresenter extends code.name.monkey.retromusic.mvp.Presenter implements code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract.Presenter {
    private final code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract.AlbumDetailsView view = null;
    private final int albumId = 0;
    
    @java.lang.Override()
    public void subscribe() {
    }
    
    @java.lang.Override()
    public void unsubscribe() {
    }
    
    @java.lang.Override()
    public void loadAlbumSongs(int albumId) {
    }
    
    private final void showAlbum(code.name.monkey.retromusic.model.Album album) {
    }
    
    public AlbumDetailsPresenter(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract.AlbumDetailsView view, int albumId) {
        super();
    }
}