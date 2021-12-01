/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.Constants.PRO_VERSION_PRODUCT_ID
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.databinding.ActivityProVersionBinding
import code.name.monkey.retromusic.extensions.setDrawUnderStatusBar
import code.name.monkey.retromusic.extensions.setLightStatusBar
import code.name.monkey.retromusic.extensions.setStatusBarColor
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo

class PurchaseActivity : AbsBaseActivity(), BillingProcessor.IBillingHandler {

    private lateinit var binding: ActivityProVersionBinding
    private lateinit var billingProcessor: BillingProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityProVersionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(Color.TRANSPARENT)
        setLightStatusBar(false)
        binding.toolbar.navigationIcon?.setTint(Color.WHITE)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.restoreButton.isEnabled = false
        binding.purchaseButton.isEnabled = false

        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY, this)

        MaterialUtil.setTint(binding.purchaseButton, true)

        binding.restoreButton.setOnClickListener {
            restorePurchase()
        }
        binding.purchaseButton.setOnClickListener {
            billingProcessor.purchase(this@PurchaseActivity, PRO_VERSION_PRODUCT_ID)
        }
        binding.bannerContainer.backgroundTintList =
            ColorStateList.valueOf(ThemeStore.accentColor(this))
    }

    private fun restorePurchase() {
        Toast.makeText(this, R.string.restoring_purchase, Toast.LENGTH_SHORT)
            .show()
        billingProcessor.loadOwnedPurchasesFromGoogleAsync(object :
            BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
                onPurchaseHistoryRestored()
            }

            override fun onPurchasesError() {
                Toast.makeText(
                    this@PurchaseActivity,
                    R.string.could_not_restore_purchase,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        Toast.makeText(this, R.string.thank_you, Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
    }

    override fun onPurchaseHistoryRestored() {
        if (App.isProVersion()) {
            Toast.makeText(
                this,
                R.string.restored_previous_purchase_please_restart,
                Toast.LENGTH_LONG
            ).show()
            setResult(RESULT_OK)
        } else {
            Toast.makeText(this, R.string.no_purchase_found, Toast.LENGTH_SHORT).show()
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
