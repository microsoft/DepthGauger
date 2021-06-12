package com.microsoft.depthgauger.onnx;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.memory.MemoryStats;
import com.microsoft.depthgauger.utils.Stats;

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
    public void loadModel(Stats stats, MemoryStats baselineMemoryStats) throws Exception {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        session = env.createSession(readFileToBytes(modelPath), opts);
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        stats.appendTimeMs(loadTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }

    @Override
    public void call(Stats stats, MemoryStats baselineMemoryStats) throws Exception {
        final long startCallTimeMs = System.currentTimeMillis();
        final Map<String, OnnxTensor> inputs = config.getOnnxTensors(env);
        try {
            session.run(inputs);
            final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
            stats.appendTimeMs(callTimeMs);
            stats.appendMemoryStats(baselineMemoryStats);
        } finally {
            for (OnnxTensor tensor : inputs.values()) {
                tensor.close();
            }
        }
    }
}
