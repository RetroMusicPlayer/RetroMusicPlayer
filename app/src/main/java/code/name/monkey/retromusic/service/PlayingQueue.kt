package code.name.monkey.retromusic.service

import code.name.monkey.retromusic.helper.ShuffleHelper.makeShuffleList
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.Song.Companion.emptySong

class PlayingQueue(cb: QueueChangedCallback) {
    @JvmField
    var playingQueue: MutableList<Song> = ArrayList()

    @JvmField
    var position = -1
    var originalPlayingQueue: MutableList<Song> = ArrayList()

    private var shuffled = false

    val callback = cb

    val isLastTrack: Boolean
        get() = position == playingQueue.size - 1

    fun setQueue(
        playingQueue: List<Song>,
        startPosition: Int
    ) {
        // it is important to copy the playing queue here first as we might add/remove songs later
        originalPlayingQueue = ArrayList(playingQueue)
        this.playingQueue = ArrayList(originalPlayingQueue)
        position = startPosition
        callback.notifyQueueChanged()
    }

    fun size(): Int {
        return playingQueue.size
    }

    fun isEmpty(): Boolean {
        return playingQueue.isEmpty()
    }

    fun duration(fromPosition: Int): Long {
        var duration: Long = 0
        for (i in fromPosition + 1 until playingQueue.size) {
            duration += playingQueue[i].duration
        }
        return duration
    }

    fun shuffle() {
        makeShuffleList(playingQueue, position)
        position = 0
        shuffled = true
        callback.notifyQueueChanged()
    }

    fun unshuffle(currentSong: Song?) {
        playingQueue = ArrayList(originalPlayingQueue)
        var newPosition = 0
        if (currentSong != null) {
            for (song in playingQueue) {
                if (song.id == currentSong.id) {
                    newPosition = playingQueue.indexOf(song)
                    break;
                }
            }
        }
        position = newPosition
        shuffled = false
        callback.notifyQueueChanged()
    }

    fun getSongAt(position: Int): Song {
        return if ((position >= 0) && (position < playingQueue.size)) {
            playingQueue[position]
        } else {
            emptySong
        }
    }

    fun addSong(position: Int, song: Song) {
        playingQueue.add(position, song)
        originalPlayingQueue.add(position, song)
        callback.notifyQueueChanged()
    }

    fun addSong(song: Song) {
        playingQueue.add(song)
        originalPlayingQueue.add(song)
        callback.notifyQueueChanged()
    }

    fun addSongs(position: Int, songs: List<Song>?) {
        playingQueue.addAll(position, songs!!)
        originalPlayingQueue.addAll(position, songs)
        callback.notifyQueueChanged()
    }

    fun addSongs(songs: List<Song>?) {
        playingQueue.addAll(songs!!)
        originalPlayingQueue.addAll(songs)
        callback.notifyQueueChanged()
    }

    fun moveSong(from: Int, to: Int) {
        if (from == to) {
            return
        }
        val currentPosition = position
        val songToMove = playingQueue.removeAt(from)
        playingQueue.add(to, songToMove)
        if (!shuffled) {
            val tmpSong = originalPlayingQueue.removeAt(from)
            originalPlayingQueue.add(to, tmpSong)
        }
        when {
            currentPosition in to until from -> {
                position = currentPosition + 1
            }

            currentPosition in (from + 1)..to -> {
                position = currentPosition - 1
            }

            from == currentPosition -> {
                position = to
            }
        }
        callback.notifyQueueChanged()
    }

    fun removeSong(position: Int) {
        if (shuffled) {
            originalPlayingQueue.remove(playingQueue.removeAt(position))
        } else {
            playingQueue.removeAt(position)
            originalPlayingQueue.removeAt(position)
        }
        rePosition(position)
        callback.notifyQueueChanged()
    }

    fun removeSongImpl(song: Song) {
        val deletePosition = playingQueue.indexOf(song)
        if (deletePosition != -1) {
            playingQueue.removeAt(deletePosition)
            rePosition(deletePosition)
        }

        val originalDeletePosition = originalPlayingQueue.indexOf(song)
        if (originalDeletePosition != -1) {
            originalPlayingQueue.removeAt(originalDeletePosition)
            rePosition(originalDeletePosition)
        }
        callback.notifyQueueChanged()
    }

    fun clear() {
        playingQueue.clear()
        originalPlayingQueue.clear()
        position = -1
        callback.notifyQueueChanged()
    }

    private fun rePosition(deletedPosition: Int) {
        val currentPosition = position
        if (deletedPosition < currentPosition) {
            position = currentPosition - 1
        } else if (deletedPosition == currentPosition) {
            if (playingQueue.size <= deletedPosition) {
                position = position - 1
            }
        }
    }

    fun restore(
        restoredOriginalQueue: MutableList<Song>,
        restoredQueue: MutableList<Song>,
        restoredPosition: Int
    ) {
        originalPlayingQueue = restoredOriginalQueue
        playingQueue = restoredQueue
        position = restoredPosition
    }

    interface QueueChangedCallback {
        fun notifyQueueChanged()
    }
}