package com.dsa.minisearchengine.algorithm;

import com.dsa.minisearchengine.model.AlgorithmMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * DSA ALGORITHM: KNUTH-MORRIS-PRATT (KMP) STRING SEARCH
 * ============================================================================
 * 
 * Syllabus Module 2: Linear-Time Exact String Matching
 * 
 * Algorithm Concept:
 * Avoids redundant comparisons by exploiting information gathered during previous
 * partial matches. Constructs an LPS (Longest Proper Prefix which is also a Suffix)
 * array / failure function for the pattern of size m. When a mismatch occurs at
 * pattern index j, the next character to compare is at index lps[j-1], never
 * backtracking the text pointer i.
 * 
 * Complexity:
 * - Preprocessing Time (LPS): O(m)
 * - Matching Time:             O(n)
 * - Overall Time Complexity:   O(n + m) deterministic linear time
 * - Space Complexity:          O(m) auxiliary space for the LPS array
 * ============================================================================
 */
public class KMPStringSearch {

    /**
     * Precomputes the Longest Prefix Suffix (LPS) array for the given pattern.
     * lps[i] stores the length of the longest proper prefix of pattern[0...i]
     * that is also a suffix of pattern[0...i].
     */
    public static int[] computeLPSArray(String pattern, long[] comparisonCounter) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; // length of previous longest prefix suffix
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        while (i < m) {
            if (comparisonCounter != null) comparisonCounter[0]++;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    // Fall back to previous matching prefix length without incrementing i
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static AlgorithmMatchResult search(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new AlgorithmMatchResult("KMP Search", 0, new ArrayList<>(), 0, 0);
        }

        long startTime = System.nanoTime();
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();
        long[] comparisons = new long[]{0};

        // Step 1: Preprocess LPS table in O(m)
        int[] lps = computeLPSArray(pattern, comparisons);

        // Step 2: Linear scan of text in O(n)
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            comparisons[0]++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                // Match found at 0-indexed position (i - j)
                positions.add(i - j);
                // Reset j to the longest prefix suffix to look for subsequent matches
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                // Mismatch after j matches
                if (j != 0) {
                    // Do not increment i; jump j to failure index
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new AlgorithmMatchResult("KMP Search", positions.size(), positions, comparisons[0], executionTime);
    }
}
