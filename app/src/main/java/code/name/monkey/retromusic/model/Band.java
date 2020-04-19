package code.name.monkey.retromusic.model;

public class Band {
    public final int centerFreq;
    public short level;

    private final int[] freqRange;
    public final int freqLow;
    public final int freqHigh;
    public final short rangeMin;
    public final short rangeMax;

    public Band(int centerFreq, short level, int[] freqRange, short rangeMin, short rangeMax) {
        this.centerFreq = centerFreq;
        this.level = level;
        this.freqRange = freqRange;

        if (0 < freqRange.length) {
            freqLow = freqRange[0];
            freqHigh = freqRange[freqRange.length - 1];
        } else {
            freqLow = 0;
            freqHigh = 0;
        }

        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }
}