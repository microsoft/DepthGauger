package com.microsoft.depthgauger.benchmark;

import com.google.common.base.Preconditions;
import com.microsoft.depthgauger.utils.Stats;

import java.util.List;
import java.util.Map;

public class BenchmarkStats {
    public int n_warm_up_samples;
    public Map<String, Double> times_ms;
    public List<Long> ordered_times_ms;
    public Map<String, Double> java_pss;
    public List<Long> ordered_java_pss;
    public Map<String, Double> native_pss;
    public List<Long> ordered_native_pss;
    public String name;

    private BenchmarkStats(
            int nWarmUpSamples,
            Map<String, Double> timesMsStats,
            List<Long> timesMs,
            Map<String, Double> javaPssStats,
            List<Long> javaPss,
            Map<String, Double> nativePssStats,
            List<Long> nativePss,
            String name) {
        this.n_warm_up_samples = nWarmUpSamples;
        this.times_ms = Preconditions.checkNotNull(timesMsStats);
        this.ordered_times_ms = Preconditions.checkNotNull(timesMs);
        this.java_pss = Preconditions.checkNotNull(javaPssStats);
        this.ordered_java_pss = Preconditions.checkNotNull(javaPss);
        this.native_pss = Preconditions.checkNotNull(nativePssStats);
        this.ordered_native_pss = Preconditions.checkNotNull(nativePss);
        this.name = Preconditions.checkNotNull(name);
    }

    static BenchmarkStats fromStats(Stats stats) {
        return new BenchmarkStats(
                stats.getNWarmUpSamples(),
                stats.getTimesMsStats(),
                stats.getTimesMs(),
                stats.getJavaPssStats(),
                stats.getJavaPss(),
                stats.getNativePssStats(),
                stats.getNativePss(),
                stats.getName());
    }
}
