package com.microsoft.depthgauger.benchmark;

import androidx.benchmark.BenchmarkState;
import androidx.benchmark.junit4.BenchmarkRule;

import com.microsoft.appcenter.espresso.Factory;
import com.microsoft.appcenter.espresso.ReportHelper;
import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.memory.MemoryStats;
import com.microsoft.depthgauger.utils.Stats;
import com.squareup.moshi.Moshi;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static com.microsoft.depthgauger.memory.MemoryStats.getMemoryStats;

abstract class BaseBenchmark<T extends BaseRunner> {
    private static final int N_WARM_UP_SAMPLES = 5;

    @Rule
    public BenchmarkRule benchmarkRule = new BenchmarkRule();

    // For MS App Center
    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();

    private static MemoryStats baselineMemoryStats;

    private T runner;

    abstract T getRunner() throws Exception;

    @BeforeClass
    public static void setUpClass() {
        baselineMemoryStats = getMemoryStats();
    }

    @Before
    public void setUp() throws Exception {
        runner = getRunner();
        runner.loadModel(new Stats(), baselineMemoryStats);
    }

    @After
    public void tearDown() throws Exception {
        if (runner != null) {
            runner.unloadModel();
        }
    }

    @Test
    public void loads() throws Exception {
        final Stats loadStats = new Stats("loads", N_WARM_UP_SAMPLES);
        final BenchmarkState state = benchmarkRule.getState();
        while (state.keepRunning()) {
            state.pauseTiming();
            runner.unloadModel();
            state.resumeTiming();
            runner.loadModel(loadStats, baselineMemoryStats);
        }
        state.pauseTiming();
        printStats(loadStats);
    }

    @Test
    public void calls() throws Exception {
        final Stats callStats = new Stats("calls", N_WARM_UP_SAMPLES);
        final BenchmarkState state = benchmarkRule.getState();
        while (state.keepRunning()) {
            runner.call(callStats, baselineMemoryStats);
        }
        state.pauseTiming();
        printStats(callStats);
    }

    static void printStats(Stats stats) {
        final String json = new Moshi.Builder()
                .build()
                .adapter(BenchmarkStats.class)
                .toJson(BenchmarkStats.fromStats(stats));
        System.out.println(String.format("DepthGauger stats: %s", json));
    }
}
