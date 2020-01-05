package me.everything.android.ui.overscroll;

import android.view.View;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import me.everything.android.ui.overscroll.adapters.NestedScrollViewOverScrollDecorAdapter;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter;
import me.everything.android.ui.overscroll.adapters.StaticOverScrollDecorAdapter;

/**
 * @author amit
 */
public class OverScrollDecoratorHelper {

    public static final int ORIENTATION_VERTICAL = 0;

    public static final int ORIENTATION_HORIZONTAL = 1;

    /**
     * Set up the over-scroll effect over a specified {@link RecyclerView} view.
     * <br/>Only recycler-views using <b>native</b> Android layout managers (i.e. {@link LinearLayoutManager},
     * {@link GridLayoutManager} and {@link StaggeredGridLayoutManager}) are currently supported
     * by this convenience method.
     *
     * @param recyclerView The view.
     * @param orientation  Either {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */
    @NonNull
    public static IOverScrollDecor setUpOverScroll(@NonNull RecyclerView recyclerView, int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(
                        new RecyclerViewOverScrollDecorAdapter(recyclerView));
            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(
                        new RecyclerViewOverScrollDecorAdapter(recyclerView));
            default:
                throw new IllegalArgumentException("orientation");
        }
    }

    @NonNull
    public static IOverScrollDecor setUpOverScroll(@NonNull ScrollView scrollView) {
        return new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(scrollView));
    }

    @NonNull
    public static IOverScrollDecor setUpOverScroll(@NonNull NestedScrollView nestedScrollView) {
        return new VerticalOverScrollBounceEffectDecorator(
                new NestedScrollViewOverScrollDecorAdapter(nestedScrollView));
    }

    /**
     * Set up the over-scroll over a generic view, assumed to always be over-scroll ready (e.g.
     * a plain text field, image view).
     *
     * @param view        The view.
     * @param orientation One of {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */
    public static IOverScrollDecor setUpStaticOverScroll(View view, int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));

            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));

            default:
                throw new IllegalArgumentException("orientation");
        }
    }

}
