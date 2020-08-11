package code.name.monkey.retromusic.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.findNavController
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : AbsMainActivityFragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.hideBottomNavigation()
        mainActivity.setBottomBarVisibility(View.GONE)
        val navController: NavController = findNavController(R.id.contentFrame)
        navController.addOnDestinationChangedListener { _, _, _ ->
            toolbar.title = navController.currentDestination?.label
        }
    }
}