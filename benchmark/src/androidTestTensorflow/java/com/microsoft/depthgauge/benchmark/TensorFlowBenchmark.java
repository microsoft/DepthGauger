package com.microsoft.depthgauger.benchmark;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.microsoft.depthgauger.tensorflow.TensorFlowConfig;
import com.microsoft.depthgauger.tensorflow.TensorFlowRunner;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TensorFlowBenchmark extends BaseBenchmark<TensorFlowRunner> {
    @Override
    TensorFlowRunner getRunner() throws Exception {
        final Context context = getInstrumentation().getTargetContext();
        final TensorFlowConfig config = new TensorFlowConfig(context);
        return new TensorFlowRunner(context, config);
    }
}