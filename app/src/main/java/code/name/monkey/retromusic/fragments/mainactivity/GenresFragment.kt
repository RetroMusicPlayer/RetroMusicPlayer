/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.GenreAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewFragment
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.mvp.presenter.GenresPresenter
import code.name.monkey.retromusic.mvp.presenter.GenresView
import javax.inject.Inject


class GenresFragment : AbsLibraryPagerRecyclerViewFragment<GenreAdapter, LinearLayoutManager>(), GenresView {
    override fun genres(genres: ArrayList<Genre>) {
        adapter?.swapDataSet(genres)
    }

    override fun showEmptyView() {

    }

    override fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    override fun createAdapter(): GenreAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return GenreAdapter(libraryFragment.mainActivity, dataSet, R.layout.item_list_no_image)
    }

    override val emptyMessage: Int
        get() = R.string.no_genres


    @Inject
    lateinit var genresPresenter: GenresPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genresPresenter.attachView(this)
    }
    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isEmpty()) {
            genresPresenter.loadGenres()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        genresPresenter.detachView()
    }

    override fun onMediaStoreChanged() {
        genresPresenter.loadGenres()
    }

    companion object {
        @JvmField
        val TAG: String = GenresFragment::class.java.simpleName

        fun newInstance(): GenresFragment {
            return GenresFragment()
        }
    }
}