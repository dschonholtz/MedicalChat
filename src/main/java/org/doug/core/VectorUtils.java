package org.doug.core;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class VectorUtils {

    public static double dotProduct(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        RealVector vecA = new ArrayRealVector(vectorA);
        RealVector vecB = new ArrayRealVector(vectorB);

        return vecA.dotProduct(vecB);
    }

    public static double[] floatToDouble(float[] floats) {
        double[] doubles = new double[floats.length];
        for (int i = 0; i < floats.length; i++) {
            doubles[i] = floats[i];
        }
        return doubles;
    }
}