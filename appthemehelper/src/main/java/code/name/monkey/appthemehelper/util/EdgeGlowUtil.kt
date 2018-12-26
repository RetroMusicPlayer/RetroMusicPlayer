package code.name.monkey.appthemehelper.util

import android.annotation.TargetApi
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.viewpager.widget.ViewPager
import androidx.core.widget.EdgeEffectCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import android.widget.AbsListView
import android.widget.EdgeEffect
import android.widget.ScrollView

import code.name.monkey.appthemehelper.BuildConfig

import java.lang.reflect.Field

class EdgeGlowUtil protected constructor() {
    companion object {

        // Invalidation methods

        private var EDGE_GLOW_FIELD_EDGE: Field? = null
        private var EDGE_GLOW_FIELD_GLOW: Field? = null
        private var EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT: Field? = null

        private fun invalidateEdgeEffectFields() {
            if (EDGE_GLOW_FIELD_EDGE != null && EDGE_GLOW_FIELD_GLOW != null &&
                    EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT != null) {
                EDGE_GLOW_FIELD_EDGE!!.isAccessible = true
                EDGE_GLOW_FIELD_GLOW!!.isAccessible = true
                EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT!!.isAccessible = true
                return
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                var edge: Field? = null
                var glow: Field? = null
                var cls: Class<*>? = null
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    try {
                        cls = Class.forName("android.widget.EdgeGlow")
                    } catch (e: ClassNotFoundException) {
                        if (BuildConfig.DEBUG) e.printStackTrace()
                    }

                } else {
                    cls = EdgeEffect::class.java
                }
                if (cls != null) {
                    for (f in cls.declaredFields) {
                        when (f.name) {
                            "mEdge" -> {
                                f.isAccessible = true
                                edge = f
                            }
                            "mGlow" -> {
                                f.isAccessible = true
                                glow = f
                            }
                        }
                    }
                }
                EDGE_GLOW_FIELD_EDGE = edge
                EDGE_GLOW_FIELD_GLOW = glow
            } else {
                EDGE_GLOW_FIELD_EDGE = null
                EDGE_GLOW_FIELD_GLOW = null
            }

            var efc: Field? = null
            try {
                efc = EdgeEffectCompat::class.java.getDeclaredField("mEdgeEffect")
            } catch (e: NoSuchFieldException) {
                if (BuildConfig.DEBUG) e.printStackTrace()
            }

            EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT = efc
        }

        private var SCROLL_VIEW_FIELD_EDGE_GLOW_TOP: Field? = null
        private var SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM: Field? = null

        private fun invalidateScrollViewFields() {
            if (SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null && SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
                SCROLL_VIEW_FIELD_EDGE_GLOW_TOP!!.isAccessible = true
                SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.isAccessible = true
                return
            }
            val cls = ScrollView::class.java
            for (f in cls.declaredFields) {
                when (f.name) {
                    "mEdgeGlowTop" -> {
                        f.isAccessible = true
                        SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = f
                    }
                    "mEdgeGlowBottom" -> {
                        f.isAccessible = true
                        SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = f
                    }
                }
            }
        }

        private var NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP: Field? = null
        private var NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM: Field? = null

        private fun invalidateNestedScrollViewFields() {
            if (NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null && NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
                NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP!!.isAccessible = true
                NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.isAccessible = true
                return
            }
            val cls = ATHUtil.inClassPath("android.support.v4.widget.NestedScrollView")
            for (f in cls.declaredFields) {
                when (f.name) {
                    "mEdgeGlowTop" -> {
                        f.isAccessible = true
                        NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = f
                    }
                    "mEdgeGlowBottom" -> {
                        f.isAccessible = true
                        NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = f
                    }
                }
            }
        }

        private var LIST_VIEW_FIELD_EDGE_GLOW_TOP: Field? = null
        private var LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM: Field? = null

        private fun invalidateListViewFields() {
            if (LIST_VIEW_FIELD_EDGE_GLOW_TOP != null && LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
                LIST_VIEW_FIELD_EDGE_GLOW_TOP!!.isAccessible = true
                LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.isAccessible = true
                return
            }
            val cls = AbsListView::class.java
            for (f in cls.declaredFields) {
                when (f.name) {
                    "mEdgeGlowTop" -> {
                        f.isAccessible = true
                        LIST_VIEW_FIELD_EDGE_GLOW_TOP = f
                    }
                    "mEdgeGlowBottom" -> {
                        f.isAccessible = true
                        LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = f
                    }
                }
            }
        }

        private var RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP: Field? = null
        private var RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT: Field? = null
        private var RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT: Field? = null
        private var RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM: Field? = null

