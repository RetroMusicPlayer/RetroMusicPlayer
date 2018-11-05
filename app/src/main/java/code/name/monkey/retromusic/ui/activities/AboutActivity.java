package code.name.monkey.retromusic.ui.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Contributor;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;
import code.name.monkey.retromusic.ui.adapter.ContributorAdapter;
import code.name.monkey.retromusic.util.NavigationUtil;

import static code.name.monkey.retromusic.Constants.APP_INSTAGRAM_LINK;
import static code.name.monkey.retromusic.Constants.APP_TWITTER_LINK;
import static code.name.monkey.retromusic.Constants.GITHUB_PROJECT;
import static code.name.monkey.retromusic.Constants.GOOGLE_PLUS_COMMUNITY;
import static code.name.monkey.retromusic.Constants.RATE_ON_GOOGLE_PLAY;
import static code.name.monkey.retromusic.Constants.TELEGRAM_CHANGE_LOG;
import static code.name.monkey.retromusic.Constants.TRANSLATE;

/**
 * @author Hemanth S (h4h13)
 */

public class AboutActivity extends AbsBaseActivity {

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_version)
    TextView appVersion;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setLightNavigationBar(true);

        loadContributors();
        setUpToolbar();

        appVersion.setText(getAppVersion());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(null);
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this));
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    @OnClick({R.id.app_github, R.id.faq_link, R.id.app_google_plus, R.id.app_translation,
            R.id.app_rate, R.id.app_share, R.id.instagram_link, R.id.twitter_link, R.id.changelog,
            R.id.open_source, R.id.discord_link, R.id.telegram_link, R.id.donate_link})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.faq_link:
                openUrl(Constants.FAQ_LINK);
                break;
            case R.id.telegram_link:
                openUrl(Constants.APP_TELEGRAM_LINK);
                break;
            case R.id.discord_link:
                openUrl(Constants.DISCORD_LINK);
                break;
            case R.id.app_github:
                openUrl(GITHUB_PROJECT);
                break;
            case R.id.app_google_plus:
                openUrl(GOOGLE_PLUS_COMMUNITY);
                break;
            case R.id.app_translation:
                openUrl(TRANSLATE);
                break;
            case R.id.app_rate:
                openUrl(RATE_ON_GOOGLE_PLAY);
                break;
            case R.id.app_share:
                shareApp();
                break;
            case R.id.donate_link:
                NavigationUtil.goToSupportDevelopment(this);
                break;
            case R.id.instagram_link:
                openUrl(APP_INSTAGRAM_LINK);
                break;
            case R.id.twitter_link:
                openUrl(APP_TWITTER_LINK);
                break;
            case R.id.changelog:
                showChangeLogOptions();
                break;
            case R.id.open_source:
                NavigationUtil.goToOpenSource(this);
                break;
        }
    }

    private void showChangeLogOptions() {
        new MaterialDialog.Builder(this)
                .items(new String[]{"Telegram Channel", "App"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (position == 0) {
                            openUrl(TELEGRAM_CHANGE_LOG);
                        } else {
                            NavigationUtil.gotoWhatNews(AboutActivity.this);
                        }
                    }
                })
                .build()
                .show();
    }

    private String getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0.0";
        }
    }

    private void shareApp() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("songText/plain")
                .setText(String.format(getString(R.string.app_share), getPackageName()))
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(
                    Intent.createChooser(shareIntent, getResources().getText(R.string.action_share)));
        }
    }

    public void loadContributors() {
        String data = getAssetJsonData();
        Type type = new TypeToken<List<Contributor>>() {
        }.getType();
        List<Contributor> contributors = new Gson().fromJson(data, type);

        ContributorAdapter contributorAdapter = new ContributorAdapter(contributors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contributorAdapter);
    }

    public String getAssetJsonData() {
        String json = null;
        try {
            InputStream is = getAssets().open("contributors.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
