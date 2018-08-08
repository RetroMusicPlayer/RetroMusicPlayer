package code.name.monkey.retromusic.ui.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.retromusic.BuildConfig;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;
import code.name.monkey.retromusic.util.RetroUtil;
import code.name.monkey.retromusic.views.IconImageView;

import static code.name.monkey.retromusic.Constants.PAYPAL_ME_URL;

/**
 * @author Hemanth S (h4h13).
 */

public class SupportDevelopmentActivity extends AbsBaseActivity implements BillingProcessor.IBillingHandler {
    public static final String TAG = SupportDevelopmentActivity.class.getSimpleName();
    private static final int DONATION_PRODUCT_IDS = R.array.donation_ids;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.progress_container)
    View mProgressContainer;

    @BindView(R.id.list)
    RecyclerView mListView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.root)
    ViewGroup mViewGroup;

    @BindView(R.id.title)
    TextView title;
    private BillingProcessor mBillingProcessor;
    private AsyncTask skuDetailsLoadAsyncTask;

    private static List<SkuDetails> getDetails() {
        List<SkuDetails> skuDetails = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < 6; i++) {
                jsonObject.put("songTitle", "Coffee");
                jsonObject.put("price", "$100");
                jsonObject.put("description", "" + i);
                skuDetails.add(new SkuDetails(jsonObject));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return skuDetails;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void donate(int i) {
        final String[] ids = getResources().getStringArray(DONATION_PRODUCT_IDS);
        mBillingProcessor.purchase(this, ids[i]);
    }

    @OnClick(R.id.donate)
    void donate() {
        RetroUtil.openUrl(this, PAYPAL_ME_URL);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);


        setupToolbar();

        mBillingProcessor
                = new BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSE_KEY, this);
        MDTintHelper.setTint(mProgressBar, ThemeStore.accentColor(this));

        ((TextView) findViewById(R.id.donation)).setTextColor(ThemeStore.accentColor(this));
    }

    private void setupToolbar() {
        title.setTextColor(ThemeStore.textColorPrimary(this));
        int primaryColor = ThemeStore.primaryColor(this);
        mAppBarLayout.setBackgroundColor(primaryColor);
        mToolbar.setBackgroundColor(primaryColor);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(mToolbar);
        setTitle(null);
    }

    @Override
    public void onBillingInitialized() {
        loadSkuDetails();
    }

    private void loadSkuDetails() {
        if (skuDetailsLoadAsyncTask != null) {
            skuDetailsLoadAsyncTask.cancel(false);
        }
        skuDetailsLoadAsyncTask = new SkuDetailsLoadAsyncTask(this).execute();

    }

    @Override
    public void onProductPurchased(@NonNull String productId, TransactionDetails details) {
        //loadSkuDetails();
        Toast.makeText(this, R.string.thank_you, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.e(TAG, "Billing error: code = " + errorCode, error);
    }

    @Override
    public void onPurchaseHistoryRestored() {
        //loadSkuDetails();
        Toast.makeText(this, R.string.restored_previous_purchases, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mBillingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (mBillingProcessor != null) {
            mBillingProcessor.release();
        }
        if (skuDetailsLoadAsyncTask != null) {
            skuDetailsLoadAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    private static class SkuDetailsLoadAsyncTask extends AsyncTask<Void, Void, List<SkuDetails>> {
        private final WeakReference<SupportDevelopmentActivity> donationDialogWeakReference;

        SkuDetailsLoadAsyncTask(SupportDevelopmentActivity donationsDialog) {
            this.donationDialogWeakReference = new WeakReference<>(donationsDialog);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SupportDevelopmentActivity dialog = donationDialogWeakReference.get();
            if (dialog == null) return;

            dialog.mProgressContainer.setVisibility(View.VISIBLE);
            dialog.mListView.setVisibility(View.GONE);
        }

        @Override
        protected List<SkuDetails> doInBackground(Void... params) {
            SupportDevelopmentActivity dialog = donationDialogWeakReference.get();
            if (dialog != null) {
                final String[] ids = dialog.getResources().getStringArray(DONATION_PRODUCT_IDS);
                return dialog.mBillingProcessor.getPurchaseListingDetails(new ArrayList<>(Arrays.asList(ids)));
            }
            cancel(false);
            return null;
        }

        @Override
        protected void onPostExecute(List<SkuDetails> skuDetails) {
            super.onPostExecute(skuDetails);
            SupportDevelopmentActivity dialog = donationDialogWeakReference.get();
            if (dialog == null) return;

            if (skuDetails == null || skuDetails.isEmpty()) {
                dialog.mProgressContainer.setVisibility(View.GONE);
                return;
            }

            //noinspection ConstantConditions
            dialog.mProgressContainer.setVisibility(View.GONE);
            dialog.mListView.setItemAnimator(new DefaultItemAnimator());
            dialog.mListView.setLayoutManager(new GridLayoutManager(dialog, 2));
            dialog.mListView.setAdapter(new SkuDetailsAdapter(dialog, skuDetails));
            dialog.mListView.setVisibility(View.VISIBLE);
        }


    }

    static class SkuDetailsAdapter extends RecyclerView.Adapter<SkuDetailsAdapter.ViewHolder> {
        @LayoutRes
        private static int LAYOUT_RES_ID = R.layout.item_donation_option;

        SupportDevelopmentActivity donationsDialog;
        List<SkuDetails> skuDetailsList = new ArrayList<>();

        SkuDetailsAdapter(@NonNull SupportDevelopmentActivity donationsDialog, @NonNull List<SkuDetails> objects) {
            this.donationsDialog = donationsDialog;
            skuDetailsList = objects;
        }

        private static void strikeThrough(TextView textView, boolean strikeThrough) {
            textView.setPaintFlags(strikeThrough ? textView.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG : textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        private int getIcon(int position) {
            switch (position) {
                case 0:
                    return R.drawable.ic_cookie_white_24dp;
                case 1:
                    return R.drawable.ic_take_away_white_24dp;
                case 2:
                    return R.drawable.ic_take_away_coffe_white_24dp;
                case 3:
                    return R.drawable.ic_beer_white_24dp;
                case 4:
                    return R.drawable.ic_fast_food_meal_white_24dp;
                case 5:
                    return R.drawable.ic_popcorn_white_24dp;
                case 6:
                    return R.drawable.ic_card_giftcard_white_24dp;
                default:
                    return R.drawable.ic_card_giftcard_white_24dp;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(donationsDialog)
                    .inflate(LAYOUT_RES_ID, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            SkuDetails skuDetails = skuDetailsList.get(i);
            if (skuDetails != null) {
                viewHolder.title.setText(skuDetails.title.replace("(Retro Music Player)", "").trim());
                viewHolder.text.setText(skuDetails.description);
                viewHolder.text.setVisibility(View.GONE);
                viewHolder.price.setText(skuDetails.priceText);
                viewHolder.image.setImageResource(getIcon(i));

                final boolean purchased = donationsDialog.mBillingProcessor.isPurchased(skuDetails.productId);
                int titleTextColor = purchased ? ATHUtil.resolveColor(donationsDialog, android.R.attr.textColorHint) : ThemeStore.textColorPrimary(donationsDialog);
                int contentTextColor = purchased ? titleTextColor : ThemeStore.textColorSecondary(donationsDialog);

                //noinspection ResourceAsColor
                viewHolder.title.setTextColor(titleTextColor);
                viewHolder.text.setTextColor(contentTextColor);
                viewHolder.price.setTextColor(titleTextColor);

                strikeThrough(viewHolder.title, purchased);
                strikeThrough(viewHolder.text, purchased);
                strikeThrough(viewHolder.price, purchased);

                viewHolder.itemView.setOnTouchListener((v, event) -> purchased);
                viewHolder.itemView.setOnClickListener(v -> donationsDialog.donate(i));
            }
        }

        @Override
        public int getItemCount() {
            return skuDetailsList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.title)
            TextView title;
            @BindView(R.id.text)
            TextView text;
            @BindView(R.id.price)
            TextView price;
            @BindView(R.id.image)
            IconImageView image;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
