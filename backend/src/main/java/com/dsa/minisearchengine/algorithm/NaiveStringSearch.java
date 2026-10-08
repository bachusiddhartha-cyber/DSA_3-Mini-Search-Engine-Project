package com.dsa.minisearchengine.algorithm;

import com.dsa.minisearchengine.model.AlgorithmMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * DSA ALGORITHM: NAIVE STRING MATCHING (SLIDING WINDOW)
 * ============================================================================
 * 
 * Syllabus Module 2: String Algorithms & Pattern Search
 * 
 * Algorithm Concept:
 * Slides a pattern window of size m along text of size n character-by-character.
 * At every offset i from 0 to (n - m), tests if text[i ... i+m-1] matches pattern[0 ... m-1].
 * 
 * Complexity:
 * - Time:  Worst Case O(n * m), Best Case O(n)
 * - Space: O(1) auxiliary space (excluding match positions list)
 * ============================================================================
 */
public class NaiveStringSearch {

    public static AlgorithmMatchResult search(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new AlgorithmMatchResult("Naive Search", 0, new ArrayList<>(), 0, 0);
        }

        long startTime = System.nanoTime();
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();
        long comparisons = 0;

        // Slide pattern over text window by window
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            // Check for pattern match at current index i
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            // If pattern matched completely
            if (j == m) {
                positions.add(i);
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new AlgorithmMatchResult("Naive Search", positions.size(), positions, comparisons, executionTime);
    }
}
