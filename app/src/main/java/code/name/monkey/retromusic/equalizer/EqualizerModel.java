package code.name.monkey.retromusic.equalizer;

import java.io.Serializable;

/**
 * Created by Harjot on 09-Dec-16.
 */

public class EqualizerModel implements Serializable {
    private boolean isEqualizerEnabled;
    private int[] seekbarpos = new int[5];
    private int presetPos;
    private short reverbPreset;
    private short bassStrength;
    private short virtualizerStrength;

    public EqualizerModel() {
        isEqualizerEnabled = true;
        reverbPreset = -1;
        bassStrength = -1;
    }

    public boolean isEqualizerEnabled() {
        return isEqualizerEnabled;
    }

    public void setEqualizerEnabled(boolean equalizerEnabled) {
        isEqualizerEnabled = equalizerEnabled;
    }

    public int[] getSeekbarpos() {
        return seekbarpos;
    }

    public void setSeekbarpos(int[] seekbarpos) {
        this.seekbarpos = seekbarpos;
    }

    public int getPresetPos() {
        return presetPos;
    }

    public void setPresetPos(int presetPos) {
        this.presetPos = presetPos;
    }

    public short getReverbPreset() {
        return reverbPreset;
    }

    public void setReverbPreset(short reverbPreset) {
        this.reverbPreset = reverbPreset;
    }

    public short getBassStrength() {
        return bassStrength;
    }

    public void setBassStrength(short bassStrength) {
        this.bassStrength = bassStrength;
    }

    public short getVirtualizerStrength() {
        return virtualizerStrength;
    }

    public void setVirtualizerStrength(short virtualizerStrength) {
        this.virtualizerStrength = virtualizerStrength;
    }
}