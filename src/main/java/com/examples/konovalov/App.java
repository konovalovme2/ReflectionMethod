package com.examples.konovalov;


import java.util.Arrays;

public class App
{
    public static void main( String[] args )
    {
        double[][] matrix = {
                {4, 2, 1},
                {2, 5, 3},
                {1, 3, 6}
        };

        ReflectionMethod method = new ReflectionMethod(cloneMatrix(matrix));
        double[][] inv = method.findReversedMatrix();

        double[][] product = multiply(matrix, inv);
        printMatrix(product);
    }

    public static double[][] cloneMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            result[i] = Arrays.copyOf(matrix[i], n);
        }
        return result;
    }

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.printf("%10.4f ", val);
            }
            System.out.println();
        }
    }
}
