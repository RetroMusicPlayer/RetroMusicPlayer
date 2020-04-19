package code.name.monkey.retromusic.equalizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.util.Log;

import code.name.monkey.retromusic.R;


public class AudioEffects {

    public static final short BASSBOOST_MAX_STRENGTH = 1000;
    private static final String PREF_EQ_ENABLED = "enabled";
    private static final String PREF_BAND_LEVEL = "level";
    private static final String PREF_PRESET = "preset";
    private static final String PREF_BASSBOOST = "bassboost";
    private static final String AUDIO_EFFECTS_PREFS = "audioeffects";

    private static final BassBoostValues sBassBoostValues = new BassBoostValues();
    private static final EqualizerValues sEqualizerValues = new EqualizerValues();
    private static BassBoost sBassBoost;
    private static Equalizer sEqualizer;
    private static boolean sCustomPreset;

    public static void init(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AUDIO_EFFECTS_PREFS, Context.MODE_PRIVATE);

        initBassBoostValues(prefs);
        initEqualizerValues(prefs);
    }


    public static void openAudioEffectSession(Context context, int audioSessionId) {
        SharedPreferences prefs = context.getSharedPreferences(AUDIO_EFFECTS_PREFS, Context.MODE_PRIVATE);

        initBassBoost(audioSessionId);
        initEqualizer(prefs, audioSessionId);
    }

    public static void closeAudioEffectSession() {
        if (sBassBoost != null) {
            sBassBoost.release();
            sBassBoost = null;
        }

        if (sEqualizer != null) {
            sEqualizer.release();
            sEqualizer = null;
        }
    }

    private static void initBassBoostValues(SharedPreferences prefs) {
        sBassBoostValues.enabled = prefs.getBoolean(PREF_EQ_ENABLED, false);
        sBassBoostValues.strength = (short) prefs.getInt(PREF_BASSBOOST, 0);
    }

    private static void initBassBoost(int audioSessionId) {
        if (sBassBoost != null) {
            sBassBoost.release();
            sBassBoost = null;
        }
        sBassBoost = new BassBoost(0, audioSessionId);
        sBassBoost.setEnabled(sBassBoostValues.enabled);

        short strength = sBassBoostValues.strength;

        if (strength >= 0 && strength <= BASSBOOST_MAX_STRENGTH) {
            sBassBoost.setStrength(strength);
        }
    }

    private static void initEqualizerValues(SharedPreferences prefs) {


        sEqualizerValues.enabled = prefs.getBoolean(PREF_EQ_ENABLED, false);

        sEqualizerValues.preset = (short) prefs.getInt(PREF_PRESET, -1);

        if (sEqualizerValues.preset == -1) {
            sCustomPreset = true;
        }
    }

    private static void initEqualizer(SharedPreferences prefs, int audioSessionId) {

        if (sEqualizer != null) {
            sEqualizer.release();
            sEqualizer = null;
        }
        sEqualizer = new Equalizer(0, audioSessionId);
        sEqualizer.setEnabled(sEqualizerValues.enabled);

        if (!sCustomPreset) {
            usePreset(sEqualizerValues.preset);

        }

        sEqualizerValues.numberOfBands = sEqualizer.getNumberOfBands();


        if (!sEqualizerValues.levelsSet) {
            sEqualizerValues.bandLevels = new short[sEqualizerValues.numberOfBands];
        }
        for (short b = 0; b < sEqualizerValues.numberOfBands; b++) {
            if (!sEqualizerValues.levelsSet) {
                short level = (short) prefs.getInt(PREF_BAND_LEVEL + b, sEqualizer.getBandLevel(b));
                sEqualizerValues.bandLevels[b] = level;
                if (sCustomPreset) {

                    sEqualizer.setBandLevel(b, level);
                }
            } else {
                sEqualizer.setBandLevel(b, sEqualizerValues.bandLevels[b]);
            }
        }

        sEqualizerValues.levelsSet = true;


    }

    public static short getBassBoostStrength() {
        return sBassBoostValues.strength;
    }

    public static void setBassBoostStrength(short strength) {
        sBassBoostValues.strength = strength;
        if (sBassBoost != null) {
            sBassBoost.setStrength(strength);
        }

    }

    public static short[] getBandLevelRange() {
        if (sEqualizer == null) {
            return null;
        }
        return sEqualizer.getBandLevelRange();
    }

    public static short getBandLevel(short band) {
        if (sEqualizer == null) {
            if (sEqualizerValues.levelsSet && sEqualizerValues.bandLevels.length > band) {
                return sEqualizerValues.bandLevels[band];
            }
        }
        Log.d("audiofx", "eeeD");
        return sEqualizer.getBandLevel(band);
    }

    public static boolean areAudioEffectsEnabled() {
        if (sEqualizer == null) {
            return sEqualizerValues.enabled;
        }
        return sEqualizer.getEnabled();
    }

    public static void setAudioEffectsEnabled(boolean enabled) {
        if (sEqualizer == null || sBassBoost == null) {
            return;
        }
        sBassBoost.setEnabled(true);
        sEqualizer.setEnabled(enabled);
    }

    public static void setBandLevel(short band, short level) {
        sCustomPreset = true;

        if (sEqualizerValues.bandLevels.length > band) {
            sEqualizerValues.preset = -1;
            sEqualizerValues.bandLevels[band] = level;
        }

        if (sEqualizer != null) {
            sEqualizer.setBandLevel(band, level);
        }
    }

    public static String[] getEqualizerPresets(Context context) {
        if (sEqualizer == null) {
            return new String[]{};
        }
        short numberOfPresets = sEqualizer.getNumberOfPresets();

        String[] presets = new String[numberOfPresets + 1];

        presets[0] = context.getResources().getString(R.string.custom);

        for (short n = 0; n < numberOfPresets; n++) {
            presets[n + 1] = sEqualizer.getPresetName(n);
        }

        return presets;
    }

    public static int getCurrentPreset() {
        if (sEqualizer == null || sCustomPreset) {
            return 0;
        }

        return sEqualizer.getCurrentPreset() + 1;
    }

    public static void usePreset(short preset) {
        if (sEqualizer == null) {
            return;
        }
        sCustomPreset = false;
        sEqualizer.usePreset(preset);

    }


    public static short getNumberOfBands() {
        if (sEqualizer == null) {
            return 0;
        }
        return sEqualizer.getNumberOfBands();
    }

    public static int getCenterFreq(short band) {
        if (sEqualizer == null) {
            return 0;
        }
        return sEqualizer.getCenterFreq(band);
    }

    @SuppressLint("CommitPrefEdits")
    public static void savePrefs(Context context) {
        if (sEqualizer == null || sBassBoost == null) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(AUDIO_EFFECTS_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(PREF_BASSBOOST, sBassBoostValues.strength);

        short preset = sCustomPreset ? -1 : sEqualizer.getCurrentPreset();
        editor.putInt(PREF_PRESET, preset);


        short bands = sEqualizer.getNumberOfBands();

        for (short b = 0; b < bands; b++) {
            short level = sEqualizer.getBandLevel(b);

            editor.putInt(PREF_BAND_LEVEL + b, level);
        }
        editor.putBoolean(PREF_EQ_ENABLED,
                sEqualizer.getEnabled());

        editor.commit();
    }

    private static class BassBoostValues {
        public boolean enabled;
        public short strength;
    }

    private static class EqualizerValues {
        public boolean enabled;
        public short preset;
        public short numberOfBands;
        public short[] bandLevels;

        public boolean levelsSet = false;
    }
}