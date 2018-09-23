package code.name.monkey.retromusic.ui.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;
import code.name.monkey.retromusic.util.Compressor;
import code.name.monkey.retromusic.util.ImageUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.CircularImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static code.name.monkey.retromusic.Constants.INSTANCE;

public class UserInfoActivity extends AbsBaseActivity {
    private static final String TAG = "UserInfoActivity";

    private static final int PICK_IMAGE_REQUEST = 9002;
    private static final int PICK_BANNER_REQUEST = 9003;
    private static final int PROFILE_ICON_SIZE = 400;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.name_container)
    TextInputLayout nameLayout;

    @BindView(R.id.name)
    TextInputEditText name;

    @BindView(R.id.user_image)
    CircularImageView userImage;

    @BindView(R.id.banner_image)
    ImageView image;

    @BindView(R.id.next)
    FloatingActionButton nextButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    private CompositeDisposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        setupToolbar();

        disposable = new CompositeDisposable();

        title.setTextColor(ThemeStore.textColorPrimary(this));
        nameLayout.setBoxStrokeColor(ThemeStore.accentColor(this));
        name.setText(PreferenceUtil.getInstance(this).getUserName());

        if (!PreferenceUtil.getInstance(this).getProfileImage().isEmpty()) {
            loadImageFromStorage(PreferenceUtil.getInstance(this).getProfileImage());
        }
        if (!PreferenceUtil.getInstance(this).getBannerImage().isEmpty()) {
            loadBannerFromStorage(PreferenceUtil.getInstance(this).getBannerImage());
        }
    }

    private void setupToolbar() {
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this));
        nextButton.setBackgroundTintList(ColorStateList.valueOf(ThemeStore.accentColor(this)));
    }

    @OnClick({R.id.next, R.id.banner_select})
    void next(View view) {
        switch (view.getId()) {
            case R.id.banner_select:
                showBannerOptions();
                break;
            case R.id.next:
                String nameString = name.getText().toString().trim();
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, "Umm name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //noinspection ConstantConditions
                PreferenceUtil.getInstance(this).setUserName(nameString);
                setResult(RESULT_OK);
                //((UserInfoActivity) getActivity()).setFragment(new ChooseThemeFragment(), true);
                finish();
                break;
        }
    }

    private void showBannerOptions() {
        //noinspection ConstantConditions
        new MaterialDialog.Builder(this)
                .title(R.string.select_banner_photo)
                .items(Arrays.asList(getString(R.string.new_banner_photo),
                        getString(R.string.remove_banner_photo)))
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            selectBannerImage();
                            break;
                        case 1:
                            PreferenceUtil.getInstance(this).setBannerImagePath("");
                            break;
                    }
                }).show();
    }

    private void selectBannerImage() {
        //noinspection ConstantConditions
        if (PreferenceUtil.getInstance(this).getBannerImage().isEmpty()) {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*");
            pickImageIntent.putExtra("crop", "true");
            pickImageIntent.putExtra("outputX", 1290);
            pickImageIntent.putExtra("outputY", 720);
            pickImageIntent.putExtra("aspectX", 16);
            pickImageIntent.putExtra("aspectY", 9);
            pickImageIntent.putExtra("scale", true);
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(pickImageIntent,
                    "Select Picture"), PICK_BANNER_REQUEST);
        } else {
            PreferenceUtil.getInstance(this).setBannerImagePath("");
            image.setImageResource(android.R.color.transparent);
        }
    }

    @OnClick(R.id.user_image)
    public void onViewClicked() {
        //noinspection ConstantConditions
        new MaterialDialog.Builder(this)
                .title("Set a profile photo")
                .items(Arrays.asList(getString(R.string.new_profile_photo),
                        getString(R.string.remove_profile_photo)))
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            pickNewPhoto();
                            break;
                        case 1:
                            PreferenceUtil.getInstance(this).saveProfileImage("");
                            break;
                    }
                }).show();
    }

    private void pickNewPhoto() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("crop", "true");
        pickImageIntent.putExtra("outputX", 512);
        pickImageIntent.putExtra("outputY", 512);
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        startActivityForResult(Intent.createChooser(pickImageIntent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = ImageUtil.getResizedBitmap(Media.getBitmap(getContentResolver(), uri), PROFILE_ICON_SIZE);
                String profileImagePath = saveToInternalStorage(bitmap, Constants.USER_PROFILE);
                PreferenceUtil.getInstance(this).saveProfileImage(profileImagePath);
                loadImageFromStorage(profileImagePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_BANNER_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = Media.getBitmap(getContentResolver(), uri);
                String profileImagePath = saveToInternalStorage(bitmap, Constants.USER_BANNER);
                PreferenceUtil.getInstance(this).setBannerImagePath(profileImagePath);
                loadBannerFromStorage(profileImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBannerFromStorage(String profileImagePath) {
        disposable.add(new Compressor(this)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(new File(profileImagePath, Constants.USER_BANNER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    image.setImageBitmap(bitmap);
                }));
    }

    private void loadImageFromStorage(String path) {
        disposable.add(new Compressor(this)
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(new File(path,  Constants.USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    userImage.setImageBitmap(bitmap);
                }));
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String userBanner) {
        ContextWrapper cw = new ContextWrapper(this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, userBanner);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.WEBP, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
