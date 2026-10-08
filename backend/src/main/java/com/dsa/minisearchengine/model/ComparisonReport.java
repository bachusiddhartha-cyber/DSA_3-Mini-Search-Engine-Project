package com.dsa.minisearchengine.model;

import java.util.List;

/**
 * Report comparing multiple string matching algorithms side-by-side
 * on a single document text and query pattern.
 */
public class ComparisonReport {

    private String documentId;
    private String documentName;
    private String pattern;
    private int documentLength;
    private int patternLength;
    private List<AlgorithmMatchResult> results;
    private String fastestAlgorithm;

    public ComparisonReport() {
    }

    public ComparisonReport(String documentId, String documentName, String pattern,
                            int documentLength, int patternLength, List<AlgorithmMatchResult> results,
                            String fastestAlgorithm) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.pattern = pattern;
        this.documentLength = documentLength;
        this.patternLength = patternLength;
        this.results = results;
        this.fastestAlgorithm = fastestAlgorithm;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getDocumentLength() {
        return documentLength;
    }

    public void setDocumentLength(int documentLength) {
        this.documentLength = documentLength;
    }

    public int getPatternLength() {
        return patternLength;
    }

    public void setPatternLength(int patternLength) {
        this.patternLength = patternLength;
    }

    public List<AlgorithmMatchResult> getResults() {
        return results;
    }

    public void setResults(List<AlgorithmMatchResult> results) {
        this.results = results;
    }

    public String getFastestAlgorithm() {
        return fastestAlgorithm;
    }

    public void setFastestAlgorithm(String fastestAlgorithm) {
        this.fastestAlgorithm = fastestAlgorithm;
    }
}
