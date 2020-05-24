package code.name.monkey.retromusic.activities.bugreport

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringDef
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsThemeActivity
import code.name.monkey.retromusic.activities.bugreport.model.DeviceInfo
import code.name.monkey.retromusic.activities.bugreport.model.Report
import code.name.monkey.retromusic.activities.bugreport.model.github.ExtraInfo
import code.name.monkey.retromusic.activities.bugreport.model.github.GithubLogin
import code.name.monkey.retromusic.activities.bugreport.model.github.GithubTarget
import code.name.monkey.retromusic.misc.DialogAsyncTask
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_bug_report.*
import kotlinx.android.synthetic.main.bug_report_card_device_info.*
import kotlinx.android.synthetic.main.bug_report_card_report.*
import org.eclipse.egit.github.core.Issue
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.client.RequestException
import org.eclipse.egit.github.core.service.IssueService
import java.io.IOException

private const val RESULT_SUCCESS = "RESULT_OK"
private const val RESULT_BAD_CREDENTIALS = "RESULT_BAD_CREDENTIALS"
private const val RESULT_INVALID_TOKEN = "RESULT_INVALID_TOKEN"
private const val RESULT_ISSUES_NOT_ENABLED = "RESULT_ISSUES_NOT_ENABLED"
private const val RESULT_UNKNOWN = "RESULT_UNKNOWN"

@StringDef(
    RESULT_SUCCESS,
    RESULT_BAD_CREDENTIALS,
    RESULT_INVALID_TOKEN,
    RESULT_ISSUES_NOT_ENABLED,
    RESULT_UNKNOWN
)
@Retention(AnnotationRetention.SOURCE)
private annotation class Result

open class BugReportActivity : AbsThemeActivity() {

