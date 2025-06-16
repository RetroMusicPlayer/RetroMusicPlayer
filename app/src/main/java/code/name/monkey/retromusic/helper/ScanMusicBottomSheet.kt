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
    // The File object that will be passed
    private var targetFile: File? = null

    interface ScanMusicStartListener {
        fun onMusicScanStart(fileToScan: File)

    }

    var listener: ScanMusicStartListener? = null

    // In ScanMusicBottomSheet
    private lateinit var scanViewModel: ScanViewModel // Init with by activityViewModels() or by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

        // Retrieve the File from arguments
        arguments?.let {
            // Check for Serializable because File is Serializable
            // For API 33+ you can use getSerializable(String, Class<T>)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                targetFile = it.getSerializable(ARG_TARGET_FILE, File::class.java)
            } else {
                @Suppress("DEPRECATION") // Suppress for older APIs
                targetFile = it.getSerializable(ARG_TARGET_FILE) as? File
            }
        }

        if (targetFile == null) {
            Log.e("ScanMusicBottomSheet", "Error: Target file not provided or couldn't be deserialized.")
            // Handle the error, e.g., dismiss the dialog or show an error message
            // Toast.makeText(requireContext(), "Error: No scan target specified.", Toast.LENGTH_LONG).show()
            // dismissAllowingStateLoss()
            // return
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
        // It's important to do this in onViewCreated or later,
        // after the dialog's view hierarchy is established.

        scanViewModel =
            ViewModelProvider(requireActivity())[ScanViewModel::class.java] // Or appropriate scope

        // In your Fragment or Activity observing the ScanViewModel
// scanViewModel = ... (initialized)

        scanViewModel.scanStatus.observe(viewLifecycleOwner) { result ->
            Log.d("MyFragment", "Scan status changed: $result")
            when (result) {
                is ScanResult.NotStarted -> {
                    // Initial state or reset: Update UI accordingly
                    //binding.tvLoading.text = "Ready to scan"

                }
                is ScanResult.InProgress -> {
                    // Scan is running: Show animation, update text
                    binding.tvLoading.text =
                        getString(R.string.scanning_folders_files)
                    binding.tvLoading.show()
                    binding.animationView.show()
                    binding.animationView.playAnimation()
                    binding.btnStar.visibility = View.GONE
                    binding.btnClose.visibility = View.GONE
                }
                is ScanResult.Path -> {
                    binding.tvPath.text = buildString {
        append(getString(R.string.scanning_path))
        append(" ")
        append(result.path)
    }
                }
                is ScanResult.Success -> {
                    // Scan succeeded: Stop animation, show success message, items count
                    binding.tvLoading.text = buildString {
        append(result.message)
        append(" ( ")
                        append(getString(R.string.found))
        append(result.itemsScanned)
        append(")")
    }
                    binding.animationView.pauseAnimation()
                   // binding.animationView.visibility = View.GONE
                    binding.btnStar.visibility = View.GONE
                    binding.tvPath.hide()
                    binding.btnClose.show() // Show a close/done button
                    scanViewModel.resetScanStatus()
                    //Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
                is ScanResult.Error -> {
                    // Scan failed: Stop animation, show error message
                    binding.tvLoading.text = buildString {
        append(getString(R.string.scan_failed))
        append(result.errorMessage)
    }
                    binding.animationView.cancelAnimation()
                    binding.animationView.visibility = View.GONE
                    binding.btnStar.visibility = View.VISIBLE // Allow retry or show error
                    binding.btnClose.show()
                    //Toast.makeText(requireContext(), "Scan failed: ${result.errorMessage}", Toast.LENGTH_LONG).show()
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
                behavior.isDraggable = false // This prevents dragging (and thus swipe-to-dismiss)
            }
        }
        binding.btnStar.accentColor()
        binding.tvLoading.text = getString(R.string.ready_to_scan)
        binding.tvLoading.show()
        binding.btnStar.setOnClickListener {

            binding.tvPath.show()
            listener?.onMusicScanStart(targetFile!!)

        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listener = null

    }

    companion object {
        private const val ARG_TARGET_FILE = "arg_target_file"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param targetFile The file or directory to be scanned.
         * @return A new instance of fragment ScanMusicBottomSheet.
         */
        @JvmStatic // If you need to call this from Java
        fun newInstance(targetFile: File): ScanMusicBottomSheet {
            val fragment = ScanMusicBottomSheet()
            val args = Bundle()
            args.putSerializable(ARG_TARGET_FILE, targetFile) // File is Serializable
            fragment.arguments = args
            return fragment
        }
    }
}