package code.name.monkey.retromusic.ui.activities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.Constants.USER_PROFILE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.util.Compressor
import code.name.monkey.retromusic.util.ImageUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UserInfoActivity : AbsBaseActivity() {

    private var disposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)


        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        setupToolbar()

        disposable = CompositeDisposable()

        bannerTitle.setTextColor(ThemeStore.textColorPrimary(this))
        nameContainer.boxStrokeColor = ThemeStore.accentColor(this)
        name!!.setText(PreferenceUtil.getInstance().userName)

        if (!PreferenceUtil.getInstance().profileImage.isEmpty()) {
            loadImageFromStorage(PreferenceUtil.getInstance().profileImage)
        }
        if (!PreferenceUtil.getInstance().bannerImage.isEmpty()) {
            loadBannerFromStorage(PreferenceUtil.getInstance().bannerImage)
        }
        bannerImage.setOnClickListener {
            MaterialDialog.Builder(this)
                    .title("Set a profile photo")
                    .items(Arrays.asList(getString(R.string.new_profile_photo), getString(R.string.remove_profile_photo)))
                    .itemsCallback { _, _, position, _ ->
                        when (position) {
                            0 -> pickNewPhoto()
                            1 -> PreferenceUtil.getInstance().saveProfileImage("")
                        }
                    }.show()
        }
        bannerSelect.setOnClickListener {
            showBannerOptions()
        }
        next.setOnClickListener {
            val nameString = name!!.text!!.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, "Umm name is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            PreferenceUtil.getInstance().userName = nameString
            setResult(Activity.RESULT_OK)
            finish()
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
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            setBackgroundColor(primaryColor)
        }
        appBarLayout.setBackgroundColor(primaryColor)
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this))
        title = null
        setSupportActionBar(toolbar)
    }

    private fun showBannerOptions() {

        MaterialDialog.Builder(this)
                .title(R.string.select_banner_photo)
                .items(Arrays.asList(getString(R.string.new_banner_photo),
                        getString(R.string.remove_banner_photo)))
                .itemsCallback { _, _, position, _ ->
                    when (position) {
                        0 -> selectBannerImage()
                        1 -> PreferenceUtil.getInstance().setBannerImagePath("")
                    }
                }.show()
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null &&
                data.data != null) {
            val uri = data.data
            try {
                val bitmap = ImageUtil.getResizedBitmap(Media.getBitmap(contentResolver, uri), PROFILE_ICON_SIZE)
                val profileImagePath = saveToInternalStorage(bitmap, USER_PROFILE)
                PreferenceUtil.getInstance().saveProfileImage(profileImagePath)
                loadImageFromStorage(profileImagePath)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        if (requestCode == PICK_BANNER_REQUEST && resultCode == Activity.RESULT_OK && data != null &&
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

        }
    }

    private fun loadBannerFromStorage(profileImagePath: String) {
        disposable!!.add(Compressor(this)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(File(profileImagePath, USER_BANNER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap -> bannerImage.setImageBitmap(bitmap) })
    }

    private fun loadImageFromStorage(path: String) {
        disposable!!.add(Compressor(this)
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

        private val PICK_IMAGE_REQUEST = 9002
        private val PICK_BANNER_REQUEST = 9003
        private val PROFILE_ICON_SIZE = 400
    }
}