    private var deviceInfo: DeviceInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_report)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()

        initViews()

        if (TextUtils.isEmpty(title)) setTitle(R.string.report_an_issue)

        deviceInfo = DeviceInfo(this)
        airTextDeviceInfo.text = deviceInfo.toString()
    }

    private fun initViews() {
        val accentColor = ThemeStore.accentColor(this)
        val primaryColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        toolbar.setBackgroundColor(primaryColor)
        setSupportActionBar(toolbar)
        ToolbarContentTintHelper.colorBackButton(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        TintHelper.setTintAuto(optionUseAccount, accentColor, false)
        optionUseAccount?.setOnClickListener {
            inputTitle.isEnabled = true
            inputDescription.isEnabled = true
            inputUsername.isEnabled = true
            inputPassword.isEnabled = true

            optionAnonymous.isChecked = false
            sendFab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    sendFab.setImageResource(R.drawable.ic_send_white_24dp)
                    sendFab.show()
                }
            })
        }
        TintHelper.setTintAuto(optionAnonymous, accentColor, false)
        optionAnonymous.setOnClickListener {
            inputTitle.isEnabled = false
            inputDescription.isEnabled = false
            inputUsername.isEnabled = false
            inputPassword.isEnabled = false

            optionUseAccount.isChecked = false
            sendFab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    sendFab.setImageResource(R.drawable.ic_open_in_browser_white_24dp)
                    sendFab.show()
                }
            })
        }

        inputPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                reportIssue()
                return@setOnEditorActionListener true
            }
            false
        }

        airTextDeviceInfo.setOnClickListener { copyDeviceInfoToClipBoard() }

        TintHelper.setTintAuto(sendFab, accentColor, true)
        sendFab.setOnClickListener { reportIssue() }

        MaterialUtil.setTint(inputLayoutTitle, false)
        MaterialUtil.setTint(inputLayoutDescription, false)
        MaterialUtil.setTint(inputLayoutUsername, false)
        MaterialUtil.setTint(inputLayoutPassword, false)
    }

    private fun reportIssue() {
        if (optionUseAccount.isChecked) {
            if (!validateInput()) return
            val username = inputUsername.text.toString()
            val password = inputPassword.text.toString()
            sendBugReport(GithubLogin(username, password))
        } else {
            copyDeviceInfoToClipBoard()

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(ISSUE_TRACKER_LINK)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }
    }

    private fun copyDeviceInfoToClipBoard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.device_info), deviceInfo?.toMarkdown())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            this@BugReportActivity,
            R.string.copied_device_info_to_clipboard,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun validateInput(): Boolean {
        var hasErrors = false

        if (optionUseAccount.isChecked) {
            if (TextUtils.isEmpty(inputUsername.text)) {
                setError(inputLayoutUsername, R.string.bug_report_no_username)
                hasErrors = true
            } else {
                removeError(inputLayoutUsername)
            }

            if (TextUtils.isEmpty(inputPassword.text)) {
                setError(inputLayoutPassword, R.string.bug_report_no_password)
                hasErrors = true
            } else {
                removeError(inputLayoutPassword)
            }
        }

        if (TextUtils.isEmpty(inputTitle.text)) {
            setError(inputLayoutTitle, R.string.bug_report_no_title)
            hasErrors = true
        } else {
            removeError(inputLayoutTitle)
        }

        if (TextUtils.isEmpty(inputDescription.text)) {
            setError(inputLayoutDescription, R.string.bug_report_no_description)
            hasErrors = true
        } else {
            removeError(inputLayoutDescription)
        }

        return !hasErrors
    }

    private fun setError(editTextLayout: TextInputLayout, @StringRes errorRes: Int) {
        editTextLayout.error = getString(errorRes)
    }

    private fun removeError(editTextLayout: TextInputLayout) {
        editTextLayout.error = null
    }

    private fun sendBugReport(login: GithubLogin) {
        if (!validateInput()) return

        val bugTitle = inputTitle.text.toString()
        val bugDescription = inputDescription.text.toString()

        val extraInfo = ExtraInfo()
        onSaveExtraInfo()

        val report = Report(bugTitle, bugDescription, deviceInfo, extraInfo)
        val target = GithubTarget("h4h13", "RetroMusicPlayer")

        ReportIssueAsyncTask.report(this, report, target, login)
    }

    private fun onSaveExtraInfo() {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private class ReportIssueAsyncTask private constructor(
        activity: Activity,
        private val report: Report,
        private val target: GithubTarget,
        private val login: GithubLogin
    ) : DialogAsyncTask<Void, Void, String>(activity) {

        override fun createDialog(context: Context): Dialog {
            return AlertDialog.Builder(context).show()
        }

        @Result
        override fun doInBackground(vararg params: Void): String {
            val client: GitHubClient = if (login.shouldUseApiToken()) {
                GitHubClient().setOAuth2Token(login.apiToken)
            } else {
                GitHubClient().setCredentials(login.username, login.password)
            }

            val issue = Issue().setTitle(report.title).setBody(report.description)
            try {
                IssueService(client).createIssue(target.username, target.repository, issue)
                return RESULT_SUCCESS
            } catch (e: RequestException) {
                return when (e.status) {
                    STATUS_BAD_CREDENTIALS -> {
                        if (login.shouldUseApiToken()) RESULT_INVALID_TOKEN else RESULT_BAD_CREDENTIALS
                    }
                    STATUS_ISSUES_NOT_ENABLED -> RESULT_ISSUES_NOT_ENABLED
                    else -> {
                        e.printStackTrace()
                        RESULT_UNKNOWN
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return RESULT_UNKNOWN
            }
        }

        override fun onPostExecute(@Result result: String) {
            super.onPostExecute(result)

            val context = context ?: return

            when (result) {
                RESULT_SUCCESS -> tryToFinishActivity()
                RESULT_BAD_CREDENTIALS -> MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.bug_report_failed)
                    .setMessage(R.string.bug_report_failed_wrong_credentials)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
                RESULT_INVALID_TOKEN -> MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.bug_report_failed)
                    .setMessage(R.string.bug_report_failed_invalid_token)
                    .setPositiveButton(android.R.string.ok, null).show()
                RESULT_ISSUES_NOT_ENABLED -> MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.bug_report_failed)
                    .setMessage(R.string.bug_report_failed_issues_not_available)
                    .setPositiveButton(android.R.string.ok, null)

                else -> MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.bug_report_failed)
                    .setMessage(R.string.bug_report_failed_unknown)
                    .setPositiveButton(android.R.string.ok) { _, _ -> tryToFinishActivity() }
                    .setNegativeButton(android.R.string.cancel) { _, _ -> { tryToFinishActivity() } }
            }
        }

        private fun tryToFinishActivity() {
            val context = context
            if (context is Activity && !context.isFinishing) {
                context.finish()
            }
        }


        companion object {

            fun report(
                activity: Activity,
                report: Report,
                target: GithubTarget,
                login: GithubLogin
            ) {
                ReportIssueAsyncTask(activity, report, target, login).execute()
            }
        }
    }

    companion object {

        private const val STATUS_BAD_CREDENTIALS = 401
        private const val STATUS_ISSUES_NOT_ENABLED = 410
        private const val ISSUE_TRACKER_LINK = "https://github.com/h4h13/RetroMusicPlayer"
    }
}
