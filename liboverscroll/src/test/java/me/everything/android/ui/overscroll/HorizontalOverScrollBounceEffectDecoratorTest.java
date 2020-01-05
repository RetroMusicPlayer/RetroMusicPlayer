package me.everything.android.ui.overscroll;

import android.view.MotionEvent;
import android.view.View;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter;

import static me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator.DEFAULT_DECELERATE_FACTOR;
import static me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator.DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
import static me.everything.android.ui.overscroll.IOverScrollState.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author amitd
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HorizontalOverScrollBounceEffectDecoratorTest {

    View mView;
    IOverScrollDecoratorAdapter mViewAdapter;
    IOverScrollStateListener mStateListener;
    IOverScrollUpdateListener mUpdateListener;

    @Before
    public void setUp() throws Exception {
        mView = mock(View.class);
        mViewAdapter = mock(IOverScrollDecoratorAdapter.class);
        when(mViewAdapter.getView()).thenReturn(mView);

        mStateListener = mock(IOverScrollStateListener.class);
        mUpdateListener = mock(IOverScrollUpdateListener.class);
    }

    @Test
    public void detach_decoratorIsAttached_detachFromView() throws Exception {

        // Arrange

        HorizontalOverScrollBounceEffectDecorator uut = new HorizontalOverScrollBounceEffectDecorator(mViewAdapter);

        // Act

        uut.detach();

        // Assert

        verify(mView).setOnTouchListener(eq((View.OnTouchListener) null));
        verify(mView).setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    @Test
    public void detach_overScrollInEffect_detachFromView() throws Exception {

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, createShortRightMoveEvent());

        // Act

        uut.detach();

        // Assert

        verify(mView).setOnTouchListener(eq((View.OnTouchListener) null));
        verify(mView).setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    /*
     * Move-action event
     */

    @Test
    public void onTouchMoveAction_notInViewEnds_ignoreTouchEvent() throws Exception {

        // Arrange

        MotionEvent event = createShortRightMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        boolean ret = uut.onTouch(mView, event);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView, never()).setTranslationY(anyFloat());
        assertFalse(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        verify(mStateListener, never()).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        verify(mUpdateListener, never()).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_dragRightInLeftEnd_overscrollRight() throws Exception {

        // Arrange

        MotionEvent event = createShortRightMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        final boolean ret = uut.onTouch(mView, event);

        // Assert

        final float expectedTransX = (event.getX() - event.getHistoricalX(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationX(expectedTransX);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_START_SIDE, uut.getCurrentState());

        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransX));
    }

    @Test
    public void onTouchMoveAction_dragLeftInRightEnd_overscrollLeft() throws Exception {

        // Arrange

        MotionEvent event = createShortLeftMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        final boolean ret = uut.onTouch(mView, event);

        // Assert

        final float expectedTransX = (event.getX() - event.getHistoricalX(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationX(expectedTransX);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_END_SIDE, uut.getCurrentState());

        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransX));
    }

    @Test
    public void onTouchMoveAction_dragLeftInLeftEnd_ignoreTouchEvent() throws Exception {

        // Arrange

        MotionEvent event = createShortLeftMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        final boolean ret = uut.onTouch(mView, event);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView, never()).setTranslationY(anyFloat());
        assertFalse(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        verify(mStateListener, never()).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        verify(mUpdateListener, never()).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_dragRightInRightEnd_ignoreTouchEvent() throws Exception {

        // Arrange

        MotionEvent event = createShortRightMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        boolean ret = uut.onTouch(mView, event);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView, never()).setTranslationY(anyFloat());
        assertFalse(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        verify(mStateListener, never()).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        verify(mUpdateListener, never()).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_2ndRightDragInLeftEnd_overscrollRightFurther() throws Exception {

        // Arrange

        // Bring UUT to a right-overscroll state
        MotionEvent event1 = createShortRightMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, event1);
        reset(mView);

        // Create 2nd right-drag event
        MotionEvent event2 = createLongRightMoveEvent();

        // Act

        final boolean ret = uut.onTouch(mView, event2);

        // Assert

        final float expectedTransX1 = (event1.getX() - event1.getHistoricalX(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        final float expectedTransX2 = (event2.getX() - event2.getHistoricalX(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationX(expectedTransX2);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_START_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransX1));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransX2));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_2ndLeftDragInRightEnd_overscrollLeftFurther() throws Exception {

        // Arrange

        // Bring UUT to a left-overscroll state
        MotionEvent event1 = createShortLeftMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, event1);
        reset(mView);

        // Create 2nd left-drag event
        MotionEvent event2 = createLongLeftMoveEvent();

        // Act

        final boolean ret = uut.onTouch(mView, event2);

        // Assert

        final float expectedTransX1 = (event1.getX() - event1.getHistoricalX(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        final float expectedTransX2 = (event2.getX() - event2.getHistoricalX(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationX(expectedTransX2);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_END_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransX1));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransX2));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    /**
     * When over-scroll has already started (to the right in this case) and suddenly the user changes
     * their mind and scrolls a bit in the other direction:
     * <br/>We expect the <b>touch to still be intercepted</b> in that case, and the <b>overscroll to
     * remain in effect</b>.
     */
    @Test
    public void onTouchMoveAction_dragLeftWhenRightOverscolled_continueOverscrollingLeft() throws Exception {

        // Arrange

        // In left & right tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a right-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveRight = createLongRightMoveEvent();
        uut.onTouch(mView, eventMoveRight);
        reset(mView);
        float startTransX = (eventMoveRight.getX() - eventMoveRight.getHistoricalX(0)) / touchDragRatioFwd;
        when(mView.getTranslationX()).thenReturn(startTransX);

        // Create the left-drag event
        MotionEvent eventMoveLeft = createShortLeftMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveLeft);

        // Assert

        float expectedTransX = startTransX +
                                (eventMoveLeft.getX() - eventMoveLeft.getHistoricalX(0)) / touchDragRatioBck;
        verify(mView).setTranslationX(expectedTransX);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_START_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(startTransX));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransX));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    /**
     * When over-scroll has already started (to the left in this case) and suddenly the user changes
     * their mind and scrolls a bit in the other direction:
     * <br/>We expect the <b>touch to still be intercepted</b> in that case, and the <b>overscroll to remain in effect</b>.
     */
    @Test
    public void onTouchMoveAction_dragRightWhenLeftOverscolled_continueOverscrollingRight() throws Exception {

        // Arrange

        // In left & right tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a left-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveLeft = createLongLeftMoveEvent();
        uut.onTouch(mView, eventMoveLeft);
        reset(mView);

        float startTransX = (eventMoveLeft.getX() - eventMoveLeft.getHistoricalX(0)) / touchDragRatioFwd;
        when(mView.getTranslationX()).thenReturn(startTransX);

        // Create the right-drag event
        MotionEvent eventMoveRight = createShortRightMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveRight);

        // Assert

        float expectedTransX = startTransX + (eventMoveRight.getX() - eventMoveRight.getHistoricalX(0)) / touchDragRatioBck;
        verify(mView).setTranslationX(expectedTransX);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_END_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(startTransX));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransX));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_undragWhenRightOverscrolled_endOverscrolling() throws Exception {

        // Arrange

        // In left & right tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a right-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveRight = createLongRightMoveEvent();
        uut.onTouch(mView, eventMoveRight);
        reset(mView);
        float startTransX = (eventMoveRight.getX() - eventMoveRight.getHistoricalX(0)) / touchDragRatioFwd;
        when(mView.getTranslationX()).thenReturn(startTransX);

        // Create the left-drag event
        MotionEvent eventMoveLeft = createLongLeftMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveLeft);

        // Assert

        verify(mView).setTranslationX(0);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        // State-change listener invoked to say drag-on and drag-off (idle).
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_DRAG_START_SIDE), eq(STATE_IDLE));
        verify(mStateListener, times(2)).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(startTransX));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(0f));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_undragWhenLeftOverscrolled_endOverscrolling() throws Exception {

        // Arrange

        // In left & right tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a left-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveLeft = createLongLeftMoveEvent();
        uut.onTouch(mView, eventMoveLeft);
        reset(mView);
        float startTransX = (eventMoveLeft.getX() - eventMoveLeft.getHistoricalX(0)) / touchDragRatioFwd;
        when(mView.getTranslationX()).thenReturn(startTransX);

        // Create the left-drag event
        MotionEvent eventMoveRight = createLongRightMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveRight);

        // Assert

        verify(mView).setTranslationX(0);
        verify(mView, never()).setTranslationY(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        // State-change listener invoked to say drag-on and drag-off (idle).
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_DRAG_END_SIDE), eq(STATE_IDLE));
        verify(mStateListener, times(2)).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(startTransX));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(0f));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }


    /*
     * Up action event
     */

    @Test
    public void onTouchUpAction_eventWhenNotOverscrolled_ignoreTouchEvent() throws Exception {

        // Arrange

        MotionEvent event = createDefaultUpActionEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        boolean ret = uut.onTouch(mView, event);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView, never()).setTranslationY(anyFloat());
        assertFalse(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        verify(mStateListener, never()).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        verify(mUpdateListener, never()).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    /**
     * TODO: Make this work using a decent animation shadows / newer Robolectric
     * @throws Exception
     */
    @Ignore
    @Test
    public void onTouchUpAction_eventWhenLeftOverscrolling_smoothScrollBackToRightEnd() throws Exception {

        // Arrange

        // Bring UUT to a left-overscroll state
        MotionEvent moveEvent = createShortLeftMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        HorizontalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, moveEvent);
        reset(mView);

        // Make the view as though it's been moved by the move event
        float viewX = moveEvent.getX();
        when(mView.getTranslationX()).thenReturn(viewX);

        MotionEvent upEvent = createDefaultUpActionEvent();

        // Act

        boolean ret = uut.onTouch(mView, upEvent);

        // Assert

        assertTrue(ret);

        verify(mView, atLeastOnce()).setTranslationX(anyFloat());
    }

    protected MotionEvent createShortRightMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(100f);
        when(event.getY()).thenReturn(200f);
        when(event.getX(0)).thenReturn(100f);
        when(event.getY(0)).thenReturn(200f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(80f);
        when(event.getHistoricalY(eq(0))).thenReturn(190f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(80f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(190f);
        return event;
    }

    protected MotionEvent createLongRightMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(150f);
        when(event.getY()).thenReturn(250f);
        when(event.getX(0)).thenReturn(150f);
        when(event.getY(0)).thenReturn(250f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(100f);
        when(event.getHistoricalY(eq(0))).thenReturn(200f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(100f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(200f);
        return event;
    }

    protected MotionEvent createShortLeftMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(100f);
        when(event.getY()).thenReturn(200f);
        when(event.getX(0)).thenReturn(100f);
        when(event.getY(0)).thenReturn(200f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(120f);
        when(event.getHistoricalY(eq(0))).thenReturn(220f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(120f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(220f);
        return event;
    }

    protected MotionEvent createLongLeftMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(50f);
        when(event.getY()).thenReturn(150f);
        when(event.getX(0)).thenReturn(50f);
        when(event.getY(0)).thenReturn(150f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(100f);
        when(event.getHistoricalY(eq(0))).thenReturn(200f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(100f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(200f);
        return event;
    }

    protected MotionEvent createDefaultUpActionEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_UP);
        return event;
    }

    protected HorizontalOverScrollBounceEffectDecorator getUUT() {
        HorizontalOverScrollBounceEffectDecorator uut = new HorizontalOverScrollBounceEffectDecorator(mViewAdapter);
        uut.setOverScrollStateListener(mStateListener);
        uut.setOverScrollUpdateListener(mUpdateListener);
        return uut;
    }

    protected HorizontalOverScrollBounceEffectDecorator getUUT(float touchDragRatioFwd, float touchDragRatioBck) {
        HorizontalOverScrollBounceEffectDecorator uut = new HorizontalOverScrollBounceEffectDecorator(mViewAdapter, touchDragRatioFwd, touchDragRatioBck, DEFAULT_DECELERATE_FACTOR);
        uut.setOverScrollStateListener(mStateListener);
        uut.setOverScrollUpdateListener(mUpdateListener);
        return uut;
    }
}
