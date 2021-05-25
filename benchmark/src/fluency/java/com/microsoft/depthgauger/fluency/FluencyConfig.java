package com.microsoft.depthgauger.fluency;

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

public class FluencyConfig extends BaseConfig {
    public FluencyConfig(Context context) throws IOException {
        super(context);
    }
    public Map<String, long[]> getFluencyTensors() {
        final ImmutableMap.Builder<String, long[]> tensors = ImmutableMap.builder();
        for (Parameter parameter : inputParameters) {
            final long[] tensor;
            if (parameter instanceof FloatConstantParameter) {
                throw new RuntimeException(String.format("FloatConstantParameter unhandled"));
            } else if (parameter instanceof LongFlatTensorConstantParameter) {
                final List<Long> values = ((LongFlatTensorConstantParameter) parameter).getValue();
                tensor = values.stream().mapToLong(l -> l).toArray();
            } else if (parameter instanceof IntegerTensorConstantParameter) {
                final List<Integer> values = ((IntegerTensorConstantParameter) parameter).getValue();
                tensor = values.stream().mapToLong(l -> l).toArray();
            } else {
                throw new RuntimeException(String.format("Unhandled Parameter: %s", parameter));
            }
            tensors.put(parameter.getName(), tensor);
        }
        return tensors.build();
    }
}
