package com.examples.konovalov;


public class App 
{
    public static void main( String[] args )
    {
        double[][] Matrix = new double[][]{{2,2,1},{1,6,5},{2,1,1}};
        ReflectionMethod newMatrix = new ReflectionMethod(Matrix);

        double[][] A = newMatrix.findReversedMatrix();

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println("\n");
        }

        System.out.println( "Hello World!" );
    }
}
