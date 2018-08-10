package code.name.monkey.retromusic.ui.fragments.mainactivity.folders;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialdialogs.MaterialDialog;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.common.ATHToolbarActivity;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.menu.SongMenuHelper;
import code.name.monkey.retromusic.helper.menu.SongsMenuHelper;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.interfaces.LoaderIds;
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks;
import code.name.monkey.retromusic.misc.DialogAsyncTask;
import code.name.monkey.retromusic.misc.UpdateToastMediaScannerCompletionListener;
import code.name.monkey.retromusic.misc.WrappedAsyncTaskLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.SongFileAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsMainActivityFragment;
import code.name.monkey.retromusic.util.FileUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.ViewUtil;
import code.name.monkey.retromusic.views.BreadCrumbLayout;

public class FoldersFragment extends AbsMainActivityFragment implements
        MainActivityFragmentCallbacks,
        CabHolder, BreadCrumbLayout.SelectionCallback, SongFileAdapter.Callbacks,
        AppBarLayout.OnOffsetChangedListener, LoaderManager.LoaderCallbacks<List<File>> {

    public static final String TAG = FoldersFragment.class.getSimpleName();
    public static final FileFilter AUDIO_FILE_FILTER = file -> !file.isHidden() && (file.isDirectory()
            ||
            FileUtil.fileIsMimeType(file, "audio/*", MimeTypeMap.getSingleton()) ||
            FileUtil.fileIsMimeType(file, "application/opus", MimeTypeMap.getSingleton()) ||
            FileUtil.fileIsMimeType(file, "application/ogg", MimeTypeMap.getSingleton()));

    protected static final String PATH = "path";
    protected static final String CRUMBS = "crumbs";
    private static final int LOADER_ID = LoaderIds.FOLDERS_FRAGMENT;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.container)
    View container;

    @BindView(R.id.title)
    TextView title;

    @BindView(android.R.id.empty)
    View empty;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bread_crumbs)
    BreadCrumbLayout breadCrumbs;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.recycler_view)
    FastScrollRecyclerView recyclerView;

    Comparator<File> fileComparator = (lhs, rhs) -> {
        if (lhs.isDirectory() && !rhs.isDirectory()) {
            return -1;
        } else if (!lhs.isDirectory() && rhs.isDirectory()) {
            return 1;
        } else {
            return lhs.getName().compareToIgnoreCase
                    (rhs.getName());
        }
    };

    private Unbinder unbinder;
    private SongFileAdapter adapter;
    private MaterialCab cab;

    public FoldersFragment() {
    }

    public static FoldersFragment newInstance(Context context) {
        return newInstance(PreferenceUtil.getInstance(context).getStartDirectory());
    }

    public static FoldersFragment newInstance(File directory) {
        FoldersFragment frag = new FoldersFragment();
        Bundle b = new Bundle();
        b.putSerializable(PATH, directory);
        frag.setArguments(b);
        return frag;
    }


    public static File getDefaultStartDirectory() {
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File startFolder;
        if (musicDir.exists() && musicDir.isDirectory()) {
            startFolder = musicDir;
        } else {
            File externalStorage = Environment.getExternalStorageDirectory();
            if (externalStorage.exists() && externalStorage.isDirectory()) {
                startFolder = externalStorage;
            } else {
                startFolder = new File("/"); // root
            }
        }
        return startFolder;
    }

    private static File tryGetCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }

    public void setCrumb(BreadCrumbLayout.Crumb crumb, boolean addToHistory) {
        if (crumb == null) {
            return;
        }
        saveScrollPosition();
        breadCrumbs.setActiveOrAdd(crumb, false);
        if (addToHistory) {
            breadCrumbs.addHistory(crumb);
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void saveScrollPosition() {
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null) {
            crumb.setScrollPosition(
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        }
    }

    @Nullable
    private BreadCrumbLayout.Crumb getActiveCrumb() {
        return breadCrumbs != null && breadCrumbs.size() > 0 ? breadCrumbs
                .getCrumb(breadCrumbs.getActiveIndex()) : null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CRUMBS, breadCrumbs.getStateWrapper());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            //noinspection ConstantConditions
            setCrumb(new BreadCrumbLayout.Crumb(
                    FileUtil.safeGetCanonicalFile((File) getArguments().getSerializable(PATH))), true);
        } else {
            breadCrumbs.restoreFromStateWrapper(savedInstanceState.getParcelable(CRUMBS));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setStatusbarColorAuto(view);
        getMainActivity().getSlidingUpPanelLayout().setShadowHeight(0);
        getMainActivity().setBottomBarVisibility(View.GONE);

        setUpAppbarColor();
        setUpToolbar();
        setUpBreadCrumbs();
        setUpRecyclerView();
        setUpAdapter();

    }

    private void setUpAppbarColor() {
        //noinspection ConstantConditions
        int primaryColor = ThemeStore.primaryColor(getActivity());
        TintHelper.setTintAuto(container, primaryColor, true);
        appbar.setBackgroundColor(primaryColor);
        toolbar.setBackgroundColor(primaryColor);
        //breadCrumbs.setBackgroundColor(primaryColor);
        breadCrumbs.setActivatedContentColor(ToolbarContentTintHelper.toolbarTitleColor(getActivity(), ColorUtil.darkenColor(primaryColor)));
        breadCrumbs.setDeactivatedContentColor(ToolbarContentTintHelper.toolbarSubtitleColor(getActivity(), ColorUtil.darkenColor(primaryColor)));
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> getMainActivity().setLightStatusbar(!ATHUtil.isWindowBackgroundDark(getContext())));
    }

    private void setUpToolbar() {
        //noinspection ConstantConditions
        title.setTextColor(ThemeStore.textColorPrimary(getContext()));
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        //noinspection ConstantConditions
        getActivity().setTitle(R.string.folders);
        getMainActivity().setSupportActionBar(toolbar);
    }

    private void setUpBreadCrumbs() {
        breadCrumbs.setCallback(this);
    }

    private void setUpRecyclerView() {
        //noinspection ConstantConditions
        ViewUtil.setUpFastScrollRecyclerViewColor(getActivity(), recyclerView,
                ThemeStore.accentColor(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appbar.addOnOffsetChangedListener(this);
    }

    private void setUpAdapter() {
        adapter = new SongFileAdapter(getMainActivity(), new LinkedList<File>(), R.layout.item_list,
                this, this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
            }
        });
        recyclerView.setAdapter(adapter);
        checkIsEmpty();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveScrollPosition();
    }

    @Override
    public void onDestroyView() {
        appbar.removeOnOffsetChangedListener(this);
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public boolean handleBackPress() {
        if (cab != null && cab.isActive()) {
            cab.finish();
            return true;
        }
        if (breadCrumbs != null && breadCrumbs.popHistory()) {
            setCrumb(breadCrumbs.lastHistory(), false);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public MaterialCab openCab(int menuRes, MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) {
            cab.finish();
        }
        cab = new MaterialCab(getMainActivity(), R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(RetroColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor
                        (getActivity())))
                .start(callback);
        return cab;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_folders, menu);
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(getActivity(), toolbar, menu,
                ATHToolbarActivity.getToolbarBackgroundColor(toolbar));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(getActivity(), toolbar);
    }

    @Override
    public void onCrumbSelection(BreadCrumbLayout.Crumb crumb, int index) {
        setCrumb(crumb, true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //noinspection ConstantConditions
                getActivity().onBackPressed();
                break;
            case R.id.action_go_to_start_directory:
                setCrumb(new BreadCrumbLayout.Crumb(
                                tryGetCanonicalFile(PreferenceUtil.getInstance(getActivity()).getStartDirectory())),
                        true);
                return true;
            case R.id.action_scan:
                BreadCrumbLayout.Crumb crumb = getActiveCrumb();
                if (crumb != null) {
                    //noinspection Convert2MethodRef
                    new ListPathsAsyncTask(getActivity(), paths -> scanPaths(paths))
                            .execute(new ListPathsAsyncTask.LoadingInfo(crumb.getFile(), AUDIO_FILE_FILTER));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFileSelected(File file) {
        file = tryGetCanonicalFile(file); // important as we compare the path value later
        if (file.isDirectory()) {
            setCrumb(new BreadCrumbLayout.Crumb(file), true);
        } else {
            FileFilter fileFilter = pathname -> !pathname.isDirectory() && AUDIO_FILE_FILTER
                    .accept(pathname);
            new ListSongsAsyncTask(getActivity(), file, (songs, extra) -> {
                File file1 = (File) extra;
                int startIndex = -1;
                for (int i = 0; i < songs.size(); i++) {
                    if (file1.getPath().equals(songs.get(i).data)) { // path is already canonical here
                        startIndex = i;
                        break;
                    }
                }
                if (startIndex > -1) {
                    MusicPlayerRemote.openQueue(songs, startIndex, true);
                } else {
                    final File finalFile = file1;
                    Snackbar.make(coordinatorLayout, Html.fromHtml(
                            String.format(getString(R.string.not_listed_in_media_store), file1.getName())),
                            Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_scan,
                                    v -> new ListPathsAsyncTask(getActivity(), paths -> scanPaths(paths))
                                            .execute(new ListPathsAsyncTask.LoadingInfo(finalFile, AUDIO_FILE_FILTER)))
                            .setActionTextColor(ThemeStore.accentColor(getActivity()))
                            .show();
                }
            }).execute(new ListSongsAsyncTask.LoadingInfo(toList(file.getParentFile()), fileFilter,
                    getFileComparator()));
        }
    }

    @Override
    public void onMultipleItemAction(MenuItem item, ArrayList<File> files) {
        final int itemId = item.getItemId();
        new ListSongsAsyncTask(getActivity(), null,
                (songs, extra) -> SongsMenuHelper.handleMenuClick(getActivity(), songs, itemId))
                .execute(new ListSongsAsyncTask.LoadingInfo(files, AUDIO_FILE_FILTER, getFileComparator()));
    }

    private ArrayList<File> toList(File file) {
        ArrayList<File> files = new ArrayList<>(1);
        files.add(file);
        return files;
    }

    private Comparator<File> getFileComparator() {
        return fileComparator;
    }

    @Override
    public void onFileMenuClicked(final File file, View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        if (file.isDirectory()) {
            popupMenu.inflate(R.menu.menu_item_directory);
            popupMenu.setOnMenuItemClickListener(item -> {
                final int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action_play_next:
                    case R.id.action_add_to_current_playing:
                    case R.id.action_add_to_playlist:
                    case R.id.action_delete_from_device:
                        new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> {
                            if (!songs.isEmpty()) {
                                SongsMenuHelper.handleMenuClick(getActivity(), songs, itemId);
                            }
                        }).execute(new ListSongsAsyncTask.LoadingInfo(toList(file), AUDIO_FILE_FILTER,
                                getFileComparator()));
                        return true;
                    case R.id.action_set_as_start_directory:
                        PreferenceUtil.getInstance(getActivity()).setStartDirectory(file);
                        Toast.makeText(getActivity(),
                                String.format(getString(R.string.new_start_directory), file.getPath()),
                                Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_scan:
                        new ListPathsAsyncTask(getActivity(), paths -> scanPaths(paths))
                                .execute(new ListPathsAsyncTask.LoadingInfo(file, AUDIO_FILE_FILTER));
                        return true;
                }
                return false;
            });
        } else {
            popupMenu.inflate(R.menu.menu_item_file);
            popupMenu.setOnMenuItemClickListener(item -> {
                final int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action_play_next:
                    case R.id.action_add_to_current_playing:
                    case R.id.action_add_to_playlist:
                    case R.id.action_go_to_album:
                    case R.id.action_go_to_artist:
                    case R.id.action_share:
                    case R.id.action_tag_editor:
                    case R.id.action_details:
                    case R.id.action_set_as_ringtone:
                    case R.id.action_delete_from_device:
                        new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> SongMenuHelper
                                .handleMenuClick(getActivity(), songs.get(0), itemId)).execute(
                                new ListSongsAsyncTask.LoadingInfo(toList(file), AUDIO_FILE_FILTER,
                                        getFileComparator()));
                        return true;
                    case R.id.action_scan:
                        new ListPathsAsyncTask(getActivity(), paths -> scanPaths(paths))
                                .execute(new ListPathsAsyncTask.LoadingInfo(file, AUDIO_FILE_FILTER));
                        return true;
                }
                return false;
            });
        }
        popupMenu.show();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        container.setPadding(container.getPaddingLeft(), container.getPaddingTop(),
                container.getPaddingRight(), appbar.getTotalScrollRange() + verticalOffset);
    }

    private void checkIsEmpty() {
        if (empty != null) {
            empty
                    .setVisibility(adapter == null || adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void scanPaths(@Nullable String[] toBeScanned) {
        if (getActivity() == null) {
            return;
        }
        if (toBeScanned == null || toBeScanned.length < 1) {
            Toast.makeText(getActivity(), R.string.nothing_to_scan, Toast.LENGTH_SHORT).show();
        } else {
            MediaScannerConnection.scanFile(getActivity().getApplicationContext(), toBeScanned, null,
                    new UpdateToastMediaScannerCompletionListener(getActivity(), toBeScanned));
        }
    }

    private void updateAdapter(@NonNull List<File> files) {
        adapter.swapDataSet(files);
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null && recyclerView != null) {
            ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .scrollToPositionWithOffset(crumb.getScrollPosition(), 0);
        }
    }

    @NonNull
    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        return new AsyncFileLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<File>> loader, List<File> data) {
        updateAdapter(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<File>> loader) {
        updateAdapter(new LinkedList<File>());
    }

    private static class AsyncFileLoader extends WrappedAsyncTaskLoader<List<File>> {

        private WeakReference<FoldersFragment> fragmentWeakReference;

        public AsyncFileLoader(FoldersFragment foldersFragment) {
            super(foldersFragment.getActivity());
            fragmentWeakReference = new WeakReference<>(foldersFragment);
        }

        @Override
        public List<File> loadInBackground() {
            FoldersFragment foldersFragment = fragmentWeakReference.get();
            File directory = null;
            if (foldersFragment != null) {
                BreadCrumbLayout.Crumb crumb = foldersFragment.getActiveCrumb();
                if (crumb != null) {
                    directory = crumb.getFile();
                }
            }
            if (directory != null) {
                List<File> files = FileUtil.listFiles(directory, AUDIO_FILE_FILTER);
                Collections.sort(files, foldersFragment.getFileComparator());
                return files;
            } else {
                return new LinkedList<>();
            }
        }
    }

    private static class ListSongsAsyncTask extends
            ListingFilesDialogAsyncTask<ListSongsAsyncTask.LoadingInfo, Void, ArrayList<Song>> {

        private final Object extra;
        private WeakReference<Context> contextWeakReference;
        private WeakReference<OnSongsListedCallback> callbackWeakReference;

        ListSongsAsyncTask(Context context, Object extra, OnSongsListedCallback callback) {
            super(context);
            this.extra = extra;
            contextWeakReference = new WeakReference<>(context);
            callbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkCallbackReference();
            checkContextReference();
        }

        @Override
        protected ArrayList<Song> doInBackground(LoadingInfo... params) {
            try {
                LoadingInfo info = params[0];
                List<File> files = FileUtil.listFilesDeep(info.files, info.fileFilter);

                if (isCancelled() || checkContextReference() == null
                        || checkCallbackReference() == null) {
                    return null;
                }

                Collections.sort(files, info.fileComparator);

                Context context = checkContextReference();
                if (isCancelled() || context == null || checkCallbackReference() == null) {
                    return null;
                }

                return FileUtil.matchFilesWithMediaStore(context, files).blockingFirst();
            } catch (Exception e) {
                e.printStackTrace();
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            super.onPostExecute(songs);
            OnSongsListedCallback callback = checkCallbackReference();
            if (songs != null && callback != null) {
                callback.onSongsListed(songs, extra);
            }
        }

        private Context checkContextReference() {
            Context context = contextWeakReference.get();
            if (context == null) {
                cancel(false);
            }
            return context;
        }

        private OnSongsListedCallback checkCallbackReference() {
            OnSongsListedCallback callback = callbackWeakReference.get();
            if (callback == null) {
                cancel(false);
            }
            return callback;
        }

        public interface OnSongsListedCallback {

            void onSongsListed(@NonNull ArrayList<Song> songs, Object extra);
        }

        static class LoadingInfo {

            final Comparator<File> fileComparator;
            final FileFilter fileFilter;
            final List<File> files;

            LoadingInfo(@NonNull List<File> files, @NonNull FileFilter fileFilter,
                        @NonNull Comparator<File> fileComparator) {
                this.fileComparator = fileComparator;
                this.fileFilter = fileFilter;
                this.files = files;
            }
        }
    }

    public static class ListPathsAsyncTask extends
            ListingFilesDialogAsyncTask<ListPathsAsyncTask.LoadingInfo, String, String[]> {

        private WeakReference<OnPathsListedCallback> onPathsListedCallbackWeakReference;

        public ListPathsAsyncTask(Context context, OnPathsListedCallback callback) {
            super(context);
            onPathsListedCallbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkCallbackReference();
        }

        @Override
        protected String[] doInBackground(LoadingInfo... params) {
            try {
                if (isCancelled() || checkCallbackReference() == null) {
                    return null;
                }

                LoadingInfo info = params[0];

                final String[] paths;

                if (info.file.isDirectory()) {
                    List<File> files = FileUtil.listFilesDeep(info.file, info.fileFilter);

                    if (isCancelled() || checkCallbackReference() == null) {
                        return null;
                    }

                    paths = new String[files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        File f = files.get(i);
                        paths[i] = FileUtil.safeGetCanonicalPath(f);

                        if (isCancelled() || checkCallbackReference() == null) {
                            return null;
                        }
                    }
                } else {
                    paths = new String[1];
                    paths[0] = info.file.getPath();
                }

                return paths;
            } catch (Exception e) {
                e.printStackTrace();
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] paths) {
            super.onPostExecute(paths);
            OnPathsListedCallback callback = checkCallbackReference();
            if (callback != null && paths != null) {
                callback.onPathsListed(paths);
            }
        }

        private OnPathsListedCallback checkCallbackReference() {
            OnPathsListedCallback callback = onPathsListedCallbackWeakReference.get();
            if (callback == null) {
                cancel(false);
            }
            return callback;
        }

        public interface OnPathsListedCallback {

            void onPathsListed(@NonNull String[] paths);
        }

        public static class LoadingInfo {

            public final File file;
            final FileFilter fileFilter;

            public LoadingInfo(File file, FileFilter fileFilter) {
                this.file = file;
                this.fileFilter = fileFilter;
            }
        }
    }

    private static abstract class ListingFilesDialogAsyncTask<Params, Progress, Result> extends
            DialogAsyncTask<Params, Progress, Result> {

        ListingFilesDialogAsyncTask(Context context) {
            super(context);
        }

        public ListingFilesDialogAsyncTask(Context context, int showDelay) {
            super(context, showDelay);
        }

        @Override
        protected Dialog createDialog(@NonNull Context context) {
            return new MaterialDialog.Builder(context)
                    .title(R.string.listing_files)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .cancelListener(dialog -> cancel(false))
                    .dismissListener(dialog -> cancel(false))
                    .negativeText(android.R.string.cancel)
                    .onNegative((dialog, which) -> cancel(false))
                    .show();
        }
    }
}
