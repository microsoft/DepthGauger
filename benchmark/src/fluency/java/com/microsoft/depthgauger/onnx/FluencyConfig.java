package com.microsoft.depthgauger.onnx;

import android.content.Context;

import com.google.common.collect.ImmutableMap;
import com.microsoft.depthgauger.BaseConfig;
import com.microsoft.depthgauger.parameters.FloatConstantParameter;
import com.microsoft.depthgauger.parameters.IntegerTensorConstantParameter;
import com.microsoft.depthgauger.parameters.LongFlatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.Parameter;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.List;
import java.util.Map;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;

public class FluencyConfig extends BaseConfig {
    public FluencyConfig(Context context) throws IOException {
        super(context);
    }
}
