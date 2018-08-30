/*
 * Copyright (C) 2017. Alexander Bilchuk <a.bilchuk@sandrlab.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package code.name.monkey.retromusic.ui.adapter.album;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.views.MetalRecyclerViewPager;

public class AlbumFullWithAdapter extends
        MetalRecyclerViewPager.MetalAdapter<AlbumFullWithAdapter.FullMetalViewHolder> {

    private Activity activity;
    private List<Album> dataSet = new ArrayList<>();

    public AlbumFullWithAdapter(@NonNull Activity activity,
                                @NonNull DisplayMetrics metrics) {
        super(metrics);
        this.activity = activity;
    }

    public void swapData(ArrayList<Album> list) {
        dataSet = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FullMetalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);
        return new FullMetalViewHolder(viewItem);
    }

    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                h = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth()
                        : bitmap.get(i + 1).getWidth();
            }
            w += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0, left = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.d("HTML", "Combine: " + i + "/" + bitmap.size() + 1);

            top = (i == 0 ? 0 : top + bitmap.get(i).getHeight());
            left = (i == 0 ? 0 : top + bitmap.get(i).getWidth());
            canvas.drawBitmap(bitmap.get(i), left, 0f, null);
        }
        return temp;
    }

    @Override
    public void onBindViewHolder(@NonNull FullMetalViewHolder holder, int position) {
        // don't forget about calling supper.onBindViewHolder!
        super.onBindViewHolder(holder, position);

        final Album album = dataSet.get(position);

        if (holder.title != null) {
            holder.title.setText(getAlbumTitle(album));
        }
        if (holder.text != null) {
            holder.text.setText(getAlbumText(album));
        }
        if (holder.playSongs != null) {
            holder.playSongs.setOnClickListener(v -> MusicPlayerRemote.openQueue(album.songs, 0, true));
        }
        loadAlbumCover(album, holder);
    }

    private String getAlbumTitle(Album album) {
        return album.getTitle();
    }

    private String getAlbumText(Album album) {
        return album.getArtistName();
    }

    private void loadAlbumCover(Album album, FullMetalViewHolder holder) {
        if (holder.image == null) {
            return;
        }

        SongGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity).build()
                .into(new RetroMusicColoredTarget(holder.image) {
                    @Override
                    public void onColorReady(int color) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class FullMetalViewHolder extends MetalRecyclerViewPager.MetalViewHolder {

        FullMetalViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            Pair[] albumPairs = new Pair[]{
                    Pair.create(image, activity.getResources().getString(R.string.transition_album_art))};
            NavigationUtil.goToAlbum(activity, dataSet.get(getAdapterPosition()).getId(), albumPairs);
        }
    }
}