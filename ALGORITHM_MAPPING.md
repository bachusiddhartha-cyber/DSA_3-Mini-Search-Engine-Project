# Algorithm Mapping & Syllabus Coverage: Mini Search Engine

**Course**: Advanced Data Structures and Algorithms (Course Code: 25CS2103E / B.Tech CSE)  
**Project Title**: Mini Search Engine Using Data Structures and Information Retrieval  
**Implementation Language**: Java 25 (Spring Boot 4.0.8) + React (Vite) + MongoDB  
**Author**: B.Tech CSE Student

---

## 1. Executive Summary

This document provides a formal academic mapping between the university syllabus topics in **Advanced Algorithms / Data Structures (DSA-3)** and their exact implementations in the **Mini Search Engine** codebase. 

All algorithms are implemented **from scratch** using pure Java standard library data structures (`HashMap`, `ArrayList`, primitive arrays) without relying on external search frameworks (such as Apache Lucene or Elasticsearch).

---

## 2. Master Algorithm Mapping Table

| # | Syllabus Algorithm / Topic | Code Implementation File | IR / Search Engine Feature | Best Time | Avg Time | Worst Time | Space | Academic Selection Rationale |
|---|----------------------------|--------------------------|----------------------------|-----------|----------|------------|-------|------------------------------|
| 1 | **Inverted Index (Hash-based Indexing)** | `InvertedIndexService.java` | Core Search Indexing (Term-to-Document Posting List) | $O(1)$ | $O(1)$ | $O(N)$ lookup | $O(V \cdot D)$ | The cornerstone of Information Retrieval; maps distinct vocabulary tokens to document IDs and frequencies. |
| 2 | **Naive / Brute-Force String Matching** | `NaiveStringSearch.java` | Baseline Exact Search & Algorithm Benchmark | $O(N)$ | $O(N \cdot M)$ | $O((N-M+1)M)$ | $O(1)$ | Standard educational baseline for empirical performance benchmarking and verification. |
| 3 | **Knuth-Morris-Pratt (KMP)** | `KMPStringSearch.java` | Single-term Exact Search & Document Inspection | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | $O(M)$ | Employs a deterministic Longest Proper Prefix-Suffix (LPS) failure function to prevent redundant character backtracking. |
| 4 | **Z-Algorithm (Linear Pattern Matching)** | `ZAlgorithm.java` | Pattern Benchmark & Prefix Analysis | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | Computes Z-array on combined string `pattern + "$" + text` using sliding matching boxes $[L, R]$. |
| 5 | **Rabin-Karp Algorithm** | `RabinKarp.java` | Substring Fingerprinting Benchmark | $O(N + M)$ | $O(N + M)$ | $O(N \cdot M)$ | $O(1)$ | Uses Horner's rule polynomial rolling hash with modular arithmetic ($\text{prime} = 10^9+7$) and true character verification on hash match. |
| 6 | **Aho-Corasick Automaton** | `AhoCorasick.java` | Multi-Keyword Tagging & Bulk Vocabulary Matching | $O(N + L + Z)$ | $O(N + L + Z)$ | $O(N + L + Z)$ | $O(L \cdot \Sigma)$ | Multi-pattern Trie with BFS-constructed suffix failure links and dictionary output links; processes all patterns simultaneously in a single linear text pass. |
| 7 | **Levenshtein Distance (Dynamic Programming)** | `LevenshteinDistance.java` | Typo Detection & Spell Checking | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | Classical Wagner-Fischer 2D dynamic programming grid for insertion, deletion, and substitution. |
| 8 | **Damerau-Levenshtein Distance (DP)** | `DamerauLevenshtein.java` | Student Typo Correction with Transpositions | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | Extends Levenshtein by modeling adjacent character transpositions (e.g., `pyhton` $\rightarrow$ `python`) in 1 edit step. |
| 9 | **Vector Space Model (Cosine Similarity)** | `CosineSimilarity.java` | Document Relatedness & Plagiarism/Duplicate Detection | $O(V)$ | $O(V)$ | $O(V)$ | $O(V)$ | Represents documents as term-frequency vectors and computes cosine angle $\frac{A \cdot B}{\|A\|_2 \|B\|_2}$. |
| 10 | **Randomized QuickSort (Las Vegas)** | `RandomizedQuickSort.java` | Search Relevance Ranking & Document Ordering | $O(N \log N)$ | $O(N \log N)$ | $O(N^2)$ (prob $\to 0$) | $O(\log N)$ | Ranks candidate documents by relevance scores with uniform random pivot selection to neutralize worst-case inputs. |
| 11 | **Randomized Polynomial Hashing** | `RandomizedHash.java` | Universal Fingerprinting & Fast Substring Equality | $O(N)$ | $O(1)$ check | $O(1)$ check | $O(N)$ | Precomputes prefix hashes with random seed base to evaluate any substring hash in $O(1)$ time with negligible collision probability. |
| 12 | **Suffix Array with Binary Search** | `SuffixArray.java` | Full-Text Substring Indexing | $O(M \log N)$ | $O(M \log N)$ | $O(M \log N)$ | $O(N)$ | Sorted permutation of all text suffixes; enables fast binary search for exact sub-word queries without full re-tokenization. |
| 13 | **Kasai's LCP Algorithm** | `KasaiLCP.java` | Longest Repeated Substring (LRS) Discovery | $O(N)$ | $O(N)$ | $O(N)$ | $O(N)$ | Derives Longest Common Prefix (LCP) array from Suffix Array in strictly linear time $O(N)$ using the property $LCP[rank[i]] \ge LCP[rank[i-1]] - 1$. |
| 14 | **Suffix Automaton (DAWG)** | `SuffixAutomaton.java` | Distinct Substring Counting & Structural Analysis | $O(N)$ build | $O(N)$ build | $O(N)$ build | $O(N \cdot \Sigma)$ | Minimal Directed Acyclic Word Graph (DAWG) representing all factors of a string; computes exact distinct substring counts via state DP in linear time. |

