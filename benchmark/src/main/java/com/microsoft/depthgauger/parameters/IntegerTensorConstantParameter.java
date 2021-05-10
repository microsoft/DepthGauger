package com.microsoft.depthgauger.parameters;

import com.google.common.base.MoreObjects;

import java.util.List;

public class IntegerTensorConstantParameter extends TensorParameter<Integer> {
    public IntegerTensorConstantParameter(String name, List<Long> shape, List<Integer> values) {
        super(name, shape, values);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("shape", shape)
                .add("values", values)
                .add("name", name)
                .toString();
    }
}
