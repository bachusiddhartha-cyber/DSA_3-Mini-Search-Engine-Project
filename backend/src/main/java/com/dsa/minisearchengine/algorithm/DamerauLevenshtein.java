package com.dsa.minisearchengine.algorithm;

/**
 * ============================================================================
 * DSA ALGORITHM: DAMERAU-LEVENSHTEIN DISTANCE (TRANSPOSITION DP)
 * ============================================================================
 * 
 * Syllabus Module 3: Dynamic Programming & Fuzzy Matching
 * 
 * Algorithm Concept:
 * Extends the Levenshtein distance by allowing ADJACENT TRANSPOSITIONS as a fourth
 * elementary operation (swapping two adjacent characters, e.g., 'ab' <-> 'ba').
 * This is crucial for real-world search engines because over 80% of human typing
 * errors involve transpositions (such as typing 'pyhton' instead of 'python').
 * 
 * Recurrence:
 *   If i > 1 and j > 1 and s1[i-1] == s2[j-2] and s1[i-2] == s2[j-1]:
 *     dp[i][j] = min(dp[i][j], dp[i-2][j-2] + 1)
 * 
 * Complexity:
 * - Time:  O(n * m)
 * - Space: O(n * m)
 * ============================================================================
 */
public class DamerauLevenshtein {

    public static int compute(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null || s1.isEmpty()) return s2 != null ? s2.length() : 0;
        if (s2 == null || s2.isEmpty()) return s1.length();

        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                // Standard Levenshtein: deletion, insertion, substitution
                dp[i][j] = Math.min(dp[i - 1][j] + 1,                     // deletion
                           Math.min(dp[i][j - 1] + 1,                     // insertion
                                    dp[i - 1][j - 1] + cost));            // substitution

                // Damerau extension: transposition of adjacent characters
                if (i > 1 && j > 1 &&
                    s1.charAt(i - 1) == s2.charAt(j - 2) &&
                    s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);  // transposition
                }
            }
        }

        return dp[n][m];
    }

    public static double similarityScore(String s1, String s2) {
        int maxLen = Math.max(s1 != null ? s1.length() : 0, s2 != null ? s2.length() : 0);
        if (maxLen == 0) return 1.0;
        int dist = compute(s1, s2);
        return Math.max(0.0, 1.0 - ((double) dist / maxLen));
    }
}
