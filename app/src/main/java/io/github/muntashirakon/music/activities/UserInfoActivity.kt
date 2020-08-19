package io.github.muntashirakon.music.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import io.github.muntashirakon.music.Constants.USER_BANNER
import io.github.muntashirakon.music.Constants.USER_PROFILE
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.activities.base.AbsBaseActivity
import io.github.muntashirakon.music.extensions.accentColor
import io.github.muntashirakon.music.extensions.applyToolbar
import io.github.muntashirakon.music.glide.ProfileBannerGlideRequest
import io.github.muntashirakon.music.glide.UserProfileGlideRequest
import io.github.muntashirakon.music.util.ImageUtil
import io.github.muntashirakon.music.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserInfoActivity : AbsBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)
        applyToolbar(toolbar)

        nameContainer.accentColor()
        name.setText(PreferenceUtil.userName)

        userImage.setOnClickListener {
            pickNewPhoto()
        }

        bannerSelect.setOnClickListener {
            selectBannerImage()
        }

        next.setOnClickListener {
            val nameString = name.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, "Umm you're name can't be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            PreferenceUtil.userName = nameString
            setResult(Activity.RESULT_OK)
            finish()
        }

        val textColor =
            MaterialValueHelper.getPrimaryTextColor(this, ColorUtil.isColorLight(accentColor()))
        next.backgroundTintList = ColorStateList.valueOf(accentColor())
        next.iconTint = ColorStateList.valueOf(textColor)
        next.setTextColor(textColor)
        loadProfile()
    }

    private fun loadProfile() {
        bannerImage?.let {
            ProfileBannerGlideRequest.Builder.from(
                Glide.with(this),
                ProfileBannerGlideRequest.getBannerModel()
            ).build().into(it)
        }
        UserProfileGlideRequest.Builder.from(
            Glide.with(this),
            UserProfileGlideRequest.getUserModel()
        ).build().into(userImage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectBannerImage() {
        ImagePicker.with(this)
            .compress(1440)
            .provider(ImageProvider.GALLERY)
            .crop(16f, 9f)
            .start(PICK_BANNER_REQUEST)
    }

    private fun pickNewPhoto() {
        ImagePicker.with(this)
            .provider(ImageProvider.GALLERY)
            .cropSquare()
            .compress(1440)
            .start(PICK_IMAGE_REQUEST)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val fileUri = data?.data
            fileUri?.let { setAndSaveUserImage(it) }
        } else if (resultCode == Activity.RESULT_OK && requestCode == PICK_BANNER_REQUEST) {
            val fileUri = data?.data
            fileUri?.let { setAndSaveBannerImage(it) }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAndSaveBannerImage(fileUri: Uri) {
        Glide.with(this)
            .load(fileUri)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Any, Bitmap> {
                override fun onException(
                    e: java.lang.Exception?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFromMemoryCache: Boolean,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let { saveImage(it, USER_BANNER) }
                    return false
                }
            })
            .into(bannerImage)
    }

    private fun saveImage(bitmap: Bitmap, fileName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val appDir = applicationContext.filesDir
            val file = File(appDir, fileName)
            var successful = false
            try {
                val os = BufferedOutputStream(FileOutputStream(file))
                successful = ImageUtil.resizeBitmap(bitmap, 2048)
                    .compress(Bitmap.CompressFormat.WEBP, 100, os)
                withContext(Dispatchers.IO) { os.close() }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (successful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserInfoActivity, "Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAndSaveUserImage(fileUri: Uri) {
        Glide.with(this)
            .load(fileUri)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Any, Bitmap> {
                override fun onException(
                    e: java.lang.Exception?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFromMemoryCache: Boolean,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let { saveImage(it, USER_PROFILE) }
                    return false
                }
            })
            .into(userImage)
    }


    companion object {
        private const val PICK_IMAGE_REQUEST = 9002
        private const val PICK_BANNER_REQUEST = 9004
    }
}