package code.name.monkey.retromusic.fragments.downloader

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "youtube_dl")
        val request = YoutubeDLRequest(binding.editText.text.toString())
        request.addOption("-o", dir.absolutePath + "/%(title)s.%(ext)s")
        request.addOption("-f", "bestaudio")
        request.addOption("--extract-audio")
        request.addOption("--audio-format", "mp3")

        YoutubeDL.getInstance().execute(request)
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