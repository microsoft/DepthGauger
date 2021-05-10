package com.microsoft.depthgauger.benchmark;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.microsoft.depthgauger.onnx.OnnxConfig;
import com.microsoft.depthgauger.onnx.OnnxRunner;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class OnnxBenchmark extends BaseBenchmark<OnnxRunner> {
    @Override
    OnnxRunner getRunner() throws Exception {
        final Context context = getInstrumentation().getTargetContext();
        final OnnxConfig config = new OnnxConfig(context);
        return new OnnxRunner(context, config);
    }
}