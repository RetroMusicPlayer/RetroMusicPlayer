package code.name.monkey.retromusic.mvp.contract;

import java.lang.System;

/**
 * * Created by hemanths on 20/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/ArtistDetailContract;", "", "ArtistsDetailsView", "Presenter", "app_normalDebug"})
public abstract interface ArtistDetailContract {
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001\u00a8\u0006\u0003"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/ArtistDetailContract$ArtistsDetailsView;", "Lcode/name/monkey/retromusic/mvp/BaseView;", "Lcode/name/monkey/retromusic/model/Artist;", "app_normalDebug"})
    public static abstract interface ArtistsDetailsView extends code.name.monkey.retromusic.mvp.BaseView<code.name.monkey.retromusic.model.Artist> {
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/ArtistDetailContract$Presenter;", "Lcode/name/monkey/retromusic/mvp/BasePresenter;", "Lcode/name/monkey/retromusic/mvp/contract/ArtistDetailContract$ArtistsDetailsView;", "loadArtistById", "", "app_normalDebug"})
    public static abstract interface Presenter extends code.name.monkey.retromusic.mvp.BasePresenter<code.name.monkey.retromusic.mvp.contract.ArtistDetailContract.ArtistsDetailsView> {
        
        public abstract void loadArtistById();
    }
}