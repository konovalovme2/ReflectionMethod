package com.examples.konovalov;

public class Table
{

    private final int N = 100; // Размерность матрицы

    // Метод для вычисления ∞-нормы вектора
    public static double vectorInfNorm(double[] vector) {
        double norm = 0.;
        for (double item : vector) {
            norm = Math.max(norm, Math.abs(item));
        }
        return norm;
    }

    // Метод для вычисления ∞-нормы матрицы
    public static double matrixInfinityNorm(double[][] matrix) {
        double max = 0.0;
        for (int i = 0; i < matrix.length; i++) {
            double rowSum = 0.0;
            for (int j = 0; j < matrix[i].length; j++) {
                rowSum += Math.abs(matrix[i][j]);
            }
            if (rowSum > max) {
                max = rowSum;
            }
        }
        return max;
    }

    public void GenTest() {
        int n = N;
        double alpha = 1.;  // Начальное значение |λ_min|
        double beta = 1.;   // Начальное значение |λ_max|

        // Инициализация массивов
        double[][] a = new double[n][n];
        double[][] a_inv = new double[n][n];
        Gen g = new Gen();

        double[][] a_copy = new double[n][n];

        // Таблица 1: Исследование при фиксированном alpha и растущем beta
        System.out.println("\nТаблица 1 (фиксированное alpha = 1, растущее beta)");
        System.out.printf("%-10s %-10s %-10s %-12s %-8s %-14s %-8s %-10s%n",
                "alpha", "beta", "||A||", "||A_inv||", "V(A)", "||z||", "E", "||r||");

        double[][] z = new double[n][n];
        while (beta <= 1.e+15) {
            // Генерация матрицы и обратной матрицы
            g.mygen(a, a_inv, n, alpha, beta, 1, 2, 1, 1);

            // Вычисление норм матрицы
            double norm = matrixInfinityNorm(a);
            double norm_inv = matrixInfinityNorm(a_inv);
            double cond = norm * norm_inv; // Число обусловленности

            // Копирование матрицы для решения
            for (int i = 0; i < n; i++) {
                System.arraycopy(a[i], 0, a_copy[i], 0, n);
            }

            // Обращение методом отражений
            double[][] b = new double[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(a_copy[i], 0, b[i], 0, n);
            }
            ReflectionMethod reverser = new ReflectionMethod(b);
            reverser.findReversedMatrix();

            z = matr_sub(b, a_inv);
            double z_norm = matrixInfinityNorm(z);
            double E = z_norm / norm_inv; // Относительная погрешность

            double[][] c = new double[n][n];
            double[][] e = new double[n][n];
            matr_mul(a_copy, b, c);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    e[i][j] = (i == j) ? 1 : 0;
                }
            }
            double[][] R = matr_sub(c, e);
            double R_norm = matrixInfinityNorm(R);

            System.out.printf("%-10.1e %-10.1e %-10.1e %-10.1e %-10.1e %-10.1e %-10.1e %-10.1e%n",
                    alpha, beta, norm, norm_inv, cond, z_norm, E, R_norm);

            beta *= 10; // Увеличиваем beta в 10 раз
        }

        System.out.println("\nТаблица 2 (фиксированное beta = 1, убывающее alpha)");
        System.out.printf("%-10s %-10s %-10s %-12s %-8s %-14s %-8s %-10s%n",
                "alpha", "beta", "||A||", "||A_inv||", "V(A)", "||z||", "E", "||r||");

        beta = 1.;
        alpha = 1.;
        for (int step = 0; step <= 20; step++) { // От 1e0 до 1e-15
            g.mygen(a, a_inv, n, alpha, beta, 1, 2, 1, 1);

            // Вычисление норм матрицы
            double norm = matrixInfinityNorm(a);
            double norm_inv = matrixInfinityNorm(a_inv);
            double cond = norm * norm_inv; // Число обусловленности

            // Копирование матрицы для решения
            for (int i = 0; i < n; i++) {
                System.arraycopy(a[i], 0, a_copy[i], 0, n);
            }

            // Обращение методом отражений
            double[][] b = new double[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(a_copy[i], 0, b[i], 0, n);
            }
            ReflectionMethod reverser = new ReflectionMethod(b);
            reverser.findReversedMatrix();

            z = matr_sub(b, a_inv);
            double z_norm = matrixInfinityNorm(z);
            double E = z_norm / norm_inv; // Относительная погрешность

            double[][] c = new double[n][n];
            double[][] e = new double[n][n];
            matr_mul(a_copy, b, c);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    e[i][j] = (i == j) ? 1 : 0;
                }
            }
            double[][] R = matr_sub(c, e);
            double R_norm = matrixInfinityNorm(R);

            System.out.printf("%-10.1e %-10.1e %-10.1e %-10.1e %-10.1e %-10.1e %-10.1e %-10.1e%n",
                    alpha, beta, norm, norm_inv, cond, z_norm, E, R_norm);

            alpha /= 10; // Уменьшаем alpha
        }
    }

    public double[][] matr_sub(double[][] a, double[][] b) {
        double[][] result = new double[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    public void matr_mul(double[][] a, double[][] b, double[][] result) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
    }
}