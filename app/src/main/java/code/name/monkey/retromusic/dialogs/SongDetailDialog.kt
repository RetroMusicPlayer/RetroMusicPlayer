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

package code.name.monkey.retromusic.dialogs

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import code.name.monkey.appthemehelper.ThemeStore

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_file_details.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.TagException
import java.io.File
import java.io.IOException

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

class SongDetailDialog : RoundedBottomSheetDialogFragment() {

    private fun setTextColor(view: ViewGroup) {
        view.forEach {
            if (it is TextView) {
                it.setTextColor(ThemeStore.textColorPrimary(context!!))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_file_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context!!

        setTextColor(view as ViewGroup)

        fileName.text = makeTextWithTitle(context, R.string.label_file_name, "-")
        filePath.text = makeTextWithTitle(context, R.string.label_file_path, "-")
        fileSize.text = makeTextWithTitle(context, R.string.label_file_size, "-")
        fileFormat.text = makeTextWithTitle(context, R.string.label_file_format, "-")
        trackLength.text = makeTextWithTitle(context, R.string.label_track_length, "-")
        bitrate.text = makeTextWithTitle(context, R.string.label_bit_rate, "-")
        samplingRate.text = makeTextWithTitle(context, R.string.label_sampling_rate, "-")

        val song = arguments!!.getParcelable<Song>("song")
        if (song != null) {
            val songFile = File(song.data)
            if (songFile.exists()) {
                fileName.text = makeTextWithTitle(context, R.string.label_file_name, songFile.name)
                filePath.text = makeTextWithTitle(context, R.string.label_file_path, songFile.absolutePath)
                fileSize.text = makeTextWithTitle(context, R.string.label_file_size, getFileSizeString(songFile.length()))
                try {
                    val audioFile = AudioFileIO.read(songFile)
                    val audioHeader = audioFile.audioHeader

                    fileFormat.text = makeTextWithTitle(context, R.string.label_file_format, audioHeader.format)
                    trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString((audioHeader.trackLength * 1000).toLong()))
                    bitrate.text = makeTextWithTitle(context, R.string.label_bit_rate, audioHeader.bitRate + " kb/s")
                    samplingRate.text = makeTextWithTitle(context, R.string.label_sampling_rate, audioHeader.sampleRate + " Hz")
                } catch (e: CannotReadException) {
                    Log.e(TAG, "error while reading the song file", e)
                    // fallback
                    trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration))
                } catch (e: IOException) {
                    Log.e(TAG, "error while reading the song file", e)
                    trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration))
                } catch (e: TagException) {
                    Log.e(TAG, "error while reading the song file", e)
                    trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration))
                } catch (e: ReadOnlyFileException) {
                    Log.e(TAG, "error while reading the song file", e)
                    trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration))
                } catch (e: InvalidAudioFrameException) {
                    Log.e(TAG, "error while reading the song file", e)
                    trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration))
                }

            } else {
                // fallback
                fileName.text = makeTextWithTitle(context, R.string.label_file_name, song.title)
                trackLength.text = makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration))
            }
        }
    }

    companion object {

        val TAG: String = SongDetailDialog::class.java.simpleName


        fun create(song: Song): SongDetailDialog {
            val dialog = SongDetailDialog()
            val args = Bundle()
            args.putParcelable("song", song)
            dialog.arguments = args
            return dialog
        }

        private fun makeTextWithTitle(context: Context, titleResId: Int, text: String?): Spanned {
            return Html.fromHtml("<b>" + context.resources.getString(titleResId) + ": " + "</b>" + text)
        }

        private fun getFileSizeString(sizeInBytes: Long): String {
            val fileSizeInKB = sizeInBytes / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            return "$fileSizeInMB MB"
        }
    }
}
