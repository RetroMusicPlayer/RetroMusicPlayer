package code.name.monkey.retromusic.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.BuildConfig;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;

/**
 * @author Hemanth S (h4h13).
 */

public class ProVersionActivity extends AbsBaseActivity implements
        BillingProcessor.IBillingHandler {

    private static final String TAG = "ProVersionActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.restore_button)
    View restoreButton;

    @BindView(R.id.purchase_button)
    View purchaseButton;

    @BindView(R.id.app_bar)
    AppBarLayout appBar;

    @BindView(R.id.title)
    TextView title;

    private BillingProcessor billingProcessor;
    private AsyncTask restorePurchaseAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_version);
        setDrawUnderStatusBar(true);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        int primaryColor = ThemeStore.primaryColor(this);
        toolbar.setBackgroundColor(primaryColor);
        appBar.setBackgroundColor(primaryColor);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        title.setTextColor(ThemeStore.textColorPrimary(this));
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);

        restoreButton.setEnabled(false);
        purchaseButton.setEnabled(false);

        billingProcessor = new BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSE_KEY, this);

    }

    private void restorePurchase() {
        if (restorePurchaseAsyncTask != null) {
            restorePurchaseAsyncTask.cancel(false);
        }
        restorePurchaseAsyncTask = new RestorePurchaseAsyncTask(this).execute();
    }

    @OnClick({R.id.restore_button, R.id.purchase_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.restore_button:
                if (restorePurchaseAsyncTask == null
                        || restorePurchaseAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
                    restorePurchase();
                }
                break;
            case R.id.purchase_button:
                billingProcessor.purchase(ProVersionActivity.this, RetroApplication.PRO_VERSION_PRODUCT_ID);
                break;
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, R.string.thank_you, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    @Override
    public void onPurchaseHistoryRestored() {
        if (RetroApplication.isProVersion()) {
            Toast.makeText(this, R.string.restored_previous_purchase_please_restart, Toast.LENGTH_LONG)
                    .show();
            setResult(RESULT_OK);
        } else {
            Toast.makeText(this, R.string.no_purchase_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.e(TAG, "Billing error: code = " + errorCode, error);
    }


    @Override
    public void onBillingInitialized() {
        restoreButton.setEnabled(true);
        purchaseButton.setEnabled(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }

    private static class RestorePurchaseAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<ProVersionActivity> buyActivityWeakReference;

        RestorePurchaseAsyncTask(ProVersionActivity purchaseActivity) {
            this.buyActivityWeakReference = new WeakReference<>(purchaseActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProVersionActivity purchaseActivity = buyActivityWeakReference.get();
            if (purchaseActivity != null) {
                Toast.makeText(purchaseActivity, R.string.restoring_purchase, Toast.LENGTH_SHORT).show();
            } else {
                cancel(false);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ProVersionActivity purchaseActivity = buyActivityWeakReference.get();
            if (purchaseActivity != null) {
                return purchaseActivity.billingProcessor.loadOwnedPurchasesFromGoogle();
            }
            cancel(false);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            ProVersionActivity purchaseActivity = buyActivityWeakReference.get();
            if (purchaseActivity == null || b == null) {
                return;
            }

            if (b) {
                purchaseActivity.onPurchaseHistoryRestored();
            } else {
                Toast.makeText(purchaseActivity, R.string.could_not_restore_purchase, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
