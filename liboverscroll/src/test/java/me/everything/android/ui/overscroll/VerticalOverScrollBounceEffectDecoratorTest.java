package me.everything.android.ui.overscroll;

import android.view.MotionEvent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter;

import static me.everything.android.ui.overscroll.IOverScrollState.*;
import static me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator.DEFAULT_DECELERATE_FACTOR;
import static me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator.DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
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
public class VerticalOverScrollBounceEffectDecoratorTest {

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

        VerticalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, createShortDownwardsMoveEvent());

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

        MotionEvent event = createShortDownwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        boolean ret = uut.onTouch(mView, event);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView, never()).setTranslationY(anyFloat());
        assertFalse(ret);
        assertEquals(STATE_IDLE, uut.getCurrentState());

        verify(mStateListener, never()).onOverScrollStateChange(eq(uut),anyInt(), anyInt());
        verify(mUpdateListener, never()).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_dragDownInUpperEnd_overscrollDownwards() throws Exception {

        // Arrange

        MotionEvent event = createShortDownwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        boolean ret = uut.onTouch(mView, event);

        // Assert

        float expectedTransY = (event.getY() - event.getHistoricalY(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationY(expectedTransY);
        verify(mView, never()).setTranslationX(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_START_SIDE, uut.getCurrentState());

        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransY));
    }

    @Test
    public void onTouchMoveAction_dragUpInBottomEnd_overscrollUpwards() throws Exception {

        // Arrange

        MotionEvent event = createShortUpwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();

        // Act

        boolean ret = uut.onTouch(mView, event);

        // Assert

        float expectedTransY = (event.getY() - event.getHistoricalY(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationY(expectedTransY);
        verify(mView, never()).setTranslationX(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_END_SIDE, uut.getCurrentState());

        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransY));
    }

    @Test
    public void onTouchMoveAction_dragUpInUpperEnd_ignoreTouchEvent() throws Exception {

        // Arrange

        MotionEvent event = createShortUpwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();

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
    public void onTouchMoveAction_dragDownInBottomEnd_ignoreTouchEvent() throws Exception {

        // Arrange

        MotionEvent event = createShortDownwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();

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
    public void onTouchMoveAction_2ndDownDragInUpperEnd_overscrollDownwardsFurther() throws Exception {

        // Arrange

        // Bring UUT to a downwards-overscroll state
        MotionEvent event1 = createShortDownwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, event1);
        reset(mView);

        // Create 2nd downwards-drag event
        MotionEvent event2 = createLongDownwardsMoveEvent();

        // Act

        final boolean ret = uut.onTouch(mView, event2);

        // Assert

        final float expectedTransY1 = (event1.getY() - event1.getHistoricalY(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        final float expectedTransY2 = (event2.getY() - event2.getHistoricalY(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationY(expectedTransY2);
        verify(mView, never()).setTranslationX(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_START_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransY1));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransY2));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_2ndUpDragInBottomEnd_overscrollUpwardsFurther() throws Exception {

        // Arrange

        // Bring UUT to an upwards-overscroll state
        MotionEvent event1 = createShortUpwardsMoveEvent();

        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        VerticalOverScrollBounceEffectDecorator uut = getUUT();
        uut.onTouch(mView, event1);
        reset(mView);

        // Create 2nd upward-drag event
        MotionEvent event2 = createLongUpwardsMoveEvent();

        // Act

        final boolean ret = uut.onTouch(mView, event2);

        // Assert

        final float expectedTransY1 = (event1.getY() - event1.getHistoricalY(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        final float expectedTransY2 = (event2.getY() - event2.getHistoricalY(0)) / DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD;
        verify(mView).setTranslationY(expectedTransY2);
        verify(mView, never()).setTranslationX(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_END_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransY1));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransY2));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    /**
     * When over-scroll has already started (downwards in this case) and suddenly the user changes
     * their mind and scrolls a bit in the other direction:
     * <br/>We expect the <b>touch to still be intercepted</b> in that case, and the <b>overscroll to remain in effect</b>.
     */
    @Test
    public void onTouchMoveAction_dragUpWhenDownOverscolled_continueOverscrollingUpwards() throws Exception {

        // Arrange

        // In down & up drag tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a downwrads-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        VerticalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveRight = createLongDownwardsMoveEvent();
        uut.onTouch(mView, eventMoveRight);
        reset(mView);
        float startTransY = (eventMoveRight.getY() - eventMoveRight.getHistoricalY(0)) / touchDragRatioFwd;
        when(mView.getTranslationY()).thenReturn(startTransY);

        // Create the up-drag event
        MotionEvent eventMoveUpwards = createShortUpwardsMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveUpwards);

        // Assert

        float expectedTransY = startTransY +
                (eventMoveUpwards.getY() - eventMoveUpwards.getHistoricalY(0)) / touchDragRatioBck;
        verify(mView).setTranslationY(expectedTransY);
        verify(mView, never()).setTranslationX(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_START_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_START_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(startTransY));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_START_SIDE), eq(expectedTransY));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    /**
     * When over-scroll has already started (upwards in this case) and suddenly the user changes
     * their mind and scrolls a bit in the other direction:
     * <br/>We expect the <b>touch to still be intercepted</b> in that case, and the <b>overscroll to remain in effect</b>.
     */
    @Test
    public void onTouchMoveAction_dragDownWhenUpOverscolled_continueOverscrollingDownwards() throws Exception {

        // Arrange

        // In up & down drag tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to an upwards-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        VerticalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveUp = createLongUpwardsMoveEvent();
        uut.onTouch(mView, eventMoveUp);
        reset(mView);

        float startTransY = (eventMoveUp.getY() - eventMoveUp.getHistoricalY(0)) / touchDragRatioFwd;
        when(mView.getTranslationY()).thenReturn(startTransY);

        // Create the down-drag event
        MotionEvent eventMoveDown = createShortDownwardsMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveDown);

        // Assert

        float expectedTransY = startTransY + (eventMoveDown.getY() - eventMoveDown.getHistoricalY(0)) / touchDragRatioBck;
        verify(mView).setTranslationY(expectedTransY);
        verify(mView, never()).setTranslationX(anyFloat());
        assertTrue(ret);
        assertEquals(STATE_DRAG_END_SIDE, uut.getCurrentState());

        // State-change listener called only once?
        verify(mStateListener).onOverScrollStateChange(eq(uut), eq(STATE_IDLE), eq(STATE_DRAG_END_SIDE));
        verify(mStateListener).onOverScrollStateChange(eq(uut), anyInt(), anyInt());
        // Update-listener called exactly twice?
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(startTransY));
        verify(mUpdateListener).onOverScrollUpdate(eq(uut), eq(STATE_DRAG_END_SIDE), eq(expectedTransY));
        verify(mUpdateListener, times(2)).onOverScrollUpdate(eq(uut), anyInt(), anyFloat());
    }

    @Test
    public void onTouchMoveAction_undragWhenDownOverscrolled_endOverscrolling() throws Exception {

        // Arrange

        // In left & right tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a downwards-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(true);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(false);

        VerticalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveDown = createLongDownwardsMoveEvent();
        uut.onTouch(mView, eventMoveDown);
        reset(mView);
        float startTransX = (eventMoveDown.getX() - eventMoveDown.getHistoricalX(0)) / touchDragRatioFwd;
        when(mView.getTranslationX()).thenReturn(startTransX);

        // Create the (negative) upwards-drag event
        MotionEvent eventMoveUp = createLongUpwardsMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveUp);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView).setTranslationY(0);
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
    public void onTouchMoveAction_undragWhenUpOverscrolled_endOverscrolling() throws Exception {

        // Arrange

        // In left & right tests we use equal ratios to avoid the effect's under-scroll handling
        final float touchDragRatioFwd = 3f;
        final float touchDragRatioBck = 3f;

        // Bring UUT to a left-overscroll state
        when(mViewAdapter.isInAbsoluteStart()).thenReturn(false);
        when(mViewAdapter.isInAbsoluteEnd()).thenReturn(true);

        VerticalOverScrollBounceEffectDecorator uut = getUUT(touchDragRatioFwd, touchDragRatioBck);
        MotionEvent eventMoveUp = createLongUpwardsMoveEvent();
        uut.onTouch(mView, eventMoveUp);
        reset(mView);
        float startTransX = (eventMoveUp.getX() - eventMoveUp.getHistoricalX(0)) / touchDragRatioFwd;
        when(mView.getTranslationX()).thenReturn(startTransX);

        // Create the (negative) downwards-drag event
        MotionEvent eventMoveDown = createLongDownwardsMoveEvent();

        // Act

        boolean ret = uut.onTouch(mView, eventMoveDown);

        // Assert

        verify(mView, never()).setTranslationX(anyFloat());
        verify(mView).setTranslationY(0);
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

        VerticalOverScrollBounceEffectDecorator uut = getUUT();

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

    protected MotionEvent createShortDownwardsMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(200f);
        when(event.getY()).thenReturn(100f);
        when(event.getX(0)).thenReturn(200f);
        when(event.getY(0)).thenReturn(100f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(190f);
        when(event.getHistoricalY(eq(0))).thenReturn(80f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(190f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(80f);
        return event;
    }

    protected MotionEvent createLongDownwardsMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(250f);
        when(event.getY()).thenReturn(150f);
        when(event.getX(0)).thenReturn(250f);
        when(event.getY(0)).thenReturn(150f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(200f);
        when(event.getHistoricalY(eq(0))).thenReturn(100f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(200f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(100f);
        return event;
    }

    protected MotionEvent createShortUpwardsMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(200f);
        when(event.getY()).thenReturn(100f);
        when(event.getX(0)).thenReturn(200f);
        when(event.getY(0)).thenReturn(100f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(220f);
        when(event.getHistoricalY(eq(0))).thenReturn(120f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(220f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(120f);
        return event;
    }

    protected MotionEvent createLongUpwardsMoveEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_MOVE);
        when(event.getX()).thenReturn(200f);
        when(event.getY()).thenReturn(100f);
        when(event.getX(0)).thenReturn(200f);
        when(event.getY(0)).thenReturn(100f);
        when(event.getHistorySize()).thenReturn(1);
        when(event.getHistoricalX(eq(0))).thenReturn(250f);
        when(event.getHistoricalY(eq(0))).thenReturn(150f);
        when(event.getHistoricalX(eq(0), eq(0))).thenReturn(250f);
        when(event.getHistoricalY(eq(0), eq(0))).thenReturn(150f);
        return event;
    }

    protected MotionEvent createDefaultUpActionEvent() {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_UP);
        return event;
    }

    protected VerticalOverScrollBounceEffectDecorator getUUT() {
        VerticalOverScrollBounceEffectDecorator uut = new VerticalOverScrollBounceEffectDecorator(mViewAdapter);
        uut.setOverScrollStateListener(mStateListener);
        uut.setOverScrollUpdateListener(mUpdateListener);
        return uut;
    }

    protected VerticalOverScrollBounceEffectDecorator getUUT(float touchDragRatioFwd, float touchDragRatioBck) {
        VerticalOverScrollBounceEffectDecorator uut = new VerticalOverScrollBounceEffectDecorator(mViewAdapter, touchDragRatioFwd, touchDragRatioBck, DEFAULT_DECELERATE_FACTOR);
        uut.setOverScrollStateListener(mStateListener);
        uut.setOverScrollUpdateListener(mUpdateListener);
        return uut;
    }
}