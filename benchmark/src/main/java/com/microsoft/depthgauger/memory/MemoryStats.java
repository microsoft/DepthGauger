package com.microsoft.depthgauger.memory;

import android.os.Debug;

/**
 * Measures the memory being used by the Android process.
 * <p>
 * @see <a href="https://stackoverflow.com/a/30963839">this StackOverflow post</a> for good
 * descriptions of the MemoryInfo values.
 */
public class MemoryStats {
    private final int nativePss;
    private final int javaPss;

    public static MemoryStats getMemoryStats() {
        final Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return new MemoryStats(memoryInfo.nativePss, memoryInfo.dalvikPss);
    }

    public static MemoryStats getDiffMemoryStats(MemoryStats baselineMemoryStats) {
        final MemoryStats memoryStats = getMemoryStats();
        return new MemoryStats(
                memoryStats.nativePss - baselineMemoryStats.nativePss,
                memoryStats.javaPss - baselineMemoryStats.javaPss);
    }

    private MemoryStats(int nativePss, int javaPss) {
        this.nativePss = nativePss;
        this.javaPss = javaPss;
    }

    public int getJavaPss() {
        return javaPss;
    }

    public int getNativePss() {
        return nativePss;
    }
}
