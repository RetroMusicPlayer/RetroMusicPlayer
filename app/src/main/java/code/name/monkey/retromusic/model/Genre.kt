package code.name.monkey.retromusic.model

import java.io.Serializable

/**
 * @author Hemanth S (h4h13).
 */

class Genre : Serializable {
    val id: Int
    val name: String?
    val songCount: Int

    constructor(id: Int, name: String, songCount: Int) {
        this.id = id
        this.name = name
        this.songCount = songCount
    }

    // For unknown genre
    constructor(name: String, songCount: Int) {
        this.id = -1
        this.name = name
        this.songCount = songCount
    }
}