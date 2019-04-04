package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialcab.MaterialCab;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.common.ATHToolbarActivity;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog;
import code.name.monkey.retromusic.helper.SortOrder;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks;
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment;
import code.name.monkey.retromusic.ui.fragments.base.AbsMainActivityFragment;
import code.name.monkey.retromusic.util.Compressor;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static code.name.monkey.retromusic.Constants.USER_PROFILE;

public class LibraryFragment extends AbsMainActivityFragment implements CabHolder, MainActivityFragmentCallbacks {

    public static final String TAG = "LibraryFragment";
    private static final String CURRENT_TAB_ID = "current_tab_id";

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TextView bannerTitle;
    private View contentContainer;

    private MaterialCab cab;
    private FragmentManager fragmentManager;
    private ImageView userImage;
    private CompositeDisposable disposable;

    @NonNull
    public static Fragment newInstance(int tab) {
        Bundle args = new Bundle();
        args.putInt(CURRENT_TAB_ID, tab);
        LibraryFragment fragment = new LibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static Fragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        disposable = new CompositeDisposable();
        contentContainer = view.findViewById(R.id.fragmentContainer);
        bannerTitle = view.findViewById(R.id.bannerTitle);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        toolbar = view.findViewById(R.id.toolbar);
        userImage = view.findViewById(R.id.userImage);
        userImage.setOnClickListener(v -> showMainMenu());

        loadImageFromStorage();
        return view;
    }

