package code.name.monkey.retromusic.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;

/**
 * Created by yu on 2016/11/10.
 */
@SuppressLint("RestrictedApi")
public class BottomNavigationViewEx extends BottomNavigationView {
    // used for animation
    private int mShiftAmount;
    private float mScaleUpFactor;
    private float mScaleDownFactor;
    private boolean animationRecord;
    private float mLargeLabelSize;
    private float mSmallLabelSize;
    private boolean visibilityTextSizeRecord;
    private boolean visibilityHeightRecord;
    private int mItemHeight;
    private boolean textVisibility = true;
    // used for animation end

    // used for setupWithViewPager
    private ViewPager mViewPager;
    private MyOnNavigationItemSelectedListener mMyOnNavigationItemSelectedListener;
    private BottomNavigationViewExOnPageChangeListener mPageChangeListener;
    private BottomNavigationMenuView mMenuView;
    private BottomNavigationItemView[] mButtons;
    // used for setupWithViewPager end

    public BottomNavigationViewEx(Context context) {
        super(context);
        tintColor( );
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        tintColor( );
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tintColor( );
    }

    /**
     * get text height by font size
     *
     * @param fontSize
     * @return
     */
    private static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * dp to px
     *
     * @param context
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static void setItemIconColors(@NonNull BottomNavigationView view, @ColorInt int normalColor, @ColorInt int selectedColor) {
        ColorStateList iconSl = new ColorStateList(new int[][]{{-16842912}, {16842912}}, new int[]{normalColor, selectedColor});
        view.setItemIconTintList(iconSl);
    }

    private static void setItemTextColors(@NonNull BottomNavigationView view, @ColorInt int normalColor, @ColorInt int selectedColor) {
        ColorStateList textSl = new ColorStateList(new int[][]{{-16842912}, {16842912}}, new int[]{normalColor, selectedColor});
        view.setItemTextColor(textSl);
    }

    public void setIconAndTextColor(int i) {
        tintColor(ATHUtil.resolveColor(getContext(), android.R.attr.textColorSecondary), i);
    }

    private void tintColor() {
        int color = ATHUtil.resolveColor(getContext(), android.R.attr.textColorSecondary);
        int accentColor = ThemeStore.accentColor(getContext());
        tintColor(color, accentColor);
    }

    private void tintColor(int color, int accentColor) {
        setItemIconColors(this, color, accentColor);
        setItemTextColors(this, color, accentColor);
    }

    private void init() {
        try {
            addAnimationListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAnimationListener() {
        /**
         * 1. BottomNavigationMenuView mMenuView
         * 2. private final BottomNavigationAnimationHelperBase mAnimationHelper;
         * 3. private final TransitionSet mSet;
         */
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        Object mAnimationHelper = getField(mMenuView.getClass(), mMenuView, "mAnimationHelper");
        TransitionSet mSet = getField(mAnimationHelper.getClass(), mAnimationHelper, "mSet");
        mSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                refreshTextViewVisibility();
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {
                refreshTextViewVisibility();
            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {
            }
        });
    }

    private void refreshTextViewVisibility() {
        if (!textVisibility)
            return;
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();

        int currentItem = getCurrentItem();

        // 3. get field mShiftingMode and TextView in mButtons
        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");

            if (mLargeLabel != null) {
                mLargeLabel.clearAnimation();
            }
            if (mSmallLabel != null) {
                mSmallLabel.clearAnimation();
            }

            // mShiftingMode
            boolean mShiftingMode = getField(button.getClass(), button, "mShiftingMode");
            boolean selected = button.getItemPosition() == currentItem;
            if (mShiftingMode) {
                if (selected) {
                    mLargeLabel.setVisibility(VISIBLE);
                } else {
                    mLargeLabel.setVisibility(INVISIBLE);
                }
                mSmallLabel.setVisibility(INVISIBLE);
            } else {
                if (selected) {
                    mLargeLabel.setVisibility(VISIBLE);
                    mSmallLabel.setVisibility(INVISIBLE);
                } else {
                    mLargeLabel.setVisibility(INVISIBLE);
                    mSmallLabel.setVisibility(VISIBLE);
                }
            }
        }
    }

    /**
     * change the visibility of icon
     *
     * @param visibility
     */
    public void setIconVisibility(boolean visibility) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mButtons
        private BottomNavigationItemView[] mButtons;

        3. get mIcon in mButtons
        private ImageView mIcon

        4. set mIcon visibility gone

