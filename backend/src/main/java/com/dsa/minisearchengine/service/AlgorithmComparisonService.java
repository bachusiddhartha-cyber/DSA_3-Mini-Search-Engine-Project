package com.dsa.minisearchengine.service;

import com.dsa.minisearchengine.algorithm.*;
import com.dsa.minisearchengine.model.AlgorithmMatchResult;
import com.dsa.minisearchengine.model.ComparisonReport;
import com.dsa.minisearchengine.model.DocumentModel;
import com.dsa.minisearchengine.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service orchestrating side-by-side benchmark comparison between
 * Naive, KMP, Z Algorithm, and Rabin-Karp on any stored document.
 */
@Service
public class AlgorithmComparisonService {

    private final DocumentRepository documentRepository;

    public AlgorithmComparisonService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Runs all four string search algorithms on the specified document's content
     * and compiles a detailed comparative benchmark report.
     */
    public ComparisonReport compareAlgorithms(String documentId, String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("Search pattern cannot be empty.");
        }

        Optional<DocumentModel> docOpt = documentRepository.findById(documentId);
        if (docOpt.isEmpty()) {
            throw new IllegalArgumentException("Document not found with ID: " + documentId);
        }

        DocumentModel doc = docOpt.get();
        String text = doc.getContent();
        String query = pattern.trim();

        // Case-insensitive normalization for pattern searching
        String lowerText = text.toLowerCase();
        String lowerPattern = query.toLowerCase();

        List<AlgorithmMatchResult> results = new ArrayList<>();

        // 1. Run Naive String Search
        results.add(NaiveStringSearch.search(lowerText, lowerPattern));

        // 2. Run KMP String Search
        results.add(KMPStringSearch.search(lowerText, lowerPattern));

        // 3. Run Z Algorithm
        results.add(ZAlgorithm.search(lowerText, lowerPattern));

        // 4. Run Rabin-Karp Algorithm
        results.add(RabinKarp.search(lowerText, lowerPattern));

        // Determine the fastest algorithm based on executionTimeNanos
        String fastest = results.stream()
                .min(Comparator.comparingLong(AlgorithmMatchResult::getExecutionTimeNanos))
                .map(AlgorithmMatchResult::getAlgorithmName)
                .orElse("N/A");

        return new ComparisonReport(
                doc.getId(),
                doc.getFileName(),
                query,
                text.length(),
                query.length(),
                results,
                fastest
        );
    }
}
