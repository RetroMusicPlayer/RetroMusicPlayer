package code.name.monkey.retromusic.misc;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LagTracker {
    private static Map<String, Long> mMap;
    private static LagTracker mSingleton;
    private boolean mEnabled = true;

    private LagTracker() {
        mMap = new HashMap();
    }

    public static LagTracker get() {
        if (mSingleton == null) {
            mSingleton = new LagTracker();
        }
        return mSingleton;
    }

    private void print(String str, long j) {
        long toMillis = TimeUnit.NANOSECONDS.toMillis(j);
        Log.d("LagTracker", "[" + str + " completed in]: " + j + " ns (" + toMillis + "ms, " + TimeUnit.NANOSECONDS.toSeconds(j) + "s)");
    }

    public LagTracker disable() {
        this.mEnabled = false;
        return this;
    }

    public LagTracker enable() {
        this.mEnabled = true;
        return this;
    }

    public void end(String str) {
        long nanoTime = System.nanoTime();
        if (this.mEnabled) {
            if (mMap.containsKey(str)) {
                print(str, nanoTime - mMap.get(str).longValue());
                mMap.remove(str);
                return;
            }
            throw new IllegalStateException("No start time found for " + str);
        } else if (!mMap.isEmpty()) {
            mMap.clear();
        }
    }

    public void start(String str) {
        long nanoTime = System.nanoTime();
        if (this.mEnabled) {
            mMap.put(str, Long.valueOf(nanoTime));
        } else if (!mMap.isEmpty()) {
            mMap.clear();
        }
    }
}