        5. change mItemHeight to only text size in mMenuView
         */
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // 3. get mIcon in mButtons
        for (BottomNavigationItemView button : mButtons) {
            ImageView mIcon = getField(button.getClass(), button, "mIcon");
            // 4. set mIcon visibility gone
            mIcon.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        }

        // 5. change mItemHeight to only text size in mMenuView
        if (!visibility) {
            // if not record mItemHeight
            if (!visibilityHeightRecord) {
                visibilityHeightRecord = true;
                mItemHeight = getItemHeight();
            }

            // change mItemHeight
            BottomNavigationItemView button = mButtons[0];
            if (null != button) {
                final ImageView mIcon = getField(button.getClass(), button, "mIcon");
//                System.out.println("mIcon.getMeasuredHeight():" + mIcon.getMeasuredHeight());
                if (null != mIcon) {
                    mIcon.post(new Runnable() {
                        @Override
                        public void run() {
//                            System.out.println("mIcon.getMeasuredHeight():" + mIcon.getMeasuredHeight());
                            setItemHeight(mItemHeight - mIcon.getMeasuredHeight());
                        }
                    });
                }
            }
        } else {
            // if not record the mItemHeight, we need do nothing.
            if (!visibilityHeightRecord)
                return;

            // restore it
            setItemHeight(mItemHeight);
        }

