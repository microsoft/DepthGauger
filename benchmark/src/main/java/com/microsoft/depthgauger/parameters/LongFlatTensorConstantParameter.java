package com.microsoft.depthgauger.parameters;

import com.google.common.base.MoreObjects;

import java.util.List;

public class LongFlatTensorConstantParameter extends TensorParameter<Long> {
    public LongFlatTensorConstantParameter(String name, List<Long> shape, List<Long> values) {
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
