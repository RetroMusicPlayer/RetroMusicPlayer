package code.name.monkey.retromusic.mvp.contract;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract;", "", "AlbumDetailsView", "Presenter", "app_normalDebug"})
public abstract interface AlbumDetailsContract {
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001\u00a8\u0006\u0003"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$AlbumDetailsView;", "Lcode/name/monkey/retromusic/mvp/BaseView;", "Lcode/name/monkey/retromusic/model/Album;", "app_normalDebug"})
    public static abstract interface AlbumDetailsView extends code.name.monkey.retromusic.mvp.BaseView<code.name.monkey.retromusic.model.Album> {
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$Presenter;", "Lcode/name/monkey/retromusic/mvp/BasePresenter;", "Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$AlbumDetailsView;", "loadAlbumSongs", "", "albumId", "", "app_normalDebug"})
    public static abstract interface Presenter extends code.name.monkey.retromusic.mvp.BasePresenter<code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract.AlbumDetailsView> {
        
        public abstract void loadAlbumSongs(int albumId);
    }
}