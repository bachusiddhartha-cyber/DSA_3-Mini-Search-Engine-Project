package com.dsa.minisearchengine.model;

import java.util.Map;

/**
 * Result representation for Aho-Corasick multi-pattern search.
 * Contains occurrences per pattern for a specific document.
 */
public class MultiPatternResult {

    private String documentId;
    private String fileName;
    private Map<String, Integer> patternCounts;
    private int totalMatches;
    private long executionTimeNanos;

    public MultiPatternResult() {
    }

    public MultiPatternResult(String documentId, String fileName, Map<String, Integer> patternCounts,
                              int totalMatches, long executionTimeNanos) {
        this.documentId = documentId;
        this.fileName = fileName;
        this.patternCounts = patternCounts;
        this.totalMatches = totalMatches;
        this.executionTimeNanos = executionTimeNanos;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Integer> getPatternCounts() {
        return patternCounts;
    }

    public void setPatternCounts(Map<String, Integer> patternCounts) {
        this.patternCounts = patternCounts;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }
}
