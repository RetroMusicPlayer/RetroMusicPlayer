package code.name.monkey.retromusic.interfaces;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;

/**
 * @author Hemanth S (h4h13).
 */

public interface EqualizerInterface {
    int getBandLevelLow();

    int getBandLevelHigh();

    int getNumberOfBands();

    int getCenterFreq(int band);

    int getBandLevel(int band);

    void setBandLevel(int band, int level);

    boolean isBassBoostEnabled();

    void setBassBoostEnabled(boolean isEnabled);

    int getBassBoostStrength();

    void setBassBoostStrength(int strength);

    boolean isVirtualizerEnabled();

    void setVirtualizerEnabled(boolean isEnabled);

    int getVirtualizerStrength();

    void setVirtualizerStrength(int strength);

    boolean isRunning();

    Equalizer getEqualizer();

    BassBoost getBassBoost();

    Virtualizer getVirtualizer();


}
