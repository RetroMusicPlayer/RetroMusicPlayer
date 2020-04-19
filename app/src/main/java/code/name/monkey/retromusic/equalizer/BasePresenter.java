package code.name.monkey.retromusic.equalizer;

import code.name.monkey.retromusic.mvp.BaseView;

public class BasePresenter<T extends BaseView> {
	protected T mView;

	final public void attachView(T view){
		mView = view;
	}

	final public void detachView(){
		mView = null;
	}
}