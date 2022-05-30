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
package code.name.monkey.retromusic.activities

import android.Manifest
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.databinding.ActivityPermissionBinding
import code.name.monkey.retromusic.extensions.*

class PermissionActivity : AbsMusicServiceActivity() {
    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColorAuto()
        setTaskDescriptionColorAuto()
        setupTitle()

        binding.storagePermission.setButtonClick {
            requestPermissions()
        }
        if (VersionUtils.hasMarshmallow()) {
            binding.audioPermission.show()
            binding.audioPermission.setButtonClick {
                if (!hasAudioPermission()) {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = ("package:" + applicationContext.packageName).toUri()
                    startActivity(intent)
                }
            }
        }

        if (VersionUtils.hasS()) {
            binding.bluetoothPermission.show()
            binding.bluetoothPermission.setButtonClick {
                ActivityCompat.requestPermissions(this,
                    arrayOf(BLUETOOTH_CONNECT),
                    BLUETOOTH_PERMISSION_REQUEST)
            }
        } else {
            binding.audioPermission.setNumber("2")
        }

        binding.finish.accentBackgroundColor()
        binding.finish.setOnClickListener {
            if (hasPermissions()) {
                startActivity(
                    Intent(this, MainActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                )
                finish()
            }
        }
    }

    private fun setupTitle() {
        val color = accentColor()
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        val appName =
            getString(R.string.message_welcome,
                "<b>Retro <span  style='color:$hexColor';>Music</span></b>")
                .parseAsHtml()
        binding.appNameText.text = appName
    }

    override fun onResume() {
        super.onResume()
        binding.finish.isEnabled = hasStoragePermission()
        if (hasStoragePermission()) {
            binding.storagePermission.checkImage.isVisible = true
            binding.storagePermission.checkImage.imageTintList =
                ColorStateList.valueOf(accentColor())
        }
        if (VersionUtils.hasMarshmallow()) {
            if (hasAudioPermission()) {
                binding.audioPermission.checkImage.isVisible = true
                binding.audioPermission.checkImage.imageTintList =
                    ColorStateList.valueOf(accentColor())
            }
        }
        if (VersionUtils.hasS()) {
            if (hasBluetoothPermission()) {
                binding.bluetoothPermission.checkImage.isVisible = true
                binding.bluetoothPermission.checkImage.imageTintList =
                    ColorStateList.valueOf(accentColor())
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this,
            BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasAudioPermission(): Boolean {
        return Settings.System.canWrite(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