    private void loadImageFromStorage() {
        disposable.add(new Compressor(Objects.requireNonNull(getContext()))
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(new File(PreferenceUtil.getInstance().getProfileImage(), USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> userImage.setImageBitmap(bitmap),
                        throwable -> userImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_person_flat))));
    }

    public void setTitle(@StringRes int name) {
        bannerTitle.setText(getString(name));
    }

    public void addOnAppBarOffsetChangedListener(AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
    }

    public void removeOnAppBarOffsetChangedListener(AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener);
    }

    public int getTotalAppBarScrollingRange() {
        return appBarLayout.getTotalScrollRange();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStatusbarColorAuto(view);
        setupToolbar();
        inflateFragment();
    }

    private void inflateFragment() {
        if (getArguments() == null) {
            selectedFragment(SongsFragment.Companion.newInstance());
            return;
        }
        switch (getArguments().getInt(CURRENT_TAB_ID)) {
            default:
            case R.id.action_song:
                selectedFragment(SongsFragment.Companion.newInstance());
                break;
            case R.id.action_album:
                selectedFragment(AlbumsFragment.Companion.newInstance());
                break;
            case R.id.action_artist:
                selectedFragment(ArtistsFragment.Companion.newInstance());
                break;
            case R.id.action_playlist:
                selectedFragment(PlaylistsFragment.Companion.newInstance());
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupToolbar() {
        bannerTitle.setTextColor(ThemeStore.Companion.textColorPrimary(getContext()));

        int primaryColor = ThemeStore.Companion.primaryColor(getContext());
        TintHelper.setTintAuto(contentContainer, primaryColor, true);

        toolbar.setBackgroundColor(primaryColor);
        toolbar.setNavigationIcon(R.drawable.ic_search_white_24dp);
        appBarLayout.setBackgroundColor(primaryColor);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) ->
                getMainActivity().setLightStatusbar(!ATHUtil.INSTANCE.isWindowBackgroundDark(getContext())));
        getMainActivity().setTitle(null);
        getMainActivity().setSupportActionBar(toolbar);
    }

    private Fragment getCurrentFragment() {
        if (fragmentManager == null) {
            return SongsFragment.Companion.newInstance();
        }
        return fragmentManager.findFragmentByTag(LibraryFragment.TAG);
    }

    @Override
    public boolean handleBackPress() {
        if (cab != null && cab.isActive()) {
            cab.finish();
            return true;
        }
        return false;
    }

    private void selectedFragment(Fragment fragment) {
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .replace(R.id.fragmentContainer, fragment, TAG)
                .commit();
    }

    @NonNull
    @Override
    public MaterialCab openCab(int menuRes, @NonNull MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) {
            cab.finish();
        }
        //noinspection ConstantConditions
        cab = new MaterialCab(getMainActivity(), R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(
                        RetroColorUtil.shiftBackgroundColorForLightText(ThemeStore.Companion.primaryColor(getActivity())))
                .start(callback);
        return cab;
    }


    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof AbsLibraryPagerRecyclerViewCustomGridSizeFragment
                && currentFragment.isAdded()) {
            AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment = (AbsLibraryPagerRecyclerViewCustomGridSizeFragment) currentFragment;

            MenuItem gridSizeItem = menu.findItem(R.id.action_grid_size);
            if (RetroUtil.isLandscape()) {
                gridSizeItem.setTitle(R.string.action_grid_size_land);
            }
            setUpGridSizeMenu(fragment, gridSizeItem.getSubMenu());

            setUpSortOrderMenu(fragment, menu.findItem(R.id.action_sort_order).getSubMenu());

        } else {
            menu.add(0, R.id.action_new_playlist, 0, R.string.new_playlist_title).setIcon(R.drawable.ic_playlist_add_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.removeItem(R.id.action_grid_size);
        }
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(getActivity(), toolbar, menu, ATHToolbarActivity.getToolbarBackgroundColor(toolbar));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(activity, toolbar);
    }


    private void setUpSortOrderMenu(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
            @NonNull SubMenu sortOrderMenu) {
        String currentSortOrder = fragment.getSortOrder();
        sortOrderMenu.clear();

        if (fragment instanceof AlbumsFragment) {
            sortOrderMenu.add(0, R.id.action_album_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_A_Z));
            sortOrderMenu.add(0, R.id.action_album_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_Z_A));
            sortOrderMenu.add(0, R.id.action_album_sort_order_artist, 2, R.string.sort_order_artist)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_ARTIST));
            sortOrderMenu.add(0, R.id.action_album_sort_order_year, 3, R.string.sort_order_year)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_YEAR));
        } else if (fragment instanceof ArtistsFragment) {
            sortOrderMenu.add(0, R.id.action_artist_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SortOrder.ArtistSortOrder.ARTIST_A_Z));
            sortOrderMenu.add(0, R.id.action_artist_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SortOrder.ArtistSortOrder.ARTIST_Z_A));
        } else if (fragment instanceof SongsFragment) {
            sortOrderMenu.add(0, R.id.action_song_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_A_Z));
            sortOrderMenu.add(0, R.id.action_song_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_Z_A));
            sortOrderMenu.add(0, R.id.action_song_sort_order_artist, 2, R.string.sort_order_artist)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_ARTIST));
            sortOrderMenu.add(0, R.id.action_song_sort_order_album, 3, R.string.sort_order_album)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_ALBUM));
            sortOrderMenu.add(0, R.id.action_song_sort_order_year, 4, R.string.sort_order_year)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_YEAR));
            sortOrderMenu.add(0, R.id.action_song_sort_order_date, 5, R.string.sort_order_date)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_DATE));
            sortOrderMenu.add(0, R.id.action_song_sort_order_composer, 6, R.string.sort_order_composer)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.COMPOSER));
        }

        sortOrderMenu.setGroupCheckable(0, true, true);
    }

    private boolean handleSortOrderMenuItem(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment
                    fragment, @NonNull MenuItem item) {
        String sortOrder = null;
        if (fragment instanceof AlbumsFragment) {
            switch (item.getItemId()) {
                case R.id.action_album_sort_order_asc:
                    sortOrder = SortOrder.AlbumSortOrder.ALBUM_A_Z;
                    break;
                case R.id.action_album_sort_order_desc:
                    sortOrder = SortOrder.AlbumSortOrder.ALBUM_Z_A;
                    break;
                case R.id.action_album_sort_order_artist:
                    sortOrder = SortOrder.AlbumSortOrder.ALBUM_ARTIST;
                    break;
                case R.id.action_album_sort_order_year:
                    sortOrder = SortOrder.AlbumSortOrder.ALBUM_YEAR;
                    break;
            }
        } else if (fragment instanceof ArtistsFragment) {
            switch (item.getItemId()) {
                case R.id.action_artist_sort_order_asc:
                    sortOrder = SortOrder.ArtistSortOrder.ARTIST_A_Z;
                    break;
                case R.id.action_artist_sort_order_desc:
                    sortOrder = SortOrder.ArtistSortOrder.ARTIST_Z_A;
                    break;
            }
        } else if (fragment instanceof SongsFragment) {
            switch (item.getItemId()) {
                case R.id.action_song_sort_order_asc:
                    sortOrder = SortOrder.SongSortOrder.SONG_A_Z;
                    break;
                case R.id.action_song_sort_order_desc:
                    sortOrder = SortOrder.SongSortOrder.SONG_Z_A;
                    break;
                case R.id.action_song_sort_order_artist:
                    sortOrder = SortOrder.SongSortOrder.SONG_ARTIST;
                    break;
                case R.id.action_song_sort_order_album:
                    sortOrder = SortOrder.SongSortOrder.SONG_ALBUM;
                    break;
                case R.id.action_song_sort_order_year:
                    sortOrder = SortOrder.SongSortOrder.SONG_YEAR;
                    break;
                case R.id.action_song_sort_order_date:
                    sortOrder = SortOrder.SongSortOrder.SONG_DATE;
                    break;
                case R.id.action_song_sort_order_composer:
                    sortOrder = SortOrder.SongSortOrder.COMPOSER;
                    break;

            }
        }

        if (sortOrder != null) {
            item.setChecked(true);
            fragment.setAndSaveSortOrder(sortOrder);
            return true;
        }

        return false;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if (pager == null) return false;
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof AbsLibraryPagerRecyclerViewCustomGridSizeFragment) {
            AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment = (AbsLibraryPagerRecyclerViewCustomGridSizeFragment) currentFragment;
            if (handleGridSizeMenuItem(fragment, item)) {
                return true;
            }
            if (handleSortOrderMenuItem(fragment, item)) {
                return true;
            }
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new_playlist:
                CreatePlaylistDialog.Companion.create().show(getChildFragmentManager(), "CREATE_PLAYLIST");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpGridSizeMenu(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
            @NonNull SubMenu gridSizeMenu) {
        switch (fragment.getGridSize()) {
            case 1:
                gridSizeMenu.findItem(R.id.action_grid_size_1).setChecked(true);
                break;
            case 2:
                gridSizeMenu.findItem(R.id.action_grid_size_2).setChecked(true);
                break;
            case 3:
                gridSizeMenu.findItem(R.id.action_grid_size_3).setChecked(true);
                break;
            case 4:
                gridSizeMenu.findItem(R.id.action_grid_size_4).setChecked(true);
                break;
            case 5:
                gridSizeMenu.findItem(R.id.action_grid_size_5).setChecked(true);
                break;
            case 6:
                gridSizeMenu.findItem(R.id.action_grid_size_6).setChecked(true);
                break;
            case 7:
                gridSizeMenu.findItem(R.id.action_grid_size_7).setChecked(true);
                break;
            case 8:
                gridSizeMenu.findItem(R.id.action_grid_size_8).setChecked(true);
                break;
        }
        int maxGridSize = fragment.getMaxGridSize();
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(R.id.action_grid_size_8).setVisible(false);
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(R.id.action_grid_size_7).setVisible(false);
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(R.id.action_grid_size_6).setVisible(false);
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(R.id.action_grid_size_5).setVisible(false);
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(R.id.action_grid_size_4).setVisible(false);
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(R.id.action_grid_size_3).setVisible(false);
        }
    }


    private boolean handleGridSizeMenuItem(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment
                    fragment, @NonNull MenuItem item) {
        int gridSize = 0;
        switch (item.getItemId()) {
            case R.id.action_grid_size_1:
                gridSize = 1;
                break;
            case R.id.action_grid_size_2:
                gridSize = 2;
                break;
            case R.id.action_grid_size_3:
                gridSize = 3;
                break;
            case R.id.action_grid_size_4:
                gridSize = 4;
                break;
            case R.id.action_grid_size_5:
                gridSize = 5;
                break;
            case R.id.action_grid_size_6:
                gridSize = 6;
                break;
            case R.id.action_grid_size_7:
                gridSize = 7;
                break;
            case R.id.action_grid_size_8:
                gridSize = 8;
                break;
        }

        if (gridSize > 0) {
            item.setChecked(true);
            fragment.setAndSaveGridSize(gridSize);
            return true;
        }
        return false;
    }


}