package code.name.monkey.retromusic.mvp.presenter;

import java.lang.System;

/**
 * * Created by hemanths on 20/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0012\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\u0016\u0010\n\u001a\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0002J\b\u0010\u000e\u001a\u00020\u0007H\u0016J\b\u0010\u000f\u001a\u00020\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcode/name/monkey/retromusic/mvp/presenter/SearchPresenter;", "Lcode/name/monkey/retromusic/mvp/Presenter;", "Lcode/name/monkey/retromusic/mvp/contract/SearchContract$SearchPresenter;", "view", "Lcode/name/monkey/retromusic/mvp/contract/SearchContract$SearchView;", "(Lcode/name/monkey/retromusic/mvp/contract/SearchContract$SearchView;)V", "search", "", "query", "", "showList", "albums", "Ljava/util/ArrayList;", "", "subscribe", "unsubscribe", "app_normalDebug"})
public final class SearchPresenter extends code.name.monkey.retromusic.mvp.Presenter implements code.name.monkey.retromusic.mvp.contract.SearchContract.SearchPresenter {
    private final code.name.monkey.retromusic.mvp.contract.SearchContract.SearchView view = null;
    
    @java.lang.Override()
    public void subscribe() {
    }
    
    @java.lang.Override()
    public void unsubscribe() {
    }
    
    private final void showList(java.util.ArrayList<java.lang.Object> albums) {
    }
    
    @java.lang.Override()
    public void search(@org.jetbrains.annotations.Nullable()
    java.lang.String query) {
    }
    
    public SearchPresenter(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.mvp.contract.SearchContract.SearchView view) {
        super();
    }
}