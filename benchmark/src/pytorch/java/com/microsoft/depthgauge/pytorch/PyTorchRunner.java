package com.microsoft.depthgauger.pytorch;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.memory.MemoryStats;
import com.microsoft.depthgauger.utils.Stats;

import org.pytorch.Module;

import java.io.IOException;

public class PyTorchRunner extends BaseRunner<PyTorchConfig> {
    private Module module;

    public PyTorchRunner(Context context, PyTorchConfig config) throws IOException {
        super(context, config);
    }

    @Override
    public void unloadModel() {
        if (module != null) {
            module.destroy();
        }
    }

    @Override
    public void loadModel(Stats stats, MemoryStats baselineMemoryStats) {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        module = Module.load(modelPath);
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        stats.appendTimeMs(loadTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }

    @Override
    public void call(Stats stats, MemoryStats baselineMemoryStats) {
        final long startCallTimeMs = System.currentTimeMillis();
        module.runMethod(config.getMethodName(), config.getIValues());
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        stats.appendTimeMs(callTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }
}
