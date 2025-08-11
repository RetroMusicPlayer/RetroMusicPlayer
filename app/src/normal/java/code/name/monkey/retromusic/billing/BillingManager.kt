package code.name.monkey.retromusic.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.showToast
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(private val context: Context) {
    private var billingClient: BillingClient
    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle user cancellation
            Log.d(TAG, "Purchase canceled by user")
        } else {
            // Handle other error
            Log.e(TAG, "Purchase error: ${billingResult.debugMessage}")
        }
    }

    private val _isProVersion = MutableLiveData<Boolean>()
    val isProVersion: Boolean get() = _isProVersion.value ?: false

    init {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished successfully")
                    queryPurchases()
                } else {
                    Log.e(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Billing service disconnected")
            }
        })
    }

    private fun queryPurchases() {
        CoroutineScope(Dispatchers.IO).launch {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
            
            billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val hasProVersion = purchasesList.any { purchase ->
                        purchase.products.contains(Constants.PRO_VERSION_PRODUCT_ID) && 
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    }
                    
                    CoroutineScope(Dispatchers.Main).launch {
                        _isProVersion.value = hasProVersion
                    }
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Acknowledge the purchase if it hasn't been acknowledged yet
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                
                CoroutineScope(Dispatchers.IO).launch {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.d(TAG, "Purchase acknowledged")
                        }
                    }
                }
            }
            
            if (purchase.products.contains(Constants.PRO_VERSION_PRODUCT_ID)) {
                _isProVersion.value = true
                context.showToast(R.string.thank_you)
            }
        }
    }

    fun launchBillingFlow(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
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
                        val productDetailsParamsList = listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                        )

                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .build()

                        CoroutineScope(Dispatchers.Main).launch {
                            billingClient.launchBillingFlow(activity, billingFlowParams)
                        }
                    }
                }
            }
        }
    }

    fun restorePurchases() {
        context.showToast(R.string.restoring_purchase)
        queryPurchases()
    }

    fun release() {
        billingClient.endConnection()
    }

    companion object {
        private const val TAG = "BillingManager"
    }
}