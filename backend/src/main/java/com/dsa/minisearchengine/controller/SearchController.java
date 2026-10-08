package com.dsa.minisearchengine.controller;

import com.dsa.minisearchengine.model.SearchResult;
import com.dsa.minisearchengine.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for search operations with algorithm and sorting selection.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "*"})
public class SearchController {

    private final DocumentService documentService;

    public SearchController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Search endpoint supporting custom search algorithms and ranking methods:
     * GET /api/search?query=java&mode=KMP&sort=RANDOMIZED_QUICKSORT
     *
     * Modes:
     * - INVERTED_INDEX (default)
     * - NAIVE
     * - KMP
     * - Z_ALGORITHM
     * - RABIN_KARP
     * - SUFFIX_ARRAY
     *
     * Sorting:
     * - RANDOMIZED_QUICKSORT (default)
     * - STANDARD_SORT
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "mode", defaultValue = "INVERTED_INDEX") String mode,
            @RequestParam(value = "sort", defaultValue = "RANDOMIZED_QUICKSORT") String sort) {

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<SearchResult> results = documentService.search(query.trim(), mode, sort);
        return ResponseEntity.ok(results);
    }

    /**
     * Viva inspection endpoint: returns statistics and live in-memory HashMap state.
     * GET /api/index-stats
     */
    @GetMapping("/index-stats")
    public ResponseEntity<Map<String, Object>> getIndexStats() {
        return ResponseEntity.ok(documentService.getIndexStats());
    }
}
