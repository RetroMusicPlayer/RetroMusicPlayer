package code.name.monkey.core.dagger.songs

import code.name.monkey.core.DataManager
import code.name.monkey.core.Presenter
import javax.inject.Inject

interface SongsPresenter : Presenter<SongView> {
    fun loadSongs()

    class SongsPresenterImpl @Inject constructor(private val dataManager: DataManager)
}

interface SongView {
    fun songs()
}
