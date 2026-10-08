package com.dsa.minisearchengine.algorithm;

import java.security.SecureRandom;

/**
 * ============================================================================
 * DSA ALGORITHM: RANDOMIZED UNIVERSAL HASHING
 * ============================================================================
 * 
 * Syllabus Module 6: Randomized Algorithms & Universal Hashing
 * 
 * Algorithm Concept:
 * Fixed polynomial hash functions can be subject to worst-case adversarial collisions
 * where an attacker crafts strings with identical hashes.
 * Universal Hashing selects a hash function h(x) at RANDOM at application startup
 * from a family of universal hash functions:
 *   h(x) = (Sum_{i} x_i * a^(m-1-i) + b) mod p
 * 
 * Because the coefficient 'a' and constant 'b' are selected randomly upon startup,
 * the probability of two distinct strings colliding is mathematically bounded by 1/p.
 * 
 * Used in this search engine for:
 * - Document fingerprinting
 * - Near-duplicate detection
 * - Content integrity checking
 * ============================================================================
 */
public class RandomizedHash {

    private static final long PRIME = 2_147_483_647L; // 2^31 - 1 (Mersenne prime M31)
    private static final long MULTIPLIER;
    private static final long OFFSET;

    static {
        SecureRandom rng = new SecureRandom();
        // Choose randomized multiplier coprime to PRIME (1 < a < PRIME)
        MULTIPLIER = 257L + rng.nextInt(1000);
        OFFSET = 1L + rng.nextInt(10000);
    }

    /**
     * Computes the randomized polynomial fingerprint of a string.
     */
    public static long computeFingerprint(String text) {
        if (text == null || text.isEmpty()) {
            return 0L;
        }

        long hash = OFFSET;
        for (int i = 0; i < text.length(); i++) {
            hash = (hash * MULTIPLIER + text.charAt(i)) % PRIME;
        }
        return hash;
    }

    public static long getMultiplier() {
        return MULTIPLIER;
    }

    public static long getOffset() {
        return OFFSET;
    }
}
