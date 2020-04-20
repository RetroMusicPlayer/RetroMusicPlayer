package code.name.monkey.retromusic.equalizer;

public class Settings {
    public static boolean isEqualizerEnabled = true;
    public static boolean isEqualizerReloaded = true;
    public static int[] seekbarpos = new int[6];
    public static int presetPos;
    public static short reverbPreset = -1, bassStrength = -1, virtualizerStrength = -1;
    public static EqualizerModel equalizerModel;
    public static double ratio = 1.0;
    public static boolean isEditing = false;
}