*Notation*:  
- $N$: Length of document text.  
- $M$: Length of query pattern.  
- $L$: Total concatenated length of all search patterns ($\sum |P_i|$).  
- $Z$: Total number of pattern matches found.  
- $K$: Length of vocabulary word compared during fuzzy search.  
- $V$: Number of unique terms across document vocabulary.  
- $D$: Number of documents in corpus.  
- $\Sigma$: Alphabet size.

---

## 3. Syllabus Unit Coverage Breakdown

### Unit 1: Advanced Data Structures & Inverted Indexing
- **Topics**: Dynamic arrays, Hashing, Inverted Indices, Dictionary Structures.
- **Project Coverage**:
  - `InvertedIndexService.java`: Memory-resident inverted index implemented using `HashMap<String, HashMap<String, Integer>>`.
  - Tokenization pipeline with stop-word filtering, punctuation stripping, and lowercasing.
  - Startup database hydration from MongoDB collections.

### Unit 2: Exact String Matching Algorithms
- **Topics**: Brute Force, Knuth-Morris-Pratt (LPS array), Z-Algorithm (Z-box), Rabin-Karp (Rolling Hash).
- **Project Coverage**:
  - `NaiveStringSearch.java`: Textbook $O((N-M+1)M)$ scanning.
  - `KMPStringSearch.java`: $O(M)$ LPS precomputation followed by $O(N)$ text scanning with no backtrack.
  - `ZAlgorithm.java`: $O(N+M)$ linear scanning with sliding $[L, R]$ match interval.
  - `RabinKarp.java`: Rolling hash with base 256 and modulo $10^9+7$, Horner's evaluation, and character confirmation.
  - `AlgorithmComparisonService.java`: Live side-by-side benchmark reporting exact execution time in nanoseconds and character comparison counts.

### Unit 3: Multi-Pattern Matching Automata
- **Topics**: Tries, Finite Automata, Aho-Corasick Algorithm.
- **Project Coverage**:
  - `AhoCorasick.java`: Trie construction with `children` map and `isEndOfWord`.
  - Breadth-First Search (BFS) construction of failure transitions (analogous to KMP LPS across multiple branching words).
  - Output dictionary link chaining to collect all substring pattern hits simultaneously in $O(N + L + Z)$.

### Unit 4: Dynamic Programming & Approximate String Matching
- **Topics**: Edit Distance, Matrix DP, String Similarity.
- **Project Coverage**:
  - `LevenshteinDistance.java`: Classic Wagner-Fischer 2D matrix DP for insertion, deletion, substitution.
  - `DamerauLevenshtein.java`: Modified DP matrix accounting for adjacent character transpositions (optimal string alignment distance).
  - Search fallback engine: Automatically triggers fuzzy matching when zero exact matches are found, proposing suggested corrections with distance metrics.

### Unit 5: Information Retrieval & Vector Space Models
- **Topics**: Term Frequencies, Vector Space Models, Cosine Similarity.
- **Project Coverage**:
  - `CosineSimilarity.java`: Constructs normalized word-frequency vectors across vocabulary union.
  - Computes inner product and Euclidean magnitudes to yield similarity score $\in [0.0, 1.0]$.
  - Used for document relatedness analysis and near-duplicate detection.

