import React from 'react';
import { X, BookOpen, Award, Check } from 'lucide-react';

const COMPLEXITY_DATA = [
  {
    name: 'Inverted Index',
    module: 'Core DSA',
    feature: 'Primary fast keyword retrieval',
    time: 'O(1) average lookup, O(W) insert',
    space: 'O(Total Unique Words)',
    principle: 'HashMap<String, HashMap<String, Integer>>'
  },
  {
    name: 'Naive String Search',
    module: 'Module 2',
    feature: 'Baseline pattern matching',
    time: 'O(n * m) worst case',
    space: 'O(1)',
    principle: 'Sliding window character scan'
  },
  {
    name: 'KMP Algorithm',
    module: 'Module 2',
    feature: 'Exact pattern matching',
    time: 'O(n + m) linear deterministic',
    space: 'O(m) LPS array',
    principle: 'Longest Prefix Suffix (LPS) failure function'
  },
  {
    name: 'Z Algorithm',
    module: 'Module 2',
    feature: 'Pattern search alternative',
    time: 'O(n + m) linear deterministic',
    space: 'O(n + m) Z-array',
    principle: 'Z-array prefix matching on pattern + "$" + text'
  },
  {
    name: 'Rabin-Karp',
    module: 'Module 2',
    feature: 'Hash-based pattern search',
    time: 'O(n + m) avg, O(nm) worst',
    space: 'O(1)',
    principle: 'Polynomial rolling hash modulo 10^9+7 with collision checks'
  },
  {
    name: 'Aho-Corasick',
    module: 'Module 2',
    feature: 'Multi-keyword simultaneous scan',
    time: 'O(n + matches) single pass',
    space: 'O(M * Alphabet)',
    principle: 'Trie with BFS failure & dictionary links'
  },
  {
    name: 'Levenshtein Distance',
    module: 'Module 3 (DP)',
    feature: 'Fuzzy search & spellcheck',
    time: 'O(n * m)',
    space: 'O(n * m)',
    principle: 'Wagner-Fischer 2D DP matrix (Insert, Delete, Substitute)'
  },
  {
    name: 'Damerau-Levenshtein',
    module: 'Module 3 (DP)',
    feature: 'Transposition-aware typo correction',
    time: 'O(n * m)',
    space: 'O(n * m)',
    principle: 'DP with adjacent character transposition clause'
  },
  {
    name: 'Cosine Similarity',
    module: 'Module 1 (IR)',
    feature: 'Document similarity calculation',
    time: 'O(W1 + W2 + V)',
    space: 'O(Vocabulary)',
    principle: 'Vector Space Model dot product & Euclidean norms'
  },
  {
    name: 'Randomized QuickSort',
    module: 'Module 6 (Randomized)',
    feature: 'Relevance search result ranking',
    time: 'Expected O(K log K)',
    space: 'O(log K) recursion stack',
    principle: 'Las Vegas randomized pivot selection'
  },
  {
    name: 'Randomized Hashing',
    module: 'Module 6 (Randomized)',
    feature: 'Document fingerprinting & checksums',
    time: 'O(L) where L = text length',
    space: 'O(1)',
    principle: 'Universal hashing with random multiplier & offset'
  },
  {
    name: 'Suffix Array',
    module: 'Module 2',
    feature: 'Substring binary search',
    time: 'O(m log n) search, O(n^2 log n) build',
    space: 'O(n)',
    principle: 'Sorted suffix indices with lower/upper binary search'
  },
  {
    name: 'Kasai LCP',
    module: 'Module 2',
    feature: 'Longest Repeated Substring (LRS)',
    time: 'O(n) linear construction',
    space: 'O(n)',
    principle: 'LCP array height reduction invariant h >= h - 1'
  },
  {
    name: 'Suffix Automaton (DAWG)',
    module: 'Module 2',
    feature: 'Distinct substrings counting & query',
    time: 'O(n) build, O(m) substring check',
    space: 'O(n * Alphabet)',
    principle: 'Minimal DFA recognizing all suffixes with len/link states'
  }
];

export default function DsaComplexityModal({ onClose }) {
  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-container dsa-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div className="modal-title">
            <BookOpen size={22} className="modal-file-icon" />
            <div>
              <h3>Algorithms & Complexity Master Table</h3>
              <p className="modal-subtitle">
                Official syllabus mapping for B.Tech DSA-3 viva evaluation
              </p>
            </div>
          </div>
          <button className="btn-icon modal-close" onClick={onClose} title="Close">
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          <div className="table-responsive">
            <table className="doc-table compact-table">
              <thead>
                <tr>
                  <th>Algorithm</th>
                  <th>Module</th>
                  <th>Project Feature</th>
                  <th>Time Complexity</th>
                  <th>Space</th>
                  <th>Core Mechanism</th>
                </tr>
              </thead>
              <tbody>
                {COMPLEXITY_DATA.map((row) => (
                  <tr key={row.name}>
                    <td><strong>{row.name}</strong></td>
                    <td><span className="chip" style={{ fontSize: '0.7rem' }}>{row.module}</span></td>
                    <td>{row.feature}</td>
                    <td><code>{row.time}</code></td>
                    <td><code>{row.space}</code></td>
                    <td style={{ fontSize: '0.78rem', color: '#475569' }}>{row.principle}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>
            Close Reference
          </button>
        </div>
      </div>
    </div>
  );
}
