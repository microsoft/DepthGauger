package com.microsoft.depthgauger.onnx;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.utils.TimeStats;

import java.util.Map;

public class FluencyRunner extends BaseRunner<FluencyConfig> {
    private static final String LICENSE_KEY = "";
    private Session session;

    public FluencyRunner(Context context, FluencyConfig config) throws Exception {
        super(context, config);
        session = Fluency.createSession(LICENSE_KEY);
    }

    @Override
    public void unloadModel() throws Exception {
        session.unload();
    }

    @Override
    public void loadModel(TimeStats timeStats) throws Exception {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        session.load();
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        timeStats.appendTimeMs(loadTimeMs);
    }

    @Override
    public void call(TimeStats timeStats) throws Exception {
        final long startCallTimeMs = System.currentTimeMillis();
        session.getPredictor().getPredictions(something, something, something);
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        timeStats.appendTimeMs(callTimeMs);
    }
}
