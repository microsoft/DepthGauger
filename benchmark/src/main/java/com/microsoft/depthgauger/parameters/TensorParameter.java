package com.microsoft.depthgauger.parameters;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Objects;

public abstract class TensorParameter<T> extends Parameter<List<T>> {
    protected final List<Long> shape;
    protected final List<T> values;

    public TensorParameter(String name, List<Long> shape, List<T> values) {
        super(name);
        this.shape = Preconditions.checkNotNull(shape);
        Preconditions.checkArgument(!shape.isEmpty());
        this.values = Preconditions.checkNotNull(values);
        Preconditions.checkArgument(!values.isEmpty());
    }

    public List<Long> getShape() {
        return shape;
    }

    @Override
    public List<T> getValue() {
        return values;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("shape", shape)
                .add("values", values)
                .add("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TensorParameter<?> that = (TensorParameter<?>) o;
        return shape.equals(that.shape) &&
                values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shape, values);
    }
}
