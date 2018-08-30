package code.name.monkey.retromusic.preferences;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.fragments.AlbumCoverStyle;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.ViewUtil;


public class AlbumCoverStylePreferenceDialog extends DialogFragment implements
        ViewPager.OnPageChangeListener, MaterialDialog.SingleButtonCallback {
    public static final String TAG = AlbumCoverStylePreferenceDialog.class.getSimpleName();

    private DialogAction whichButtonClicked;
    private int viewPagerPosition;

    public static AlbumCoverStylePreferenceDialog newInstance() {
        return new AlbumCoverStylePreferenceDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.preference_dialog_now_playing_screen, null);
        ViewPager viewPager = view.findViewById(R.id.now_playing_screen_view_pager);
        viewPager.setAdapter(new AlbumCoverStyleAdapter(getActivity()));
        viewPager.addOnPageChangeListener(this);
        viewPager.setPageMargin((int) ViewUtil.convertDpToPixel(32, getResources()));
        viewPager.setCurrentItem(PreferenceUtil.getInstance(getActivity()).getAlbumCoverStyle().ordinal());

        return new MaterialDialog.Builder(getActivity())
                .title(R.string.pref_title_album_cover_style)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onAny(this)
                .customView(view, false)
                .build();
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog,
                        @NonNull DialogAction which) {
        whichButtonClicked = which;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (whichButtonClicked == DialogAction.POSITIVE) {
            AlbumCoverStyle nowPlayingScreen = AlbumCoverStyle.values()[viewPagerPosition];
            PreferenceUtil.getInstance(getActivity()).setAlbumCoverStyle(nowPlayingScreen);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.viewPagerPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private static class AlbumCoverStyleAdapter extends PagerAdapter {

        private Context context;

        AlbumCoverStyleAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {
            AlbumCoverStyle albumCoverStyle = AlbumCoverStyle.values()[position];

            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.preference_now_playing_screen_item, collection, false);
            collection.addView(layout);

            ImageView image = layout.findViewById(R.id.image);
            TextView title = layout.findViewById(R.id.title);
            Glide.with(context).load(albumCoverStyle.drawableResId).into(image);
            title.setText(albumCoverStyle.titleRes);

            return layout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup collection,
                                int position,
                                @NonNull Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return AlbumCoverStyle.values().length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return context.getString(AlbumCoverStyle.values()[position].titleRes);
        }
    }
}
