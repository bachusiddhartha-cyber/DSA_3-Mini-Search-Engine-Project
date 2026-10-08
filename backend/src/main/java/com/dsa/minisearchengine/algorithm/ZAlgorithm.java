package com.dsa.minisearchengine.algorithm;

import com.dsa.minisearchengine.model.AlgorithmMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * DSA ALGORITHM: GUSFIELD'S Z ALGORITHM
 * ============================================================================
 * 
 * Syllabus Module 2: Linear-Time Pattern Matching
 * 
 * Algorithm Concept:
 * Computes the Z-array for the concatenated string S = pattern + "$" + text.
 * Z[i] is the length of the longest substring starting from S[i] that is also a
 * prefix of S. Maintains an active window [L, R] of the current prefix match to
 * achieve strictly linear time. Whenever Z[i] == pattern.length(), an exact match
 * is present in text at index (i - pattern.length() - 1).
 * 
 * Complexity:
 * - Time:  O(n + m) linear time
 * - Space: O(n + m) for concatenated string and Z-array
 * ============================================================================
 */
public class ZAlgorithm {

    public static int[] calculateZArray(String s, long[] comparisonCounter) {
        int len = s.length();
        int[] Z = new int[len];
        int L = 0, R = 0;

        for (int i = 1; i < len; i++) {
            if (i > R) {
                L = R = i;
                while (R < len && s.charAt(R) == s.charAt(R - L)) {
                    if (comparisonCounter != null) comparisonCounter[0]++;
                    R++;
                }
                if (comparisonCounter != null && R < len) comparisonCounter[0]++;
                Z[i] = R - L;
                R--;
            } else {
                int k = i - L;
                // If Z[k] is strictly within the current right interval
                if (Z[k] < R - i + 1) {
                    Z[i] = Z[k];
                } else {
                    // Try to extend the interval [L, R]
                    L = i;
                    while (R < len && s.charAt(R) == s.charAt(R - L)) {
                        if (comparisonCounter != null) comparisonCounter[0]++;
                        R++;
                    }
                    if (comparisonCounter != null && R < len) comparisonCounter[0]++;
                    Z[i] = R - L;
                    R--;
                }
            }
        }
        return Z;
    }

    public static AlgorithmMatchResult search(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new AlgorithmMatchResult("Z Algorithm", 0, new ArrayList<>(), 0, 0);
        }

        long startTime = System.nanoTime();
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();
        long[] comparisons = new long[]{0};

        // Form concatenated string with special sentinel delimiter not present in text
        String concat = pattern + "$" + text;
        int[] Z = calculateZArray(concat, comparisons);

        // Find matches: wherever Z[i] == m
        for (int i = m + 1; i < concat.length(); i++) {
            if (Z[i] == m) {
                // Offset back into original text
                positions.add(i - (m + 1));
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new AlgorithmMatchResult("Z Algorithm", positions.size(), positions, comparisons[0], executionTime);
    }
}
