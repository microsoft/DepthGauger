package com.microsoft.depthgauger.utils;

import java.util.List;

public class TypeConversion {
    public static Long[] parseCommaSeparatedLongs(String str) {
        final String[] splits = str.split(", *");
        final Long[] longs = new Long[splits.length];
        for (int i = 0; i < splits.length; i++) {
            longs[i] = Long.parseLong(splits[i].trim());
        }
        return longs;
    }

    public static Integer[] parseCommaSeparatedIntegers(String str) {
        final String[] splits = str.split(", *");
        final Integer[] ints = new Integer[splits.length];
        for (int i = 0; i < splits.length; i++) {
            ints[i] = Integer.parseInt(splits[i].trim());
        }
        return ints;
    }

    public static Float[] parseCommaSeparatedFloats(String str) {
        final String[] splits = str.split(", *");
        final Float[] floats = new Float[splits.length];
        for (int i = 0; i < splits.length; i++) {
            floats[i] = Float.parseFloat(splits[i].trim());
        }
        return floats;
    }

    public static long[] longListToLongArrayPrimitive(List<Long> longs) {
        final int size = longs.size();
        final long[] output = new long[size];
        for (int i = 0; i < size; i++) {
            output[i] = longs.get(i);
        }
        return output;
    }

    public static int[] integerListToIntegerPrimitiveArray(List<Integer> integers) {
        final int size = integers.size();
        final int[] output = new int[size];
        for (int i = 0; i < size; i++) {
            output[i] = integers.get(i);
        }
        return output;
    }

    public static int[] longListToIntegerArrayPrimitive(List<Long> longs) {
        final int size = longs.size();
        final int[] output = new int[size];
        for (int i = 0; i < size; i++) {
            output[i] = longs.get(i).intValue();
        }
        return output;
    }

    public static float[] floatListToFloatArrayPrimitive(List<Float> floats) {
        final int size = floats.size();
        final float[] output = new float[size];
        for (int i = 0; i < size; i++) {
            output[i] = floats.get(i);
        }
        return output;
    }

    public static long[][] expandTo2DLongArrayPrimitive(List<Long> longs, List<Long> shape) {
        final int firstDim = shape.get(0).intValue();
        final int secondDim = shape.get(1).intValue();
        final long[][] output = new long[firstDim][secondDim];
        for (int i = 0; i < longs.size(); i++) {
            final int a = i / secondDim;
            final int b = i % secondDim;
            output[a][b] = longs.get(i);
        }
        return output;
    }

    public static int[][] expandTo2DIntegerArrayPrimitive(List<Integer> integers, List<Long> shape) {
        final int firstDim = shape.get(0).intValue();
        final int secondDim = shape.get(1).intValue();
        final int[][] output = new int[firstDim][secondDim];
        for (int i = 0; i < integers.size(); i++) {
            final int a = i / secondDim;
            final int b = i % secondDim;
            output[a][b] = integers.get(i);
        }
        return output;
    }

    public static float[][] expandTo2DFloatArrayPrimitive(List<Float> floats, List<Long> shape) {
        final int firstDim = shape.get(0).intValue();
        final int secondDim = shape.get(1).intValue();
        final float[][] output = new float[firstDim][secondDim];
        for (int i = 0; i < floats.size(); i++) {
            final int a = i / secondDim;
            final int b = i % secondDim;
            output[a][b] = floats.get(i);
        }
        return output;
    }
}
