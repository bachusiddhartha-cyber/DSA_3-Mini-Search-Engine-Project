package com.dsa.minisearchengine.service;

import com.dsa.minisearchengine.algorithm.*;
import com.dsa.minisearchengine.model.AlgorithmMatchResult;
import com.dsa.minisearchengine.model.SearchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite verifying all 13 syllabus algorithms.
 */
class AlgorithmSuiteTest {

    private final String sampleText = "java is a programming language. java is object oriented. java is easy.";
    private final String samplePattern = "java";

    @Test
    @DisplayName("Module 2: Naive, KMP, Z, and Rabin-Karp should all find identical match positions")
    void testPatternMatchingEquivalence() {
        AlgorithmMatchResult naive = NaiveStringSearch.search(sampleText, samplePattern);
        AlgorithmMatchResult kmp = KMPStringSearch.search(sampleText, samplePattern);
        AlgorithmMatchResult z = ZAlgorithm.search(sampleText, samplePattern);
        AlgorithmMatchResult rk = RabinKarp.search(sampleText, samplePattern);

        assertEquals(3, naive.getMatchCount(), "Naive should find 3 matches of 'java'");
        assertEquals(3, kmp.getMatchCount(), "KMP should find 3 matches of 'java'");
        assertEquals(3, z.getMatchCount(), "Z Algorithm should find 3 matches of 'java'");
        assertEquals(3, rk.getMatchCount(), "Rabin-Karp should find 3 matches of 'java'");

        // Verify match indices match exactly: [0, 32, 57]
        assertEquals(naive.getMatchPositions(), kmp.getMatchPositions());
        assertEquals(kmp.getMatchPositions(), z.getMatchPositions());
        assertEquals(z.getMatchPositions(), rk.getMatchPositions());
    }

    @Test
    @DisplayName("Module 2: Aho-Corasick should find multiple patterns in a single pass")
    void testAhoCorasickMultiPattern() {
        List<String> patterns = Arrays.asList("java", "programming", "easy", "python");
        AhoCorasick ac = new AhoCorasick(patterns);

        Map<String, List<Integer>> matches = ac.searchInText(sampleText);

        assertTrue(matches.containsKey("java"));
        assertEquals(3, matches.get("java").size());

        assertTrue(matches.containsKey("programming"));
        assertEquals(1, matches.get("programming").size());

        assertTrue(matches.containsKey("easy"));
        assertEquals(1, matches.get("easy").size());

        assertFalse(matches.containsKey("python"), "Text does not contain python");
    }

    @Test
    @DisplayName("Module 3: Levenshtein vs Damerau-Levenshtein transposition handling")
    void testEditDistanceAndTransposition() {
        // Test typo 'pyhton' vs 'python'
        // Levenshtein treats transposition as 2 operations (deletion + insertion or 2 substitutions)
        int lev = LevenshteinDistance.compute("pyhton", "python");
        assertEquals(2, lev, "Standard Levenshtein requires 2 operations for transposition");

        // Damerau-Levenshtein treats adjacent transposition as 1 operation!
        int dam = DamerauLevenshtein.compute("pyhton", "python");
        assertEquals(1, dam, "Damerau-Levenshtein requires only 1 operation for adjacent swap");

        // Test typo 'jav' vs 'java'
        assertEquals(1, LevenshteinDistance.compute("jav", "java"));
        assertEquals(1, DamerauLevenshtein.compute("jav", "java"));

        // Identical strings have distance 0
        assertEquals(0, DamerauLevenshtein.compute("algorithm", "algorithm"));
    }

    @Test
    @DisplayName("Module 1: Cosine Similarity between word frequency vectors")
    void testCosineSimilarity() {
        Map<String, Integer> doc1 = Map.of("data", 3, "structures", 2, "algorithms", 1);
        Map<String, Integer> doc2 = Map.of("data", 2, "structures", 1, "python", 2);

        CosineSimilarity.SimilarityMetric metric = CosineSimilarity.compute(doc1, doc2);

        assertTrue(metric.score > 0.0, "Documents share words so similarity must be > 0");
        assertTrue(metric.percentage > 0.0);
        assertTrue(metric.sharedWords.contains("data"));
        assertTrue(metric.sharedWords.contains("structures"));
        assertFalse(metric.sharedWords.contains("algorithms"));
    }

    @Test
    @DisplayName("Module 6: Randomized QuickSort ranks results descending")
    void testRandomizedQuickSort() {
        List<SearchResult> list = new ArrayList<>();
        list.add(new SearchResult("Doc1.txt", "1", 4, ""));
        list.add(new SearchResult("Doc2.txt", "2", 15, ""));
        list.add(new SearchResult("Doc3.txt", "3", 8, ""));
        list.add(new SearchResult("Doc4.txt", "4", 1, ""));

        RandomizedQuickSort.sortDescending(list);

        assertEquals(15, list.get(0).getOccurrences());
        assertEquals("Doc2.txt", list.get(0).getFileName());
        assertEquals(8, list.get(1).getOccurrences());
        assertEquals("Doc3.txt", list.get(1).getFileName());
        assertEquals(4, list.get(2).getOccurrences());
        assertEquals(1, list.get(3).getOccurrences());
    }

    @Test
    @DisplayName("Module 6: Randomized Hashing generates consistent fingerprint")
    void testRandomizedHash() {
        long fp1 = RandomizedHash.computeFingerprint("hello world");
        long fp2 = RandomizedHash.computeFingerprint("hello world");
        long fp3 = RandomizedHash.computeFingerprint("different text");

        assertEquals(fp1, fp2, "Same input must produce identical fingerprint");
        assertNotEquals(fp1, fp3, "Different input should produce different fingerprint");
    }

    @Test
    @DisplayName("Module 2: Suffix Array binary search pattern search")
    void testSuffixArraySearch() {
        SuffixArray sa = new SuffixArray("banana");
        AlgorithmMatchResult result = sa.search("an");

        assertEquals(2, result.getMatchCount(), "'banana' contains 2 occurrences of 'an'");
        assertEquals(Arrays.asList(1, 3), result.getMatchPositions());
    }

    @Test
    @DisplayName("Module 2: Kasai LCP finds Longest Repeated Substring")
    void testKasaiLCP() {
        String text = "banana";
        SuffixArray sa = new SuffixArray(text);
        KasaiLCP.LCPResult lcpResult = KasaiLCP.buildLCPArray(text, sa.getSuffixArray());

        assertEquals("ana", lcpResult.longestRepeatedSubstring,
                "In 'banana', the longest repeated substring is 'ana' of length 3");
        assertEquals(3, lcpResult.lrsLength);
    }

    @Test
    @DisplayName("Module 2: Suffix Automaton counts distinct substrings")
    void testSuffixAutomaton() {
        // "aba" has distinct substrings: "a", "b", "ab", "ba", "aba" (total 5)
        SuffixAutomaton sam = new SuffixAutomaton("aba");
        assertEquals(5, sam.countDistinctSubstrings());

        assertTrue(sam.containsSubstring("ab"));
        assertTrue(sam.containsSubstring("ba"));
        assertFalse(sam.containsSubstring("c"));
    }
}
