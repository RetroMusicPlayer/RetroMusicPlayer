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

package code.name.monkey.retromusic

import android.content.Context
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class App : MultiDexApplication() {

    lateinit var billingProcessor: BillingProcessor

    override fun onCreate() {
        super.onCreate()
        instance = this

        // default theme
        if (!ThemeStore.isConfigured(this, 3)) {
            ThemeStore.editTheme(this)
                    .accentColorRes(R.color.md_green_A200)
                    .coloredNavigationBar(true)
                    .commit()
        }

        if (VersionUtils.hasNougatMR())
            DynamicShortcutManager(this).initDynamicShortcuts()


        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFont(R.font.circular_std_book)
                .build()
        )

        // automatically restores purchases
        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY,
                object : BillingProcessor.IBillingHandler {
                    override fun onProductPurchased(productId: String, details: TransactionDetails?) {}

                    override fun onPurchaseHistoryRestored() {
                        Toast.makeText(this@App, R.string.restored_previous_purchase_please_restart, Toast.LENGTH_LONG).show();
                    }

                    override fun onBillingError(errorCode: Int, error: Throwable?) {}

                    override fun onBillingInitialized() {}
                })
    }

    override fun onTerminate() {
        super.onTerminate()
        billingProcessor.release()
    }

    companion object {

        const val PRO_VERSION_PRODUCT_ID = "pro_version"

        lateinit var instance: App
            private set

        val context: Context
            get() = instance.applicationContext

        val isProVersion: Boolean
            get() = BuildConfig.DEBUG || instance.billingProcessor.isPurchased(PRO_VERSION_PRODUCT_ID)
    }
}
