package code.name.monkey.retromusic.ui.activities.bugreport;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0016\u0018\u0000 \u001d2\u00020\u0001:\u0002\u001d\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0002J\u0012\u0010\b\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0014J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0006H\u0002J\u0010\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u001a\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0001\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\fH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/bugreport/BugReportActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsThemeActivity;", "()V", "deviceInfo", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/DeviceInfo;", "copyDeviceInfoToClipBoard", "", "initViews", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onSaveExtraInfo", "extraInfo", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/ExtraInfo;", "removeError", "editTextLayout", "Lcom/google/android/material/textfield/TextInputLayout;", "reportIssue", "sendBugReport", "login", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubLogin;", "setError", "errorRes", "", "validateInput", "Companion", "ReportIssueAsyncTask", "app_normalDebug"})
public class BugReportActivity extends code.name.monkey.retromusic.ui.activities.base.AbsThemeActivity {
    private code.name.monkey.retromusic.ui.activities.bugreport.model.DeviceInfo deviceInfo;
    private static final int STATUS_BAD_CREDENTIALS = 401;
    private static final int STATUS_ISSUES_NOT_ENABLED = 410;
    private static final java.lang.String ISSUE_TRACKER_LINK = "https://github.com/h4h13/RetroMusicPlayer";
    public static final code.name.monkey.retromusic.ui.activities.bugreport.BugReportActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initViews() {
    }
    
    private final void reportIssue() {
    }
    
    private final void copyDeviceInfoToClipBoard() {
    }
    
    private final boolean validateInput() {
        return false;
    }
    
    private final void setError(com.google.android.material.textfield.TextInputLayout editTextLayout, @androidx.annotation.StringRes()
    int errorRes) {
    }
    
    private final void removeError(com.google.android.material.textfield.TextInputLayout editTextLayout) {
    }
    
    private final void sendBugReport(code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubLogin login) {
    }
    
    private final void onSaveExtraInfo(code.name.monkey.retromusic.ui.activities.bugreport.model.github.ExtraInfo extraInfo) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    public BugReportActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u0000 \u00192\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001\u0019B\'\b\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0014J!\u0010\u0011\u001a\u00020\u00032\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0013\"\u00020\u0002H\u0014\u00a2\u0006\u0002\u0010\u0014J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0003H\u0014J\b\u0010\u0018\u001a\u00020\u0016H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/bugreport/BugReportActivity$ReportIssueAsyncTask;", "Lcode/name/monkey/retromusic/misc/DialogAsyncTask;", "Ljava/lang/Void;", "", "activity", "Landroid/app/Activity;", "report", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/Report;", "target", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubTarget;", "login", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubLogin;", "(Landroid/app/Activity;Lcode/name/monkey/retromusic/ui/activities/bugreport/model/Report;Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubTarget;Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubLogin;)V", "createDialog", "Landroid/app/Dialog;", "context", "Landroid/content/Context;", "doInBackground", "params", "", "([Ljava/lang/Void;)Ljava/lang/String;", "onPostExecute", "", "result", "tryToFinishActivity", "Companion", "app_normalDebug"})
    static final class ReportIssueAsyncTask extends code.name.monkey.retromusic.misc.DialogAsyncTask<java.lang.Void, java.lang.Void, java.lang.String> {
        private final code.name.monkey.retromusic.ui.activities.bugreport.model.Report report = null;
        private final code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubTarget target = null;
        private final code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubLogin login = null;
        public static final code.name.monkey.retromusic.ui.activities.bugreport.BugReportActivity.ReportIssueAsyncTask.Companion Companion = null;
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        protected android.app.Dialog createDialog(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        @Result()
        @java.lang.Override()
        protected java.lang.String doInBackground(@org.jetbrains.annotations.NotNull()
        java.lang.Void... params) {
            return null;
        }
        
        @java.lang.Override()
        protected void onPostExecute(@org.jetbrains.annotations.NotNull()
        @Result()
        java.lang.String result) {
        }
        
        private final void tryToFinishActivity() {
        }
        
        private ReportIssueAsyncTask(android.app.Activity activity, code.name.monkey.retromusic.ui.activities.bugreport.model.Report report, code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubTarget target, code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubLogin login) {
            super(null);
        }
        
        @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b\u00a8\u0006\f"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/bugreport/BugReportActivity$ReportIssueAsyncTask$Companion;", "", "()V", "report", "", "activity", "Landroid/app/Activity;", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/Report;", "target", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubTarget;", "login", "Lcode/name/monkey/retromusic/ui/activities/bugreport/model/github/GithubLogin;", "app_normalDebug"})
        public static final class Companion {
            
            public final void report(@org.jetbrains.annotations.NotNull()
            android.app.Activity activity, @org.jetbrains.annotations.NotNull()
            code.name.monkey.retromusic.ui.activities.bugreport.model.Report report, @org.jetbrains.annotations.NotNull()
            code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubTarget target, @org.jetbrains.annotations.NotNull()
            code.name.monkey.retromusic.ui.activities.bugreport.model.github.GithubLogin login) {
            }
            
            private Companion() {
                super();
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/bugreport/BugReportActivity$Companion;", "", "()V", "ISSUE_TRACKER_LINK", "", "STATUS_BAD_CREDENTIALS", "", "STATUS_ISSUES_NOT_ENABLED", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}