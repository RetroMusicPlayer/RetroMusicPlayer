package code.name.monkey.retromusic.adapter.album

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import code.name.monkey.retromusic.activities.LyricsActivity
import code.name.monkey.retromusic.fragments.AlbumCoverStyle
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.misc.CustomFragmentStatePagerAdapter
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import java.util.*


class AlbumCoverPagerAdapter(fm: FragmentManager, private val dataSet: ArrayList<Song>) : CustomFragmentStatePagerAdapter(fm) {

    private var currentColorReceiver: AlbumCoverFragment.ColorReceiver? = null
    private var currentColorReceiverPosition = -1

    override fun getItem(position: Int): Fragment {
        return AlbumCoverFragment.newInstance(dataSet[position])
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        if (currentColorReceiver != null && currentColorReceiverPosition == position) {
            receiveColor(currentColorReceiver!!, currentColorReceiverPosition)
        }
        return o
    }

    /**
     * Only the latest passed [AlbumCoverFragment.ColorReceiver] is guaranteed to receive a
     * response
     */
    fun receiveColor(colorReceiver: AlbumCoverFragment.ColorReceiver, position: Int) {

        if (getFragment(position) is AlbumCoverFragment) {
            val fragment = getFragment(position) as AlbumCoverFragment
            currentColorReceiver = null
            currentColorReceiverPosition = -1
            fragment.receiveColor(colorReceiver, position)
        } else {
            currentColorReceiver = colorReceiver
            currentColorReceiverPosition = position
        }
    }

    class AlbumCoverFragment : Fragment() {

        lateinit var albumCover: ImageView
        private var isColorReady: Boolean = false
        private var color: Int = 0
        private lateinit var song: Song
        private var colorReceiver: ColorReceiver? = null
        private var request: Int = 0

        private val layout: Int
            get() {
                return when (PreferenceUtil.getInstance(activity).albumCoverStyle) {
                    AlbumCoverStyle.NORMAL -> code.name.monkey.retromusic.R.layout.fragment_album_cover
                    AlbumCoverStyle.FLAT -> code.name.monkey.retromusic.R.layout.fragment_album_flat_cover
                    AlbumCoverStyle.CIRCLE -> code.name.monkey.retromusic.R.layout.fragment_album_circle_cover
                    AlbumCoverStyle.CARD -> code.name.monkey.retromusic.R.layout.fragment_album_card_cover
                    AlbumCoverStyle.MATERIAL -> code.name.monkey.retromusic.R.layout.fragment_album_material_cover
                    AlbumCoverStyle.FULL -> code.name.monkey.retromusic.R.layout.fragment_album_full_cover
                    AlbumCoverStyle.FULL_CARD -> code.name.monkey.retromusic.R.layout.fragment_album_full_card_cover
                    else -> code.name.monkey.retromusic.R.layout.fragment_album_cover
                }
            }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (arguments != null) {
                song = arguments!!.getParcelable(SONG_ARG)!!
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val finalLayout = when {
                PreferenceUtil.getInstance(activity).carouselEffect() -> code.name.monkey.retromusic.R.layout.fragment_album_carousel_cover
                else -> layout
            }
            val view = inflater.inflate(finalLayout, container, false)
            albumCover = view.findViewById(code.name.monkey.retromusic.R.id.player_image)
            albumCover.setOnClickListener { startActivity(Intent(context, LyricsActivity::class.java)) }
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            loadAlbumCover()
        }


        override fun onDestroyView() {
            super.onDestroyView()
            colorReceiver = null
        }

        private fun loadAlbumCover() {
            SongGlideRequest.Builder.from(Glide.with(requireContext()), song)
                    .checkIgnoreMediaStore(activity)
                    .generatePalette(activity).build()
                    .into(object : RetroMusicColoredTarget(albumCover) {
                        override fun onColorReady(color: Int) {
                            setColor(color)
                        }
                    })
        }

        private fun setColor(color: Int) {
            this.color = color
            isColorReady = true
            if (colorReceiver != null) {
                colorReceiver!!.onColorReady(color, request)
                colorReceiver = null
            }
        }

        internal fun receiveColor(colorReceiver: ColorReceiver, request: Int) {
            if (isColorReady) {
                colorReceiver.onColorReady(color, request)
            } else {
                this.colorReceiver = colorReceiver
                this.request = request
            }
        }

        interface ColorReceiver {
            fun onColorReady(color: Int, request: Int)
        }

        companion object {

            private const val SONG_ARG = "song"

            fun newInstance(song: Song): AlbumCoverFragment {
                val frag = AlbumCoverFragment()
                val args = Bundle()
                args.putParcelable(SONG_ARG, song)
                frag.arguments = args
                return frag
            }
        }
    }

    companion object {
        val TAG: String = AlbumCoverPagerAdapter::class.java.simpleName
    }
}

