package com.microsoft.depthgauger.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.microsoft.depthgauger.memory.MemoryStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.microsoft.depthgauger.memory.MemoryStats.getDiffMemoryStats;

public class Stats {
    private final List<Long> timesMs = new ArrayList<>();
    private final List<Long> javaPss = new ArrayList<>();
    private final List<Long> nativePss = new ArrayList<>();
    private final int nWarmUpSamples;
    private final String name;

    public Stats() {
        this("dummy", 0);
    }

    public Stats(String name, int nWarmUpSamples) {
        this.name = name;
        this.nWarmUpSamples = nWarmUpSamples;
    }

    public void reset() {
        timesMs.clear();
        javaPss.clear();
        nativePss.clear();
    }

    public String getName() {
        return name;
    }

    public int getNWarmUpSamples() {
        return nWarmUpSamples;
    }

    public void appendTimeMs(long ms) {
        timesMs.add(ms);
    }

    public void appendMemoryStats(MemoryStats baseline) {
        final MemoryStats memoryStats = getDiffMemoryStats(baseline);
        javaPss.add((long) memoryStats.getJavaPss());
        nativePss.add((long) memoryStats.getNativePss());
    }

    public List<Long> getTimesMs() {
        return ImmutableList.copyOf(timesMs);
    }

    public List<Long> getJavaPss() {
        return ImmutableList.copyOf(javaPss);
    }

    public List<Long> getNativePss() {
        return ImmutableList.copyOf(nativePss);
    }

    public Map<String, Double> getTimesMsStats() {
        return getStats(timesMs);
    }

    public Map<String, Double> getJavaPssStats() {
        return getStats(javaPss);
    }

    public Map<String, Double> getNativePssStats() {
        return getStats(nativePss);
    }

    private Map<String, Double> getStats(List<Long> values) {
        final ImmutableMap.Builder<String, Double> stats = ImmutableMap.builder();
        if (!values.isEmpty()) {
            final List<Long> itemsCopy = getSortedCopyExcludingWarmUp(values);
            final int n = itemsCopy.size();
            stats.put("min", getPercentile(itemsCopy, n, 0));
            stats.put("5%", getPercentile(itemsCopy, n, 5));
            stats.put("10%", getPercentile(itemsCopy, n, 10));
            stats.put("25%", getPercentile(itemsCopy, n, 25));
            stats.put("median", getPercentile(itemsCopy, n, 50));
            stats.put("75%", getPercentile(itemsCopy, n, 75));
            stats.put("90%", getPercentile(itemsCopy, n, 90));
            stats.put("95%", getPercentile(itemsCopy, n, 95));
            stats.put("max", getPercentile(itemsCopy, n, 100));
            final double mean = mean(itemsCopy, n);
            stats.put("mean", mean);
            stats.put("sd", sd(itemsCopy, mean, n));
        }
        return stats.build();
    }

    private List<Long> getSortedCopyExcludingWarmUp(List<Long> values) {
        final List<Long> copy = new ArrayList<>(values.subList(nWarmUpSamples, values.size()));
        Collections.sort(copy);
        return copy;
    }

    private static Double getPercentile(List<Long> values, int n, int percentile) {
        final int index = Math.min(n - 1, Math.max(0, ((int) ((percentile / 100.0) * n)) - 1));
        return values.get(index).doubleValue();
    }

    private static Double mean(List<Long> values, int n) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += values.get(i);
        }
        return sum / n;
    }

    private static Double sd(List<Long> values, double mean, int n) {
        if (n == 1) {
            return Double.NaN;
        }
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.pow(values.get(i) - mean, 2);
        }
        return Math.sqrt(sum / (n - 1));
    }
}
