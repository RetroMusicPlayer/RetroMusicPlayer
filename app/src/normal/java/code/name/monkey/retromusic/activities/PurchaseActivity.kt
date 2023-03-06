package code.name.monkey.retromusic.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsThemeActivity
import code.name.monkey.retromusic.databinding.ActivityProVersionBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.setLightStatusBar
import code.name.monkey.retromusic.extensions.setStatusBarColor
import code.name.monkey.retromusic.extensions.showToast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo

class PurchaseActivity : AbsThemeActivity(), BillingProcessor.IBillingHandler {

    private lateinit var binding: ActivityProVersionBinding
    private lateinit var billingProcessor: BillingProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProVersionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(Color.TRANSPARENT)
        setLightStatusBar(false)
        binding.toolbar.navigationIcon?.setTint(Color.WHITE)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.restoreButton.isEnabled = false
        binding.purchaseButton.isEnabled = false

        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY, this)

        MaterialUtil.setTint(binding.purchaseButton, true)

        binding.restoreButton.setOnClickListener {
            restorePurchase()
        }
        binding.purchaseButton.setOnClickListener {
            billingProcessor.purchase(this@PurchaseActivity, Constants.PRO_VERSION_PRODUCT_ID)
        }
        binding.bannerContainer.backgroundTintList =
            ColorStateList.valueOf(accentColor())
    }

    private fun restorePurchase() {
        showToast(R.string.restoring_purchase)
        billingProcessor.loadOwnedPurchasesFromGoogleAsync(object :
            BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
                onPurchaseHistoryRestored()
            }

            override fun onPurchasesError() {
                showToast(R.string.could_not_restore_purchase)
            }
        })
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        showToast(R.string.thank_you)
        setResult(RESULT_OK)
    }

    override fun onPurchaseHistoryRestored() {
        if (App.isProVersion()) {
            showToast(R.string.restored_previous_purchase_please_restart)
            setResult(RESULT_OK)
        } else {
            showToast(R.string.no_purchase_found)
        }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.e(TAG, "Billing error: code = $errorCode", error)
    }

    override fun onBillingInitialized() {
        binding.restoreButton.isEnabled = true
        binding.purchaseButton.isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        billingProcessor.release()
        super.onDestroy()
    }

    companion object {
        private const val TAG: String = "PurchaseActivity"
    }
}