package com.microsoft.depthgauger.parameters;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;

public abstract class Parameter<T> {
    protected final String name;

    public Parameter(String name) {
        this.name = Preconditions.checkNotNull(name);
    }

    public abstract T getValue();

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter<?> parameter = (Parameter<?>) o;
        return name.equals(parameter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
