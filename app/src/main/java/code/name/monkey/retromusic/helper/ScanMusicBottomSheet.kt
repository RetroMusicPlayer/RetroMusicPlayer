package code.name.monkey.retromusic.helper

/**
 * @Author by Pinankh Patel
 * Created on Date = 13-05-2025  17:54
 * Github = https://github.com/Pinankh
 * LinkdIN = https://www.linkedin.com/in/pinankh-patel-19400350/
 * Stack Overflow = https://stackoverflow.com/users/4564376/pinankh
 * Medium = https://medium.com/@pinankhpatel
 * Email = pinankhpatel@gmail.com
 */
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.BottomSheetScanMusicBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.folder.ScanResult
import code.name.monkey.retromusic.fragments.folder.ScanViewModel

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import java.io.File

import kotlin.jvm.java
import kotlin.let

class ScanMusicBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetScanMusicBinding? = null
    private val binding get() = _binding!!

    private var targetFile: File? = null

    interface ScanMusicStartListener {
        fun onMusicScanStart(fileToScan: File)

    }

    var listener: ScanMusicStartListener? = null


    private lateinit var scanViewModel: ScanViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

        arguments?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                targetFile = it.getSerializable(ARG_TARGET_FILE, File::class.java)
            } else {
                @Suppress("DEPRECATION")
                targetFile = it.getSerializable(ARG_TARGET_FILE) as? File
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetScanMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        scanViewModel =
            ViewModelProvider(requireActivity())[ScanViewModel::class.java]

        scanViewModel.scanStatus.observe(viewLifecycleOwner) { result ->

            when (result) {
                is ScanResult.NotStarted -> {
                }
                is ScanResult.InProgress -> {
                    binding.tvLoading.text =
                        getString(R.string.scanning_folders_files)
                    binding.tvLoading.show()
                    binding.progressCircular.show()

                    binding.btnClose.visibility = View.GONE
                }
                is ScanResult.Path -> {
                    binding.tvPath.text = result.path
                }
                is ScanResult.Success -> {
                    binding.tvLoading.text = result.message
                    binding.progressCircular.hide()

                    binding.tvPath.hide()
                    binding.btnClose.show()
                    scanViewModel.resetScanStatus()
                }
            }
        }

        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as? BottomSheetDialog
            val bottomSheet = bottomSheetDialog?.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)

                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    val layoutParams = bottomSheet.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    bottomSheet.layoutParams = layoutParams
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                behavior.isDraggable = false
            }
        }


        binding.tvLoading.show()
        binding.progressCircular.accentColor()
        binding.progressCircular.hide()
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.tvPath.show()
        listener?.onMusicScanStart(targetFile!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listener = null

    }

    companion object {
        private const val ARG_TARGET_FILE = "arg_target_file"

        @JvmStatic
        fun newInstance(targetFile: File): ScanMusicBottomSheet {
            val fragment = ScanMusicBottomSheet()
            val args = Bundle()
            args.putSerializable(ARG_TARGET_FILE, targetFile)
            fragment.arguments = args
            return fragment
        }
    }
}