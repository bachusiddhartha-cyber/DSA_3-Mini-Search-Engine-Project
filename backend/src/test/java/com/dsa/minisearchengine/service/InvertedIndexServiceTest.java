package com.dsa.minisearchengine.service;

import com.dsa.minisearchengine.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying the Inverted Index DSA implementation.
 */
class InvertedIndexServiceTest {

    private TextProcessingService textProcessingService;
    private InvertedIndexService invertedIndexService;

    @BeforeEach
    void setUp() {
        textProcessingService = new TextProcessingService();
        invertedIndexService = new InvertedIndexService(textProcessingService);
    }

    @Test
    @DisplayName("Should correctly index and search documents by keyword")
    void testIndexingAndSearch() {
        String javaContent = "Java is a programming language.\nJava is object oriented.\nJava is easy to learn.";
        String pythonContent = "Python is a programming language.\nPython is easy to learn.\nPython is popular in machine learning.";
        String dsaContent = "Data structures are important.\nJava can be used to implement data structures.\nDSA includes searching and sorting algorithms.";

        invertedIndexService.addDocument("Java.txt", javaContent);
        invertedIndexService.addDocument("Python.txt", pythonContent);
        invertedIndexService.addDocument("DSA.txt", dsaContent);

        Map<String, String> dummyIdMap = new HashMap<>();
        Map<String, String> contentMap = Map.of(
                "Java.txt", javaContent,
                "Python.txt", pythonContent,
                "DSA.txt", dsaContent
        );

        // Search for "java"
        List<SearchResult> javaResults = invertedIndexService.search("java", dummyIdMap, contentMap);
        assertEquals(2, javaResults.size());
        assertEquals("Java.txt", javaResults.get(0).getFileName());
        assertEquals(3, javaResults.get(0).getOccurrences(), "Java.txt should contain 3 occurrences of 'java'");
        assertEquals("DSA.txt", javaResults.get(1).getFileName());
        assertEquals(1, javaResults.get(1).getOccurrences(), "DSA.txt should contain 1 occurrence of 'java'");

        // Search for "programming" -> should be in Java.txt (1) and Python.txt (1)
        List<SearchResult> progResults = invertedIndexService.search("programming", dummyIdMap, contentMap);
        assertEquals(2, progResults.size());

        // Search for "data" -> should be in DSA.txt (2 occurrences)
        List<SearchResult> dataResults = invertedIndexService.search("data", dummyIdMap, contentMap);
        assertEquals(1, dataResults.size());
        assertEquals("DSA.txt", dataResults.get(0).getFileName());
        assertEquals(2, dataResults.get(0).getOccurrences());

        // Search for "xyz" -> empty result
        List<SearchResult> emptyResults = invertedIndexService.search("xyz", dummyIdMap, contentMap);
        assertTrue(emptyResults.isEmpty());
    }

    @Test
    @DisplayName("Should remove document and clean up empty terms in inverted index")
    void testDocumentRemoval() {
        invertedIndexService.addDocument("Doc1.txt", "Apple Banana Orange");
        invertedIndexService.addDocument("Doc2.txt", "Apple Pineapple");

        assertTrue(invertedIndexService.getRawIndex().containsKey("banana"));
        assertTrue(invertedIndexService.getRawIndex().containsKey("apple"));

        // Remove Doc1.txt
        invertedIndexService.removeDocument("Doc1.txt");

        // "banana" was only in Doc1.txt, so it should be completely removed from index
        assertFalse(invertedIndexService.getRawIndex().containsKey("banana"),
                "Orphaned term 'banana' should be pruned from HashMap");

        // "apple" is still present in Doc2.txt
        assertTrue(invertedIndexService.getRawIndex().containsKey("apple"));
        assertEquals(1, invertedIndexService.getRawIndex().get("apple").size());
        assertTrue(invertedIndexService.getRawIndex().get("apple").containsKey("Doc2.txt"));
    }

    @Test
    @DisplayName("Should rank results by occurrences in descending order")
    void testRankingDescending() {
        invertedIndexService.addDocument("DocA.txt", "algorithm");
        invertedIndexService.addDocument("DocB.txt", "algorithm algorithm algorithm");
        invertedIndexService.addDocument("DocC.txt", "algorithm algorithm");

        List<SearchResult> results = invertedIndexService.search("algorithm", null, null);
        assertEquals(3, results.size());
        assertEquals("DocB.txt", results.get(0).getFileName(), "DocB has 3 occurrences (rank 1)");
        assertEquals(3, results.get(0).getOccurrences());
        assertEquals("DocC.txt", results.get(1).getFileName(), "DocC has 2 occurrences (rank 2)");
        assertEquals(2, results.get(1).getOccurrences());
        assertEquals("DocA.txt", results.get(2).getFileName(), "DocA has 1 occurrence (rank 3)");
        assertEquals(1, results.get(2).getOccurrences());
    }
}
