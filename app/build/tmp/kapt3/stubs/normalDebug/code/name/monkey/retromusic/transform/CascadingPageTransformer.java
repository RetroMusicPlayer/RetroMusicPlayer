package code.name.monkey.retromusic.transform;

import java.lang.System;

/**
 * *****************************************************************
 * * * * * *   * * * *   *     *       Created by OCN.Yang
 * * *     *   *         * *   *       Time:2017/12/7 19:32.
 * * *     *   *         *   * *       Email address:ocnyang@gmail.com
 * * * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0004J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcode/name/monkey/retromusic/transform/CascadingPageTransformer;", "Landroidx/viewpager/widget/ViewPager$PageTransformer;", "()V", "mScaleOffset", "", "setScaleOffset", "", "transformPage", "page", "Landroid/view/View;", "position", "", "app_normalDebug"})
public final class CascadingPageTransformer implements androidx.viewpager.widget.ViewPager.PageTransformer {
    
    /**
     * * 偏移量
     */
    private int mScaleOffset;
    
    /**
     * * @param mScaleOffset 缩放偏移量 单位 px
     */
    public final void setScaleOffset(int mScaleOffset) {
    }
    
    @android.annotation.SuppressLint(value = {"NewApi"})
    @java.lang.Override()
    public void transformPage(@org.jetbrains.annotations.NotNull()
    android.view.View page, float position) {
    }
    
    public CascadingPageTransformer() {
        super();
    }
}