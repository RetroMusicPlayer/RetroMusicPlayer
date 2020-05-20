package code.name.monkey.retromusic.activities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.Images.Media.getBitmap
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.Constants.USER_PROFILE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.extensions.applyToolbar
import code.name.monkey.retromusic.util.Compressor
import code.name.monkey.retromusic.util.ImageUtil.getResizedBitmap
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        MaterialUtil.setTint(nameContainer, false)
        name.setText(PreferenceUtil.getInstance(this).userName)

        if (PreferenceUtil.getInstance(this).profileImage.isNotEmpty()) {
            loadImageFromStorage(PreferenceUtil.getInstance(this).profileImage)
        }
        if (PreferenceUtil.getInstance(this).bannerImage.isNotEmpty()) {
            loadBannerFromStorage(PreferenceUtil.getInstance(this).bannerImage)
        }
        userImage.setOnClickListener {
            MaterialDialog(this).show {
                cornerRadius(PreferenceUtil.getInstance(this@UserInfoActivity).dialogCorner)
                title(text = getString(R.string.set_photo))
                listItems(
                    items = listOf(
                        getString(R.string.new_profile_photo),
                        getString(R.string.remove_profile_photo)
                    )
                ) { _, position, _ ->
                    when (position) {
                        0 -> pickNewPhoto()
                        1 -> PreferenceUtil.getInstance(this@UserInfoActivity).saveProfileImage("")
                    }
                }
            }
        }
        bannerSelect.setOnClickListener {
            MaterialDialog(this).show {
                cornerRadius(PreferenceUtil.getInstance(this@UserInfoActivity).dialogCorner)
                title(R.string.select_banner_photo)
                listItems(
                    items = listOf(
                        getString(R.string.new_banner_photo),
                        getString(R.string.remove_banner_photo)
                    )
                )
                { _, position, _ ->
                    when (position) {
                        0 -> selectBannerImage()
                        1 -> PreferenceUtil.getInstance(this@UserInfoActivity)
                            .setBannerImagePath("")
                    }
                }
            }
        }
        next.setOnClickListener {
            val nameString = name.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, "Umm name is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            PreferenceUtil.getInstance(this).userName = nameString
            setResult(Activity.RESULT_OK)
            finish()
        }
        next.backgroundTintList = ColorStateList.valueOf(ThemeStore.accentColor(this))
        ColorStateList.valueOf(
            MaterialValueHelper.getPrimaryTextColor(
                this,
                ColorUtil.isColorLight(ThemeStore.accentColor(this))
            )
        )
            .apply {
                next.setTextColor(this)
                next.iconTint = this
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectBannerImage() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"
        //pickImageIntent.putExtra("crop", "true")
        pickImageIntent.putExtra("outputX", 1290)
        pickImageIntent.putExtra("outputY", 720)
        pickImageIntent.putExtra("aspectX", 16)
        pickImageIntent.putExtra("aspectY", 9)
        pickImageIntent.putExtra("scale", true)
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
            Intent.createChooser(pickImageIntent, "Select Picture"),
            PICK_BANNER_REQUEST
        )
    }

    private fun pickNewPhoto() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"
        pickImageIntent.putExtra("crop", "true")
        pickImageIntent.putExtra("outputX", 512)
        pickImageIntent.putExtra("outputY", 512)
        pickImageIntent.putExtra("aspectX", 1)
        pickImageIntent.putExtra("aspectY", 1)
        pickImageIntent.putExtra("scale", true)
        startActivityForResult(
            Intent.createChooser(pickImageIntent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    try {
                        data.data?.let {
                            val bitmap = getResizedBitmap(
                                getBitmap(contentResolver, it),
                                PROFILE_ICON_SIZE
                            )
                            val profileImagePath = saveToInternalStorage(bitmap, USER_PROFILE)
                            PreferenceUtil.getInstance(this).saveProfileImage(profileImagePath)
                            loadImageFromStorage(profileImagePath)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                PICK_BANNER_REQUEST -> {
                    try {
                        data.data?.let {
                            val bitmap = getBitmap(contentResolver, it)
                            val profileImagePath = saveToInternalStorage(bitmap, USER_BANNER)
                            PreferenceUtil.getInstance(this).setBannerImagePath(profileImagePath)
                            loadBannerFromStorage(profileImagePath)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getImagePath(aUri: Uri, aSelection: String?): String? {
        var path: String? = null
        val cursor = App.getContext().contentResolver.query(aUri, null, aSelection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun loadBannerFromStorage(profileImagePath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                val bitmap = Compressor(this@UserInfoActivity).setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(File(profileImagePath, USER_BANNER))
                withContext(Dispatchers.Main) { bannerImage.setImageBitmap(bitmap) }
            }
        }
    }

    private fun loadImageFromStorage(path: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                val bitmap = Compressor(this@UserInfoActivity)
                    .setMaxHeight(300)
                    .setMaxWidth(300)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(File(path, USER_PROFILE))
                withContext(Dispatchers.Main) { userImage.setImageBitmap(bitmap) }
            }
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap, userBanner: String): String {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myPath = File(directory, userBanner)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.WEBP, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

    companion object {

        private const val PICK_IMAGE_REQUEST = 9002
        private const val PICK_BANNER_REQUEST = 9004
        private const val PROFILE_ICON_SIZE = 400
    }
}

fun Activity.pickImage(requestCode: Int) {
    Intent(Intent.ACTION_GET_CONTENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
        startActivityForResult(this, requestCode)
    }
}
