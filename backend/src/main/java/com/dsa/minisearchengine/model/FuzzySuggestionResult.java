package com.dsa.minisearchengine.model;

import java.util.List;

/**
 * Result representation for Fuzzy Search / Spellcheck suggestions.
 * Computes both Levenshtein and Damerau-Levenshtein distances.
 */
public class FuzzySuggestionResult {

    private String originalQuery;
    private String suggestedTerm;
    private int levenshteinDistance;
    private int damerauDistance;
    private double similarityScore;
    private List<SearchResult> searchResults;

    public FuzzySuggestionResult() {
    }

    public FuzzySuggestionResult(String originalQuery, String suggestedTerm, int levenshteinDistance,
                                 int damerauDistance, double similarityScore, List<SearchResult> searchResults) {
        this.originalQuery = originalQuery;
        this.suggestedTerm = suggestedTerm;
        this.levenshteinDistance = levenshteinDistance;
        this.damerauDistance = damerauDistance;
        this.similarityScore = similarityScore;
        this.searchResults = searchResults;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public String getSuggestedTerm() {
        return suggestedTerm;
    }

    public void setSuggestedTerm(String suggestedTerm) {
        this.suggestedTerm = suggestedTerm;
    }

    public int getLevenshteinDistance() {
        return levenshteinDistance;
    }

    public void setLevenshteinDistance(int levenshteinDistance) {
        this.levenshteinDistance = levenshteinDistance;
    }

    public int getDamerauDistance() {
        return damerauDistance;
    }

    public void setDamerauDistance(int damerauDistance) {
        this.damerauDistance = damerauDistance;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }
}
