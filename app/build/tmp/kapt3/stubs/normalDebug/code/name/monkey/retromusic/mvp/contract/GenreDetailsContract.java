package code.name.monkey.retromusic.mvp.contract;

import java.lang.System;

/**
 * * @author Hemanth S (h4h13).
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract;", "", "GenreDetailsView", "Presenter", "app_normalDebug"})
public abstract interface GenreDetailsContract {
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$GenreDetailsView;", "Lcode/name/monkey/retromusic/mvp/BaseView;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "app_normalDebug"})
    public static abstract interface GenreDetailsView extends code.name.monkey.retromusic.mvp.BaseView<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> {
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$Presenter;", "Lcode/name/monkey/retromusic/mvp/BasePresenter;", "Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$GenreDetailsView;", "loadGenre", "", "genreId", "", "app_normalDebug"})
    public static abstract interface Presenter extends code.name.monkey.retromusic.mvp.BasePresenter<code.name.monkey.retromusic.mvp.contract.GenreDetailsContract.GenreDetailsView> {
        
        public abstract void loadGenre(int genreId);
    }
}