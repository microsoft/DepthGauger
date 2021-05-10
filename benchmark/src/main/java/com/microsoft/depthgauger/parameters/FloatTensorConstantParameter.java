package com.microsoft.depthgauger.parameters;

import com.google.common.base.MoreObjects;

import java.util.List;

public class FloatTensorConstantParameter extends TensorParameter<Float> {
    public FloatTensorConstantParameter(String name, List<Long> shape, List<Float> values) {
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
