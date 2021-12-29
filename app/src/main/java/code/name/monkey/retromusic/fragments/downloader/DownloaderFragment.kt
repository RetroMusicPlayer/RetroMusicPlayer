package code.name.monkey.retromusic.fragments.downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentDownloaderBinding
import code.name.monkey.retromusic.databinding.FragmentUserInfoBinding
import code.name.monkey.retromusic.extensions.applyToolbar
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
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
        /*val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "youtube_dl")
        val request = YoutubeDLRequest(binding.editText.text.toString())
        request.addOption("-o", dir.absolutePath + "/%(title)s.%(ext)s")
        request.addOption("-f", "bestaudio")
        request.addOption("--extract-audio")
        request.addOption("--audio-format", "mp3")

        YoutubeDL.getInstance().execute(request)*/

        //val notifyManager: NotificationManager = activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("Ytdl", "Retro Music", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Downloading"
            }
        }
        val builder = NotificationCompat.Builder(activity!!.applicationContext, "ytdl")
        builder
            .setContentTitle("Downloading")
            .setSmallIcon(R.drawable.ic_download_music)

        NotificationManagerCompat.from(activity!!.applicationContext).apply {
            builder.setProgress(100, 0, false)
            notify(1, builder.build())
            model.downloadVideo(link = binding.editText.text.toString(), builder = builder)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloaderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyToolbar(binding.toolbar)

        binding.downloadButton.setOnClickListener {
            onClick()
        }
    }

}