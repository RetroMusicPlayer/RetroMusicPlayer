package code.name.monkey.retromusic.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.repository.RealRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchViewModel(private val realRepository: RealRepository) : ViewModel() {
    private val results = MutableLiveData<MutableList<Any>>()

    fun getSearchResult(): LiveData<MutableList<Any>> = results

    fun search(query: String?) = viewModelScope.launch(IO) {
        val result = realRepository.search(query)
        results.value = result
    }
}