package com.dsa.minisearchengine.algorithm;

import java.util.*;

/**
 * ============================================================================
 * DSA ALGORITHM: DOCUMENT SIMILARITY (VECTOR SPACE MODEL & COSINE SIMILARITY)
 * ============================================================================
 * 
 * Syllabus Module 1: Document Similarity & Information Retrieval
 * 
 * Algorithm Concept:
 * Evaluates semantic and lexical proximity between two documents by projecting
 * each document into an N-dimensional Euclidean space where each dimension represents
 * a unique term in the combined vocabulary. The term frequency (TF) forms the vector magnitude.
 * 
 * Formula:
 *   Cosine Similarity = (A . B) / (||A|| * ||B||)
 *   where:
 *     A . B = Sum(TF_A(w) * TF_B(w))
 *     ||A|| = Sqrt(Sum(TF_A(w)^2))
 *     ||B|| = Sqrt(Sum(TF_B(w)^2))
 * 
 * Value ranges between 0.0 (orthogonal, no terms in common) to 1.0 (identical distribution).
 * 
 * Complexity:
 * - Time:  O(W1 + W2 + V) where W = words in docs, V = combined vocabulary size
 * - Space: O(V) for the term frequency maps
 * ============================================================================
 */
public class CosineSimilarity {

    public static class SimilarityMetric {
        public final double score; // 0.0 to 1.0
        public final double percentage; // 0.0% to 100.0%
        public final List<String> sharedWords;
        public final int doc1UniqueWords;
        public final int doc2UniqueWords;

        public SimilarityMetric(double score, List<String> sharedWords, int doc1UniqueWords, int doc2UniqueWords) {
            this.score = score;
            this.percentage = Math.round(score * 10000.0) / 100.0;
            this.sharedWords = sharedWords;
            this.doc1UniqueWords = doc1UniqueWords;
            this.doc2UniqueWords = doc2UniqueWords;
        }
    }

    public static SimilarityMetric compute(Map<String, Integer> freqMap1, Map<String, Integer> freqMap2) {
        if (freqMap1 == null || freqMap2 == null || freqMap1.isEmpty() || freqMap2.isEmpty()) {
            return new SimilarityMetric(0.0, Collections.emptyList(), 
                    freqMap1 != null ? freqMap1.size() : 0, 
                    freqMap2 != null ? freqMap2.size() : 0);
        }

        // Identify common vocabulary
        Set<String> allWords = new HashSet<>(freqMap1.keySet());
        allWords.addAll(freqMap2.keySet());

        List<String> commonWords = new ArrayList<>();
        double dotProduct = 0.0;
        double norm1Sq = 0.0;
        double norm2Sq = 0.0;

        for (String word : allWords) {
            int count1 = freqMap1.getOrDefault(word, 0);
            int count2 = freqMap2.getOrDefault(word, 0);

            if (count1 > 0 && count2 > 0) {
                commonWords.add(word);
            }

            dotProduct += (double) count1 * count2;
            norm1Sq += (double) count1 * count1;
            norm2Sq += (double) count2 * count2;
        }

        double denominator = Math.sqrt(norm1Sq) * Math.sqrt(norm2Sq);
        double cosine = (denominator > 0.0) ? (dotProduct / denominator) : 0.0;

        // Sort common words alphabetically
        Collections.sort(commonWords);

        return new SimilarityMetric(cosine, commonWords, freqMap1.size(), freqMap2.size());
    }
}
