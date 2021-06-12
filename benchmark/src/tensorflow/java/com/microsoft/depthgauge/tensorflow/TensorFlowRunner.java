package com.microsoft.depthgauger.tensorflow;

import android.content.Context;
import android.os.Build;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.memory.MemoryStats;
import com.microsoft.depthgauger.utils.Stats;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.nnapi.NnApiDelegate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TensorFlowRunner extends BaseRunner<TensorFlowConfig> {
    private Interpreter interpreter;
    private GpuDelegate gpuDelegate;
    private NnApiDelegate nnApiDelegate;
    private final Interpreter.Options options;

    public TensorFlowRunner(Context context, TensorFlowConfig config) throws IOException {
        super(context, config);
        this.options = new Interpreter.Options();
        CompatibilityList compatibilityList = new CompatibilityList();
        if (compatibilityList.isDelegateSupportedOnThisDevice()) {
            this.gpuDelegate = new GpuDelegate();
            this.options.addDelegate(gpuDelegate);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            this.nnApiDelegate = new NnApiDelegate();
            this.options.addDelegate(nnApiDelegate);
            this.options.setUseNNAPI(true);
        }
    }

    @Override
    public void unloadModel() {
        if (gpuDelegate != null) {
            gpuDelegate.close();
        }
        if (nnApiDelegate != null) {
            nnApiDelegate.close();
        }
        if (interpreter != null) {
            interpreter.close();
        }
    }

    @Override
    public void loadModel(Stats stats, MemoryStats baselineMemoryStats) {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        interpreter = new Interpreter(new File(modelPath), options);
        interpreter.allocateTensors();
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        stats.appendTimeMs(loadTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }

    @Override
    public void call(Stats stats, MemoryStats baselineMemoryStats) {
        final long startCallTimeMs = System.currentTimeMillis();
        runCall(config.getInputs());
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        stats.appendTimeMs(callTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }

    public Map<Integer, Object> runCall(Object[] inputs) {
        interpreter.runForMultipleInputsOutputs(inputs, config.getOutputs());
        return config.getOutputs();
    }

    public float[][] runCallSingleTimed(Stats stats) {
        final float[][] output = new float[1][2];
        final int[][] inputArray = new int[][] {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63}};
        final long startCallTimeMs = System.currentTimeMillis();
        interpreter.run(inputArray, output);
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        stats.appendTimeMs(callTimeMs);
        return output;
    }

    public float[][] runCallSingle(int[][] input) {
        float[][] output = new float[1][2];
        interpreter.run(input, output);
        return output;
    }
}
