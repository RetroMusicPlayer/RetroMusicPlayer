package code.name.monkey.retromusic.fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentListeningStatsBinding
import code.name.monkey.retromusic.extensions.applyToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ListeningStatsFragment : Fragment() {

    private var _binding: FragmentListeningStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ListeningStatsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeningStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyToolbar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = ListeningEventAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.allTime.observe(viewLifecycleOwner) { stats ->
            binding.totalTime.text = formatDuration(stats.totalMs)
            binding.playsAndSongs.text =
                getString(R.string.listening_plays_songs, stats.plays, stats.songs)
        }
        viewModel.lastSevenDays.observe(viewLifecycleOwner) { stats ->
            binding.last7Days.text =
                getString(R.string.listening_last_7_days, formatDuration(stats.totalMs))
        }
        viewModel.recent.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            binding.emptyView.isVisible = events.isEmpty()
        }
    }

    private fun formatDuration(ms: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(ms)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
        return getString(R.string.listening_duration_format, hours, minutes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
