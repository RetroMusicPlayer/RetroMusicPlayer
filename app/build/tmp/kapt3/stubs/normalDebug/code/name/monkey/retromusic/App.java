package code.name.monkey.retromusic;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0005\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\nH\u0016J\b\u0010\u000e\u001a\u00020\nH\u0016J\b\u0010\u000f\u001a\u00020\nH\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u0011"}, d2 = {"Lcode/name/monkey/retromusic/App;", "Landroidx/multidex/MultiDexApplication;", "()V", "billingProcessor", "Lcom/anjlab/android/iab/v3/BillingProcessor;", "getBillingProcessor", "()Lcom/anjlab/android/iab/v3/BillingProcessor;", "setBillingProcessor", "(Lcom/anjlab/android/iab/v3/BillingProcessor;)V", "handleUncaughtException", "", "throwable", "", "onCreate", "onTerminate", "setupErrorHandler", "Companion", "app_normalDebug"})
public final class App extends androidx.multidex.MultiDexApplication {
    @org.jetbrains.annotations.NotNull()
    public com.anjlab.android.iab.v3.BillingProcessor billingProcessor;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRO_VERSION_PRODUCT_ID = "pro_version";
    @org.jetbrains.annotations.NotNull()
    private static code.name.monkey.retromusic.App instance;
    public static final code.name.monkey.retromusic.App.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.anjlab.android.iab.v3.BillingProcessor getBillingProcessor() {
        return null;
    }
    
    public final void setBillingProcessor(@org.jetbrains.annotations.NotNull()
    com.anjlab.android.iab.v3.BillingProcessor p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    private final void setupErrorHandler() {
    }
    
    private final void handleUncaughtException(java.lang.Throwable throwable) {
    }
    
    @java.lang.Override()
    public void onTerminate() {
    }
    
    public App() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0013\u001a\u00020\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR$\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@BX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0012\u00a8\u0006\u0015"}, d2 = {"Lcode/name/monkey/retromusic/App$Companion;", "", "()V", "PRO_VERSION_PRODUCT_ID", "", "context", "Landroid/content/Context;", "getContext", "()Landroid/content/Context;", "<set-?>", "Lcode/name/monkey/retromusic/App;", "instance", "getInstance", "()Lcode/name/monkey/retromusic/App;", "setInstance", "(Lcode/name/monkey/retromusic/App;)V", "isProVersion", "", "()Z", "deleteAppData", "", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.App getInstance() {
            return null;
        }
        
        private final void setInstance(code.name.monkey.retromusic.App p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Context getContext() {
            return null;
        }
        
        public final boolean isProVersion() {
            return false;
        }
        
        public final void deleteAppData() {
        }
        
        private Companion() {
            super();
        }
    }
}