package code.name.monkey.retromusic.interfaces;

import java.lang.System;

/**
 * * @author Hemanth S (h4h13).
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010%\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0003H&J\u0010\u0010\'\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0003H&J\u0018\u0010(\u001a\u00020)2\u0006\u0010&\u001a\u00020\u00032\u0006\u0010*\u001a\u00020\u0003H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u0012\u0010\b\u001a\u00020\tX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0018\u0010\f\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\u0005\"\u0004\b\u000e\u0010\u000fR\u0012\u0010\u0010\u001a\u00020\u0011X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0018\u0010\u0014\u001a\u00020\u0015X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0012\u0010\u0019\u001a\u00020\u0015X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0016R\u0018\u0010\u001a\u001a\u00020\u0015X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u001a\u0010\u0016\"\u0004\b\u001b\u0010\u0018R\u0012\u0010\u001c\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0005R\u0012\u0010\u001e\u001a\u00020\u001fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0018\u0010\"\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b#\u0010\u0005\"\u0004\b$\u0010\u000f\u00a8\u0006+"}, d2 = {"Lcode/name/monkey/retromusic/interfaces/EqualizerInterface;", "", "bandLevelHigh", "", "getBandLevelHigh", "()I", "bandLevelLow", "getBandLevelLow", "bassBoost", "Landroid/media/audiofx/BassBoost;", "getBassBoost", "()Landroid/media/audiofx/BassBoost;", "bassBoostStrength", "getBassBoostStrength", "setBassBoostStrength", "(I)V", "equalizer", "Landroid/media/audiofx/Equalizer;", "getEqualizer", "()Landroid/media/audiofx/Equalizer;", "isBassBoostEnabled", "", "()Z", "setBassBoostEnabled", "(Z)V", "isRunning", "isVirtualizerEnabled", "setVirtualizerEnabled", "numberOfBands", "getNumberOfBands", "virtualizer", "Landroid/media/audiofx/Virtualizer;", "getVirtualizer", "()Landroid/media/audiofx/Virtualizer;", "virtualizerStrength", "getVirtualizerStrength", "setVirtualizerStrength", "getBandLevel", "band", "getCenterFreq", "setBandLevel", "", "level", "app_normalDebug"})
public abstract interface EqualizerInterface {
    
    public abstract int getBandLevelLow();
    
    public abstract int getBandLevelHigh();
    
    public abstract int getNumberOfBands();
    
    public abstract boolean isBassBoostEnabled();
    
    public abstract void setBassBoostEnabled(boolean p0);
    
    public abstract int getBassBoostStrength();
    
    public abstract void setBassBoostStrength(int p0);
    
    public abstract boolean isVirtualizerEnabled();
    
    public abstract void setVirtualizerEnabled(boolean p0);
    
    public abstract int getVirtualizerStrength();
    
    public abstract void setVirtualizerStrength(int p0);
    
    public abstract boolean isRunning();
    
    @org.jetbrains.annotations.NotNull()
    public abstract android.media.audiofx.Equalizer getEqualizer();
    
    @org.jetbrains.annotations.NotNull()
    public abstract android.media.audiofx.BassBoost getBassBoost();
    
    @org.jetbrains.annotations.NotNull()
    public abstract android.media.audiofx.Virtualizer getVirtualizer();
    
    public abstract int getCenterFreq(int band);
    
    public abstract int getBandLevel(int band);
    
    public abstract void setBandLevel(int band, int level);
}