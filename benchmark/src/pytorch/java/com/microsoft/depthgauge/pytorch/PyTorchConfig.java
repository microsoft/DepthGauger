package com.microsoft.depthgauger.pytorch;

import android.content.Context;

import com.microsoft.depthgauger.BaseConfig;
import com.microsoft.depthgauger.parameters.FloatConstantParameter;
import com.microsoft.depthgauger.parameters.FloatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.IntegerTensorConstantParameter;
import com.microsoft.depthgauger.parameters.LongFlatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.Parameter;

import org.pytorch.IValue;
import org.pytorch.Tensor;

import java.io.IOException;

import static com.microsoft.depthgauger.utils.TypeConversion.longListToLongArrayPrimitive;

public class PyTorchConfig extends BaseConfig {
    public PyTorchConfig(Context context) throws IOException {
        super(context);
    }

    public IValue[] getIValues() {
        final int nParameters = inputParameters.size();
        final IValue[] values = new IValue[nParameters];
        for (int i = 0; i < nParameters; i++) {
            final Parameter parameter = inputParameters.get(i);
            if (parameter instanceof FloatConstantParameter) {
                values[i] = floatConstantParameterToIValue(parameter);
            } else if (parameter instanceof LongFlatTensorConstantParameter) {
                values[i] = longFlatTensorConstantParameterToIValue(parameter);
            } else if ((parameter instanceof IntegerTensorConstantParameter)
                    || (parameter instanceof FloatTensorConstantParameter)) {
                throw new RuntimeException("PyTorch does not accept non-flat parameters");
            } else {
                throw new RuntimeException(String.format("Unhandled Parameter: %s", parameter));
            }
        }
        return values;
    }

    private IValue floatConstantParameterToIValue(Parameter parameter) {
        return IValue.from(((FloatConstantParameter) parameter).getValue());
    }

    private IValue longFlatTensorConstantParameterToIValue(Parameter parameter) {
        final LongFlatTensorConstantParameter param = (LongFlatTensorConstantParameter) parameter;
        return IValue.from(Tensor.fromBlob(
                longListToLongArrayPrimitive(param.getValue()),
                longListToLongArrayPrimitive(param.getShape())));
    }
}
