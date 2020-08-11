package code.name.monkey.retromusic.fragments.library

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.findNavController
import code.name.monkey.retromusic.fragments.MainActivityFragment
import code.name.monkey.retromusic.fragments.folder.FoldersFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_library.*

class LibraryFragment : MainActivityFragment(R.layout.fragment_library) {
    private val navOptions by lazy {
        navOptions {
            launchSingleTop = true
            anim {
                enter = R.anim.retro_fragment_open_enter
                exit = R.anim.retro_fragment_open_exit
                popEnter = R.anim.retro_fragment_close_enter
                popExit = R.anim.retro_fragment_close_exit
            }
        }
    }
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.libraryFragment,
                R.id.settingsFragment,
                R.id.searchFragment
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.setBottomBarVisibility(View.VISIBLE)
        mainActivity.setSupportActionBar(toolbar)
        setupNavigationController()
    }

    private fun setupNavigationController() {
        val navController = findNavController(R.id.fragment_container)
        val navOptions = navOptions {
            launchSingleTop = true
            anim {
                enter = R.anim.retro_fragment_open_enter
                exit = R.anim.retro_fragment_open_exit
                popEnter = R.anim.retro_fragment_close_enter
                popExit = R.anim.retro_fragment_close_exit
            }
            popUpTo(navController.graph.startDestination) {
                inclusive = true
            }
        }
        mainActivity.getBottomNavigationView().setOnNavigationItemSelectedListener {
            var handled = false
            if (navController.graph.findNode(it.itemId) != null) {
                navController.navigate(it.itemId, null, navOptions)
                handled = true
            }
            when (it.itemId) {
                R.id.action_folder -> navController.navigate(
                    R.id.action_folder,
                    Bundle().apply {
                        putSerializable(
                            FoldersFragment.PATH,
                            PreferenceUtil.startDirectory
                        )
                    },
                    navOptions
                )
            }
            return@setOnNavigationItemSelectedListener handled
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            toolbar,
            menu,
            getToolbarBackgroundColor(toolbar)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> findNavController().navigate(
                R.id.searchFragment,
                null,
                navOptions
            )
            R.id.action_settings -> findNavController().navigate(
                R.id.settingsFragment,
                null,
                navOptions
            )
        }
        return super.onOptionsItemSelected(item)
    }

    fun addOnAppBarOffsetChangedListener(changedListener: AppBarLayout.OnOffsetChangedListener) {
        appBarLayout.addOnOffsetChangedListener(changedListener)
    }

    fun removeOnAppBarOffsetChangedListener(changedListener: AppBarLayout.OnOffsetChangedListener) {
        appBarLayout.removeOnOffsetChangedListener(changedListener)
    }

    fun getTotalAppBarScrollingRange(): Int {
        return 0
    }
}