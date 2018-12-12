package code.name.monkey.retromusic.transform;

import java.lang.System;

/**
 * * 实现ViewPager左右滑动时的时差
 * * Created by xmuSistone on 2016/9/18.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000bH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcode/name/monkey/retromusic/transform/CarousalPagerTransformer;", "Landroidx/viewpager/widget/ViewPager$PageTransformer;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "maxTranslateOffsetX", "", "viewPager", "Landroidx/viewpager/widget/ViewPager;", "dp2px", "dipValue", "", "transformPage", "", "view", "Landroid/view/View;", "position", "app_normalDebug"})
public final class CarousalPagerTransformer implements androidx.viewpager.widget.ViewPager.PageTransformer {
    private final int maxTranslateOffsetX = 0;
    private androidx.viewpager.widget.ViewPager viewPager;
    
    @java.lang.Override()
    public void transformPage(@org.jetbrains.annotations.NotNull()
    android.view.View view, float position) {
    }
    
    private final int dp2px(android.content.Context context, float dipValue) {
        return 0;
    }
    
    public CarousalPagerTransformer(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
}