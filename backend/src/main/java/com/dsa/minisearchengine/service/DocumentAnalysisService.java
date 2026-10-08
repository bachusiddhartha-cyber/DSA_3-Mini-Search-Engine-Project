package com.dsa.minisearchengine.service;

import com.dsa.minisearchengine.algorithm.*;
import com.dsa.minisearchengine.model.*;
import com.dsa.minisearchengine.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service providing high-level analytical capabilities:
 * 1. Document Cosine Similarity
 * 2. Aho-Corasick Multi-Pattern Search
 * 3. Levenshtein & Damerau-Levenshtein Fuzzy Search / Spellcheck
 * 4. Suffix Array, Kasai LCP, and Suffix Automaton Analysis
 */
@Service
public class DocumentAnalysisService {

    private final DocumentRepository documentRepository;
    private final TextProcessingService textProcessingService;
    private final InvertedIndexService invertedIndexService;

    public DocumentAnalysisService(DocumentRepository documentRepository,
                                   TextProcessingService textProcessingService,
                                   InvertedIndexService invertedIndexService) {
        this.documentRepository = documentRepository;
        this.textProcessingService = textProcessingService;
        this.invertedIndexService = invertedIndexService;
    }

    /**
     * Computes Cosine Similarity between two documents stored in MongoDB.
     */
    public SimilarityResult calculateCosineSimilarity(String docId1, String docId2) {
        DocumentModel doc1 = documentRepository.findById(docId1)
                .orElseThrow(() -> new IllegalArgumentException("Document 1 not found: " + docId1));
        DocumentModel doc2 = documentRepository.findById(docId2)
                .orElseThrow(() -> new IllegalArgumentException("Document 2 not found: " + docId2));

        // Build word frequency maps
        Map<String, Integer> freq1 = buildFrequencyMap(doc1.getContent());
        Map<String, Integer> freq2 = buildFrequencyMap(doc2.getContent());

        CosineSimilarity.SimilarityMetric metric = CosineSimilarity.compute(freq1, freq2);

        return new SimilarityResult(
                doc1.getId(),
                doc1.getFileName(),
                doc2.getId(),
                doc2.getFileName(),
                metric.score,
                metric.percentage,
                metric.sharedWords,
                metric.doc1UniqueWords,
                metric.doc2UniqueWords
        );
    }

    /**
     * Executes Aho-Corasick simultaneous multi-pattern search across all documents.
     */
    public List<MultiPatternResult> multiPatternSearch(List<String> patterns) {
        List<MultiPatternResult> results = new ArrayList<>();
        if (patterns == null || patterns.isEmpty()) {
            return results;
        }

        List<DocumentModel> allDocs = documentRepository.findAll();
        if (allDocs.isEmpty()) {
            return results;
        }

        // Initialize Aho-Corasick automaton once with all patterns
        AhoCorasick automaton = new AhoCorasick(patterns);

        for (DocumentModel doc : allDocs) {
            long startTime = System.nanoTime();
            Map<String, List<Integer>> matchMap = automaton.searchInText(doc.getContent());
            long executionTime = System.nanoTime() - startTime;

            Map<String, Integer> counts = new LinkedHashMap<>();
            int total = 0;

            for (String p : patterns) {
                String cleanP = p.trim().toLowerCase();
                int count = matchMap.containsKey(cleanP) ? matchMap.get(cleanP).size() : 0;
                counts.put(cleanP, count);
                total += count;
            }

            // Only add documents with at least 1 match
            if (total > 0) {
                results.add(new MultiPatternResult(doc.getId(), doc.getFileName(), counts, total, executionTime));
            }
        }

        // Sort documents by total matches descending
        results.sort((a, b) -> Integer.compare(b.getTotalMatches(), a.getTotalMatches()));
        return results;
    }

