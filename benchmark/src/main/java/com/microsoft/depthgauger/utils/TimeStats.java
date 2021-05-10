package com.microsoft.depthgauger.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TimeStats {
    private final List<Long> timesMs = new ArrayList<>();
    private final List<Long> javaMemoryBytes = new ArrayList<>();
    private final List<Long> nativeMemoryBytes = new ArrayList<>();
    private final int nWarmUpSamples;
    private final String name;

    public TimeStats() {
        this("dummy", 0);
    }

    public TimeStats(String name, int nWarmUpSamples) {
        this.name = name;
        this.nWarmUpSamples = nWarmUpSamples;
    }

    public void reset() {
        timesMs.clear();
        javaMemoryBytes.clear();
        nativeMemoryBytes.clear();
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

    public void appendJavaMemoryBytes(long bytes) {
        javaMemoryBytes.add(bytes);
    }

    public void appendNativeMemoryBytes(long bytes) {
        nativeMemoryBytes.add(bytes);
    }

    public List<Long> getTimesMs() {
        return ImmutableList.copyOf(timesMs);
    }

    public List<Long> getJavaMemoryBytes() {
        return ImmutableList.copyOf(javaMemoryBytes);
    }

    public List<Long> getNativeMemoryBytes() {
        return ImmutableList.copyOf(nativeMemoryBytes);
    }

    public Map<String, Double> getTimeStats() {
        return getStats(timesMs);
    }

    public Map<String, Double> getJavaMemoryStats() {
        return getStats(javaMemoryBytes);
    }

    public Map<String, Double> getNativeMemoryStats() {
        return getStats(nativeMemoryBytes);
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
