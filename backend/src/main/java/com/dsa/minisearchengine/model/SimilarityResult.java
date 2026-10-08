package com.dsa.minisearchengine.model;

import java.util.List;

/**
 * Encapsulates Cosine Similarity analysis between two documents.
 */
public class SimilarityResult {

    private String doc1Id;
    private String doc1Name;
    private String doc2Id;
    private String doc2Name;
    private double similarityScore; // 0.0 to 1.0
    private double similarityPercentage; // 0.0% to 100.0%
    private List<String> commonWords;
    private int doc1UniqueWords;
    private int doc2UniqueWords;

    public SimilarityResult() {
    }

    public SimilarityResult(String doc1Id, String doc1Name, String doc2Id, String doc2Name,
                            double similarityScore, double similarityPercentage, List<String> commonWords,
                            int doc1UniqueWords, int doc2UniqueWords) {
        this.doc1Id = doc1Id;
        this.doc1Name = doc1Name;
        this.doc2Id = doc2Id;
        this.doc2Name = doc2Name;
        this.similarityScore = similarityScore;
        this.similarityPercentage = similarityPercentage;
        this.commonWords = commonWords;
        this.doc1UniqueWords = doc1UniqueWords;
        this.doc2UniqueWords = doc2UniqueWords;
    }

    public String getDoc1Id() {
        return doc1Id;
    }

    public void setDoc1Id(String doc1Id) {
        this.doc1Id = doc1Id;
    }

    public String getDoc1Name() {
        return doc1Name;
    }

    public void setDoc1Name(String doc1Name) {
        this.doc1Name = doc1Name;
    }

    public String getDoc2Id() {
        return doc2Id;
    }

    public void setDoc2Id(String doc2Id) {
        this.doc2Id = doc2Id;
    }

    public String getDoc2Name() {
        return doc2Name;
    }

    public void setDoc2Name(String doc2Name) {
        this.doc2Name = doc2Name;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public double getSimilarityPercentage() {
        return similarityPercentage;
    }

    public void setSimilarityPercentage(double similarityPercentage) {
        this.similarityPercentage = similarityPercentage;
    }

    public List<String> getCommonWords() {
        return commonWords;
    }

    public void setCommonWords(List<String> commonWords) {
        this.commonWords = commonWords;
    }

    public int getDoc1UniqueWords() {
        return doc1UniqueWords;
    }

    public void setDoc1UniqueWords(int doc1UniqueWords) {
        this.doc1UniqueWords = doc1UniqueWords;
    }

    public int getDoc2UniqueWords() {
        return doc2UniqueWords;
    }

    public void setDoc2UniqueWords(int doc2UniqueWords) {
        this.doc2UniqueWords = doc2UniqueWords;
    }
}