        mMenuView.updateMenuView();
    }

    /**
     * change the visibility of text
     *
     * @param visibility
     */
    public void setTextVisibility(boolean visibility) {
        this.textVisibility = visibility;
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mButtons
        private BottomNavigationItemView[] mButtons;

        3. set text size in mButtons
        private final TextView mLargeLabel
        private final TextView mSmallLabel

        4. change mItemHeight to only icon size in mMenuView
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();

        // 3. change field mShiftingMode value in mButtons
        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");

            if (!visibility) {
                // if not record the font size, record it
                if (!visibilityTextSizeRecord && !animationRecord) {
                    visibilityTextSizeRecord = true;
                    mLargeLabelSize = mLargeLabel.getTextSize();
                    mSmallLabelSize = mSmallLabel.getTextSize();
                }

                // if not visitable, set font size to 0
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);
                mSmallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);

            } else {
                // if not record the font size, we need do nothing.
                if (!visibilityTextSizeRecord)
                    break;

                // restore it
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
                mSmallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);
            }
        }

        // 4 change mItemHeight to only icon size in mMenuView
        if (!visibility) {
            // if not record mItemHeight
            if (!visibilityHeightRecord) {
                visibilityHeightRecord = true;
                mItemHeight = getItemHeight();
            }

            // change mItemHeight to only icon size in mMenuView
            // private final int mItemHeight;

            // change mItemHeight
//            System.out.println("mLargeLabel.getMeasuredHeight():" + getFontHeight(mSmallLabelSize));
            setItemHeight(mItemHeight - getFontHeight(mSmallLabelSize));

        } else {
            // if not record the mItemHeight, we need do nothing.
            if (!visibilityHeightRecord)
                return;
            // restore mItemHeight
            setItemHeight(mItemHeight);
        }

        mMenuView.updateMenuView();
    }

    /**
     * enable or disable click item animation(text scale and icon move animation in no item shifting mode)
     *
     * @param enable It means the text won't scale and icon won't move when active it in no item shifting mode if false.
     */
    public void enableAnimation(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mButtons
        private BottomNavigationItemView[] mButtons;

        3. chang mShiftAmount to 0 in mButtons
        private final int mShiftAmount

        change mScaleUpFactor and mScaleDownFactor to 1f in mButtons
        private final float mScaleUpFactor
        private final float mScaleDownFactor

        4. change label font size in mButtons
        private final TextView mLargeLabel
        private final TextView mSmallLabel
         */

        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // 3. change field mShiftingMode value in mButtons
        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");

            // if disable animation, need animationRecord the source value
            if (!enable) {
                if (!animationRecord) {
                    animationRecord = true;
                    mShiftAmount = getField(button.getClass(), button, "mShiftAmount");
                    mScaleUpFactor = getField(button.getClass(), button, "mScaleUpFactor");
                    mScaleDownFactor = getField(button.getClass(), button, "mScaleDownFactor");

                    mLargeLabelSize = mLargeLabel.getTextSize();
                    mSmallLabelSize = mSmallLabel.getTextSize();

//                    System.out.println("mShiftAmount:" + mShiftAmount + " mScaleUpFactor:"
//                            + mScaleUpFactor + " mScaleDownFactor:" + mScaleDownFactor
//                            + " mLargeLabel:" + mLargeLabelSize + " mSmallLabel:" + mSmallLabelSize);
                }
                // disable
                setField(button.getClass(), button, "mShiftAmount", 0);
                setField(button.getClass(), button, "mScaleUpFactor", 1);
                setField(button.getClass(), button, "mScaleDownFactor", 1);

                // let the mLargeLabel font size equal to mSmallLabel
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);

                // debug start
//                mLargeLabelSize = mLargeLabel.getTextSize();
//                System.out.println("mLargeLabel:" + mLargeLabelSize);
                // debug end

            } else {
                // haven't change the value. It means it was the first call this method. So nothing need to do.
                if (!animationRecord)
                    return;
                // enable animation
                setField(button.getClass(), button, "mShiftAmount", mShiftAmount);
                setField(button.getClass(), button, "mScaleUpFactor", mScaleUpFactor);
                setField(button.getClass(), button, "mScaleDownFactor", mScaleDownFactor);
                // restore
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
            }
        }
        mMenuView.updateMenuView();
    }

    /**
     * enable the shifting mode for navigation
     *
     * @param enable It will has a shift animation if true. Otherwise all items are the same width.
     */
    public void enableShiftingMode(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. change field mShiftingMode value in mMenuView
        private boolean mShiftingMode = true;
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. change field mShiftingMode value in mMenuView
        setField(mMenuView.getClass(), mMenuView, "mShiftingMode", enable);

        mMenuView.updateMenuView();
    }

    /**
     * enable the shifting mode for each item
     *
     * @param enable It will has a shift animation for item if true. Otherwise the item text always be shown.
     */
    public void enableItemShiftingMode(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in this mMenuView
        private BottomNavigationItemView[] mButtons;

        3. change field mShiftingMode value in mButtons
        private boolean mShiftingMode = true;
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // 3. change field mShiftingMode value in mButtons
        for (BottomNavigationItemView button : mButtons) {
            setField(button.getClass(), button, "mShiftingMode", enable);
        }
        mMenuView.updateMenuView();
    }

    /**
     * get the current checked item position
     *
     * @return index of item, start from 0.
     */
    public int getCurrentItem() {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mMenuView
        private BottomNavigationItemView[] mButtons;

        3. get menu and traverse it to get the checked one
         */

        // 1. get mMenuView
//        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // 3. get menu and traverse it to get the checked one
        Menu menu = getMenu();
        for (int i = 0; i < mButtons.length; i++) {
            if (menu.getItem(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * set the current checked item
     *
     * @param item start from 0.
     */
    public void setCurrentItem(int item) {
        // check bounds
        if (item < 0 || item >= getMaxItemCount()) {
            throw new ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - "
                    + (getMaxItemCount() - 1) + ". Actually " + item);
        }

        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mMenuView
        private BottomNavigationItemView[] mButtons;
        private final OnClickListener mOnClickListener;

        3. call mOnClickListener.onClick();
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // get mOnClickListener
        View.OnClickListener mOnClickListener = getField(mMenuView.getClass(), mMenuView, "mOnClickListener");

//        System.out.println("mMenuView:" + mMenuView + " mButtons:" + mButtons + " mOnClickListener" + mOnClickListener);
        // 3. call mOnClickListener.onClick();
        mOnClickListener.onClick(mButtons[item]);

    }

    /**
     * get menu item position in menu
     *
     * @param item
     * @return position if success, -1 otherwise
     */
    public int getMenuItemPosition(MenuItem item) {
        // get item id
        int itemId = item.getItemId();
        // get meunu
        Menu menu = getMenu();
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            if (menu.getItem(i).getItemId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * get OnNavigationItemSelectedListener
     *
     * @return
     */
    public OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        // private OnNavigationItemSelectedListener mListener;
        OnNavigationItemSelectedListener mListener = getField(BottomNavigationView.class, this, "mSelectedListener");
        return mListener;
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        // if not set up with view pager, the same with father
        if (null == mMyOnNavigationItemSelectedListener) {
            super.setOnNavigationItemSelectedListener(listener);
            return;
        }

        mMyOnNavigationItemSelectedListener.setOnNavigationItemSelectedListener(listener);
    }

    /**
     * get private mMenuView
     *
     * @return
     */
    private BottomNavigationMenuView getBottomNavigationMenuView() {
        if (null == mMenuView)
            mMenuView = getField(BottomNavigationView.class, this, "mMenuView");
        return mMenuView;
    }

    /**
     * get private mButtons in mMenuView
     *
     * @return
     */
    public BottomNavigationItemView[] getBottomNavigationItemViews() {
        if (null != mButtons)
            return mButtons;
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         */
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons");
        return mButtons;
    }

    /**
     * get private mButton in mMenuView at position
     *
     * @param position
     * @return
     */
    public BottomNavigationItemView getBottomNavigationItemView(int position) {
        return getBottomNavigationItemViews()[position];
    }

    /**
     * get icon at position
     *
     * @param position
     * @return
     */
    public ImageView getIconAt(int position) {
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         * 3 private ImageView mIcon;
         */
        BottomNavigationItemView mButtons = getBottomNavigationItemView(position);
        ImageView mIcon = getField(BottomNavigationItemView.class, mButtons, "mIcon");
        return mIcon;
    }

    /**
     * get small label at position
     * Each item has tow label, one is large, another is small.
     *
     * @param position
     * @return
     */
    public TextView getSmallLabelAt(int position) {
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         * 3 private final TextView mSmallLabel;
         */
        BottomNavigationItemView mButtons = getBottomNavigationItemView(position);
        TextView mSmallLabel = getField(BottomNavigationItemView.class, mButtons, "mSmallLabel");
        return mSmallLabel;
    }

    /**
     * get large label at position
     * Each item has tow label, one is large, another is small.
     *
     * @param position
     * @return
     */
    public TextView getLargeLabelAt(int position) {
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         * 3 private final TextView mLargeLabel;
         */
        BottomNavigationItemView mButtons = getBottomNavigationItemView(position);
        TextView mLargeLabel = getField(BottomNavigationItemView.class, mButtons, "mLargeLabel");
        return mLargeLabel;
    }

    /**
     * return item count
     *
     * @return
     */
    public int getItemCount() {
        BottomNavigationItemView[] bottomNavigationItemViews = getBottomNavigationItemViews();
        if (null == bottomNavigationItemViews)
            return 0;
        return bottomNavigationItemViews.length;
    }

    /**
     * set all item small TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setSmallTextSize(float sp) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getSmallLabelAt(i).setTextSize(sp);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set all item large TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal.
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setLargeTextSize(float sp) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTextSize(sp);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set all item large and small TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setTextSize(float sp) {
        setLargeTextSize(sp);
        setSmallTextSize(sp);
    }

    /**
     * set item ImageView size which at position
     *
     * @param position position start from 0
     * @param width    in dp
     * @param height   in dp
     */
    public void setIconSizeAt(int position, float width, float height) {
        ImageView icon = getIconAt(position);
        // update size
        ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
        layoutParams.width = dp2px(getContext(), width);
        layoutParams.height = dp2px(getContext(), height);
        icon.setLayoutParams(layoutParams);

        mMenuView.updateMenuView();
    }

    /**
     * set all item ImageView size
     *
     * @param width  in dp
     * @param height in dp
     */
    public void setIconSize(float width, float height) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            setIconSizeAt(i, width, height);
        }
    }

    /**
     * get menu item height
     *
     * @return in px
     */
    public int getItemHeight() {
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get private final int mItemHeight in mMenuView
        return getField(mMenuView.getClass(), mMenuView, "mItemHeight");
    }

    /**
     * set menu item height
     *
     * @param height in px
     */
    public void setItemHeight(int height) {
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. set private final int mItemHeight in mMenuView
        setField(mMenuView.getClass(), mMenuView, "mItemHeight", height);

        mMenuView.updateMenuView();
    }

    /**
     * set Typeface for all item TextView
     *
     * @attr ref android.R.styleable#TextView_typeface
     * @attr ref android.R.styleable#TextView_textStyle
     */
    public void setTypeface(Typeface typeface, int style) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTypeface(typeface, style);
            getSmallLabelAt(i).setTypeface(typeface, style);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set Typeface for all item TextView
     *
     * @attr ref android.R.styleable#TextView_typeface
     */
    public void setTypeface(Typeface typeface) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTypeface(typeface);
            getSmallLabelAt(i).setTypeface(typeface);
        }
        mMenuView.updateMenuView();
    }

    /**
     * get private filed in this specific object
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param <T>
     * @return field if success, null otherwise.
     */
    private <T> T getField(Class targetClass, Object instance, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * change the field value
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param value
     */
    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will link the given ViewPager and this BottomNavigationViewEx together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks.
     *
     * @param viewPager
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager) {
        setupWithViewPager(viewPager, false);
    }

    /**
     * This method will link the given ViewPager and this BottomNavigationViewEx together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks.
     *
     * @param viewPager
     * @param smoothScroll whether ViewPager changed with smooth scroll animation
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager, boolean smoothScroll) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
        }

        if (null == viewPager) {
            mViewPager = null;
            super.setOnNavigationItemSelectedListener(null);
            return;
        }

        mViewPager = viewPager;

        // Add our custom OnPageChangeListener to the ViewPager
        if (mPageChangeListener == null) {
            mPageChangeListener = new BottomNavigationViewExOnPageChangeListener(this);
        }
        viewPager.addOnPageChangeListener(mPageChangeListener);

        // Now we'll add a navigation item selected listener to set ViewPager's current item
        OnNavigationItemSelectedListener listener = getOnNavigationItemSelectedListener();
        mMyOnNavigationItemSelectedListener = new MyOnNavigationItemSelectedListener(viewPager, this, smoothScroll, listener);
        super.setOnNavigationItemSelectedListener(mMyOnNavigationItemSelectedListener);
    }

    public void enableShiftingMode(int position, boolean enable) {
        getBottomNavigationItemView(position).setShiftingMode(enable);
    }

    public void setItemBackground(int position, int background) {
        getBottomNavigationItemView(position).setItemBackground(background);
    }

    public void setIconTintList(int position, ColorStateList tint) {
        getBottomNavigationItemView(position).setIconTintList(tint);
    }

    public void setTextTintList(int position, ColorStateList tint) {
        getBottomNavigationItemView(position).setTextColor(tint);
    }

    /**
     * set margin top for all icons
     *
     * @param marginTop in px
     */
    public void setIconsMarginTop(int marginTop) {
        for (int i = 0; i < getItemCount(); i++) {
            setIconMarginTop(i, marginTop);
        }
    }

    /**
     * set margin top for icon
     *
     * @param position
     * @param marginTop in px
     */
    public void setIconMarginTop(int position, int marginTop) {
        /*
        1. BottomNavigationItemView
        2. private final int mDefaultMargin;
         */
        BottomNavigationItemView itemView = getBottomNavigationItemView(position);
        setField(BottomNavigationItemView.class, itemView, "mDefaultMargin", marginTop);
        mMenuView.updateMenuView();
    }


    /**
     * A {@link ViewPager.OnPageChangeListener} class which contains the
     * necessary calls back to the provided {@link BottomNavigationViewEx} so that the tab position is
     * kept in sync.
     * <p>
     * <p>This class stores the provided BottomNavigationViewEx weakly, meaning that you can use
     * {@link ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)
     * addOnPageChangeListener(OnPageChangeListener)} without removing the listener and
     * not cause a leak.
     */
    private static class BottomNavigationViewExOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<BottomNavigationViewEx> mBnveRef;

        public BottomNavigationViewExOnPageChangeListener(BottomNavigationViewEx bnve) {
            mBnveRef = new WeakReference<>(bnve);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            final BottomNavigationViewEx bnve = mBnveRef.get();
            if (null != bnve)
                bnve.setCurrentItem(position);
//            Log.d("onPageSelected", "--------- position " + position + " ------------");
        }
    }

    /**
     * Decorate OnNavigationItemSelectedListener for setupWithViewPager
     */
    private static class MyOnNavigationItemSelectedListener implements OnNavigationItemSelectedListener {
        private final WeakReference<ViewPager> viewPagerRef;
        private OnNavigationItemSelectedListener listener;
        private boolean smoothScroll;
        private SparseIntArray items;// used for change ViewPager selected item
        private int previousPosition = -1;


        MyOnNavigationItemSelectedListener(ViewPager viewPager, BottomNavigationViewEx bnve, boolean smoothScroll, OnNavigationItemSelectedListener listener) {
            this.viewPagerRef = new WeakReference<>(viewPager);
            this.listener = listener;
            this.smoothScroll = smoothScroll;

            // create items
            Menu menu = bnve.getMenu();
            int size = menu.size();
            items = new SparseIntArray(size);
            for (int i = 0; i < size; i++) {
                int itemId = menu.getItem(i).getItemId();
                items.put(itemId, i);
            }
        }

        public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int position = items.get(item.getItemId());
            // only set item when item changed
            if (previousPosition == position) {
                return true;
            }

            // user listener
            if (null != listener) {
                boolean bool = listener.onNavigationItemSelected(item);
                // if the selected is invalid, no need change the view pager
                if (!bool)
                    return false;
            }

            // change view pager
            ViewPager viewPager = viewPagerRef.get();
            if (null == viewPager)
                return false;

            viewPager.setCurrentItem(items.get(item.getItemId()), smoothScroll);

            // update previous position
            previousPosition = position;

            return true;
        }

    }

}