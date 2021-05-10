package com.microsoft.depthgauger.pytorch;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.utils.TimeStats;

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
    public void loadModel(TimeStats timeStats) {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        module = Module.load(modelPath);
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        timeStats.appendTimeMs(loadTimeMs);
    }

    @Override
    public void call(TimeStats timeStats) {
        final long startCallTimeMs = System.currentTimeMillis();
        module.runMethod(config.getMethodName(), config.getIValues());
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        timeStats.appendTimeMs(callTimeMs);
    }
}
