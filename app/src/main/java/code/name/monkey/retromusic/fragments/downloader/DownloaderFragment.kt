package code.name.monkey.retromusic.fragments.downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.tageditor.AbsTagEditorActivity
import code.name.monkey.retromusic.activities.tageditor.SongTagEditorActivity
import code.name.monkey.retromusic.adapter.YTSearchAdapter
import code.name.monkey.retromusic.databinding.FragmentDownloaderBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.applyToolbar
import code.name.monkey.retromusic.extensions.elevatedAccentColor
import code.name.monkey.retromusic.util.MediaStoreUtil
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import java.io.File

class DownloaderFragment : Fragment() {
    private var _binding: FragmentDownloaderBinding? = null
    private val binding get() = _binding!!

    private val model: DownloaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            YoutubeDL.getInstance().init(activity!!.applicationContext)
            FFmpeg.getInstance().init(activity!!.applicationContext)
        } catch (e: YoutubeDLException) {
            Log.e("Youtube", "Error")
        }
    }

    fun onClick() {
        model.searchVideos(binding.searchView.text.toString())
    }

    fun download(link: String): Unit {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Retro Music",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Downloading"
            }
        }
        val builder = NotificationCompat.Builder(activity!!.applicationContext, NOTIFICATION_CHANNEL_ID)
        builder
            .setContentTitle("Downloading")
            .setSmallIcon(R.drawable.ic_download_music)

        Log.d("Downloader", MediaStore.Audio.Media.getContentUri("external").path!!)

        model.downloadVideo(link = link, builder = builder, context = context!!, id = id)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloaderBinding.inflate(layoutInflater)
        binding.progressBar.elevatedAccentColor()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyToolbar(binding.toolbar)

        binding.downloadButton.setOnClickListener {
            binding.downloadButton.isEnabled = false
            download(binding.searchView.text.toString())
        }
        model.progress.observeForever {
            binding.progressBar.progress = it
        }
        //binding.searchResults.adapter =
        //    model.results?.let { YTSearchAdapter(data = it, onClick = {download(it)}) }
        model.songInfo.observeForever {
            val tagEditorIntent = Intent(activity, SongTagEditorActivity::class.java)
            tagEditorIntent.putExtra(AbsTagEditorActivity.TITLE_ID, it.title)
            tagEditorIntent.putExtra(AbsTagEditorActivity.AUTHOR_ID, it.author)
            tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_ID, getSongId(it.path))
            activity?.startActivity(tagEditorIntent)
            MediaStoreUtil.addToMediaStore(File(it.path), context!!)
            binding.downloadButton.isEnabled = true
        }
    }



    private fun getSongId(path: String): Long {
        val cr = context!!.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection: String = MediaStore.Audio.Media.DATA
        val selArgs = arrayOf(path)
        val projection = arrayOf(MediaStore.Audio.Media._ID)

        val values = ContentValues()
        values.put(MediaStore.Audio.Media.DATA, path)

        val cursor = cr.query(uri, projection, "$selection=?", selArgs, null)
        var songId: Long = 0
        if (cursor != null && cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            songId = cursor.getString(idIndex).toLong()
        }
        return songId
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID: String = "yt_downloader"
    }

}