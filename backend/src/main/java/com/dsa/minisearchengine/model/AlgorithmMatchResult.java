package com.dsa.minisearchengine.model;

import java.util.List;

/**
 * Encapsulates the execution metrics of a string matching algorithm.
 * Tracks match count, exact 0-indexed positions in the text,
 * total character comparisons, and runtime measured via System.nanoTime().
 */
public class AlgorithmMatchResult {

    private String algorithmName;
    private int matchCount;
    private List<Integer> matchPositions;
    private long characterComparisons;
    private long executionTimeNanos;
    private double executionTimeMicros;

    public AlgorithmMatchResult() {
    }

    public AlgorithmMatchResult(String algorithmName, int matchCount, List<Integer> matchPositions,
                                long characterComparisons, long executionTimeNanos) {
        this.algorithmName = algorithmName;
        this.matchCount = matchCount;
        this.matchPositions = matchPositions;
        this.characterComparisons = characterComparisons;
        this.executionTimeNanos = executionTimeNanos;
        this.executionTimeMicros = executionTimeNanos / 1000.0;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public List<Integer> getMatchPositions() {
        return matchPositions;
    }

    public void setMatchPositions(List<Integer> matchPositions) {
        this.matchPositions = matchPositions;
    }

    public long getCharacterComparisons() {
        return characterComparisons;
    }

    public void setCharacterComparisons(long characterComparisons) {
        this.characterComparisons = characterComparisons;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
        this.executionTimeMicros = executionTimeNanos / 1000.0;
    }

    public double getExecutionTimeMicros() {
        return executionTimeMicros;
    }

    public void setExecutionTimeMicros(double executionTimeMicros) {
        this.executionTimeMicros = executionTimeMicros;
    }
}
