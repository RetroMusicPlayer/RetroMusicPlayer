/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
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
import code.name.monkey.retromusic.databinding.ActivityBugReportBinding
import code.name.monkey.retromusic.misc.DialogAsyncTask
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
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

    private lateinit var binding: ActivityBugReportBinding
    private var deviceInfo: DeviceInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()

        initViews()

        if (TextUtils.isEmpty(title)) setTitle(R.string.report_an_issue)

        deviceInfo = DeviceInfo(this)
        binding.cardDeviceInfo.airTextDeviceInfo.text = deviceInfo.toString()
    }

    private fun initViews() {
        val accentColor = ThemeStore.accentColor(this)
        val primaryColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        binding.toolbar.setBackgroundColor(primaryColor)
        setSupportActionBar(binding.toolbar)
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        TintHelper.setTintAuto(binding.cardReport.optionUseAccount, accentColor, false)
        binding.cardReport.optionUseAccount.setOnClickListener {
            binding.cardReport.inputTitle.isEnabled = true
            binding.cardReport.inputDescription.isEnabled = true
            binding.cardReport.inputUsername.isEnabled = true
            binding.cardReport.inputPassword.isEnabled = true

            binding.cardReport.optionAnonymous.isChecked = false
            binding.sendFab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    binding.sendFab.setImageResource(R.drawable.ic_send)
                    binding.sendFab.show()
                }
            })
        }
        TintHelper.setTintAuto(binding.cardReport.optionAnonymous, accentColor, false)
        binding.cardReport.optionAnonymous.setOnClickListener {
            binding.cardReport.inputTitle.isEnabled = false
            binding.cardReport.inputDescription.isEnabled = false
            binding.cardReport.inputUsername.isEnabled = false
            binding.cardReport.inputPassword.isEnabled = false

            binding.cardReport.optionUseAccount.isChecked = false
            binding.sendFab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    binding.sendFab.setImageResource(R.drawable.ic_open_in_browser)
                    binding.sendFab.show()
                }
            })
        }

        binding.cardReport.inputPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                reportIssue()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.cardDeviceInfo.airTextDeviceInfo.setOnClickListener { copyDeviceInfoToClipBoard() }

        TintHelper.setTintAuto(binding.sendFab, accentColor, true)
        binding.sendFab.setOnClickListener { reportIssue() }

        MaterialUtil.setTint(binding.cardReport.inputLayoutTitle, false)
        MaterialUtil.setTint(binding.cardReport.inputLayoutDescription, false)
        MaterialUtil.setTint(binding.cardReport.inputLayoutUsername, false)
        MaterialUtil.setTint(binding.cardReport.inputLayoutPassword, false)
    }

    private fun reportIssue() {
        if (binding.cardReport.optionUseAccount.isChecked) {
            if (!validateInput()) return
            val username = binding.cardReport.inputUsername.text.toString()
            val password = binding.cardReport.inputPassword.text.toString()
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

        if (binding.cardReport.optionUseAccount.isChecked) {
            if (TextUtils.isEmpty(binding.cardReport.inputUsername.text)) {
                setError(binding.cardReport.inputLayoutUsername, R.string.bug_report_no_username)
                hasErrors = true
            } else {
                removeError(binding.cardReport.inputLayoutUsername)
            }

            if (TextUtils.isEmpty(binding.cardReport.inputPassword.text)) {
                setError(binding.cardReport.inputLayoutPassword, R.string.bug_report_no_password)
                hasErrors = true
            } else {
                removeError(binding.cardReport.inputLayoutPassword)
            }
        }

        if (TextUtils.isEmpty(binding.cardReport.inputTitle.text)) {
            setError(binding.cardReport.inputLayoutTitle, R.string.bug_report_no_title)
            hasErrors = true
        } else {
            removeError(binding.cardReport.inputLayoutTitle)
        }

        if (TextUtils.isEmpty(binding.cardReport.inputDescription.text)) {
            setError(binding.cardReport.inputLayoutDescription, R.string.bug_report_no_description)
            hasErrors = true
        } else {
            removeError(binding.cardReport.inputLayoutDescription)
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

        val bugTitle = binding.cardReport.inputTitle.text.toString()
        val bugDescription = binding.cardReport.inputDescription.text.toString()

        val extraInfo = ExtraInfo()
        onSaveExtraInfo()

        val report = Report(bugTitle, bugDescription, deviceInfo, extraInfo)
        val target = GithubTarget("RetroMusicPlayer", "RetroMusicPlayer")

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
                    .setNegativeButton(android.R.string.cancel) { _, _ -> tryToFinishActivity() }
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
        private const val ISSUE_TRACKER_LINK = "https://github.com/RetroMusicPlayer/RetroMusicPlayer"
    }
}
