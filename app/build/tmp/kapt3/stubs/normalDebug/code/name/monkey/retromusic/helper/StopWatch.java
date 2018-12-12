package code.name.monkey.retromusic.helper;

import java.lang.System;

/**
 * * Simple thread safe stop watch.
 * *
 * * @author Karim Abou Zeid (kabouzeid)
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\fJ\u0006\u0010\u000e\u001a\u00020\fJ\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcode/name/monkey/retromusic/helper/StopWatch;", "", "()V", "elapsedTime", "", "getElapsedTime", "()J", "isRunning", "", "previousElapsedTime", "startTime", "pause", "", "reset", "start", "toString", "", "app_normalDebug"})
public final class StopWatch {
    
    /**
     * * The time the stop watch was last started.
     */
    private long startTime;
    
    /**
     * * The time elapsed before the current [.startTime].
     */
    private long previousElapsedTime;
    
    /**
     * * Whether the stop watch is currently running or not.
     */
    private boolean isRunning;
    
    public final long getElapsedTime() {
        return 0L;
    }
    
    /**
     * * Starts or continues the stop watch.
     *     *
     *     * @see .pause
     *     * @see .reset
     */
    public final void start() {
    }
    
    /**
     * * Pauses the stop watch. It can be continued later from [.start].
     *     *
     *     * @see .start
     *     * @see .reset
     */
    public final void pause() {
    }
    
    /**
     * * Stops and resets the stop watch to zero milliseconds.
     *     *
     *     * @see .start
     *     * @see .pause
     */
    public final void reset() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public StopWatch() {
        super();
    }
}