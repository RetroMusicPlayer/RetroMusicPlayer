package code.name.monkey.retromusic.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.showToast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * BillingManager handles all Google Play Billing operations for the RetroMusicPlayer app.
 * This class manages in-app purchases for the Pro version upgrade, using Google Play Billing Library 7.0.0.
 *
 * Key Features:
 * - Connects to Google Play Billing service
 * - Handles purchase flows for Pro version upgrade
 * - Manages purchase verification and acknowledgment (required by Google Play policy)
 * - Restores previous purchases across devices
 * - Provides proper callback mechanisms for UI state management
 *
 * @param context Application context for billing operations
 */
class BillingManager(private val context: Context) : PurchasesUpdatedListener {
    // Initialize billing client with purchase listener and enable pending purchases
    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
        )
        .build()

    init {
        startConnection {}
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase?>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase != null) handlePurchase(purchase)
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "Purchase canceled by user")
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.d(TAG, "Item already owned - refreshing purchase status")
                queryPurchases()
            }

            else -> {
                Log.e(TAG, "Purchase error: ${billingResult.debugMessage}")
            }
        }
    }

    private val _isProVersion = MutableLiveData<Boolean>()

    val isProVersion: Boolean get() = _isProVersion.value ?: false

    /**
     * Establishes connection to Google Play Billing service.
     * This must complete successfully before any billing operations can be performed.
     * Uses proper callback mechanism instead of arbitrary delays.
     */
    fun startConnection(onConnected: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished successfully - billing client ready")
                    queryPurchases()
                    onConnected()
                } else {
                    Log.e(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                    CoroutineScope(Dispatchers.Main).launch {
                        context.showToast(R.string.billing_error)
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Billing service disconnected - will auto-reconnect when needed")
            }
        })
    }

    /**
     * Queries all existing purchases to determine current Pro version ownership status.
     */
    private fun queryPurchases(callback: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    CoroutineScope(Dispatchers.Main).launch {
                        purchasesList.forEach { purchase -> handlePurchase(purchase) }
                        callback?.invoke()
                    }
                } else {
                    Log.e(TAG, "Failed to query purchases: ${billingResult.debugMessage}")
                    CoroutineScope(Dispatchers.Main).launch {
                        context.showToast(R.string.billing_error)
                    }
                }
            }
        }
    }

    /**
     * Processes a completed purchase from Google Play.
     * Handles purchase acknowledgment (required by Google Play policy) and updates app state.
     *
     * Important: All purchases must be acknowledged within 3 days or they will be refunded.
     *
     * @param purchase The purchase object received from Google Play
     */
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Acknowledge the purchase if not already acknowledged
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                CoroutineScope(Dispatchers.IO).launch {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.d(TAG, "Purchase acknowledged successfully")
                        } else {
                            Log.e(
                                TAG,
                                "Failed to acknowledge purchase: ${billingResult.debugMessage}"
                            )
                        }
                    }
                }
            }

            // Update Pro version status if this is the Pro version purchase
        if (purchase.products.contains(Constants.PRO_VERSION_PRODUCT_ID) && !isProVersion) {
                _isProVersion.value = true
                context.showToast(R.string.thank_you)
                Log.d(TAG, "Pro version activated successfully")
            }
        } else {
            Log.d(TAG, "Purchase state: ${purchase.purchaseState}")
        }
    }

    /**
     * Initiates the purchase flow for the Pro version upgrade.
     *
     * Flow:
     * 1. Query product details from Google Play
     * 2. Build billing flow parameters
     * 3. Launch Google Play billing UI
     * 4. Handle result in purchaseUpdateListener
     *
     * @param activity The activity that will host the billing flow UI
     */
    fun launchBillingFlow(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            // First, query product details for the Pro version from Google Play
            val productDetailsParams = QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constants.PRO_VERSION_PRODUCT_ID)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(listOf(productDetailsParams))
                .build()

            billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val productDetails = productDetailsList.firstOrNull()
                    if (productDetails != null) {
                        // Build billing flow parameters with valid product details
                        val productDetailsParamsList = listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                        )

                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .build()

                        // Launch billing flow on main thread (UI operation)
                        CoroutineScope(Dispatchers.Main).launch {
                            val result =
                                billingClient.launchBillingFlow(activity, billingFlowParams)
                            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                                Log.e(TAG, "Failed to launch billing flow: ${result.debugMessage}")
                            } else {
                                Log.d(TAG, "Billing flow launched successfully")
                            }
                        }
                    } else {
                        Log.e(
                            TAG,
                            "Product details not found for Pro version - check product ID in Play Console"
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            context.showToast(R.string.billing_error)
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to query product details: ${billingResult.debugMessage}")
                }
            }
        }
    }

    /**
     * Restores previous purchases by re-querying Google Play for owned items.
     */
    fun restorePurchases(callback: () -> Unit) {
        context.showToast(R.string.restoring_purchase)
        Log.d(TAG, "Restoring purchases...")
        queryPurchases {
            Log.d(TAG, "Restored purchases")
            callback()
        }
    }

    /**
     * Disconnects from Google Play Billing service and cleans up resources.
     * Should be called when the billing manager is no longer needed (e.g., app shutdown).
     */
    fun release() {
        if (billingClient.isReady) {
            billingClient.endConnection()
            Log.d(TAG, "Billing client connection ended")
        }
    }

    companion object {
        private const val TAG = "BillingManager"
    }
}