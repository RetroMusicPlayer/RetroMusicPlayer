package code.name.monkey.retromusic.activities

import android.app.Activity
import android.content.*
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.FileProvider
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
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
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserInfoActivity : AbsBaseActivity() {

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        setupToolbar()

        MaterialUtil.setTint(nameContainer, false)
        MaterialUtil.setTint(bioContainer, false)
        name.setText(PreferenceUtil.getInstance().userName)
        bio.setText(PreferenceUtil.getInstance().userBio)

        if (!PreferenceUtil.getInstance().profileImage.isEmpty()) {
            loadImageFromStorage(PreferenceUtil.getInstance().profileImage)
        }
        if (!PreferenceUtil.getInstance().bannerImage.isEmpty()) {
            loadBannerFromStorage(PreferenceUtil.getInstance().bannerImage)
        }
        userImage.setOnClickListener {
            MaterialDialog(this, BottomSheet()).show {
                title(text = getString(R.string.set_photo))
                listItems(items = listOf(getString(R.string.new_profile_photo), getString(R.string.remove_profile_photo))) { _, position, _ ->
                    when (position) {
                        0 -> pickNewPhoto()
                        1 -> PreferenceUtil.getInstance().saveProfileImage("")
                    }
                }
            }
        }
        bannerSelect.setOnClickListener {
            showBannerOptions()
        }
        next.setOnClickListener {
            val nameString = name.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, "Umm name is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val bioString = bio.text.toString().trim() { it <= ' ' }
            if (TextUtils.isEmpty(bioString)) {
                Toast.makeText(this, "Umm bio is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            PreferenceUtil.getInstance().userName = nameString
            PreferenceUtil.getInstance().userBio = bioString
            setResult(Activity.RESULT_OK)
            finish()
        }
        next.backgroundTintList = ColorStateList.valueOf(ThemeStore.accentColor(this))
        ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(this, ColorUtil.isColorLight(ThemeStore.accentColor(this)))).apply {
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

    private fun setupToolbar() {
        val primaryColor = ThemeStore.primaryColor(this)
        applyToolbar(toolbar)
        appBarLayout.setBackgroundColor(primaryColor)
    }

    private fun showBannerOptions() {
        MaterialDialog(this, BottomSheet()).show {
            title(R.string.select_banner_photo)
            listItems(items = listOf(getString(R.string.new_banner_photo), getString(R.string.remove_banner_photo)))
            { _, position, _ ->
                when (position) {
                    0 -> selectBannerImage()
                    1 -> PreferenceUtil.getInstance().setBannerImagePath("")
                }
            }
        }
    }

    private fun selectBannerImage() {

        if (PreferenceUtil.getInstance().bannerImage.isEmpty()) {
            val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageIntent.type = "image/*"
            pickImageIntent.putExtra("crop", "true")
            pickImageIntent.putExtra("outputX", 1290)
            pickImageIntent.putExtra("outputY", 720)
            pickImageIntent.putExtra("aspectX", 16)
            pickImageIntent.putExtra("aspectY", 9)
            pickImageIntent.putExtra("scale", true)
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(pickImageIntent,
                    "Select Picture"), PICK_BANNER_REQUEST)
        } else {
            PreferenceUtil.getInstance().setBannerImagePath("")
            bannerImage.setImageResource(android.R.color.transparent)
        }
    }


    private fun pickNewPhoto() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"
        pickImageIntent.putExtra("crop", "true")
        pickImageIntent.putExtra("outputX", 512)
        pickImageIntent.putExtra("outputY", 512)
        pickImageIntent.putExtra("aspectX", 1)
        pickImageIntent.putExtra("aspectY", 1)
        pickImageIntent.putExtra("scale", true)
        startActivityForResult(Intent.createChooser(pickImageIntent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val uri = data!!.data
                    try {
                        val bitmap = getResizedBitmap(getBitmap(contentResolver, uri), PROFILE_ICON_SIZE)
                        val profileImagePath = saveToInternalStorage(bitmap, USER_PROFILE)
                        PreferenceUtil.getInstance().saveProfileImage(profileImagePath)
                        loadImageFromStorage(profileImagePath)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                CROP_IMAGE_REQUEST -> {
                    val extras: Bundle = data?.extras!!
                    val selectedBitmap: Bitmap = extras.getParcelable("data");
                    val profileImagePath = saveToInternalStorage(selectedBitmap, USER_PROFILE)
                    PreferenceUtil.getInstance().saveProfileImage(profileImagePath)
                    loadImageFromStorage(profileImagePath)
                }
                PICK_BANNER_REQUEST -> {
                    val uri = data?.data
                    try {
                        val bitmap = getBitmap(contentResolver, uri)
                        val profileImagePath = saveToInternalStorage(bitmap, USER_BANNER)
                        PreferenceUtil.getInstance().setBannerImagePath(profileImagePath)
                        loadBannerFromStorage(profileImagePath)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                CROP_BANNER_REQUEST -> {
                    val extras: Bundle = data?.extras!!
                    val selectedBitmap: Bitmap = extras.getParcelable("data");
                    val profileImagePath = saveToInternalStorage(selectedBitmap, USER_BANNER)
                    PreferenceUtil.getInstance().saveProfileImage(profileImagePath)
                    loadImageFromStorage(profileImagePath)
                }
            }
        }
        /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val picturePath = data.data
            performCrop(picturePath)
        } else if (requestCode == CROP_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                val bitmap = ImageUtil.getResizedBitmap(Media.getBitmap(contentResolver, uri), PROFILE_ICON_SIZE)
                val profileImagePath = saveToInternalStorage(bitmap, USER_PROFILE)
                PreferenceUtil.getInstance().saveProfileImage(profileImagePath)
                loadImageFromStorage(profileImagePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == PICK_BANNER_REQUEST && resultCode == Activity.RESULT_OK && data != null &&
                data.data != null) {
            val uri = data.data
            try {
                val bitmap = Media.getBitmap(contentResolver, uri)
                val profileImagePath = saveToInternalStorage(bitmap, USER_BANNER)
                PreferenceUtil.getInstance().setBannerImagePath(profileImagePath)
                loadBannerFromStorage(profileImagePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }*/
    }


    private fun getImagePathFromUri(aUri: Uri?): String? {
        var imagePath: String? = null
        if (aUri == null) {
            return imagePath
        }
        if (DocumentsContract.isDocumentUri(App.context, aUri)) {
            val documentId = DocumentsContract.getDocumentId(aUri)
            if ("com.android.providers.media.documents" == aUri.authority) {
                val id = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == aUri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(documentId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(aUri.scheme!!, ignoreCase = true)) {
            imagePath = getImagePath(aUri, null)
        } else if ("file".equals(aUri.scheme!!, ignoreCase = true)) {
            imagePath = aUri.path
        }
        return imagePath
    }

    private fun getImagePath(aUri: Uri, aSelection: String?): String? {
        var path: String? = null
        val cursor = App.context.contentResolver.query(aUri, null, aSelection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun performBannerCrop(picturePath: Uri?) {
        val photoUri = FileProvider.getUriForFile(this, "$packageName.provider", File(getImagePathFromUri(picturePath)))
        try {

            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(photoUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("return-data", true)
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(cropIntent, CROP_BANNER_REQUEST)
        } catch (anfe: ActivityNotFoundException) {
            val errorMessage = "your device doesn't support the crop action!"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun performCrop(imageUri: Uri) {
        val photoUri = FileProvider.getUriForFile(this, "$packageName.provider", File(getImagePathFromUri(imageUri)))
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(photoUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputX", 280)
            cropIntent.putExtra("outputY", 280)
            cropIntent.putExtra("return-data", true)
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST)
        } catch (anfe: ActivityNotFoundException) {
            val errorMessage = "your device doesn't support the crop action!"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadBannerFromStorage(profileImagePath: String) {
        disposable.add(Compressor(this)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(File(profileImagePath, USER_BANNER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap -> bannerImage.setImageBitmap(bitmap) })
    }

    private fun loadImageFromStorage(path: String) {
        disposable.add(Compressor(this)
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(File(path, USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap -> userImage!!.setImageBitmap(bitmap) })
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
        private const val CROP_IMAGE_REQUEST = 9003
        private const val PICK_BANNER_REQUEST = 9004
        private const val CROP_BANNER_REQUEST = 9005
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
