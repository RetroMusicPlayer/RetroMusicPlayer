package me.everything.android.ui.overscroll;

/**
 * A callback-listener enabling over-scroll effect clients to be notified of effect state transitions.
 * <br/>Invoked whenever state is transitioned onto one of {@link IOverScrollState#STATE_IDLE},
 * {@link IOverScrollState#STATE_DRAG_START_SIDE}, {@link IOverScrollState#STATE_DRAG_END_SIDE}
 * or {@link IOverScrollState#STATE_BOUNCE_BACK}.
 *
 * @author amit
 *
 * @see IOverScrollUpdateListener
 */
public interface IOverScrollStateListener {

    /**
     * The invoked callback.
     *
     * @param decor The associated over-scroll 'decorator'.
     * @param oldState The old over-scroll state; ID's specified by {@link IOverScrollState}, e.g.
     *                 {@link IOverScrollState#STATE_IDLE}.
     * @param newState The <b>new</b> over-scroll state; ID's specified by {@link IOverScrollState},
     *                 e.g. {@link IOverScrollState#STATE_IDLE}.
     */
    void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState);
}
