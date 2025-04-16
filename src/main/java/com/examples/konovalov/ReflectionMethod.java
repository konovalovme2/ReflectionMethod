package com.examples.konovalov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class ReflectionMethod {
    private double[][] A;
    private int n;
    private double[] diag;
    private int[] C;
    private ArrayList<Double> sqLenV;

    public ReflectionMethod(double[][] matrix){
        A = matrix;
        n = A.length;
        diag = new double[n];
        C = new int[n-1];
        sqLenV = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            sqLenV.add(0.0);
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                sqLenV.set(j, sqLenV.get(j) + A[i][j]*A[i][j]);
            }
        }
    }

    public double[][] findReversedMatrix(){
        findUpperTriangleMatrix();
        findReversedUpperTriangleMatrix();
        A[n-1][n-1] = diag[n-1];
        for(int j = n - 2; j > -1; j--) {
            double[] wj = new double[n - j];
            for (int i = j; i < n; i++) {
                wj[i - j] = A[i][j];
                A[i][j] = 0;
            }
            A[j][j] = diag[j];
            double sum = 0;
            for (int i = j; i < n; i++) {
                sum += A[j][i] * wj[i - j];
            }
            for (int k = 0; k < n; k++){
                for (int i = j; i < n; i++) {
                    A[j][i] = A[j][i] - 2 * sum * wj[i - j];
                }
            }
        }

        for(int j = n - 2; j > -1; j--){
            double[] temp = A[j];
            A[j] = A[C[j]];
            A[C[j]] = temp;
        }

        return A;
    }

    private void findUpperTriangleMatrix(){
        double lenW = 0;
        double el = 0;

        for(int i = 0; i < n-1; i++){
            lenW = findMainColumnLength(i);
            el = A[i][i] - Math.sqrt(lenW);
            lenW = lenW - A[i][i]*A[i][i] + el*el;
            diag[i] = A[i][i];
            A[i][i] = el;

            for(int k = i + 1; k < n; k++){
                double lenX = A[i][i]*A[i][k];
                for(int m = i + 1; m < n; m++){
                    lenX += A[m][i]*A[m][k];
                }
                for(int m = i + 1; m < n; m++){
                    A[m][k] = A[m][k] - 2 * (lenX/lenW) * A[m][i];
                }
            }

        }
        diag[n-1] = A[n-1][n-1];
    }

    private void findReversedUpperTriangleMatrix(){
        for(int i = n - 1; i > -1; i--){
            diag[i] = 1 / diag[i];
            double[] mas = Arrays.copyOf(A[i], n);
            for(int j = i + 1; j < n; j++){
                double sum = 0;
                for(int k = i + 1; k < j; k++){
                    sum += mas[k]*A[k][j];
                }
                sum += mas[j]*diag[j];
                A[i][j] = -diag[i] * sum;
            }
        }
    }

    private double findMainColumnLength(int j){
        double max = 0;
        int iMax = 0;
        for(int i = 0; i < sqLenV.size(); i++){
            if (max < sqLenV.get(i)){
                max = sqLenV.get(i);
                iMax = i;
            }
        }
        Collections.swap(sqLenV, 0, iMax);
        swapColumns(j, iMax + j);
        C[j] = iMax + j;
        for(int i = 0; i < sqLenV.size(); i++){
            sqLenV.set(i, sqLenV.get(i) - A[j][i]*A[j][i]);
        }
        sqLenV.remove(0);
        return max;
    }

    private void swapColumns(int first, int second) {
        for(int i = 0; i < n; i++) {
            double temp = A[i][first];
            A[i][first] = A[i][second];
            A[i][second] = temp;
        }
    }
}
