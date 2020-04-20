package code.name.monkey.retromusic

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import code.name.monkey.retromusic.fragments.player.normal.PlayerFragment
import dev.olog.scrollhelper.ScrollHelper

class RetroScrollHelper(
    private val activity: FragmentActivity
) : ScrollHelper(activity,
    true,
    false // TODO when true, scrolls both bottomsheet and bottom navigation
) {

    private val skipFragment = listOf(
        PlayerFragment::class.java.name
    )

    // TODO every fragment has to have it's unique tag in order to work correctly
    //   here you can decide what fragment will be processed by the library
    //   probably you want to skip player fragments, ecc ..
    override fun shouldSkipFragment(fragment: Fragment): Boolean {
        return fragment::class.java.name in skipFragment
    }

    override fun findBottomNavigation(): View? {
        return activity.findViewById(R.id.bottomNavigationView)
    }

    override fun findBottomSheet(): View? {
        return activity.findViewById(R.id.slidingPanel)
    }

    override fun findFab(fragment: Fragment): View? {
        return null
    }

    override fun findRecyclerView(fragment: Fragment): RecyclerView? {
        return fragment.requireView().findViewById(R.id.recyclerView)
    }

    override fun findToolbar(fragment: Fragment): View? {
        return fragment.requireActivity().findViewById(R.id.toolbarContainer)
    }

    override fun findTabLayout(fragment: Fragment): View? {
        return null
    }

    override fun findViewPager(fragment: Fragment): ViewPager2? {
        return null
    }

    // TODO override this if you want to apply custom padding
    override fun updateRecyclerViewPadding(
        fragment: Fragment,
        recyclerView: RecyclerView,
        topPadding: Int,
        bottomPadding: Int
    ) {
        super.updateRecyclerViewPadding(fragment, recyclerView, topPadding, bottomPadding)
    }

}