        private fun invalidateRecyclerViewFields() {
            if (RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP != null && RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT != null &&
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT != null && RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
                RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP!!.isAccessible = true
                RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT!!.isAccessible = true
                RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT!!.isAccessible = true
                RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.isAccessible = true
                return
            }
            val cls = ATHUtil.inClassPath("android.support.v7.widget.RecyclerView")
            for (f in cls.declaredFields) {
                when (f.name) {
                    "mTopGlow" -> {
                        f.isAccessible = true
                        RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP = f
                    }
                    "mBottomGlow" -> {
                        f.isAccessible = true
                        RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM = f
                    }
                    "mLeftGlow" -> {
                        f.isAccessible = true
                        RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT = f
                    }
                    "mRightGlow" -> {
                        f.isAccessible = true
                        RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT = f
                    }
                }
            }
        }

        private var VIEW_PAGER_FIELD_EDGE_GLOW_LEFT: Field? = null
        private var VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT: Field? = null

        private fun invalidateViewPagerFields() {
            if (VIEW_PAGER_FIELD_EDGE_GLOW_LEFT != null && VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT != null) {
                VIEW_PAGER_FIELD_EDGE_GLOW_LEFT!!.isAccessible = true
                VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT!!.isAccessible = true
                return
            }
            val cls = ATHUtil.inClassPath("android.support.v4.view.ViewPager")
            for (f in cls.declaredFields) {
                when (f.name) {
                    "mLeftEdge" -> {
                        f.isAccessible = true
                        VIEW_PAGER_FIELD_EDGE_GLOW_LEFT = f
                    }
                    "mRightEdge" -> {
                        f.isAccessible = true
                        VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT = f
                    }
                }
            }
        }

        // Setter methods

        fun setEdgeGlowColor(scrollView: ScrollView, @ColorInt color: Int) {
            invalidateScrollViewFields()
            try {
                var ee: Any
                ee = SCROLL_VIEW_FIELD_EDGE_GLOW_TOP!!.get(scrollView)
                setEffectColor(ee, color)
                ee = SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.get(scrollView)
                setEffectColor(ee, color)
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) ex.printStackTrace()
            }

        }

        fun setEdgeGlowColor(scrollView: NestedScrollView, @ColorInt color: Int) {
            invalidateNestedScrollViewFields()
            try {
                var ee: Any
                ee = NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP!!.get(scrollView)
                setEffectColor(ee, color)
                ee = NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.get(scrollView)
                setEffectColor(ee, color)
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) ex.printStackTrace()
            }

        }

        fun setEdgeGlowColor(listView: AbsListView, @ColorInt color: Int) {
            invalidateListViewFields()
            try {
                var ee: Any
                ee = LIST_VIEW_FIELD_EDGE_GLOW_TOP!!.get(listView)
                setEffectColor(ee, color)
                ee = LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.get(listView)
                setEffectColor(ee, color)
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) ex.printStackTrace()
            }

        }

        fun setEdgeGlowColor(scrollView: RecyclerView, @ColorInt color: Int, scrollListener: RecyclerView.OnScrollListener?) {
            var scrollListener = scrollListener
            invalidateRecyclerViewFields()
            invalidateRecyclerViewFields()
            if (scrollListener == null) {
                scrollListener = object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        EdgeGlowUtil.setEdgeGlowColor(recyclerView, color, this)
                    }
                }
                scrollView.addOnScrollListener(scrollListener)
            }
            try {
                var ee: Any
                ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP!!.get(scrollView)
                setEffectColor(ee, color)
                ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM!!.get(scrollView)
                setEffectColor(ee, color)
                ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT!!.get(scrollView)
                setEffectColor(ee, color)
                ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT!!.get(scrollView)
                setEffectColor(ee, color)
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) ex.printStackTrace()
            }

        }

        fun setEdgeGlowColor(pager: ViewPager, @ColorInt color: Int) {
            invalidateViewPagerFields()
            try {
                var ee: Any
                ee = VIEW_PAGER_FIELD_EDGE_GLOW_LEFT!!.get(pager)
                setEffectColor(ee, color)
                ee = VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT!!.get(pager)
                setEffectColor(ee, color)
            } catch (ex: Exception) {
                if (BuildConfig.DEBUG) ex.printStackTrace()
            }

        }

        // Utilities

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setEffectColor(edgeEffect: Any?, @ColorInt color: Int) {
            var edgeEffect = edgeEffect
            invalidateEdgeEffectFields()
            if (edgeEffect is EdgeEffectCompat) {
                // EdgeEffectCompat
                try {
                    EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT!!.isAccessible = true
                    edgeEffect = EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT!!.get(edgeEffect)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                    return
                }

            }
            if (edgeEffect == null)
                return
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // EdgeGlow
                try {
                    EDGE_GLOW_FIELD_EDGE!!.isAccessible = true
                    val mEdge = EDGE_GLOW_FIELD_EDGE!!.get(edgeEffect) as Drawable
                    EDGE_GLOW_FIELD_GLOW!!.isAccessible = true
                    val mGlow = EDGE_GLOW_FIELD_GLOW!!.get(edgeEffect) as Drawable
                    mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    mEdge.callback = null // free up any references
                    mGlow.callback = null // free up any references
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            } else {
                // EdgeEffect
                (edgeEffect as EdgeEffect).color = color
            }
        }
    }
}
