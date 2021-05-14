package com.microsoft.depthgauger.onnx;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.utils.TimeStats;

import java.util.Map;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.SessionOptions;

import static ai.onnxruntime.OrtSession.SessionOptions.OptLevel.ALL_OPT;
import static com.microsoft.depthgauger.utils.IOUtils.readFileToBytes;

public class OnnxRunner extends BaseRunner<OnnxConfig> {
    private final SessionOptions opts;
    private final OrtEnvironment env;
    private OrtSession session;

    public OnnxRunner(Context context, OnnxConfig config) throws Exception {
        super(context, config);
        this.env = OrtEnvironment.getEnvironment();
        this.opts = new SessionOptions();
        opts.setOptimizationLevel(ALL_OPT);
    }

    @Override
    public void unloadModel() throws Exception {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    @Override
    public void loadModel(TimeStats timeStats) throws Exception {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        session = env.createSession(readFileToBytes(modelPath), opts);
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        timeStats.appendTimeMs(loadTimeMs);
    }

    @Override
    public void call(TimeStats timeStats) throws Exception {
        final long startCallTimeMs = System.currentTimeMillis();
        final Map<String, OnnxTensor> inputs = config.getOnnxTensors(env);
        try {
            session.run(inputs);
            final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
            timeStats.appendTimeMs(callTimeMs);
        } finally {
            for (OnnxTensor tensor : inputs.values()) {
                tensor.close();
            }
        }
    }
}
