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
package code.name.monkey.retromusic.glide.audiocover

/** @author Karim Abou Zeid (kabouzeid)
 */
/**
 * Model class representing an audio file for cover art extraction.
 * 
 * This class serves as a Glide model for loading individual song artwork
 * from audio files. It implements proper equality and hashing for efficient
 * caching and comparison operations.
 * 
 * @param filePath Absolute path to the audio file
 * 
 * @author Karim Abou Zeid (kabouzeid)
 */
class AudioFileCover(val filePath: String) {
    
    init {
        require(filePath.isNotBlank()) { "File path cannot be blank" }
    }
    
    override fun hashCode(): Int {
        return filePath.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AudioFileCover) return false
        return filePath == other.filePath
    }
    
    override fun toString(): String {
        return "AudioFileCover(filePath='$filePath')"
    }
}