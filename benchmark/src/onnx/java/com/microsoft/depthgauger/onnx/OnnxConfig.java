package com.microsoft.depthgauger.onnx;

import android.content.Context;

import com.google.common.collect.ImmutableMap;
import com.microsoft.depthgauger.BaseConfig;
import com.microsoft.depthgauger.parameters.Parameter;

import java.io.IOException;
import java.util.Map;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;

public class OnnxConfig extends BaseConfig {
    public OnnxConfig(Context context) throws IOException {
        super(context);
    }

    public Map<String, OnnxTensor> getOnnxTensors(OrtEnvironment env) {
        final ImmutableMap.Builder<String, OnnxTensor> tensors = ImmutableMap.builder();
        try {
            for (Parameter parameter : inputParameters) {
                tensors.put(parameter.getName(), OnnxTensor.createTensor(env, parameter.getValue()));
            }
        } catch (OrtException e) {
            for (OnnxTensor tensor : tensors.build().values()) {
                tensor.close();
            }
        }
        return tensors.build();
    }
}
