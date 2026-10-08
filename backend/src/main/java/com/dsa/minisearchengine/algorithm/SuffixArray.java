package com.dsa.minisearchengine.algorithm;

import com.dsa.minisearchengine.model.AlgorithmMatchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * DSA ALGORITHM: SUFFIX ARRAY & BINARY SEARCH PATTERN MATCHING
 * ============================================================================
 * 
 * Syllabus Module 2: Suffix Arrays & Substring Indexing
 * 
 * Algorithm Concept:
 * A Suffix Array is an array of integers representing the starting positions of
 * all suffixes of a string S sorted in lexicographical order.
 * 
 * Suffix Array Search:
 * Once the Suffix Array SA is constructed for text S, ANY pattern P of length m
 * can be located using TWO BINARY SEARCHES (lower bound and upper bound) because
 * all suffixes that share P as a prefix will form a contiguous block in SA!
 * 
 * Complexity:
 * - Construction: O(n * log(n) * n) = O(n^2 log n) using standard suffix sorting,
 *                 or O(n log^2 n) via prefix doubling.
 *                 (SA-IS is theoretical O(n) but significantly more complex).
 * - Pattern Search: O(m * log n) where m is pattern length and n is text length.
 * - Space Complexity: O(n) for suffix indices.
 * ============================================================================
 */
public class SuffixArray {

    private final String text;
    private final int[] suffixArray;

    public SuffixArray(String text) {
        this.text = text;
        this.suffixArray = buildSuffixArray(text);
    }

    public int[] getSuffixArray() {
        return suffixArray;
    }

    public String getText() {
        return text;
    }

    /**
     * Constructs the Suffix Array by sorting suffix indices.
     */
    private int[] buildSuffixArray(String s) {
        int n = s.length();
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        // Sort indices based on lexicographical order of suffixes starting at those indices
        Arrays.sort(indices, (a, b) -> {
            int lenA = n - a;
            int lenB = n - b;
            int minLen = Math.min(lenA, lenB);
            for (int i = 0; i < minLen; i++) {
                char c1 = s.charAt(a + i);
                char c2 = s.charAt(b + i);
                if (c1 != c2) return Character.compare(c1, c2);
            }
            return Integer.compare(lenA, lenB);
        });

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = indices[i];
        }
        return sa;
    }

    /**
     * Searches for occurrences of a pattern using Binary Search on the Suffix Array.
     * Returns matching 0-based positions in text.
     */
    public AlgorithmMatchResult search(String pattern) {
        if (pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new AlgorithmMatchResult("Suffix Array Search", 0, new ArrayList<>(), 0, 0);
        }

        long startTime = System.nanoTime();
        int n = text.length();
        int m = pattern.length();
        long comparisons = 0;

        // Binary search for first occurrence (lower bound)
        int low = 0, high = n - 1;
        int first = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffixStart = suffixArray[mid];

            // Compare pattern with suffix text[suffixStart ... ]
            int cmp = comparePatternWithSuffix(pattern, suffixStart, n);
            comparisons++;

            if (cmp == 0) {
                first = mid;
                high = mid - 1; // Look left for first matching suffix
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        List<Integer> positions = new ArrayList<>();
        if (first != -1) {
            // Binary search for last occurrence (upper bound)
            low = first;
            high = n - 1;
            int last = first;

            while (low <= high) {
                int mid = low + (high - low) / 2;
                int suffixStart = suffixArray[mid];
                int cmp = comparePatternWithSuffix(pattern, suffixStart, n);
                comparisons++;

                if (cmp == 0) {
                    last = mid;
                    low = mid + 1; // Look right for last matching suffix
                } else if (cmp < 0) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }

            // All suffixes from index 'first' to 'last' in suffixArray start with pattern
            for (int i = first; i <= last; i++) {
                positions.add(suffixArray[i]);
            }
            positions.sort(Integer::compareTo);
        }

        long executionTime = System.nanoTime() - startTime;
        return new AlgorithmMatchResult("Suffix Array Search", positions.size(), positions, comparisons, executionTime);
    }

    private int comparePatternWithSuffix(String pattern, int suffixStart, int textLength) {
        int m = pattern.length();
        int available = textLength - suffixStart;
        int len = Math.min(m, available);

        for (int i = 0; i < len; i++) {
            char pChar = pattern.charAt(i);
            char sChar = text.charAt(suffixStart + i);
            if (pChar != sChar) {
                return Character.compare(pChar, sChar);
            }
        }

        if (available >= m) {
            return 0; // Pattern is a prefix of this suffix
        }
        return 1; // Pattern is longer than suffix
    }
}
