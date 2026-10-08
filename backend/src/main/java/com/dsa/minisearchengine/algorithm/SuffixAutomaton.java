package com.dsa.minisearchengine.algorithm;

import java.util.*;

/**
 * ============================================================================
 * DSA ALGORITHM: SUFFIX AUTOMATON (DIRECTED ACYCLIC WORD GRAPH - DAWG)
 * ============================================================================
 * 
 * Syllabus Module 2: Suffix Automata
 * 
 * Algorithm Concept:
 * A Suffix Automaton (SAM) is a minimal Deterministic Finite Automaton (DFA) that
 * recognizes all suffixes of a string S.
 * 
 * Incredible Theoretical Properties:
 * 1. Contains at most (2N - 1) states and (3N - 4) transitions for a string of length N!
 * 2. Constructed online character-by-character in strictly O(N) LINEAR TIME!
 * 3. Can determine if ANY pattern P is a substring of S in O(|P|) time.
 * 4. Counts the EXACT NUMBER OF DISTINCT SUBSTRINGS of S in O(N) time without
 *    explicitly generating them, via the formula:
 *       Distinct Substrings = Sum_{v != root} (len[v] - len[link[v]])
 * 
 * Complexity:
 * - Construction: O(N)
 * - Substring Query: O(M)
 * - Space Complexity: O(N * Alphabet_Size)
 * ============================================================================
 */
public class SuffixAutomaton {

    public static class State {
        public int len;
        public int link;
        public Map<Character, Integer> next = new HashMap<>();

        public State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    private final List<State> states = new ArrayList<>();
    private int last;

    public SuffixAutomaton(String s) {
        // Initialize root state (state 0)
        states.add(new State(0, -1));
        last = 0;

        // Build online character by character in linear time
        for (int i = 0; i < s.length(); i++) {
            extend(s.charAt(i));
        }
    }

    /**
     * Online linear-time extension adding character 'c'.
     */
    private void extend(char c) {
        int cur = states.size();
        states.add(new State(states.get(last).len + 1, 0));

        int p = last;
        while (p != -1 && !states.get(p).next.containsKey(c)) {
            states.get(p).next.put(c, cur);
            p = states.get(p).link;
        }

        if (p == -1) {
            states.get(cur).link = 0;
        } else {
            int q = states.get(p).next.get(c);
            if (states.get(p).len + 1 == states.get(q).len) {
                states.get(cur).link = q;
            } else {
                int clone = states.size();
                State qState = states.get(q);
                State cloneState = new State(states.get(p).len + 1, qState.link);
                cloneState.next.putAll(qState.next);
                states.add(cloneState);

                while (p != -1 && states.get(p).next.get(c) == q) {
                    states.get(p).next.put(c, clone);
                    p = states.get(p).link;
                }

                states.get(q).link = clone;
                states.get(cur).link = clone;
            }
        }
        last = cur;
    }

    /**
     * Tests if a given pattern exists as a substring in O(|pattern|) time.
     */
    public boolean containsSubstring(String pattern) {
        if (pattern == null || pattern.isEmpty()) return true;
        int cur = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (!states.get(cur).next.containsKey(c)) {
                return false;
            }
            cur = states.get(cur).next.get(c);
        }
        return true;
    }

    /**
     * Counts the total number of distinct substrings in the document in O(V) time
     * using state length differentials.
     */
    public long countDistinctSubstrings() {
        long total = 0;
        for (int i = 1; i < states.size(); i++) {
            total += states.get(i).len - states.get(states.get(i).link).len;
        }
        return total;
    }

    public int getStateCount() {
        return states.size();
    }
}
