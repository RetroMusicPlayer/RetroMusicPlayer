package code.name.monkey.retromusic

import android.os.Build

import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.bumptech.glide.Glide
import androidx.multidex.MultiDexApplication
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class RetroApplication : MultiDexApplication() {

    private var billingProcessor: BillingProcessor? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        setupErrorHandler()

        // default theme
        if (!ThemeStore.isConfigured(this, 1)) {
            ThemeStore.editTheme(this)
                    .accentColorRes(R.color.md_green_A200)
                    .commit()
        }

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/circular_std_book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            DynamicShortcutManager(this).initDynamicShortcuts()
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
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable -> handleUncaughtException(thread, throwable) }
    }

    private fun handleUncaughtException(thread: Thread, throwable: Throwable) {
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
        billingProcessor!!.release()
    }

    companion object {

        const val PRO_VERSION_PRODUCT_ID = "pro_version"

        var instance: RetroApplication? = null
            private set

        val isProVersion: Boolean
            get() = BuildConfig.DEBUG || instance!!.billingProcessor!!.isPurchased(PRO_VERSION_PRODUCT_ID)

        fun deleteAppData() {
            try {
                // clearing app data
                val packageName = instance!!.packageName
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear $packageName")

                System.exit(0)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