    /**
     * Performs Fuzzy Search using Levenshtein & Damerau-Levenshtein distances.
     * Searches all indexed unique vocabulary words to suggest the closest matches ("Did you mean?").
     */
    public FuzzySuggestionResult fuzzySearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new FuzzySuggestionResult(query, "", 0, 0, 0.0, Collections.emptyList());
        }

        String lowerQuery = query.trim().toLowerCase();
        Set<String> vocabulary = invertedIndexService.getRawIndex().keySet();

        String bestCandidate = null;
        int bestLevDistance = Integer.MAX_VALUE;
        int bestDamDistance = Integer.MAX_VALUE;
        double bestScore = 0.0;

        for (String word : vocabulary) {
            int lev = LevenshteinDistance.compute(lowerQuery, word);
            int dam = DamerauLevenshtein.compute(lowerQuery, word);
            double score = DamerauLevenshtein.similarityScore(lowerQuery, word);

            // Favor lowest Damerau distance (considers transpositions)
            if (dam < bestDamDistance || (dam == bestDamDistance && lev < bestLevDistance)) {
                bestDamDistance = dam;
                bestLevDistance = lev;
                bestCandidate = word;
                bestScore = score;
            }
        }

        // If candidate found within reasonable distance threshold (<= 3 or >= 0.5 similarity)
        List<SearchResult> searchResults = new ArrayList<>();
        if (bestCandidate != null && bestDamDistance <= 3) {
            List<DocumentModel> allDocs = documentRepository.findAll();
            Map<String, String> docIdMap = new HashMap<>();
            Map<String, String> docContentMap = new HashMap<>();
            for (DocumentModel d : allDocs) {
                docIdMap.put(d.getFileName(), d.getId());
                docContentMap.put(d.getFileName(), d.getContent());
            }
            searchResults = invertedIndexService.search(bestCandidate, docIdMap, docContentMap);
        }

        return new FuzzySuggestionResult(
                query,
                bestCandidate != null ? bestCandidate : "",
                bestLevDistance != Integer.MAX_VALUE ? bestLevDistance : 0,
                bestDamDistance != Integer.MAX_VALUE ? bestDamDistance : 0,
                Math.round(bestScore * 1000.0) / 10.0,
                searchResults
        );
    }

    /**
     * Performs Advanced String Analysis on a single document:
     * - Suffix Array sample
     * - Kasai LCP: Longest Repeated Substring
     * - Suffix Automaton: Distinct Substrings Count
     */
    public AdvancedAnalysisResult performAdvancedAnalysis(String documentId) {
        DocumentModel doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with ID: " + documentId));

        String text = doc.getContent();
        if (text == null || text.isEmpty()) {
            return new AdvancedAnalysisResult(doc.getId(), doc.getFileName(), 0, "", 0, 0,
                    Collections.emptyList(), Collections.emptyList(), 0);
        }

        long start = System.nanoTime();

        // 1. Suffix Array
        SuffixArray sa = new SuffixArray(text);
        int[] saArray = sa.getSuffixArray();

        // 2. Kasai LCP: Longest Repeated Substring
        KasaiLCP.LCPResult lcpResult = KasaiLCP.buildLCPArray(text, saArray);

        // 3. Suffix Automaton: Distinct Substrings
        SuffixAutomaton sam = new SuffixAutomaton(text);
        long distinctSubstrings = sam.countDistinctSubstrings();

        long duration = System.nanoTime() - start;

        // Sample up to 10 entries for presentation display
        int sampleSize = Math.min(10, saArray.length);
        List<String> saSample = new ArrayList<>();
        List<Integer> lcpSample = new ArrayList<>();

        for (int i = 0; i < sampleSize; i++) {
            int suffixStart = saArray[i];
            String preview = text.substring(suffixStart, Math.min(text.length(), suffixStart + 25))
                    .replaceAll("\\r?\\n", " ");
            saSample.add("SA[" + i + "]=" + suffixStart + " -> \"" + preview + (text.length() > suffixStart + 25 ? "..." : "") + "\"");
            lcpSample.add(lcpResult.lcp[i]);
        }

        return new AdvancedAnalysisResult(
                doc.getId(),
                doc.getFileName(),
                text.length(),
                lcpResult.longestRepeatedSubstring,
                lcpResult.lrsLength,
                distinctSubstrings,
                saSample,
                lcpSample,
                duration
        );
    }

    private Map<String, Integer> buildFrequencyMap(String content) {
        Map<String, Integer> map = new HashMap<>();
        List<String> words = textProcessingService.extractWords(content);
        for (String w : words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }
        return map;
    }
}
