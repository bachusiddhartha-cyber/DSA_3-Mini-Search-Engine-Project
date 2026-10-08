package com.dsa.minisearchengine.service;

import com.dsa.minisearchengine.model.SearchResult;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ============================================================================
 * CORE DSA IMPLEMENTATION: INVERTED INDEX USING JAVA HASHMAP
 * ============================================================================
 * 
 * Project: Mini Search Engine Using Data Structures and Information Retrieval
 * 
 * Concept:
 * An Inverted Index is an information retrieval data structure mapping words (terms)
 * to their locations of occurrence in a document collection.
 * 
 * Data Structure:
 *   HashMap<String, HashMap<String, Integer>>
 * 
 * Mapping Hierarchy:
 *   WORD (term)
 *     ↓
 *   DOCUMENT (fileName)
 *     ↓
 *   OCCURRENCE COUNT (term frequency)
 * 
 * Concrete Example:
 *   "java"        → { "Java.txt": 3, "DSA.txt": 1 }
 *   "python"      → { "Python.txt": 3 }
 *   "programming" → { "Java.txt": 1, "Python.txt": 1 }
 *   "data"        → { "DSA.txt": 2 }
 * 
 * Complexity Analysis:
 * - Search Lookup:     O(1) average time complexity using HashMap hashing
 * - Document Indexing: O(W) where W is the total word count of the document
 * - Document Deletion: O(U) where U is the total unique words in the index
 * - Ranking / Sorting: O(K log K) where K is the number of matching documents
 * ============================================================================
 */
@Service
public class InvertedIndexService {

    private final TextProcessingService textProcessingService;

    // Inverted index: word -> (fileName -> frequency)
    private final Map<String, Map<String, Integer>> index = new HashMap<>();

    public InvertedIndexService(TextProcessingService textProcessingService) {
        this.textProcessingService = textProcessingService;
    }

    /**
     * Adds and indexes a document's content into the Inverted Index.
     * 
     * DSA Flow:
     * 1. If the document was previously indexed, remove its old entries first (handles updates/re-uploads).
     * 2. Tokenize raw text into normalized words using String Processing.
     * 3. For each word, insert or increment its occurrence count in the inner HashMap.
     * 
     * Time Complexity: O(W) where W is total words in document.
     * Space Complexity: O(U) where U is unique terms in document.
     */
    public synchronized void addDocument(String fileName, String content) {
        if (fileName == null || content == null) {
            return;
        }

        // Handle edits or re-uploads: purge existing entries for this file first
        removeDocument(fileName);

        // Tokenize and normalize words from content
        List<String> words = textProcessingService.extractWords(content);

        // Insert into Inverted Index: word -> document -> frequency
        for (String word : words) {
            // Get or create posting map for this word
            Map<String, Integer> documentPostings = index.computeIfAbsent(word, k -> new HashMap<>());

            // Increment frequency count for this document
            documentPostings.put(fileName, documentPostings.getOrDefault(fileName, 0) + 1);
        }
    }

    /**
     * Removes all references of a document from the Inverted Index.
     * 
     * DSA Flow:
     * 1. Iterate through all word entries in the outer HashMap.
     * 2. Remove the document entry from the inner map.
     * 3. If an inner map becomes empty (no other document contains this word),
     *    remove the word key completely from the outer HashMap.
     * 
     * Time Complexity: O(U) where U is total unique words indexed.
     */
    public synchronized void removeDocument(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }

        // Using iterator to safely remove keys while iterating
        Iterator<Map.Entry<String, Map<String, Integer>>> iterator = index.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, Integer>> entry = iterator.next();
            Map<String, Integer> docMap = entry.getValue();

            // Remove this document from posting map
            docMap.remove(fileName);

            // Clean up memory: if no document contains this word anymore, delete the word entry
            if (docMap.isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * Searches the Inverted Index for a given query term or multi-word query.
     * 
     * DSA Flow:
     * 1. Tokenize and normalize query words.
     * 2. For each query term, perform an O(1) average lookup in the outer HashMap.
     * 3. Aggregate occurrence counts for matching documents.
     * 4. Convert aggregated results into an ArrayList of SearchResult objects.
     * 5. Rank / Sort results in descending order of occurrences using Comparator.
     * 
     * Time Complexity: O(Q + K log K)
     *   - Q is number of query words (typically 1 to 5) -> O(1) lookups
     *   - K is number of matching documents
     *   - Sorting K documents takes O(K log K)
     */
    public synchronized List<SearchResult> search(String query, Map<String, String> docIdMap, Map<String, String> docContentMap) {
        List<SearchResult> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        List<String> queryWords = textProcessingService.extractQueryWords(query);
        if (queryWords.isEmpty()) {
            return results;
        }

        // Map to aggregate total occurrences per document for the query terms
        Map<String, Integer> aggregatedOccurrences = new HashMap<>();

        for (String term : queryWords) {
            // O(1) lookup in HashMap inverted index
            Map<String, Integer> docPostings = index.get(term);
            if (docPostings != null) {
                for (Map.Entry<String, Integer> entry : docPostings.entrySet()) {
                    String docName = entry.getKey();
                    int count = entry.getValue();
                    aggregatedOccurrences.put(docName, aggregatedOccurrences.getOrDefault(docName, 0) + count);
                }
            }
        }

        // Convert the aggregated document matches into an ArrayList
        for (Map.Entry<String, Integer> entry : aggregatedOccurrences.entrySet()) {
            String fileName = entry.getKey();
            int count = entry.getValue();
            String docId = docIdMap != null ? docIdMap.get(fileName) : null;
            String rawContent = docContentMap != null ? docContentMap.get(fileName) : "";
            String snippet = textProcessingService.generateSnippet(rawContent, queryWords.get(0));

            results.add(new SearchResult(fileName, docId, count, snippet));
        }

        // DSA Concept: Sorting using Comparator
        // Sort results in descending order based on occurrences count (higher relevance first)
        results.sort(new Comparator<SearchResult>() {
            @Override
            public int compare(SearchResult r1, SearchResult r2) {
                // Descending order comparison
                return Integer.compare(r2.getOccurrences(), r1.getOccurrences());
            }
        });

        return results;
    }

    /**
     * Clears the entire in-memory Inverted Index.
     */
    public synchronized void clear() {
        index.clear();
    }

    /**
     * Returns a snapshot of the live Inverted Index for educational inspection and viva demos.
     */
    public synchronized Map<String, Map<String, Integer>> getRawIndex() {
        Map<String, Map<String, Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : index.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Returns the total count of unique indexed words.
     */
    public synchronized int getTotalUniqueWords() {
        return index.size();
    }

    /**
     * Returns the total count of unique indexed documents currently tracked in the index.
     */
    public synchronized int getTotalIndexedDocuments() {
        Set<String> uniqueDocs = new HashSet<>();
        for (Map<String, Integer> docMap : index.values()) {
            uniqueDocs.addAll(docMap.keySet());
        }
        return uniqueDocs.size();
    }
}