### Unit 6: Randomized Algorithms
- **Topics**: Las Vegas vs. Monte Carlo, Randomized Sorting, Universal Hashing.
- **Project Coverage**:
  - `RandomizedQuickSort.java`: Las Vegas randomized pivot selection (`ThreadLocalRandom.current().nextInt(low, high + 1)`) applied to search result ranking. Guarantees deterministic correctness while achieving expected $O(N \log N)$ performance regardless of initial input order.
  - `RandomizedHash.java`: Polynomial string hashing with a randomized base multiplier chosen at runtime.

### Unit 7: Advanced Suffix Structures
- **Topics**: Suffix Arrays, LCP Arrays, Suffix Automata.
- **Project Coverage**:
  - `SuffixArray.java`: Lexicographically sorted suffix indices enabling binary search for arbitrary substrings.
  - `KasaiLCP.java`: Linear-time $O(N)$ calculation of Longest Common Prefix array. Locates the Longest Repeated Substring (LRS) across the document.
  - `SuffixAutomaton.java`: Minimal Directed Acyclic Word Graph (DAWG) constructed online in $O(N)$ time. Dynamically counts all distinct substrings using topological state dynamic programming.

---

## 4. Omitted Syllabus Algorithms & Academic Justification

To maintain academic rigor and architectural honesty, certain algorithms commonly present in general DSA syllabi were **deliberately omitted**. The academic justifications are summarized below:

| Omitted Algorithm / Topic | Category | Theoretical Reason for Omission from Search Engine |
|---------------------------|----------|----------------------------------------------------|
| **Ford-Fulkerson / Edmonds-Karp / Dinic** | Maximum Flow / Network Flows | Text indexing and retrieval operates on discrete token sequences and vocabulary vectors, not directed capacity networks. Flow conservation constraints do not model keyword search or relevance ranking. |
| **Max Flow Min-Cut Theorem** | Network Cut Theory | Image segmentation or network bipartition use min-cut, but document retrieval evaluates textual relevance, which is an orthogonal problem. |
| **Hopcroft-Karp / Bipartite Matching** | Maximum Bipartite Matching | Query-document matching is an ranking and scoring problem (graded relevance), not a 1-to-1 unweighted maximum cardinality assignment. |
| **Traveling Salesperson Problem (TSP)** | NP-Hard / Combinatorial Optimization | Information retrieval requires real-time sub-millisecond query responses. TSP solves Hamiltonian tour minimization on metric graphs and has no valid reduction in text retrieval. |
| **Vertex Cover / Set Cover Approximations** | NP-Hard / Greedy Approximation | Document indexing processes entire texts. Approximation algorithms for NP-complete graph problems do not apply to inverted indexing or substring matching. |
| **Matrix Chain Multiplication (MCM)** | Associative Dynamic Programming | Computes optimal parenthesization of matrix chains. IR retrieval queries compare terms and documents directly without tensor contraction chains. |
| **Optimal Binary Search Trees (OBST)** | Static Probability DP Trees | Search engine vocabularies are dynamic; an Inverted Index using Hash Tables ($O(1)$ expected lookup) strictly outperforms OBST ($O(\log N)$ with expensive $O(N^3)$ or $O(N^2)$ precomputation). |
| **Bitmask Dynamic Programming** | Exponential DP ($O(2^N \cdot \text{poly})$) | Bitmask state compression is limited to small inputs ($N \le 20$). Document collections contain thousands of words, making bitmask representations infeasible and mathematically irrelevant. |

---

## 5. Verification & Testing Evidence

All algorithms have been validated through automated JUnit unit tests (`AlgorithmSuiteTest.java`) and end-to-end REST API integration tests:

1. **Test Suite Status**: 13/13 JUnit tests passing (0 failures, 0 errors).
2. **Benchmark Verification**: Validated on `Java.txt`, `Python.txt`, and `DSA.txt` comparing character operations and CPU execution nanoseconds.
3. **Multi-Pattern Verification**: Validated simultaneous recognition of `["stack", "queue", "algorithm", "python"]` across documents via Aho-Corasick.
4. **Fuzzy Search Verification**: Typo `pyhton` accurately resolved to `python` with Levenshtein distance 2 and Damerau-Levenshtein distance 1.
5. **Structural Analysis Verification**: Identified Longest Repeated Substring and exact count of distinct substrings on uploaded text files.
