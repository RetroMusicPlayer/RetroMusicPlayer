package code.name.monkey.retromusic.misc

import android.content.Context
import androidx.loader.content.AsyncTaskLoader

/**
 * [Issue
 * 14944](http://code.google.com/p/android/issues/detail?id=14944)
 *
 * @author Alexander Blom
 */
abstract class WrappedAsyncTaskLoader<D>
/**
 * Constructor of `WrappedAsyncTaskLoader`
 *
 * @param context The [Context] to use.
 */
(context: Context) : AsyncTaskLoader<D>(context) {

    private var mData: D? = null

    /**
     * {@inheritDoc}
     */
    override fun deliverResult(data: D?) {
        if (!isReset) {
            this.mData = data
            super.deliverResult(data)
        } else {
            // An asynchronous query came in while the loader is stopped
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onStartLoading() {
        super.onStartLoading()
        if (this.mData != null) {
            deliverResult(this.mData)
        } else if (takeContentChanged() || this.mData == null) {
            forceLoad()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onStopLoading() {
        super.onStopLoading()
        // Attempt to cancel the current load task if possible
        cancelLoad()
    }

    /**
     * {@inheritDoc}
     */
    override fun onReset() {
        super.onReset()
        // Ensure the loader is stopped
        onStopLoading()
        this.mData = null
    }
}
