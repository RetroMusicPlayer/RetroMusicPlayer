package code.name.monkey.retromusic

import android.content.Context
import androidx.multidex.MultiDexApplication
import code.name.monkey.appthemehelper.ThemeStore
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.bumptech.glide.Glide

class App : MultiDexApplication() {

    lateinit var billingProcessor: BillingProcessor

    override fun onCreate() {
        super.onCreate()
        instance = this

        setupErrorHandler()

        // default theme
        if (!ThemeStore.isConfigured(this, 3)) {
            ThemeStore.editTheme(this)
                    .accentColorRes(R.color.md_green_A200)
                    .coloredNavigationBar(true)
                    .commit()
        }

        // automatically restores purchases
        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSE_KEY,
                object : BillingProcessor.IBillingHandler {
                    override fun onProductPurchased(productId: String, details: TransactionDetails?) {}

                    override fun onPurchaseHistoryRestored() {
                        //Toast.makeText(App.this, R.string.restored_previous_purchase_please_restart, Toast.LENGTH_LONG).show();
                    }

                    override fun onBillingError(errorCode: Int, error: Throwable?) {}

                    override fun onBillingInitialized() {}
                })
    }

    private fun setupErrorHandler() {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable -> handleUncaughtException(throwable) }
    }

    private fun handleUncaughtException(throwable: Throwable) {
        throwable.printStackTrace()
        deleteAppData()
        //Intent intent = new Intent(this, ErrorHandlerActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.with(this).onLowMemory()
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

        fun deleteAppData() {
            try {
                // clearing app data
                val packageName = instance.packageName
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear $packageName")

                System.exit(0)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
