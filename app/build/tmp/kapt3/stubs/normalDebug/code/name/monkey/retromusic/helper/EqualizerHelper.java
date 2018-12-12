package code.name.monkey.retromusic.helper;

import java.lang.System;

/**
 * * @author Hemanth S (h4h13).
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 02\u00020\u0001:\u00010B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010)\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u0004H\u0016J\u0010\u0010+\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u0004H\u0016J\n\u0010,\u001a\u0004\u0018\u00010\u0000H\u0004J\u0018\u0010-\u001a\u00020.2\u0006\u0010*\u001a\u00020\u00042\u0006\u0010/\u001a\u00020\u0004H\u0016R\u0014\u0010\u0003\u001a\u00020\u0004X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\nX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR$\u0010\u000e\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00048V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000f\u0010\u0006\"\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u0013X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R$\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00178V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u0017X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0019\"\u0004\b\u001d\u0010\u001bR$\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00178V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u001e\u0010\u0019\"\u0004\b\u001f\u0010\u001bR\u0014\u0010 \u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0006R\u0014\u0010\"\u001a\u00020#X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R$\u0010&\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00048V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\'\u0010\u0006\"\u0004\b(\u0010\u0011\u00a8\u00061"}, d2 = {"Lcode/name/monkey/retromusic/helper/EqualizerHelper;", "Lcode/name/monkey/retromusic/interfaces/EqualizerInterface;", "()V", "bandLevelHigh", "", "getBandLevelHigh", "()I", "bandLevelLow", "getBandLevelLow", "bassBoost", "Landroid/media/audiofx/BassBoost;", "getBassBoost", "()Landroid/media/audiofx/BassBoost;", "strength", "bassBoostStrength", "getBassBoostStrength", "setBassBoostStrength", "(I)V", "equalizer", "Landroid/media/audiofx/Equalizer;", "getEqualizer", "()Landroid/media/audiofx/Equalizer;", "isEnabled", "", "isBassBoostEnabled", "()Z", "setBassBoostEnabled", "(Z)V", "isRunning", "setRunning", "isVirtualizerEnabled", "setVirtualizerEnabled", "numberOfBands", "getNumberOfBands", "virtualizer", "Landroid/media/audiofx/Virtualizer;", "getVirtualizer", "()Landroid/media/audiofx/Virtualizer;", "virtualizerStrength", "getVirtualizerStrength", "setVirtualizerStrength", "getBandLevel", "band", "getCenterFreq", "readResolve", "setBandLevel", "", "level", "Companion", "app_normalDebug"})
public final class EqualizerHelper implements code.name.monkey.retromusic.interfaces.EqualizerInterface {
    @org.jetbrains.annotations.NotNull()
    private final android.media.audiofx.Equalizer equalizer = null;
    @org.jetbrains.annotations.NotNull()
    private final android.media.audiofx.BassBoost bassBoost = null;
    @org.jetbrains.annotations.NotNull()
    private final android.media.audiofx.Virtualizer virtualizer = null;
    private final int bandLevelHigh = 0;
    private final int bandLevelLow = 0;
    private boolean isRunning;
    private static final java.lang.String TAG = "EqualizerHelper";
    private static volatile code.name.monkey.retromusic.helper.EqualizerHelper ourInstance;
    public static final code.name.monkey.retromusic.helper.EqualizerHelper.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.media.audiofx.Equalizer getEqualizer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.media.audiofx.BassBoost getBassBoost() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.media.audiofx.Virtualizer getVirtualizer() {
        return null;
    }
    
    @java.lang.Override()
    public int getBandLevelHigh() {
        return 0;
    }
    
    @java.lang.Override()
    public int getBandLevelLow() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean isRunning() {
        return false;
    }
    
    public void setRunning(boolean p0) {
    }
    
    @java.lang.Override()
    public int getNumberOfBands() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean isBassBoostEnabled() {
        return false;
    }
    
    @java.lang.Override()
    public void setBassBoostEnabled(boolean isEnabled) {
    }
    
    @java.lang.Override()
    public int getBassBoostStrength() {
        return 0;
    }
    
    @java.lang.Override()
    public void setBassBoostStrength(int strength) {
    }
    
    @java.lang.Override()
    public boolean isVirtualizerEnabled() {
        return false;
    }
    
    @java.lang.Override()
    public void setVirtualizerEnabled(boolean isEnabled) {
    }
    
    @java.lang.Override()
    public int getVirtualizerStrength() {
        return 0;
    }
    
    @java.lang.Override()
    public void setVirtualizerStrength(int strength) {
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final code.name.monkey.retromusic.helper.EqualizerHelper readResolve() {
        return null;
    }
    
    @java.lang.Override()
    public int getCenterFreq(int band) {
        return 0;
    }
    
    @java.lang.Override()
    public int getBandLevel(int band) {
        return 0;
    }
    
    @java.lang.Override()
    public void setBandLevel(int band, int level) {
    }
    
    private EqualizerHelper() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/helper/EqualizerHelper$Companion;", "", "()V", "TAG", "", "instance", "Lcode/name/monkey/retromusic/helper/EqualizerHelper;", "getInstance", "()Lcode/name/monkey/retromusic/helper/EqualizerHelper;", "ourInstance", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.Nullable()
        public final code.name.monkey.retromusic.helper.EqualizerHelper getInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}