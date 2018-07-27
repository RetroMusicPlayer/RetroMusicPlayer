package code.name.monkey.retromusic.mvp;

/**
 * Created by hemanths on 09/08/17.
 */

public interface BasePresenter<T> {

    void subscribe();

    void unsubscribe();
}
