package com.dsa.minisearchengine.model;

import java.util.List;

/**
 * Result representation for Advanced String Analysis:
 * Suffix Array, Kasai LCP (Longest Repeated Substring), and Suffix Automaton (Distinct Substrings).
 */
public class AdvancedAnalysisResult {

    private String documentId;
    private String documentName;
    private int textLength;
    private String longestRepeatedSubstring;
    private int lrsLength;
    private long distinctSubstringsCount;
    private List<String> suffixArraySample;
    private List<Integer> lcpArraySample;
    private long analysisTimeNanos;

    public AdvancedAnalysisResult() {
    }

    public AdvancedAnalysisResult(String documentId, String documentName, int textLength,
                                  String longestRepeatedSubstring, int lrsLength,
                                  long distinctSubstringsCount, List<String> suffixArraySample,
                                  List<Integer> lcpArraySample, long analysisTimeNanos) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.textLength = textLength;
        this.longestRepeatedSubstring = longestRepeatedSubstring;
        this.lrsLength = lrsLength;
        this.distinctSubstringsCount = distinctSubstringsCount;
        this.suffixArraySample = suffixArraySample;
        this.lcpArraySample = lcpArraySample;
        this.analysisTimeNanos = analysisTimeNanos;
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

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public String getLongestRepeatedSubstring() {
        return longestRepeatedSubstring;
    }

    public void setLongestRepeatedSubstring(String longestRepeatedSubstring) {
        this.longestRepeatedSubstring = longestRepeatedSubstring;
    }

    public int getLrsLength() {
        return lrsLength;
    }

    public void setLrsLength(int lrsLength) {
        this.lrsLength = lrsLength;
    }

    public long getDistinctSubstringsCount() {
        return distinctSubstringsCount;
    }

    public void setDistinctSubstringsCount(long distinctSubstringsCount) {
        this.distinctSubstringsCount = distinctSubstringsCount;
    }

    public List<String> getSuffixArraySample() {
        return suffixArraySample;
    }

    public void setSuffixArraySample(List<String> suffixArraySample) {
        this.suffixArraySample = suffixArraySample;
    }

    public List<Integer> getLcpArraySample() {
        return lcpArraySample;
    }

    public void setLcpArraySample(List<Integer> lcpArraySample) {
        this.lcpArraySample = lcpArraySample;
    }

    public long getAnalysisTimeNanos() {
        return analysisTimeNanos;
    }

    public void setAnalysisTimeNanos(long analysisTimeNanos) {
        this.analysisTimeNanos = analysisTimeNanos;
    }
}
