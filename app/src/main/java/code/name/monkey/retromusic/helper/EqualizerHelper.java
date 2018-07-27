package code.name.monkey.retromusic.helper;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.util.Log;

import code.name.monkey.retromusic.interfaces.EqualizerInterface;

/**
 * @author Hemanth S (h4h13).
 */

public class EqualizerHelper implements EqualizerInterface {
    private static final String TAG = "EqualizerHelper";
    private static volatile EqualizerHelper ourInstance;
    private Equalizer mEqualizer;
    private BassBoost mBassBoost;
    private Virtualizer mVirtualizer;

    private int mMaxLevel, mMinLevel;
    private boolean isRunning = false;

    private EqualizerHelper() {

        //Prevent form the reflection api.
        if (ourInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        int i = MusicPlayerRemote.getAudioSessionId();

        mEqualizer = new Equalizer(100, i);
        if (mEqualizer == null) {
            Log.i(TAG, "onCreate: Equalizer is null");
            return;
        }
        mEqualizer.setEnabled(true);


        mBassBoost = new BassBoost(100, i);
        if (mBassBoost == null) {
            Log.i(TAG, "onCreate: BassBoost is null");
            return;
        }

        mVirtualizer = new Virtualizer(100, i);
        if (mVirtualizer == null) {
            Log.i(TAG, "onCreate: Virtualizer is null");
            return;
        }

        mMaxLevel = (int) mEqualizer.getBandLevelRange()[1];
        mMinLevel = (int) mEqualizer.getBandLevelRange()[0];

        Log.i(TAG, "onCreate: " + mMaxLevel + " " + mMinLevel);
        isRunning = true;
    }

    public static EqualizerHelper getInstance() {
        //Double check locking pattern
        if (ourInstance == null) {//Check for the first time

            synchronized (EqualizerHelper.class) {//Check for the second time.

                //if there is no instance available... create new one
                if (ourInstance == null) {
                    ourInstance = new EqualizerHelper();
                }
            }
        }
        return ourInstance;
    }

    //Make singleton from serialize and deserialize operation.
    protected EqualizerHelper readResolve() {
        return getInstance();
    }

    @Override
    public Equalizer getEqualizer() {
        return mEqualizer;
    }

    @Override
    public BassBoost getBassBoost() {
        return mBassBoost;
    }

    @Override
    public Virtualizer getVirtualizer() {
        return mVirtualizer;
    }

    @Override
    public int getBandLevelLow() {
        return mMinLevel;
    }

    @Override
    public int getBandLevelHigh() {
        return mMaxLevel;
    }

    @Override
    public int getNumberOfBands() {
        return (int) mEqualizer.getNumberOfBands();
    }

    @Override
    public int getCenterFreq(int band) {
        return (int) mEqualizer.getCenterFreq((short) band);
    }


    @Override
    public int getBandLevel(int band) {
        return (int) mEqualizer.getBandLevel((short) band);
    }

    @Override
    public void setBandLevel(int band, int level) {
        mEqualizer.setBandLevel((short) band, (short) level);
    }

    @Override
    public boolean isBassBoostEnabled() {
        return mBassBoost.getEnabled();
    }

    @Override
    public void setBassBoostEnabled(boolean isEnabled) {
        mBassBoost.setEnabled(isEnabled);
    }

    @Override
    public int getBassBoostStrength() {
        return (int) mBassBoost.getRoundedStrength();
    }

    @Override
    public void setBassBoostStrength(int strength) {
        mBassBoost.setStrength((short) strength);
    }

    @Override
    public boolean isVirtualizerEnabled() {
        return mVirtualizer.getEnabled();
    }

    @Override
    public void setVirtualizerEnabled(boolean isEnabled) {
        mVirtualizer.setEnabled(isEnabled);
    }

    @Override
    public int getVirtualizerStrength() {
        return mVirtualizer.getRoundedStrength();
    }

    @Override
    public void setVirtualizerStrength(int strength) {
        mVirtualizer.setStrength((short) strength);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
