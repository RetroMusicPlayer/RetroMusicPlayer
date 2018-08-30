package code.name.monkey.retromusic.ui.activities;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.EqualizerHelper;
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity;

/**
 * @author Hemanth S (h4h13).
 */

public class EqualizerActivity extends AbsMusicServiceActivity implements
        AdapterView.OnItemSelectedListener {

    @BindView(R.id.equalizer)
    SwitchCompat mEnable;

    @BindView(R.id.content)
    LinearLayout mContent;

    @BindView(R.id.bands)
    LinearLayout mLinearLayout;

    @BindView(R.id.bass_boost_strength)
    SeekBar mBassBoostStrength;

    @BindView(R.id.virtualizer_strength)
    SeekBar mVirtualizerStrength;

    @BindView(R.id.bass_boost)
    TextView mBassBoost;

    @BindView(R.id.virtualizer)
    TextView mVirtualizer;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @BindView(R.id.presets)
    Spinner mPresets;

    @BindView(R.id.title)
    TextView mTitle;

    private CompoundButton.OnCheckedChangeListener mListener = (buttonView, isChecked) -> {
        switch (buttonView.getId()) {
            case R.id.equalizer:
                EqualizerHelper.getInstance().getEqualizer().setEnabled(isChecked);
                TransitionManager.beginDelayedTransition(mContent);
                mContent.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
        }
    };
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (seekBar == mBassBoostStrength) {
                    mBassBoost.setEnabled(progress > 0);
                    EqualizerHelper.getInstance().setBassBoostStrength(progress);
                    EqualizerHelper.getInstance().setBassBoostEnabled(progress > 0);
                } else if (seekBar == mVirtualizerStrength) {
                    mVirtualizer.setEnabled(progress > 0);
                    EqualizerHelper.getInstance().setVirtualizerEnabled(progress > 0);
                    EqualizerHelper.getInstance().setVirtualizerStrength(progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private ArrayAdapter<String> mPresetsNamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        setupToolbar();

        mEnable.setChecked(EqualizerHelper.getInstance().getEqualizer().getEnabled());
        mEnable.setOnCheckedChangeListener(mListener);

        mPresetsNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mPresets.setAdapter(mPresetsNamesAdapter);
        mPresets.setOnItemSelectedListener(this);

        mBassBoostStrength.setProgress(EqualizerHelper.getInstance().getBassBoostStrength());
        mBassBoostStrength.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mVirtualizerStrength.setProgress(EqualizerHelper.getInstance().getVirtualizerStrength());
        mVirtualizerStrength.setOnSeekBarChangeListener(mSeekBarChangeListener);

        setupUI();
        addPresets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        mTitle.setTextColor(ThemeStore.textColorPrimary(this));
        int primaryColor = ThemeStore.primaryColor(this);
        mToolbar.setBackgroundColor(primaryColor);
        mAppBar.setBackgroundColor(primaryColor);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(mToolbar);
        setTitle(null);
    }

    private void addPresets() {
        mPresetsNamesAdapter.clear();
        mPresetsNamesAdapter.add("Custom");
        for (int j = 0; j < EqualizerHelper.getInstance().getEqualizer().getNumberOfPresets(); j++) {
            mPresetsNamesAdapter
                    .add(EqualizerHelper.getInstance().getEqualizer().getPresetName((short) j));
            mPresetsNamesAdapter.notifyDataSetChanged();
        }
        mPresets
                .setSelection((int) EqualizerHelper.getInstance().getEqualizer().getCurrentPreset() + 1);
    }

    private void setupUI() {
        mLinearLayout.removeAllViews();
        short bands;
        try {
            // get number of supported bands
            bands = (short) EqualizerHelper.getInstance().getNumberOfBands();

            // for each of the supported bands, we will set up a slider from -10dB to 10dB boost/attenuation,
            // as well as text labels to assist the user
            for (short i = 0; i < bands; i++) {
                final short band = i;

                View view = LayoutInflater.from(this).inflate(R.layout.retro_seekbar, mLinearLayout, false);
                TextView freqTextView = view.findViewById(R.id.hurtz);
                freqTextView.setText(
                        String.format("%d Hz", EqualizerHelper.getInstance().getCenterFreq((int) band) / 1000));

                TextView minDbTextView = view.findViewById(R.id.minus_db);
                minDbTextView
                        .setText(String.format("%d dB", EqualizerHelper.getInstance().getBandLevelLow() / 100));

                TextView maxDbTextView = view.findViewById(R.id.plus_db);
                maxDbTextView.setText(
                        String.format("%d dB", EqualizerHelper.getInstance().getBandLevelHigh() / 100));

                SeekBar bar = view.findViewById(R.id.seekbar);
                bar.setMax(EqualizerHelper.getInstance().getBandLevelHigh() - EqualizerHelper.getInstance()
                        .getBandLevelLow());
                bar.setProgress(
                        EqualizerHelper.getInstance().getBandLevel((int) band) - EqualizerHelper.getInstance()
                                .getBandLevelLow());
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        EqualizerHelper.getInstance().setBandLevel((int) band,
                                (int) (progress + EqualizerHelper.getInstance().getBandLevelLow()));
                        if (fromUser) {
                            mPresets.setSelection(0);
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                mLinearLayout.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        EqualizerHelper.getInstance().getEqualizer().usePreset((short) (position - 1));
        setupUI();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
