package com.dsa.minisearchengine.controller;

import com.dsa.minisearchengine.model.*;
import com.dsa.minisearchengine.service.AlgorithmComparisonService;
import com.dsa.minisearchengine.service.DocumentAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing advanced DSA algorithms:
 * - Side-by-side string matching algorithm benchmark
 * - Aho-Corasick multi-pattern search
 * - Levenshtein & Damerau-Levenshtein fuzzy matching / spellcheck
 * - Cosine document similarity
 * - Suffix Array, Kasai LCP, and Suffix Automaton analysis
 */
@RestController
@RequestMapping("/api/algorithms")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "*"})
public class AlgorithmController {

    private final AlgorithmComparisonService comparisonService;
    private final DocumentAnalysisService analysisService;

    public AlgorithmController(AlgorithmComparisonService comparisonService,
                               DocumentAnalysisService analysisService) {
        this.comparisonService = comparisonService;
        this.analysisService = analysisService;
    }

    /**
     * Compares Naive, KMP, Z Algorithm, and Rabin-Karp on a selected document.
     * POST /api/algorithms/compare
     * Request Body: { "documentId": "...", "pattern": "java" }
     */
    @PostMapping("/compare")
    public ResponseEntity<ComparisonReport> compareAlgorithms(@RequestBody Map<String, String> payload) {
        String documentId = payload.get("documentId");
        String pattern = payload.get("pattern");

        if (documentId == null || pattern == null) {
            return ResponseEntity.badRequest().build();
        }

        ComparisonReport report = comparisonService.compareAlgorithms(documentId, pattern);
        return ResponseEntity.ok(report);
    }

    /**
     * Executes Aho-Corasick simultaneous multi-pattern search across all documents.
     * POST /api/algorithms/multi-search
     * Request Body: { "patterns": ["java", "python", "stack", "queue"] }
     */
    @PostMapping("/multi-search")
    public ResponseEntity<List<MultiPatternResult>> multiSearch(@RequestBody Map<String, List<String>> payload) {
        List<String> patterns = payload.get("patterns");
        if (patterns == null || patterns.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<MultiPatternResult> results = analysisService.multiPatternSearch(patterns);
        return ResponseEntity.ok(results);
    }

    /**
     * Fuzzy search with spellcheck suggestions (Levenshtein & Damerau-Levenshtein).
     * GET /api/algorithms/fuzzy-search?query=pyhton
     */
    @GetMapping("/fuzzy-search")
    public ResponseEntity<FuzzySuggestionResult> fuzzySearch(@RequestParam("query") String query) {
        FuzzySuggestionResult result = analysisService.fuzzySearch(query);
        return ResponseEntity.ok(result);
    }

    /**
     * Calculates Cosine Similarity between two documents.
     * POST /api/algorithms/similarity
     * Request Body: { "docId1": "...", "docId2": "..." }
     */
    @PostMapping("/similarity")
    public ResponseEntity<SimilarityResult> calculateSimilarity(@RequestBody Map<String, String> payload) {
        String doc1Id = payload.get("docId1");
        String doc2Id = payload.get("docId2");

        if (doc1Id == null || doc2Id == null) {
            return ResponseEntity.badRequest().build();
        }

        SimilarityResult result = analysisService.calculateCosineSimilarity(doc1Id, doc2Id);
        return ResponseEntity.ok(result);
    }

    /**
     * Performs advanced substring analysis:
     * Suffix Array, Kasai LCP (Longest Repeated Substring), and Suffix Automaton (Distinct Substrings).
     * GET /api/algorithms/advanced-analysis/{id}
     */
    @GetMapping("/advanced-analysis/{id}")
    public ResponseEntity<AdvancedAnalysisResult> getAdvancedAnalysis(@PathVariable String id) {
        AdvancedAnalysisResult result = analysisService.performAdvancedAnalysis(id);
        return ResponseEntity.ok(result);
    }
}
