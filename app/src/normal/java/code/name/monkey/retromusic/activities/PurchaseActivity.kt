package code.name.monkey.retromusic.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsThemeActivity
import code.name.monkey.retromusic.billing.BillingManager
import code.name.monkey.retromusic.databinding.ActivityProVersionBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.setLightStatusBar
import code.name.monkey.retromusic.extensions.setStatusBarColor
import code.name.monkey.retromusic.extensions.showToast

class PurchaseActivity : AbsThemeActivity() {

    private lateinit var binding: ActivityProVersionBinding
    private lateinit var billingManager: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProVersionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor(Color.TRANSPARENT)
        setLightStatusBar(false)
        binding.toolbar.navigationIcon?.setTint(Color.WHITE)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        billingManager = BillingManager(this)

        binding.restoreButton.isEnabled = true
        binding.purchaseButton.isEnabled = true

        MaterialUtil.setTint(binding.purchaseButton, true)

        binding.restoreButton.setOnClickListener {
            showToast(R.string.restoring_purchase)
            billingManager.restorePurchases {
                if (App.isProVersion()) {
                    showToast(R.string.restored_previous_purchase_please_restart)
                    setResult(RESULT_OK)
                } else {
                    showToast(R.string.no_purchase_found)
                }
            }
        }

        binding.purchaseButton.setOnClickListener {
            billingManager.launchPurchaseFlow(this@PurchaseActivity)
        }

        binding.bannerContainer.backgroundTintList =
            ColorStateList.valueOf(accentColor())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        billingManager.release()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "PurchaseActivity"
    }
}
