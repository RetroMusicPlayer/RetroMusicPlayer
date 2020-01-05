package me.everything.android.ui.overscroll;

/**
 * @author amit
 */
public interface IOverScrollState {

    /** No over-scroll is in-effect. */
    int STATE_IDLE = 0;

    /** User is actively touch-dragging, thus enabling over-scroll at the view's <i>start</i> side. */
    int STATE_DRAG_START_SIDE = 1;

    /** User is actively touch-dragging, thus enabling over-scroll at the view's <i>end</i> side. */
    int STATE_DRAG_END_SIDE = 2;

    /** User has released their touch, thus throwing the view back into place via bounce-back animation. */
    int STATE_BOUNCE_BACK = 3;
}
