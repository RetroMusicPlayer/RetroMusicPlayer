package code.name.monkey.retromusic.extensions

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import code.name.monkey.retromusic.util.ArtistSeparator

/**
 * Extension function to make artist names clickable in a TextView.
 * 
 * Splits the artist string using ArtistSeparator and creates individual
 * clickable spans for each artist name.
 * 
 * Example:
 * ```
 * textView.setArtistLinks("Artist1 / Artist2 feat. Artist3") { artistName ->
 *     // Handle click on artistName
 * }
 * ```
 * 
 * @param artistName The artist string (e.g., "Artist1 / Artist2 feat. Artist3")
 * @param onArtistClick Callback invoked when an artist name is clicked
 */
fun TextView.setArtistLinks(artistName: String?, onArtistClick: (artist: String) -> Unit) {
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT

    if (artistName.isNullOrEmpty()) {
        this.text = ""
        return
    }

    val artistNames = ArtistSeparator.split(artistName)
    if (artistNames.isEmpty()) {
        this.text = artistName
        return
    }

    val originalTextColor = this.currentTextColor
    
    val builder = SpannableStringBuilder()
    val separator = ", "

    artistNames.forEachIndexed { index, name ->
        val startIndex = builder.length
        builder.append(name)
        val endIndex = builder.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.cancelPendingInputEvents()
                onArtistClick(name)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = originalTextColor
                ds.isUnderlineText = false
            }
        }

        builder.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        if (index < artistNames.size - 1) {
            builder.append(separator)
        }
    }

    this.text = builder
}