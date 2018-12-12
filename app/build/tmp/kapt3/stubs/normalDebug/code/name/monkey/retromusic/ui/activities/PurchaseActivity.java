package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 #2\u00020\u00012\u00020\u0002:\u0002#$B\u0005\u00a2\u0006\u0002\u0010\u0003J\"\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u001a\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u000b2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\b\u0010\u0013\u001a\u00020\tH\u0016J\u0012\u0010\u0014\u001a\u00020\t2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\tH\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u001a\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u0010!\u001a\u00020\tH\u0016J\b\u0010\"\u001a\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0010\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/PurchaseActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsBaseActivity;", "Lcom/anjlab/android/iab/v3/BillingProcessor$IBillingHandler;", "()V", "billingProcessor", "Lcom/anjlab/android/iab/v3/BillingProcessor;", "restorePurchaseAsyncTask", "Landroid/os/AsyncTask;", "onActivityResult", "", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onBillingError", "errorCode", "error", "", "onBillingInitialized", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onProductPurchased", "productId", "", "details", "Lcom/anjlab/android/iab/v3/TransactionDetails;", "onPurchaseHistoryRestored", "restorePurchase", "Companion", "RestorePurchaseAsyncTask", "app_normalDebug"})
public final class PurchaseActivity extends code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity implements com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler {
    private com.anjlab.android.iab.v3.BillingProcessor billingProcessor;
    private android.os.AsyncTask<?, ?, ?> restorePurchaseAsyncTask;
    private static final java.lang.String TAG = "PurchaseActivity";
    public static final code.name.monkey.retromusic.ui.activities.PurchaseActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void restorePurchase() {
    }
    
    @java.lang.Override()
    public void onProductPurchased(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, @org.jetbrains.annotations.Nullable()
    com.anjlab.android.iab.v3.TransactionDetails details) {
    }
    
    @java.lang.Override()
    public void onPurchaseHistoryRestored() {
    }
    
    @java.lang.Override()
    public void onBillingError(int errorCode, @org.jetbrains.annotations.Nullable()
    java.lang.Throwable error) {
    }
    
    @java.lang.Override()
    public void onBillingInitialized() {
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public PurchaseActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J#\u0010\t\u001a\u0004\u0018\u00010\u00032\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u000b\"\u00020\u0002H\u0014\u00a2\u0006\u0002\u0010\fJ\u0017\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u0014\u00a2\u0006\u0002\u0010\u0010J\b\u0010\u0011\u001a\u00020\u000eH\u0014R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/PurchaseActivity$RestorePurchaseAsyncTask;", "Landroid/os/AsyncTask;", "Ljava/lang/Void;", "", "purchaseActivity", "Lcode/name/monkey/retromusic/ui/activities/PurchaseActivity;", "(Lcode/name/monkey/retromusic/ui/activities/PurchaseActivity;)V", "buyActivityWeakReference", "Ljava/lang/ref/WeakReference;", "doInBackground", "params", "", "([Ljava/lang/Void;)Ljava/lang/Boolean;", "onPostExecute", "", "b", "(Ljava/lang/Boolean;)V", "onPreExecute", "app_normalDebug"})
    static final class RestorePurchaseAsyncTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Boolean> {
        private final java.lang.ref.WeakReference<code.name.monkey.retromusic.ui.activities.PurchaseActivity> buyActivityWeakReference = null;
        
        @java.lang.Override()
        protected void onPreExecute() {
        }
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        protected java.lang.Boolean doInBackground(@org.jetbrains.annotations.NotNull()
        java.lang.Void... params) {
            return null;
        }
        
        @java.lang.Override()
        protected void onPostExecute(@org.jetbrains.annotations.Nullable()
        java.lang.Boolean b) {
        }
        
        public RestorePurchaseAsyncTask(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.ui.activities.PurchaseActivity purchaseActivity) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/PurchaseActivity$Companion;", "", "()V", "TAG", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}