package com.microsoft.depthgauger.parameters;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class FloatConstantParameter extends Parameter<Float> {
    private final Float value;

    public FloatConstantParameter(String name, float value) {
        super(name);
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FloatConstantParameter that = (FloatConstantParameter) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
