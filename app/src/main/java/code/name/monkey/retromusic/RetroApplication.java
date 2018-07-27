package code.name.monkey.retromusic;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
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

  @Override
  public void onCreate() {
    super.onCreate();
    app = this;

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

  @Override
  public void onTerminate() {
    super.onTerminate();
    billingProcessor.release();
  }
}
