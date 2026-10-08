package com.dsa.minisearchengine.model;

/**
 * Data Transfer Object (DTO) representing a single search result item.
 * Includes filename, occurrences count, relevance percentage,
 * matched snippet, algorithm used, and sorting method used.
 */
public class SearchResult {

    private String fileName;
    private String documentId;
    private int occurrences;
    private int relevanceScore; // 0% to 100%
    private String snippet;
    private String algorithmUsed;
    private String sortMethodUsed;

    public SearchResult() {
    }

    public SearchResult(String fileName, String documentId, int occurrences, String snippet) {
        this.fileName = fileName;
        this.documentId = documentId;
        this.occurrences = occurrences;
        this.snippet = snippet;
        this.relevanceScore = 100;
        this.algorithmUsed = "Inverted Index (HashMap)";
        this.sortMethodUsed = "Comparator";
    }

    public SearchResult(String fileName, String documentId, int occurrences, int relevanceScore,
                        String snippet, String algorithmUsed, String sortMethodUsed) {
        this.fileName = fileName;
        this.documentId = documentId;
        this.occurrences = occurrences;
        this.relevanceScore = relevanceScore;
        this.snippet = snippet;
        this.algorithmUsed = algorithmUsed;
        this.sortMethodUsed = sortMethodUsed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public int getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(int relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }

    public String getSortMethodUsed() {
        return sortMethodUsed;
    }

    public void setSortMethodUsed(String sortMethodUsed) {
        this.sortMethodUsed = sortMethodUsed;
    }
}
