package code.name.monkey.retromusic.mvp.presenter;

import java.lang.System;

/**
 * * Created by hemanths on 20/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0018\u0010\n\u001a\u00020\t2\u000e\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fH\u0002J\b\u0010\u000e\u001a\u00020\tH\u0016J\b\u0010\u000f\u001a\u00020\tH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcode/name/monkey/retromusic/mvp/presenter/GenreDetailsPresenter;", "Lcode/name/monkey/retromusic/mvp/Presenter;", "Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$Presenter;", "view", "Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$GenreDetailsView;", "genreId", "", "(Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$GenreDetailsView;I)V", "loadGenre", "", "showGenre", "songs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "subscribe", "unsubscribe", "app_normalDebug"})
public final class GenreDetailsPresenter extends code.name.monkey.retromusic.mvp.Presenter implements code.name.monkey.retromusic.mvp.contract.GenreDetailsContract.Presenter {
    private final code.name.monkey.retromusic.mvp.contract.GenreDetailsContract.GenreDetailsView view = null;
    private final int genreId = 0;
    
    @java.lang.Override()
    public void subscribe() {
    }
    
    @java.lang.Override()
    public void unsubscribe() {
    }
    
    @java.lang.Override()
    public void loadGenre(int genreId) {
    }
    
    private final void showGenre(java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs) {
    }
    
    public GenreDetailsPresenter(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.mvp.contract.GenreDetailsContract.GenreDetailsView view, int genreId) {
        super();
    }
}