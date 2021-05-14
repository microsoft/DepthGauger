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

public class OnnxConfig extends BaseConfig {
    public OnnxConfig(Context context) throws IOException {
        super(context);
    }

    public Map<String, OnnxTensor> getOnnxTensors(OrtEnvironment env) {
        final ImmutableMap.Builder<String, OnnxTensor> tensors = ImmutableMap.builder();
        try {
            for (Parameter parameter : inputParameters) {
                final OnnxTensor tensor;
                if (parameter instanceof FloatConstantParameter) {
                    tensor = OnnxTensor.createTensor(env, ((FloatConstantParameter) parameter).getValue());
                } else if (parameter instanceof LongFlatTensorConstantParameter) {
                    final List<Long> value = ((LongFlatTensorConstantParameter) parameter).getValue();
                    final LongBuffer buffer = LongBuffer.wrap(value.stream().mapToLong(Long::longValue).toArray());
                    final long[] shape = ((LongFlatTensorConstantParameter) parameter).getShape().stream()
                            .mapToLong(Long::longValue)
                            .toArray();
                    tensor = OnnxTensor.createTensor(env, buffer, shape);
                } else if (parameter instanceof IntegerTensorConstantParameter) {
                    final List<Integer> value = ((IntegerTensorConstantParameter) parameter).getValue();
                    final IntBuffer buffer = IntBuffer.wrap(value.stream().mapToInt(Integer::intValue).toArray());
                    final long[] shape = ((IntegerTensorConstantParameter) parameter).getShape().stream()
                            .mapToLong(Long::longValue)
                            .toArray();
                    tensor = OnnxTensor.createTensor(env, buffer, shape);
                } else {
                    throw new RuntimeException(String.format("Unhandled Parameter: %s", parameter));
                }
                tensors.put(parameter.getName(), tensor);
            }
        } catch (OrtException e) {
            for (OnnxTensor tensor : tensors.build().values()) {
                tensor.close();
            }
            throw new RuntimeException(e);
        }
        return tensors.build();
    }
}
