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

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.AsyncTask
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
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import java.lang.ref.WeakReference
import kotlinx.android.synthetic.main.activity_pro_version.*

class PurchaseActivity : AbsBaseActivity(), BillingProcessor.IBillingHandler {

    private lateinit var billingProcessor: BillingProcessor
    private var restorePurchaseAsyncTask: AsyncTask<*, *, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_version)
        setStatusbarColor(Color.TRANSPARENT)
        setLightStatusbar(false)
        setNavigationbarColor(Color.BLACK)
        setLightNavigationBar(false)
        toolbar.navigationIcon?.setTint(Color.WHITE)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        restoreButton.isEnabled = false
        purchaseButton.isEnabled = false

        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY, this)

        MaterialUtil.setTint(purchaseButton, true)

        restoreButton.setOnClickListener {
            if (restorePurchaseAsyncTask == null || restorePurchaseAsyncTask!!.status != AsyncTask.Status.RUNNING) {
                restorePurchase()
            }
        }
        purchaseButton.setOnClickListener {
            billingProcessor.purchase(this@PurchaseActivity, PRO_VERSION_PRODUCT_ID)
        }
        bannerContainer.backgroundTintList =
            ColorStateList.valueOf(ThemeStore.accentColor(this))
    }

    private fun restorePurchase() {
        if (restorePurchaseAsyncTask != null) {
            restorePurchaseAsyncTask!!.cancel(false)
        }
        restorePurchaseAsyncTask = RestorePurchaseAsyncTask(this).execute()
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
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
        restoreButton.isEnabled = true
        purchaseButton.isEnabled = true
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
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

    private class RestorePurchaseAsyncTask(purchaseActivity: PurchaseActivity) :
        AsyncTask<Void, Void, Boolean>() {

        private val buyActivityWeakReference: WeakReference<PurchaseActivity> = WeakReference(
            purchaseActivity
        )

        override fun onPreExecute() {
            super.onPreExecute()
            val purchaseActivity = buyActivityWeakReference.get()
            if (purchaseActivity != null) {
                Toast.makeText(purchaseActivity, R.string.restoring_purchase, Toast.LENGTH_SHORT)
                    .show()
            } else {
                cancel(false)
            }
        }

        override fun doInBackground(vararg params: Void): Boolean? {
            val purchaseActivity = buyActivityWeakReference.get()
            if (purchaseActivity != null) {
                return purchaseActivity.billingProcessor.loadOwnedPurchasesFromGoogle()
            }
            cancel(false)
            return null
        }

        override fun onPostExecute(b: Boolean?) {
            super.onPostExecute(b)
            val purchaseActivity = buyActivityWeakReference.get()
            if (purchaseActivity == null || b == null) {
                return
            }

            if (b) {
                purchaseActivity.onPurchaseHistoryRestored()
            } else {
                Toast.makeText(
                    purchaseActivity,
                    R.string.could_not_restore_purchase,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val TAG: String = "PurchaseActivity"
    }
}
