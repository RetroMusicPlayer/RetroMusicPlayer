package code.name.monkey.retromusic;

import android.os.Build;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RetroApplication extends MultiDexApplication {

    public static final String PRO_VERSION_PRODUCT_ID = "pro_version";

    private static RetroApplication app;

    private BillingProcessor billingProcessor;

    public static RetroApplication getInstance() {
        return app;
    }

    public static boolean isProVersion() {
        return BuildConfig.DEBUG || app.billingProcessor.isPurchased(PRO_VERSION_PRODUCT_ID);
    }

    public static void deleteAppData() {
        try {
            // clearing app data
            String packageName = app.getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear " + packageName);

            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        setupErrorHandler();

        // default theme
        if (!ThemeStore.isConfigured(this, 1)) {
            ThemeStore.editTheme(this)
                    .accentColorRes(R.color.md_green_A200)
                    .commit();
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/circular_std_book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            new DynamicShortcutManager(this).initDynamicShortcuts();
        }

        // automatically restores purchases
        billingProcessor = new BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSE_KEY,
                new BillingProcessor.IBillingHandler() {
                    @Override
                    public void onProductPurchased(@NonNull String productId, TransactionDetails details) {
                    }

                    @Override
                    public void onPurchaseHistoryRestored() {
                        //Toast.makeText(App.this, R.string.restored_previous_purchase_please_restart, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onBillingError(int errorCode, Throwable error) {
                    }

                    @Override
                    public void onBillingInitialized() {
                    }
                });
    }

    private void setupErrorHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                handleUncaughtException(thread, throwable);
            }
        });
    }

    private void handleUncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        deleteAppData();
        //Intent intent = new Intent(this, ErrorHandlerActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        billingProcessor.release();
    }
}
