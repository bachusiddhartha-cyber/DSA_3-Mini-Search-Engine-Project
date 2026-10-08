package com.dsa.minisearchengine.algorithm;

/**
 * ============================================================================
 * DSA ALGORITHM: KASAI'S LCP (LONGEST COMMON PREFIX) ARRAY ALGORITHM
 * ============================================================================
 * 
 * Syllabus Module 2: Suffix Arrays & LCP Arrays
 * 
 * Algorithm Concept:
 * Given a text S of length n and its Suffix Array SA, the LCP array stores the
 * length of the longest common prefix between adjacent suffixes in the sorted
 * suffix array: LCP[i] = length of LCP of suffixes SA[i] and SA[i - 1].
 * 
 * Kasai's Key Insight:
 * If the LCP of suffix SA[i] and suffix SA[i-1] has length h, then suffix SA[i] + 1
 * and suffix SA[i-1] + 1 must share at least (h - 1) characters!
 * Therefore, h can decrease by at most 1 at each step, yielding an optimal
 * O(n) LINEAR TIME complexity without re-scanning characters from scratch.
 * 
 * Search Engine Feature:
 * The maximum value in the LCP array corresponds to the
 * LONGEST REPEATED SUBSTRING (LRS) inside the document!
 * 
 * Complexity:
 * - Time:  O(n) linear time
 * - Space: O(n) for inverse suffix array and LCP array
 * ============================================================================
 */
public class KasaiLCP {

    public static class LCPResult {
        public final int[] lcp;
        public final String longestRepeatedSubstring;
        public final int lrsLength;

        public LCPResult(int[] lcp, String longestRepeatedSubstring, int lrsLength) {
            this.lcp = lcp;
            this.longestRepeatedSubstring = longestRepeatedSubstring;
            this.lrsLength = lrsLength;
        }
    }

    /**
     * Constructs the LCP array in O(n) using Kasai's algorithm and extracts the
     * longest repeated substring.
     */
    public static LCPResult buildLCPArray(String text, int[] suffixArray) {
        int n = text.length();
        if (n == 0 || suffixArray == null || suffixArray.length != n) {
            return new LCPResult(new int[0], "", 0);
        }

        int[] lcp = new int[n];
        int[] rank = new int[n];

        // Step 1: Compute inverse suffix array (rank of suffix starting at index i)
        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        // Step 2: Kasai's linear scan
        int h = 0; // length of current LCP
        int maxLcp = 0;
        int maxLcpIndex = -1;

        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = suffixArray[rank[i] - 1]; // previous suffix in sorted order

                // Extend h as long as characters match
                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) {
                    h++;
                }

                lcp[rank[i]] = h;

                // Track the longest repeated substring
                if (h > maxLcp) {
                    maxLcp = h;
                    maxLcpIndex = i;
                }

                // Kasai's invariant: h can decrease by at most 1 for the next suffix
                if (h > 0) {
                    h--;
                }
            }
        }

        String longestRepeated = (maxLcp > 0 && maxLcpIndex >= 0)
                ? text.substring(maxLcpIndex, maxLcpIndex + maxLcp).trim()
                : "";

        return new LCPResult(lcp, longestRepeated, maxLcp);
    }
}
