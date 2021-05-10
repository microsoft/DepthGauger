package com.microsoft.depthgauger.config;

import com.google.common.collect.ImmutableList;
import com.microsoft.depthgauger.parameters.FloatConstantParameter;
import com.microsoft.depthgauger.parameters.FloatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.IntegerTensorConstantParameter;
import com.microsoft.depthgauger.parameters.LongFlatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.Parameter;

import java.util.List;

public class ConfigParameter {
    public String name;
    public String parameter_type;
    public List<Long> shape;
    public List<Double> values;

    public Parameter extractParameter() {
        final Parameter parameter;
        if (parameter_type.equals(FloatConstantParameter.class.getSimpleName())) {
            parameter = extractFloatConstantParameter();
        } else if (parameter_type.equals(LongFlatTensorConstantParameter.class.getSimpleName())) {
            parameter = extractLongFlatTensorConstantParameter();
        } else if (parameter_type.equals(IntegerTensorConstantParameter.class.getSimpleName())) {
            parameter = extractIntegerTensorConstantParameter();
        } else if (parameter_type.equals(FloatTensorConstantParameter.class.getSimpleName())) {
            parameter = extractFloatTensorConstantParameter();
        } else {
            throw new RuntimeException(String.format("Unknown parameter_type: \"%s\"", parameter_type));
        }
        return parameter;
    }

    private Parameter extractFloatConstantParameter() {
        return new FloatConstantParameter(name, values.get(0).floatValue());
    }

    private Parameter extractLongFlatTensorConstantParameter() {
        final ImmutableList.Builder<Long> longValues = ImmutableList.builder();
        for (Double value : values) {
            longValues.add(value.longValue());
        }
        return new LongFlatTensorConstantParameter(name, shape, longValues.build());
    }

    private Parameter extractIntegerTensorConstantParameter() {
        final ImmutableList.Builder<Integer> integerValues = ImmutableList.builder();
        for (Double value : values) {
            integerValues.add(value.intValue());
        }
        return new IntegerTensorConstantParameter(name, shape, integerValues.build());
    }

    private Parameter extractFloatTensorConstantParameter() {
        final ImmutableList.Builder<Float> floatValues = ImmutableList.builder();
        for (Double value : values) {
            floatValues.add(value.floatValue());
        }
        return new FloatTensorConstantParameter(name, shape, floatValues.build());
    }
}
