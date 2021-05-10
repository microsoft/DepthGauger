package com.microsoft.depthgauger.benchmark;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.microsoft.depthgauger.pytorch.PyTorchConfig;
import com.microsoft.depthgauger.pytorch.PyTorchRunner;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PyTorchBenchmark extends BaseBenchmark<PyTorchRunner> {
    @Override
    PyTorchRunner getRunner() throws Exception {
        final Context context = getInstrumentation().getTargetContext();
        final PyTorchConfig config = new PyTorchConfig(context);
        return new PyTorchRunner(context, config);
    }
}