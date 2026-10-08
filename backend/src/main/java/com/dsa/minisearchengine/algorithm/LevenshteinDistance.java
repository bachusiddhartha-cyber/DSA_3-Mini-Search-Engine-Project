package com.dsa.minisearchengine.algorithm;

/**
 * ============================================================================
 * DSA ALGORITHM: LEVENSHTEIN DISTANCE (WAGNER-FISCHER DP)
 * ============================================================================
 * 
 * Syllabus Module 3: Dynamic Programming & Edit Distance
 * 
 * Algorithm Concept:
 * Computes the minimum number of single-character edit operations (Insertion,
 * Deletion, Substitution) required to transform string s1 into string s2.
 * Uses a Dynamic Programming table dp[i][j]:
 *   dp[i][j] = dp[i-1][j-1] if s1[i-1] == s2[j-1]
 *   dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) otherwise
 * 
 * Complexity:
 * - Time:  O(n * m) where n = s1.length(), m = s2.length()
 * - Space: O(n * m) or O(min(n, m)) with row optimization
 * ============================================================================
 */
public class LevenshteinDistance {

    /**
     * Calculates the minimum Levenshtein edit distance between two strings.
     */
    public static int compute(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null || s1.isEmpty()) return s2 != null ? s2.length() : 0;
        if (s2 == null || s2.isEmpty()) return s1.length();

        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        // Base cases: transforming to/from empty string
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        // DP recurrence transitions
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // No edit operation required
                } else {
                    int insertion    = dp[i][j - 1];
                    int deletion     = dp[i - 1][j];
                    int substitution = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(insertion, Math.min(deletion, substitution));
                }
            }
        }

        return dp[n][m];
    }

    /**
     * Normalized similarity score between 0.0 (completely dissimilar) and 1.0 (identical).
     */
    public static double similarityScore(String s1, String s2) {
        int maxLen = Math.max(s1 != null ? s1.length() : 0, s2 != null ? s2.length() : 0);
        if (maxLen == 0) return 1.0;
        int dist = compute(s1, s2);
        return Math.max(0.0, 1.0 - ((double) dist / maxLen));
    }
}
