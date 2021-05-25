package com.microsoft.depthgauger.benchmark;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.microsoft.depthgauger.fluency.FluencyConfig;
import com.microsoft.depthgauger.fluency.FluencyRunner;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FluencyBenchmark extends BaseBenchmark<FluencyRunner> {
    @Override
    FluencyRunner getRunner() throws Exception {
        final Context context = getInstrumentation().getTargetContext();
        final FluencyConfig config = new FluencyConfig(context);
        return new FluencyRunner(context, config);
    }
}