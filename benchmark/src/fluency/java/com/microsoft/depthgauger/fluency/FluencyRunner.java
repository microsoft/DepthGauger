package com.microsoft.depthgauger.fluency;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.utils.TimeStats;

import java.util.Map;

import static com.microsoft.depthgauger.utils.IOUtils.readFileToBytes;

public class FluencyRunner extends BaseRunner<FluencyConfig> {
    private long handle;

    private native long Load(String path);
    private native float[] Predict(long handle, long[] tokens);
    private native void Destroy(long handle);
    
    static {
        System.loadLibrary("fluency-depthgauger");
    }

    public FluencyRunner(Context context, FluencyConfig config) throws Exception {
        super(context, config);
        //    this.env = OrtEnvironment.getEnvironment();
        //    this.opts = new SessionOptions();
        //    opts.setOptimizationLevel(ALL_OPT);
    }

    @Override
    public void unloadModel() throws Exception {
        Destroy(this.handle);
        this.handle = 0;
    }

    @Override
    public void loadModel(TimeStats timeStats) throws Exception {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        this.handle = Load(modelPath);
        if (this.handle == 0) {
            throw new Exception("Failed to load Fluency lm");
        }
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        timeStats.appendTimeMs(loadTimeMs);
    }

    @Override
    public void call(TimeStats timeStats) throws Exception {
        final long startCallTimeMs = System.currentTimeMillis();
        final Map<String, long[]> inputs = config.getFluencyTensors();
        if (!inputs.containsKey("ids")) {
            throw new Exception("");
        }
        final long[] ids = inputs.get("ids");
        Predict(this.handle, ids);
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        timeStats.appendTimeMs(callTimeMs);
    }
}
