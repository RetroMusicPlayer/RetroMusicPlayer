package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 *2\u00020\u00012\u00020\u0002:\u0001*B\u0005\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\rH\u0002J\"\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\u001a\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\b\u0010\u001a\u001a\u00020\rH\u0016J\u0012\u0010\u001b\u001a\u00020\r2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\b\u0010\u001e\u001a\u00020\rH\u0016J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0016J\u001a\u0010#\u001a\u00020\r2\u0006\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\b\u0010(\u001a\u00020\rH\u0016J\b\u0010)\u001a\u00020\rH\u0002R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001c\u0010\n\u001a\u0010\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SupportDevelopmentActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsBaseActivity;", "Lcom/anjlab/android/iab/v3/BillingProcessor$IBillingHandler;", "()V", "billingProcessor", "Lcom/anjlab/android/iab/v3/BillingProcessor;", "getBillingProcessor", "()Lcom/anjlab/android/iab/v3/BillingProcessor;", "setBillingProcessor", "(Lcom/anjlab/android/iab/v3/BillingProcessor;)V", "skuDetailsLoadAsyncTask", "Landroid/os/AsyncTask;", "donate", "", "i", "", "loadSkuDetails", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onBillingError", "errorCode", "error", "", "onBillingInitialized", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onProductPurchased", "productId", "", "details", "Lcom/anjlab/android/iab/v3/TransactionDetails;", "onPurchaseHistoryRestored", "setupToolbar", "Companion", "app_normalDebug"})
public final class SupportDevelopmentActivity extends code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity implements com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler {
    @org.jetbrains.annotations.Nullable()
    private com.anjlab.android.iab.v3.BillingProcessor billingProcessor;
    private android.os.AsyncTask<?, ?, ?> skuDetailsLoadAsyncTask;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final int DONATION_PRODUCT_IDS = 2130903044;
    private static final int TEZ_REQUEST_CODE = 123;
    public static final code.name.monkey.retromusic.ui.activities.SupportDevelopmentActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    public final com.anjlab.android.iab.v3.BillingProcessor getBillingProcessor() {
        return null;
    }
    
    public final void setBillingProcessor(@org.jetbrains.annotations.Nullable()
    com.anjlab.android.iab.v3.BillingProcessor p0) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    public final void donate(int i) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupToolbar() {
    }
    
    @java.lang.Override()
    public void onBillingInitialized() {
    }
    
    private final void loadSkuDetails() {
    }
    
    @java.lang.Override()
    public void onProductPurchased(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, @org.jetbrains.annotations.Nullable()
    com.anjlab.android.iab.v3.TransactionDetails details) {
    }
    
    @java.lang.Override()
    public void onBillingError(int errorCode, @org.jetbrains.annotations.Nullable()
    java.lang.Throwable error) {
    }
    
    @java.lang.Override()
    public void onPurchaseHistoryRestored() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public SupportDevelopmentActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SupportDevelopmentActivity$Companion;", "", "()V", "DONATION_PRODUCT_IDS", "", "TAG", "", "getTAG", "()Ljava/lang/String;", "TEZ_REQUEST_CODE", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTAG() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}