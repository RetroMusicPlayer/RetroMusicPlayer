package io.github.muntashirakon.music.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.muntashirakon.music.repository.RealRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val realRepository: RealRepository) : ViewModel() {
    private val results = MutableLiveData<MutableList<Any>>()

    fun getSearchResult(): LiveData<MutableList<Any>> = results

    fun search(query: String?) = viewModelScope.launch(IO) {
        val result = realRepository.search(query)
        withContext(Main) { results.postValue(result) }
    }
}