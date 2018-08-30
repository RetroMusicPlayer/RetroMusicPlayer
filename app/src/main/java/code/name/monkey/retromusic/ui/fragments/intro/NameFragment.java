package code.name.monkey.retromusic.ui.fragments.intro;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog.Builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.Compressor;
import code.name.monkey.retromusic.util.ImageUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.CircularImageView;
import code.name.monkey.retromusic.views.IconImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static code.name.monkey.retromusic.Constants.USER_BANNER;
import static code.name.monkey.retromusic.Constants.USER_PROFILE;

public class NameFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 9002;
    private static final int PICK_BANNER_REQUEST = 9003;
    private static final int PROFILE_ICON_SIZE = 400;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.user_image)
    CircularImageView userImage;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.banner_select)
    IconImageView imageView;
    private Unbinder unbinder;
    private CompositeDisposable disposable;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_name, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //noinspection ConstantConditions
        name.setText(PreferenceUtil.getInstance(getActivity()).getUserName());
        if (!PreferenceUtil.getInstance(getActivity()).getProfileImage().isEmpty()) {
            loadImageFromStorage(PreferenceUtil.getInstance(getActivity()).getProfileImage());
        }
        if (!PreferenceUtil.getInstance(getActivity()).getBannerImage().isEmpty()) {
            loadBannerFromStorage(PreferenceUtil.getInstance(getActivity()).getBannerImage());
            imageView.setImageResource(R.drawable.ic_close_white_24dp);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
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
                    Toast.makeText(getActivity(), "Umm name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //noinspection ConstantConditions
                PreferenceUtil.getInstance(getActivity()).setUserName(nameString);
                getActivity().setResult(RESULT_OK);
                //((UserInfoActivity) getActivity()).setFragment(new ChooseThemeFragment(), true);
                getActivity().finish();
                break;
        }
    }

    private void showBannerOptions() {
        //noinspection ConstantConditions
        new Builder(getContext())
                .title(R.string.select_banner_photo)
                .items(Arrays.asList(getString(R.string.new_banner_photo),
                        getString(R.string.remove_banner_photo)))
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            selectBannerImage();
                            break;
                        case 1:
                            PreferenceUtil.getInstance(getContext()).setBannerImagePath("");
                            break;
                    }
                }).show();
    }

    private void selectBannerImage() {
        //noinspection ConstantConditions
        if (PreferenceUtil.getInstance(getActivity()).getBannerImage().isEmpty()) {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                    Media.EXTERNAL_CONTENT_URI);
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
            PreferenceUtil.getInstance(getContext()).setBannerImagePath("");
            image.setImageResource(android.R.color.transparent);
            imageView.setImageResource(R.drawable.ic_edit_white_24dp);
        }
    }

    @OnClick(R.id.image)
    public void onViewClicked() {
        //noinspection ConstantConditions
        new Builder(getContext())
                .title("Set a profile photo")
                .items(Arrays.asList(getString(R.string.new_profile_photo),
                        getString(R.string.remove_profile_photo)))
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            pickNewPhoto();
                            break;
                        case 1:
                            PreferenceUtil.getInstance(getContext()).saveProfileImage("");
                            break;
                    }
                }).show();
    }

    private void pickNewPhoto() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Bitmap bitmap = ImageUtil.getResizedBitmap(Media.getBitmap(getActivity()
                        .getContentResolver(), uri), PROFILE_ICON_SIZE);
                String profileImagePath = saveToInternalStorage(bitmap, USER_PROFILE);
                PreferenceUtil.getInstance(getActivity()).saveProfileImage(profileImagePath);
                loadImageFromStorage(profileImagePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_BANNER_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = Media.getBitmap(getActivity().getContentResolver(), uri);
                String profileImagePath = saveToInternalStorage(bitmap, USER_BANNER);
                PreferenceUtil.getInstance(getActivity()).setBannerImagePath(profileImagePath);
                loadBannerFromStorage(profileImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBannerFromStorage(String profileImagePath) {
        disposable.add(new Compressor(getActivity())
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(new File(profileImagePath, USER_BANNER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    image.setImageBitmap(bitmap);
                }));
    }

    private void loadImageFromStorage(String path) {
        disposable.add(new Compressor(getActivity())
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(new File(path, USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    userImage.setImageBitmap(bitmap);
                }));
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String userBanner) {
        ContextWrapper cw = new ContextWrapper(getActivity());
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
