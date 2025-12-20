package com.hdekker.finance_cash_flow.app.forecast;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;

import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;

/**
 * A Java record to represent a quadratic function f(x) = a*x^2 + b*x + c.
 * Records are perfect for carrying immutable data and providing utility methods.
 */
public record QuadraticCalculation(QuadraticCoefficients coefficients, YearMonth startMonth) {
	
	
	public record QuadraticCoefficients(double a, double b, double c) {}
    /**
     * Calculates the value of the quadratic function for a given input x.
     * f(x) = a*x^2 + b*x + c
     *
     * @param x The input value.
     * @return The resulting value of the function.
     */
	public double evaluate(YearMonth targetMonth) {
        // Step 1: Calculate the time index 'x'
        // x is the number of months between the startMonth and the targetMonth.
        long x_long = ChronoUnit.MONTHS.between(startMonth, targetMonth);
        double x = (double) x_long; // Convert to double for math operations

        // Retrieve coefficients
        double a = coefficients.a();
        double b = coefficients.b();
        double c = coefficients.c();

        // Step 2: Evaluate the quadratic equation
        // y = a*x^2 + b*x + c
        double termA = a * x * x; 
        double termB = b * x;
        double termC = c;
        
        return termA + termB + termC;
    }
	
public static QuadraticCalculation interpollate(Map<YearMonth, SummedTransactions> data) {
    	
    	TreeMap<YearMonth, SummedTransactions> sortedData = new TreeMap<>(data);
        if (sortedData.size() < 3) {
            throw new IllegalArgumentException("Quadratic fit requires at least 3 data points.");
        }
    	
        YearMonth startMonth = sortedData.firstKey();
        int n = sortedData.size();
        
     // Summation variables for the Normal Equations
        double sum_x = 0;
        double sum_x2 = 0;
        double sum_x3 = 0;
        double sum_x4 = 0;
        double sum_y = 0;
        double sum_xy = 0;
        double sum_x2y = 0;
        
        // 2. Calculate Summations by converting YearMonth to Time Index (x)
        for (Map.Entry<YearMonth, SummedTransactions> entry : sortedData.entrySet()) {
            // x: The index (number of months since the start month)
            double x = ChronoUnit.MONTHS.between(startMonth, entry.getKey()); 
            double y = entry.getValue().amount();

            double x2 = x * x;
            double x3 = x2 * x;
            double x4 = x3 * x;
            
            sum_x += x;
            sum_x2 += x2;
            sum_x3 += x3;
            sum_x4 += x4;
            sum_y += y;
            sum_xy += x * y;
            sum_x2y += x2 * y;
        }

        // 3. Build the 3x3 System of Linear Equations (Matrix A and Vector B)
        // A * [a, b, c]^T = B
        
        double[][] A = {
            { sum_x4, sum_x3, sum_x2 }, 
            { sum_x3, sum_x2, sum_x },  
            { sum_x2, sum_x,  (double)n } 
        };
        
        double[] B = { sum_x2y, sum_xy, sum_y };
        
        double[] X = solve3x3System(A, B); 
        
        double a = X[0];
        double b = X[1];
        double c = X[2]; 

        return new QuadraticCalculation(
        		new QuadraticCoefficients(a, b, c),
        		startMonth);
        
        
        
    }
    
    /**
     * Solves a 3x3 system of linear equations A * X = B for X using Gaussian Elimination.
     * A is the 3x3 matrix of sums (from the Normal Equations).
     * B is the 3x1 vector of sums.
     * X is the solution vector [a, b, c].
     */
    private static double[] solve3x3System(double[][] A, double[] B) {
        
        // 1. Create the Augmented Matrix (3x4)
        // Combine A and B into a single matrix [A | B]
        double[][] M = new double[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                M[i][j] = A[i][j];
            }
            M[i][3] = B[i];
        }
        
        // 2. Forward Elimination (Creating the Upper Triangular Form)
        // This process eliminates the lower-left elements (M[1][0], M[2][0], M[2][1])
        
        for (int pivot = 0; pivot < 3; pivot++) {
            // Find the largest pivot element in the current column (for stability - Partial Pivoting)
            int max = pivot;
            for (int i = pivot + 1; i < 3; i++) {
                if (Math.abs(M[i][pivot]) > Math.abs(M[max][pivot])) {
                    max = i;
                }
            }
            
            // Swap rows to move the largest pivot to the top of the section
            double[] temp = M[pivot];
            M[pivot] = M[max];
            M[max] = temp;

            // Check for singularity (matrix A is non-invertible or rank deficient)
            if (Math.abs(M[pivot][pivot]) <= 1e-9) { // Using a tolerance for floating-point comparison
                 throw new ArithmeticException("Matrix is singular or nearly singular; cannot solve system.");
            }
            
            // Eliminate all entries below the pivot
            for (int i = pivot + 1; i < 3; i++) {
                double factor = M[i][pivot] / M[pivot][pivot];
                for (int j = pivot; j < 4; j++) {
                    M[i][j] -= factor * M[pivot][j];
                }
            }
        }

        // 3. Back-Substitution (Solving for X = [a, b, c])
        double[] X = new double[3]; // X[0]=a, X[1]=b, X[2]=c
        
        // Solve for the last variable (c) first, then b, then a.
        for (int i = 2; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < 3; j++) {
                sum += M[i][j] * X[j];
            }
            // X[i] = (B'[i] - sum) / A'[i][i]
            X[i] = (M[i][3] - sum) / M[i][i];
        }
        
        // X[0]=a, X[1]=b, X[2]=c
        return X;
    }

}
