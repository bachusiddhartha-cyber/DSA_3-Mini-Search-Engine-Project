package com.dsa.minisearchengine.algorithm;

import java.util.*;

/**
 * ============================================================================
 * DSA ALGORITHM: AHO-CORASICK MULTI-PATTERN SEARCH AUTOMATON
 * ============================================================================
 * 
 * Syllabus Module 2: Multiple Pattern String Matching
 * 
 * Algorithm Concept:
 * An automaton that constructs a finite-state machine from a Trie of target patterns
 * augmented with failure transitions (similar to KMP failure function) and dictionary
 * output links. It enables searching for an arbitrary number of keywords in text
 * simultaneously in a SINGLE PASS.
 * 
 * Key Steps:
 * 1. Build Trie from all input patterns.
 * 2. Compute failure links via Breadth-First Search (BFS) using a Queue.
 * 3. Traverse text: transition state by state, collecting output matches in O(1).
 * 
 * Complexity:
 * - Construction Time: O(M) where M is the sum of lengths of all patterns
 * - Search Time:       O(N + Z) where N is text length and Z is total occurrences
 * - Space Complexity:  O(M * Alphabet_Size)
 * ============================================================================
 */
public class AhoCorasick {

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node failureLink = null;
        List<String> output = new ArrayList<>();
    }

    private final Node root = new Node();

    public AhoCorasick(Collection<String> patterns) {
        buildTrie(patterns);
        buildFailureLinks();
    }

    /**
     * Inserts all patterns into the Trie structure.
     */
    private void buildTrie(Collection<String> patterns) {
        for (String pattern : patterns) {
            if (pattern == null || pattern.trim().isEmpty()) continue;
            String normalized = pattern.trim().toLowerCase();
            Node current = root;
            for (char ch : normalized.toCharArray()) {
                current = current.children.computeIfAbsent(ch, c -> new Node());
            }
            current.output.add(normalized);
        }
    }

    /**
     * Constructs failure links and output links across the Trie using BFS.
     */
    private void buildFailureLinks() {
        Queue<Node> queue = new ArrayDeque<>();

        // Level 1 nodes fail to root
        for (Node child : root.children.values()) {
            child.failureLink = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char ch = entry.getKey();
                Node child = entry.getValue();

                // Find failure transition for this child
                Node fallback = current.failureLink;
                while (fallback != null && !fallback.children.containsKey(ch)) {
                    fallback = fallback.failureLink;
                }

                child.failureLink = (fallback != null) ? fallback.children.get(ch) : root;

                // Merge outputs from the failure node (Dictionary links)
                child.output.addAll(child.failureLink.output);

                queue.add(child);
            }
        }
    }

    /**
     * Scans the document text in a single pass and returns occurrences for each pattern.
     * 
     * @param text Document content
     * @return Map of pattern -> list of 0-based match start indices
     */
    public Map<String, List<Integer>> searchInText(String text) {
        Map<String, List<Integer>> result = new LinkedHashMap<>();
        if (text == null || text.isEmpty()) {
            return result;
        }

        String lowerText = text.toLowerCase();
        Node current = root;

        for (int i = 0; i < lowerText.length(); i++) {
            char ch = lowerText.charAt(i);

            // Follow failure links until matching transition or root is reached
            while (current != null && !current.children.containsKey(ch)) {
                current = current.failureLink;
            }

            if (current == null) {
                current = root;
                continue;
            }

            current = current.children.get(ch);

            // Record all pattern matches ending at current index i
            if (!current.output.isEmpty()) {
                for (String matchedPattern : current.output) {
                    int startIndex = i - matchedPattern.length() + 1;
                    result.computeIfAbsent(matchedPattern, k -> new ArrayList<>()).add(startIndex);
                }
            }
        }

        return result;
    }
}
