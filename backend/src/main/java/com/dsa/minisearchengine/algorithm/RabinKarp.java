package com.dsa.minisearchengine.algorithm;

import com.dsa.minisearchengine.model.AlgorithmMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * DSA ALGORITHM: RABIN-KARP STRING SEARCH (ROLLING HASH)
 * ============================================================================
 * 
 * Syllabus Module 2: Hash-Based Pattern Matching & Modular Arithmetic
 * 
 * Algorithm Concept:
 * Computes a polynomial rolling hash of the pattern and the initial text window of size m.
 * In each iteration, computes the hash of the next window in O(1) time using Horner's rule
 * and modular arithmetic:
 *   H_next = (d * (H_prev - text[i] * h) + text[i + m]) % q
 * 
 * Crucial Academic Property:
 * Spurious Hits / Collision Handling: If hash(window) == hash(pattern), the algorithm MUST
 * verify character-by-character that the strings actually match to guard against hash collisions.
 * 
 * Complexity:
 * - Time:  Average Case O(n + m), Worst Case O(n * m) (if frequent collisions occur)
 * - Space: O(1) auxiliary space
 * ============================================================================
 */
public class RabinKarp {

    // Number of characters in the alphabet (ASCII base)
    private static final int BASE = 256;
    // A large prime number to avoid integer overflow and reduce hash collisions
    private static final long MODULUS = 1_000_000_007L;

    public static AlgorithmMatchResult search(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new AlgorithmMatchResult("Rabin-Karp", 0, new ArrayList<>(), 0, 0);
        }

        long startTime = System.nanoTime();
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();
        long comparisons = 0;

        // Precompute h = pow(BASE, m - 1) % MODULUS
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % MODULUS;
        }

        long patternHash = 0;
        long windowHash = 0;

        // Calculate the initial hash value of pattern and first window of text
        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + pattern.charAt(i)) % MODULUS;
            windowHash = (BASE * windowHash + text.charAt(i)) % MODULUS;
        }

        // Slide the pattern over text one by one
        for (int i = 0; i <= n - m; i++) {
            // Check if the hash values match
            if (patternHash == windowHash) {
                // Potential match: Perform character-by-character verification to confirm true match
                boolean exactMatch = true;
                for (int j = 0; j < m; j++) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        exactMatch = false;
                        break;
                    }
                }
                if (exactMatch) {
                    positions.add(i);
                }
            }

            // Calculate rolling hash for next window: Remove leading digit, add trailing digit
            if (i < n - m) {
                windowHash = (BASE * (windowHash - text.charAt(i) * h) + text.charAt(i + m)) % MODULUS;

                // Modulo can yield negative numbers in Java; ensure non-negative
                if (windowHash < 0) {
                    windowHash = (windowHash + MODULUS);
                }
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new AlgorithmMatchResult("Rabin-Karp", positions.size(), positions, comparisons, executionTime);
    }
}
