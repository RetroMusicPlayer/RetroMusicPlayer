/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.fragments.queue

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import kotlinx.android.synthetic.main.activity_playing_queue.*

/**
 * Created by hemanths on 2019-12-08.
 */
class PlayingQueueFragment : AbsRecyclerViewFragment<PlayingQueueAdapter, LinearLayoutManager>() {

    private lateinit var wrappedAdapter: RecyclerView.Adapter<*>
    private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null
    private var recyclerViewSwipeManager: RecyclerViewSwipeManager? = null
    private var recyclerViewTouchActionGuardManager: RecyclerViewTouchActionGuardManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerViewTouchActionGuardManager = RecyclerViewTouchActionGuardManager()
        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewSwipeManager = RecyclerViewSwipeManager()

        val animator = DraggableItemAnimator()
        animator.supportsChangeAnimations = false
        wrappedAdapter =
            recyclerViewDragDropManager?.createWrappedAdapter(adapter!!) as RecyclerView.Adapter<*>
        wrappedAdapter =
            recyclerViewSwipeManager?.createWrappedAdapter(wrappedAdapter) as RecyclerView.Adapter<*>
        recyclerView().layoutManager = layoutManager
        recyclerView().adapter = wrappedAdapter
        recyclerView().itemAnimator = animator
        recyclerViewTouchActionGuardManager?.attachRecyclerView(recyclerView)
        recyclerViewDragDropManager?.attachRecyclerView(recyclerView)
        recyclerViewSwipeManager?.attachRecyclerView(recyclerView)

        layoutManager?.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    override fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(requireContext())
    }

    override fun createAdapter(): PlayingQueueAdapter {
        return PlayingQueueAdapter(
            requireActivity() as AppCompatActivity,
            MusicPlayerRemote.playingQueue.toMutableList(),
            MusicPlayerRemote.position,
            R.layout.item_queue
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateQueue()
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        updateQueue()
    }

    override fun onPlayingMetaChanged() {
        updateQueuePosition()
    }

    private fun updateQueuePosition() {
        adapter?.setCurrent(MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun updateQueue() {
        adapter?.swapDataSet(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun resetToCurrentPosition() {
        recyclerView.stopScroll()
        layoutManager?.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    override fun onPause() {
        recyclerViewDragDropManager?.cancelDrag()
        super.onPause()
    }

    override val emptyMessage: Int
        get() = R.string.no_playing_queue

    override fun onDestroyView() {
        super.onDestroyView()
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager?.release()
            recyclerViewDragDropManager = null
        }

        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager?.release()
            recyclerViewSwipeManager = null
        }

        WrapperAdapterUtils.releaseAll(wrappedAdapter)
    }

    companion object {
        @JvmField
        val TAG: String = PlayingQueueFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): PlayingQueueFragment {
            return PlayingQueueFragment()
        }
    }
}
