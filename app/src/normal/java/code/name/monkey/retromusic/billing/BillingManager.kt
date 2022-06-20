package code.name.monkey.retromusic.billing

import android.content.Context
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.showToast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo

class BillingManager(context: Context) {
    private val billingProcessor: BillingProcessor

    init {
        // automatically restores purchases
        billingProcessor = BillingProcessor(
            context, BuildConfig.GOOGLE_PLAY_LICENSING_KEY,
            object : BillingProcessor.IBillingHandler {
                override fun onProductPurchased(productId: String, details: PurchaseInfo?) {}

                override fun onPurchaseHistoryRestored() {
                    context.showToast(R.string.restored_previous_purchase_please_restart)
                }

                override fun onBillingError(errorCode: Int, error: Throwable?) {}

                override fun onBillingInitialized() {}
            })
    }

    fun release() {
        billingProcessor.release()
    }

    val isProVersion: Boolean
        get() = billingProcessor.isPurchased(Constants.PRO_VERSION_PRODUCT_ID)
}