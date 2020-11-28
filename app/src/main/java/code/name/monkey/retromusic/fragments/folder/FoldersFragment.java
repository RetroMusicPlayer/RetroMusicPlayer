/*
 * Copyright (c) 2020 Hemanth Savarala.
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

package code.name.monkey.retromusic.fragments.folder;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.adapter.SongFileAdapter;
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.menu.SongMenuHelper;
import code.name.monkey.retromusic.helper.menu.SongsMenuHelper;
import code.name.monkey.retromusic.interfaces.ICabHolder;
import code.name.monkey.retromusic.interfaces.ICallbacks;
import code.name.monkey.retromusic.interfaces.IMainActivityFragmentCallbacks;
import code.name.monkey.retromusic.misc.DialogAsyncTask;
import code.name.monkey.retromusic.misc.UpdateToastMediaScannerCompletionListener;
import code.name.monkey.retromusic.misc.WrappedAsyncTaskLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.DensityUtil;
import code.name.monkey.retromusic.util.FileUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.ThemedFastScroller;
import code.name.monkey.retromusic.views.BreadCrumbLayout;
import code.name.monkey.retromusic.views.ScrollingViewOnApplyWindowInsetsListener;
import me.zhanghai.android.fastscroll.FastScroller;

import static code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor;

public class FoldersFragment extends AbsMainActivityFragment
        implements IMainActivityFragmentCallbacks,
        ICabHolder,
        BreadCrumbLayout.SelectionCallback,
        ICallbacks,
        LoaderManager.LoaderCallbacks<List<File>> {

    public static final String TAG = FoldersFragment.class.getSimpleName();
    public static final FileFilter AUDIO_FILE_FILTER =
            file ->
                    !file.isHidden()
                            && (file.isDirectory()
                            || FileUtil.fileIsMimeType(file, "audio/*", MimeTypeMap.getSingleton())
                            || FileUtil.fileIsMimeType(file, "application/opus", MimeTypeMap.getSingleton())
                            || FileUtil.fileIsMimeType(file, "application/ogg", MimeTypeMap.getSingleton()));

    private static final String CRUMBS = "crumbs";
    private static final int LOADER_ID = 5;
    private SongFileAdapter adapter;
    private Toolbar toolbar;
    private TextView appNameText;
    private BreadCrumbLayout breadCrumbs;
    private MaterialCab cab;
    private View coordinatorLayout;
    private View empty;
    private TextView emojiText;
    private Comparator<File> fileComparator =
            (lhs, rhs) -> {
                if (lhs.isDirectory() && !rhs.isDirectory()) {
                    return -1;
                } else if (!lhs.isDirectory() && rhs.isDirectory()) {
                    return 1;
                } else {
                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }
            };
    private RecyclerView recyclerView;

    public FoldersFragment() {
        super(R.layout.fragment_folder);
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

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getMainActivity().addMusicServiceEventListener(getLibraryViewModel());
        getMainActivity().setBottomBarVisibility(View.VISIBLE);
        getMainActivity().setSupportActionBar(toolbar);
        getMainActivity().getSupportActionBar().setTitle(null);
        setStatusBarColorAuto(view);
        setUpAppbarColor();
        setUpBreadCrumbs();
        setUpRecyclerView();
        setUpAdapter();
        setUpTitle();
    }

    private void setUpTitle() {
        toolbar.setNavigationOnClickListener(
                v -> Navigation.findNavController(v).navigate(R.id.searchFragment, null, getNavOptions()));
        int color = ThemeStore.Companion.accentColor(requireContext());
        String hexColor = String.format("#%06X", 0xFFFFFF & color);
        Spanned appName =
                HtmlCompat.fromHtml(
                        "Retro <span  style='color:" + hexColor + ";'>Music</span>",
                        HtmlCompat.FROM_HTML_MODE_COMPACT);
        appNameText.setText(appName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            setCrumb(
                    new BreadCrumbLayout.Crumb(
                            FileUtil.safeGetCanonicalFile(PreferenceUtil.INSTANCE.getStartDirectory())),
                    true);
        } else {
            breadCrumbs.restoreFromStateWrapper(savedInstanceState.getParcelable(CRUMBS));
            LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveScrollPosition();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (breadCrumbs != null) {
            outState.putParcelable(CRUMBS, breadCrumbs.getStateWrapper());
        }
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
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        return new AsyncFileLoader(this);
    }

    @Override
    public void onCrumbSelection(BreadCrumbLayout.Crumb crumb, int index) {
        setCrumb(crumb, true);
    }

    @Override
    public void onFileMenuClicked(final File file, @NotNull View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        if (file.isDirectory()) {
            popupMenu.inflate(R.menu.menu_item_directory);
            popupMenu.setOnMenuItemClickListener(
                    item -> {
                        final int itemId = item.getItemId();
                        switch (itemId) {
                            case R.id.action_play_next:
                            case R.id.action_add_to_current_playing:
                            case R.id.action_add_to_playlist:
                            case R.id.action_delete_from_device:
                                new ListSongsAsyncTask(
                                        getActivity(),
                                        null,
                                        (songs, extra) -> {
                                            if (!songs.isEmpty()) {
                                                SongsMenuHelper.INSTANCE.handleMenuClick(
                                                        requireActivity(), songs, itemId);
                                            }
                                        })
                                        .execute(
                                                new ListSongsAsyncTask.LoadingInfo(
                                                        toList(file), AUDIO_FILE_FILTER, getFileComparator()));
                                return true;
                            case R.id.action_set_as_start_directory:
                                PreferenceUtil.INSTANCE.setStartDirectory(file);
                                Toast.makeText(
                                        getActivity(),
                                        String.format(getString(R.string.new_start_directory), file.getPath()),
                                        Toast.LENGTH_SHORT)
                                        .show();
                                return true;
                            case R.id.action_scan:
                                new ListPathsAsyncTask(getActivity(), this::scanPaths)
                                        .execute(new ListPathsAsyncTask.LoadingInfo(file, AUDIO_FILE_FILTER));
                                return true;
                        }
                        return false;
                    });
        } else {
            popupMenu.inflate(R.menu.menu_item_file);
            popupMenu.setOnMenuItemClickListener(
                    item -> {
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
                                new ListSongsAsyncTask(
                                        getActivity(),
                                        null,
                                        (songs, extra) ->
                                                SongMenuHelper.INSTANCE.handleMenuClick(
                                                        requireActivity(), songs.get(0), itemId))
                                        .execute(
                                                new ListSongsAsyncTask.LoadingInfo(
                                                        toList(file), AUDIO_FILE_FILTER, getFileComparator()));
                                return true;
                            case R.id.action_scan:
                                new ListPathsAsyncTask(getActivity(), this::scanPaths)
                                        .execute(new ListPathsAsyncTask.LoadingInfo(file, AUDIO_FILE_FILTER));
                                return true;
                        }
                        return false;
                    });
        }
        popupMenu.show();
    }

    @Override
    public void onFileSelected(@NotNull File file) {
        file = tryGetCanonicalFile(file); // important as we compare the path value later
        if (file.isDirectory()) {
            setCrumb(new BreadCrumbLayout.Crumb(file), true);
        } else {
            FileFilter fileFilter =
                    pathname -> !pathname.isDirectory() && AUDIO_FILE_FILTER.accept(pathname);
            new ListSongsAsyncTask(
                    getActivity(),
                    file,
                    (songs, extra) -> {
                        File file1 = (File) extra;
                        int startIndex = -1;
                        for (int i = 0; i < songs.size(); i++) {
                            if (file1
                                    .getPath()
                                    .equals(songs.get(i).getData())) { // path is already canonical here
                                startIndex = i;
                                break;
                            }
                        }
                        if (startIndex > -1) {
                            MusicPlayerRemote.openQueue(songs, startIndex, true);
                        } else {
                            final File finalFile = file1;
                            Snackbar.make(
                                    coordinatorLayout,
                                    Html.fromHtml(
                                            String.format(
                                                    getString(R.string.not_listed_in_media_store), file1.getName())),
                                    Snackbar.LENGTH_LONG)
                                    .setAction(
                                            R.string.action_scan,
                                            v ->
                                                    new ListPathsAsyncTask(requireActivity(), this::scanPaths)
                                                            .execute(
                                                                    new ListPathsAsyncTask.LoadingInfo(
                                                                            finalFile, AUDIO_FILE_FILTER)))
                                    .setActionTextColor(ThemeStore.Companion.accentColor(requireActivity()))
                                    .show();
                        }
                    })
                    .execute(
                            new ListSongsAsyncTask.LoadingInfo(
                                    toList(file.getParentFile()), fileFilter, getFileComparator()));
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<File>> loader, List<File> data) {
        updateAdapter(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<File>> loader) {
        updateAdapter(new LinkedList<File>());
    }

    @Override
    public void onMultipleItemAction(MenuItem item, @NotNull ArrayList<File> files) {
        final int itemId = item.getItemId();
        new ListSongsAsyncTask(
                getActivity(),
                null,
                (songs, extra) ->
                        SongsMenuHelper.INSTANCE.handleMenuClick(requireActivity(), songs, itemId))
                .execute(new ListSongsAsyncTask.LoadingInfo(files, AUDIO_FILE_FILTER, getFileComparator()));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, R.id.action_scan, 0, R.string.scan_media)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, R.id.action_go_to_start_directory, 1, R.string.action_go_to_start_directory)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.removeItem(R.id.action_grid_size);
        menu.removeItem(R.id.action_layout_type);
        menu.removeItem(R.id.action_sort_order);
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
                requireContext(), toolbar, menu, getToolbarBackgroundColor(toolbar));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_to_start_directory:
                setCrumb(
                        new BreadCrumbLayout.Crumb(
                                tryGetCanonicalFile(PreferenceUtil.INSTANCE.getStartDirectory())),
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
    public void onQueueChanged() {
        super.onQueueChanged();
        checkForPadding();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        checkForPadding();
    }

    @NonNull
    @Override
    public MaterialCab openCab(int menuRes, @NotNull MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) {
            cab.finish();
        }
        cab =
                new MaterialCab(getMainActivity(), R.id.cab_stub)
                        .setMenu(menuRes)
                        .setCloseDrawableRes(R.drawable.ic_close)
                        .setBackgroundColor(
                                RetroColorUtil.shiftBackgroundColorForLightText(
                                        ATHUtil.INSTANCE.resolveColor(requireContext(), R.attr.colorSurface)))
                        .start(callback);
        return cab;
    }

    private void checkForPadding() {
        final int count = adapter.getItemCount();
        final MarginLayoutParams params = (MarginLayoutParams) coordinatorLayout.getLayoutParams();
        params.bottomMargin =
                count > 0 && !MusicPlayerRemote.getPlayingQueue().isEmpty()
                        ? DensityUtil.dip2px(requireContext(), 104f)
                        : DensityUtil.dip2px(requireContext(), 54f);
    }

    private void checkIsEmpty() {
        emojiText.setText(getEmojiByUnicode(0x1F631));
        if (empty != null) {
            empty.setVisibility(
                    adapter == null || adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Nullable
    private BreadCrumbLayout.Crumb getActiveCrumb() {
        return breadCrumbs != null && breadCrumbs.size() > 0
                ? breadCrumbs.getCrumb(breadCrumbs.getActiveIndex())
                : null;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private Comparator<File> getFileComparator() {
        return fileComparator;
    }

    private void initViews(View view) {
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        breadCrumbs = view.findViewById(R.id.breadCrumbs);
        empty = view.findViewById(android.R.id.empty);
        emojiText = view.findViewById(R.id.emptyEmoji);
        toolbar = view.findViewById(R.id.toolbar);
        appNameText = view.findViewById(R.id.appNameText);
    }

    private void saveScrollPosition() {
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null) {
            crumb.setScrollPosition(
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        }
    }

    private void scanPaths(@Nullable String[] toBeScanned) {
        if (getActivity() == null) {
            return;
        }
        if (toBeScanned == null || toBeScanned.length < 1) {
            Toast.makeText(getActivity(), R.string.nothing_to_scan, Toast.LENGTH_SHORT).show();
        } else {
            MediaScannerConnection.scanFile(
                    getActivity().getApplicationContext(),
                    toBeScanned,
                    null,
                    new UpdateToastMediaScannerCompletionListener(getActivity(), Arrays.asList(toBeScanned)));
        }
    }

    private void setCrumb(BreadCrumbLayout.Crumb crumb, boolean addToHistory) {
        if (crumb == null) {
            return;
        }
        saveScrollPosition();
        breadCrumbs.setActiveOrAdd(crumb, false);
        if (addToHistory) {
            breadCrumbs.addHistory(crumb);
        }
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this);
    }

    private void setUpAdapter() {
        adapter =
                new SongFileAdapter(getMainActivity(), new LinkedList<>(), R.layout.item_list, this, this);
        adapter.registerAdapterDataObserver(
                new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        checkIsEmpty();
                        checkForPadding();
                    }
                });
        recyclerView.setAdapter(adapter);
        checkIsEmpty();
    }

    private void setUpAppbarColor() {
        breadCrumbs.setActivatedContentColor(
                ATHUtil.INSTANCE.resolveColor(requireContext(), android.R.attr.textColorPrimary));
        breadCrumbs.setDeactivatedContentColor(
                ATHUtil.INSTANCE.resolveColor(requireContext(), android.R.attr.textColorSecondary));
    }

    private void setUpBreadCrumbs() {
        breadCrumbs.setCallback(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FastScroller fastScroller = ThemedFastScroller.INSTANCE.create(recyclerView);
        recyclerView.setOnApplyWindowInsetsListener(
                new ScrollingViewOnApplyWindowInsetsListener(recyclerView, fastScroller));
    }

    private ArrayList<File> toList(File file) {
        ArrayList<File> files = new ArrayList<>(1);
        files.add(file);
        return files;
    }

    private void updateAdapter(@NonNull List<File> files) {
        adapter.swapDataSet(files);
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null && recyclerView != null) {
            ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .scrollToPositionWithOffset(crumb.getScrollPosition(), 0);
        }
    }

    public static class ListPathsAsyncTask
            extends ListingFilesDialogAsyncTask<ListPathsAsyncTask.LoadingInfo, String, String[]> {

        private WeakReference<OnPathsListedCallback> onPathsListedCallbackWeakReference;

        public ListPathsAsyncTask(Context context, OnPathsListedCallback callback) {
            super(context);
            onPathsListedCallbackWeakReference = new WeakReference<>(callback);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkCallbackReference();
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

    private static class AsyncFileLoader extends WrappedAsyncTaskLoader<List<File>> {

        private WeakReference<FoldersFragment> fragmentWeakReference;

        AsyncFileLoader(FoldersFragment foldersFragment) {
            super(foldersFragment.requireActivity());
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

    private static class ListSongsAsyncTask
            extends ListingFilesDialogAsyncTask<ListSongsAsyncTask.LoadingInfo, Void, List<Song>> {

        private final Object extra;
        private WeakReference<OnSongsListedCallback> callbackWeakReference;
        private WeakReference<Context> contextWeakReference;

        ListSongsAsyncTask(Context context, Object extra, OnSongsListedCallback callback) {
            super(context);
            this.extra = extra;
            contextWeakReference = new WeakReference<>(context);
            callbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        protected List<Song> doInBackground(LoadingInfo... params) {
            try {
                LoadingInfo info = params[0];
                List<File> files = FileUtil.listFilesDeep(info.files, info.fileFilter);

                if (isCancelled() || checkContextReference() == null || checkCallbackReference() == null) {
                    return null;
                }

                Collections.sort(files, info.fileComparator);

                Context context = checkContextReference();
                if (isCancelled() || context == null || checkCallbackReference() == null) {
                    return null;
                }

                return FileUtil.matchFilesWithMediaStore(context, files);
            } catch (Exception e) {
                e.printStackTrace();
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            OnSongsListedCallback callback = checkCallbackReference();
            if (songs != null && callback != null) {
                callback.onSongsListed(songs, extra);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkCallbackReference();
            checkContextReference();
        }

        private OnSongsListedCallback checkCallbackReference() {
            OnSongsListedCallback callback = callbackWeakReference.get();
            if (callback == null) {
                cancel(false);
            }
            return callback;
        }

        private Context checkContextReference() {
            Context context = contextWeakReference.get();
            if (context == null) {
                cancel(false);
            }
            return context;
        }

        public interface OnSongsListedCallback {

            void onSongsListed(@NonNull List<Song> songs, Object extra);
        }

        static class LoadingInfo {

            final Comparator<File> fileComparator;

            final FileFilter fileFilter;

            final List<File> files;

            LoadingInfo(
                    @NonNull List<File> files,
                    @NonNull FileFilter fileFilter,
                    @NonNull Comparator<File> fileComparator) {
                this.fileComparator = fileComparator;
                this.fileFilter = fileFilter;
                this.files = files;
            }
        }
    }

    private abstract static class ListingFilesDialogAsyncTask<Params, Progress, Result>
            extends DialogAsyncTask<Params, Progress, Result> {

        ListingFilesDialogAsyncTask(Context context) {
            super(context);
        }

        public ListingFilesDialogAsyncTask(Context context, int showDelay) {
            super(context, showDelay);
        }

        @Override
        protected Dialog createDialog(@NonNull Context context) {
            return new MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.listing_files)
                    .setCancelable(false)
                    .setView(R.layout.loading)
                    .setOnCancelListener(dialog -> cancel(false))
                    .setOnDismissListener(dialog -> cancel(false))
                    .create();
        }
    }
}
