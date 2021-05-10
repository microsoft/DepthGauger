package com.microsoft.depthgauger.tensorflow;

import android.content.Context;

import com.google.common.collect.ImmutableMap;
import com.microsoft.depthgauger.BaseConfig;
import com.microsoft.depthgauger.parameters.FloatConstantParameter;
import com.microsoft.depthgauger.parameters.FloatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.IntegerTensorConstantParameter;
import com.microsoft.depthgauger.parameters.LongFlatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.Parameter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.microsoft.depthgauger.utils.TypeConversion.expandTo2DFloatArrayPrimitive;
import static com.microsoft.depthgauger.utils.TypeConversion.expandTo2DIntegerArrayPrimitive;
import static com.microsoft.depthgauger.utils.TypeConversion.longListToLongArrayPrimitive;

public class TensorFlowConfig extends BaseConfig {
    public TensorFlowConfig(Context context) throws IOException {
        super(context);
    }

    public Object[] getInputs() {
        final int nParameters = inputParameters.size();
        final Object[] inputs = new Object[nParameters];
        for (int i = 0; i < nParameters; i++) {
            inputs[i] = extractObject(inputParameters.get(i));
        }
        return inputs;
    }

    public Map<Integer, Object> getOutputs() {
        final ImmutableMap.Builder<Integer, Object> outputs = ImmutableMap.builder();
        final int nParameters = outputParameters.size();
        for (int i = 0; i < nParameters; i++) {
            outputs.put(i, extractObject(outputParameters.get(i)));
        }
        return outputs.build();
    }

    private Object extractObject(Parameter parameter) {
        final Object object;
        if (parameter instanceof FloatConstantParameter) {
            object = floatConstantParameterToObject(parameter);
        } else if (parameter instanceof LongFlatTensorConstantParameter) {
            object = longFlatTensorConstantParameterToObject(parameter);
        } else if (parameter instanceof IntegerTensorConstantParameter) {
            object = integerTensorConstantParameterToObject(parameter);
        } else if (parameter instanceof FloatTensorConstantParameter) {
            object = floatTensorConstantParameterToObject(parameter);
        } else {
            throw new RuntimeException(String.format("Unhandled Parameter: %s", parameter));
        }
        return object;
    }

    private Object floatConstantParameterToObject(Parameter parameter) {
        final FloatConstantParameter param = (FloatConstantParameter) parameter;
        return parameter.getValue();
    }

    private Object longFlatTensorConstantParameterToObject(Parameter parameter) {
        final LongFlatTensorConstantParameter param = (LongFlatTensorConstantParameter) parameter;
        return longListToLongArrayPrimitive(param.getValue());
    }

    private Object integerTensorConstantParameterToObject(Parameter parameter) {
        final IntegerTensorConstantParameter param = (IntegerTensorConstantParameter) parameter;
        final List<Long> shape = param.getShape();
        final int nDims = shape.size();
        final Object result;
        if (nDims == 2) {
            result = expandTo2DIntegerArrayPrimitive(param.getValue(), param.getShape());
        } else {
            throw new RuntimeException(String.format(
                    Locale.US,
                    "Unsupported number of integer array dimensions: %d",
                    nDims));
        }
        return result;
    }

    private Object floatTensorConstantParameterToObject(Parameter parameter) {
        final FloatTensorConstantParameter param = (FloatTensorConstantParameter) parameter;
        final List<Long> shape = param.getShape();
        final int nDims = shape.size();
        final Object result;
        if (nDims == 2) {
            result = expandTo2DFloatArrayPrimitive(param.getValue(), param.getShape());
        } else {
            throw new RuntimeException(String.format(
                    Locale.US,
                    "Unsupported number of float array dimensions: %d",
                    nDims));
        }
        return result;
    }
}
