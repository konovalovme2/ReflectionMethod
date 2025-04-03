package com.examples.konovalov;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.swap;


public class ReflectionMethod {
    private double[][] A;
    private int n;
    private double[] diag;
    private int[] C;
    private ArrayList<Double> sqLenV;

    public ReflectionMethod(double[][] matrix){
        A = matrix;
        n = A.length;
        diag = new double[n-1];
        C = new int[n];
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
    }

    private void findReversedUpperTriangleMatrix(){

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
        swap(sqLenV, 0, iMax);
        swapColumns(j, iMax + j);
        for(int i = 0; i < sqLenV.size(); i++){
            sqLenV.set(j, sqLenV.get(i) - A[j][i]*A[j][i]);
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
