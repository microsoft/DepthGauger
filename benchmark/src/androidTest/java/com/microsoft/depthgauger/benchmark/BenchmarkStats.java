package com.microsoft.depthgauger.benchmark;

import com.google.common.base.Preconditions;
import com.microsoft.depthgauger.utils.TimeStats;

import java.util.List;
import java.util.Map;

public class BenchmarkStats {
    public int n_warm_up_samples;
    public Map<String, Double> stats_ms;
    public List<Long> ordered_times_ms;
    public String name;

    private BenchmarkStats(
            int nWarmUpSamples,
            Map<String, Double> statsMs,
            List<Long> orderedTimesMs,
            String name) {
        this.n_warm_up_samples = nWarmUpSamples;
        this.stats_ms = Preconditions.checkNotNull(statsMs);
        this.ordered_times_ms = Preconditions.checkNotNull(orderedTimesMs);
        this.name = Preconditions.checkNotNull(name);
    }

    static BenchmarkStats fromTimeStats(TimeStats timeStats) {
        return new BenchmarkStats(
                timeStats.getNWarmUpSamples(),
                timeStats.getTimeStats(),
                timeStats.getTimesMs(),
                timeStats.getName());
    }
}
