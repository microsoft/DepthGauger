package com.microsoft.depthgauger.benchmark;

import androidx.benchmark.BenchmarkState;
import androidx.benchmark.junit4.BenchmarkRule;

import com.microsoft.appcenter.espresso.Factory;
import com.microsoft.appcenter.espresso.ReportHelper;
import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.utils.TimeStats;
import com.squareup.moshi.Moshi;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

abstract class BaseBenchmark<T extends BaseRunner> {
    private static final int N_WARM_UP_SAMPLES = 5;

    @Rule
    public BenchmarkRule benchmarkRule = new BenchmarkRule();

    // For MS App Center
    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();

    private T runner;

    abstract T getRunner() throws Exception;

    @Before
    public void setUp() throws Exception {
        runner = getRunner();
        runner.loadModel(new TimeStats());
    }

    @After
    public void tearDown() throws Exception {
        if (runner != null) {
            runner.unloadModel();
        }
    }

    @Test
    public void loads() throws Exception {
        final TimeStats loadTimeStats = new TimeStats("loads", N_WARM_UP_SAMPLES);
        final BenchmarkState state = benchmarkRule.getState();
        while (state.keepRunning()) {
            state.pauseTiming();
            runner.unloadModel();
            state.resumeTiming();
            runner.loadModel(loadTimeStats);
        }
        state.pauseTiming();
        printStats(loadTimeStats);
    }

    @Test
    public void calls() throws Exception {
        final TimeStats callTimeStats = new TimeStats("calls", N_WARM_UP_SAMPLES);
        final BenchmarkState state = benchmarkRule.getState();
        while (state.keepRunning()) {
            runner.call(callTimeStats);
        }
        state.pauseTiming();
        printStats(callTimeStats);
    }

    static void printStats(TimeStats timeStats) {
        final String json = new Moshi.Builder()
                .build()
                .adapter(BenchmarkStats.class)
                .toJson(BenchmarkStats.fromTimeStats(timeStats));
        System.out.println(String.format("DepthGauger stats: %s", json));
    }
}
