package code.name.monkey.retromusic.ui.fragments.base

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.util.PreferenceUtil
import kotlinx.android.synthetic.main.volume_controls.*

/**
 * Created by hemanths on 24/09/17.
 */

abstract class AbsPlayerControlsFragment : AbsMusicServiceFragment(), MusicProgressViewUpdateHelper.Callback {

    protected abstract fun show()

    protected abstract fun hide()

    protected abstract fun updateShuffleState()

    protected abstract fun updateRepeatState()

    protected abstract fun setUpProgressSlider()

    abstract fun setDark(color: Int)

    fun showBonceAnimation(view: View) {
        view.apply {
            clearAnimation()
            scaleX = 0.9f
            scaleY = 0.9f
            visibility = View.VISIBLE
            pivotX = (view.width / 2).toFloat()
            pivotY = (view.height / 2).toFloat()

            animate().setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .withEndAction {
                        animate().setDuration(200)
                                .setInterpolator(AccelerateInterpolator())
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .start()
                    }
                    .start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideVolumeIfAvailable()
    }

    private fun hideVolumeIfAvailable() {
        volumeFragmentContainer.visibility = if (PreferenceUtil.getInstance().volumeToggle) View.VISIBLE else View.GONE
    }
}
