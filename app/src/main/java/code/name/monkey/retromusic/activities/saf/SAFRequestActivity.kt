/*
 * Copyright (c) 2021 Bartlomiej Uliasz.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.activities.saf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import code.name.monkey.retromusic.activities.saf.SAFGuideActivity.REQUEST_CODE_SAF_GUIDE
import code.name.monkey.retromusic.util.SAFUtil

/** Created by buliasz on 2021-02-07.  */
class SAFRequestActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, code.name.monkey.retromusic.activities.saf.SAFGuideActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SAF_GUIDE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            REQUEST_CODE_SAF_GUIDE -> {
                SAFUtil.openTreePicker(this)
            }
            SAFUtil.REQUEST_SAF_PICK_TREE -> {
                if (resultCode == RESULT_OK) {
                    SAFUtil.saveTreeUri(this, intent)
                }
                finish()
            }
        }
    }
}