# Mini Search Engine Using Data Structures and Information Retrieval

> **Course**: Advanced Data Structures and Algorithms (Course Code: 25CS2103E)  
> **Degree**: B.Tech Computer Science & Information Technology  
> **Student Authors**: B. Siddartha (2500090225) & C.V. Jaswanthreddy (2520080025)  
> **Supervisor / Guide**: Ms. K. Chandusha, Assistant Professor  
> **Repository**: `Mini Search Engine - Advanced DSA Edition`

---

## 1. Abstract

The **Mini Search Engine** is a full-stack information retrieval and text analysis platform engineered from the ground up to demonstrate foundational and advanced **Data Structures and Algorithms (DSA)**. Unlike commercial systems that offload algorithmic heavy-lifting to pre-built search engines (e.g., Elasticsearch, Apache Lucene), this project implements every core indexing, exact string matching, multi-pattern detection, fuzzy spell-checking, vector-space ranking, randomized sorting, and suffix-based text analysis algorithm directly in pure Java.

The system combines an in-memory **Inverted Index** (`HashMap<String, HashMap<String, Integer>>`) with state-of-the-art string algorithms including **Knuth-Morris-Pratt (KMP)**, **Z-Algorithm**, **Rabin-Karp Rolling Hash**, **Aho-Corasick Multi-Pattern Automaton**, **Levenshtein & Damerau-Levenshtein Dynamic Programming**, **Vector Space Cosine Similarity**, **Las Vegas Randomized QuickSort**, **Suffix Array with Binary Search**, **Kasai's LCP Algorithm**, and **Suffix Automaton (DAWG)**. Document persistence and metadata management are handled by **MongoDB**, with a responsive, interactive UI built using **React (Vite)**.

---

## 2. System Architecture & High-Level Design

```
+-----------------------------------------------------------------------------------+
|                              REACT + VITE FRONTEND                                |
|  [Search Bar]  [Algorithm Benchmark]  [Aho-Corasick Multi]  [Fuzzy]  [Similarity] |
|  [Suffix / LCP Inspector]  [Live Inverted Index Visualizer]  [Document Manager]   |
+------------------------------------------+----------------------------------------+
                                           | HTTP / REST API (JSON)
                                           v
+-----------------------------------------------------------------------------------+
|                        SPRING BOOT 4.0.8 BACKEND (JAVA 25)                        |
|                                                                                   |
|  +--------------------+  +----------------------+  +---------------------------+  |
|  | SearchController   |  | AlgorithmController  |  | DocumentController        |  |
|  +---------+----------+  +-----------+----------+  +-------------+-------------+  |
|            |                         |                           |                |
|            v                         v                           v                |
|  +-----------------------------------------------------------------------------+  |
|  |                             SERVICE LAYER                                   |  |
|  |  - InvertedIndexService   : HashMap<String, HashMap<String, Integer>>        |  |
|  |  - DocumentService        : File upload, content extraction, hydration      |  |
|  |  - AlgorithmCompareService: Live nanosecond benchmarking & char comparisons |  |
|  |  - DocumentAnalysisService: Suffix Array, Kasai LCP, Suffix Automaton, Cos  |  |
|  +-----------------------------------+-----------------------------------------+  |
|                                      |                                            |
|                                      v                                            |
|  +-----------------------------------------------------------------------------+  |
|  |                        ALGORITHM CORE (PURE JAVA)                           |  |
|  |  • InvertedIndex           • NaiveStringSearch        • KMPStringSearch     |  |
|  |  • ZAlgorithm              • RabinKarp                • AhoCorasick         |  |
|  |  • LevenshteinDistance     • DamerauLevenshtein       • CosineSimilarity    |  |
|  |  • RandomizedQuickSort     • RandomizedHash           • SuffixArray         |  |
|  |  • KasaiLCP                • SuffixAutomaton                                |  |
|  +-----------------------------------+-----------------------------------------+  |
|                                      |                                            |
|                                      v                                            |
|  +-----------------------------------------------------------------------------+  |
|  |                         PERSISTENCE LAYER (Spring Data)                     |  |
|  |  - DocumentRepository (MongoRepository<Document, String>)                   |  |
|  +-----------------------------------+-----------------------------------------+  |
+--------------------------------------|--------------------------------------------+
                                       | Mongo Wire Protocol
                                       v
+-----------------------------------------------------------------------------------+
|                              MONGODB 8.3.8 (LOCAL)                                |
|  Database: mini_search_engine                                                     |
|  Collection: documents { id, fileName, originalFileName, fileType, content, ... } |
+-----------------------------------------------------------------------------------+
```

---

## 3. Master Algorithm Implementation Matrix

| # | Syllabus Algorithm / Topic | Code Implementation File | IR / Search Engine Feature | Best Time | Avg Time | Worst Time | Space | Selection Rationale |
|---|----------------------------|--------------------------|----------------------------|-----------|----------|------------|-------|---------------------|
| 1 | **Inverted Index (Hash Index)** | `InvertedIndexService.java` | Core Search Indexing (Posting Lists) | $O(1)$ | $O(1)$ | $O(N)$ lookup | $O(V \cdot D)$ | Essential for sub-millisecond keyword lookup; maps token $\to$ `(docId, freq)`. |
| 2 | **Naive / Brute-Force Search** | `NaiveStringSearch.java` | Baseline Exact Search & Benchmark | $O(N)$ | $O(N \cdot M)$ | $O((N-M+1)M)$ | $O(1)$ | Educational baseline to compare against optimized string algorithms. |
| 3 | **Knuth-Morris-Pratt (KMP)** | `KMPStringSearch.java` | Exact Search & Pattern Highlighting | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | $O(M)$ | Uses Longest Proper Prefix-Suffix (LPS) failure function to prevent text backtracking. |
| 4 | **Z-Algorithm** | `ZAlgorithm.java` | Linear String Matching | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | Computes Z-array on `pattern + "$" + text` using sliding $[L, R]$ match boxes. |
| 5 | **Rabin-Karp Algorithm** | `RabinKarp.java` | Substring Fingerprinting Benchmark | $O(N + M)$ | $O(N + M)$ | $O(N \cdot M)$ | $O(1)$ | Polynomial rolling hash with Horner's evaluation ($\text{mod } 10^9+7$) and collision checks. |
| 6 | **Aho-Corasick Automaton** | `AhoCorasick.java` | Multi-Keyword Tagging & Bulk Search | $O(N + L + Z)$ | $O(N + L + Z)$ | $O(N + L + Z)$ | $O(L \cdot \Sigma)$ | Multi-pattern Trie with BFS suffix failure links; searches all patterns in one text pass. |
| 7 | **Levenshtein Distance (DP)** | `LevenshteinDistance.java` | Spell-Checking & Typo Fallback | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | Classic Wagner-Fischer 2D dynamic programming grid for insertion, deletion, substitution. |
| 8 | **Damerau-Levenshtein (DP)** | `DamerauLevenshtein.java` | Typo Correction with Transpositions | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | $O(M \cdot K)$ | Extends Levenshtein by modeling adjacent character swaps (e.g., `pyhton` $\to$ `python`) in 1 step. |
| 9 | **Vector Space Model (Cosine)** | `CosineSimilarity.java` | Document Relatedness & Plagiarism | $O(V)$ | $O(V)$ | $O(V)$ | $O(V)$ | Computes $\frac{\vec{A} \cdot \vec{B}}{\|\vec{A}\|_2 \|\vec{B}\|_2}$ across term-frequency vectors. |
| 10 | **Randomized QuickSort** | `RandomizedQuickSort.java` | Relevance Ranking & Ordering | $O(N \log N)$ | $O(N \log N)$ | $O(N^2)$ (prob $\to 0$) | $O(\log N)$ | Las Vegas randomized pivot selection eliminates worst-case quadratic degradation. |
| 11 | **Randomized Polynomial Hash** | `RandomizedHash.java` | Universal Fingerprinting | $O(N)$ | $O(1)$ check | $O(1)$ check | $O(N)$ | Prefix hashing with randomized multiplier base for $O(1)$ substring equality verification. |
| 12 | **Suffix Array + Binary Search**| `SuffixArray.java` | Full-Text Substring Indexing | $O(M \log N)$ | $O(M \log N)$ | $O(M \log N)$ | $O(N)$ | Lexicographically sorted suffix indices enabling binary search for arbitrary substrings. |
| 13 | **Kasai's LCP Algorithm** | `KasaiLCP.java` | Longest Repeated Substring (LRS) | $O(N)$ | $O(N)$ | $O(N)$ | $O(N)$ | Derives LCP array from Suffix Array in linear time $O(N)$ using $LCP[rank[i]] \ge LCP[rank[i-1]] - 1$. |
| 14 | **Suffix Automaton (DAWG)** | `SuffixAutomaton.java` | Distinct Substring Counting | $O(N)$ build | $O(N)$ build | $O(N)$ build | $O(N \cdot \Sigma)$ | Minimal Directed Acyclic Word Graph; calculates exact distinct substring counts via state DP. |

---

## 4. Omitted Syllabus Algorithms & Academic Rationale

To maintain architectural validity and academic integrity, certain algorithms present in general DSA syllabi were deliberately omitted:

| Omitted Algorithm / Topic | Category | Theoretical Reason for Omission |
|---------------------------|----------|--------------------------------|
| **Ford-Fulkerson / Edmonds-Karp / Dinic** | Maximum Network Flow | Text indexing and retrieval operates on discrete token sequences and vocabulary vectors, not directed capacity networks. Flow conservation constraints do not model keyword search or relevance ranking. |
| **Max Flow Min-Cut Theorem** | Network Cut Theory | Image segmentation or graph partitioning utilize min-cut, but document retrieval evaluates textual relevance, which is an orthogonal problem. |
| **Hopcroft-Karp / Bipartite Matching** | Maximum Cardinality Matching | Query-document matching is a graded relevance ranking problem, not a 1-to-1 unweighted maximum assignment. |
| **Traveling Salesperson Problem (TSP)** | NP-Hard Optimization | Search engines require real-time sub-millisecond query responses. TSP solves metric Hamiltonian tour minimization and has no valid reduction in text retrieval. |
| **Vertex Cover / Set Cover Approximations** | NP-Hard / Greedy Approximation | Document indexing processes entire texts. Approximation algorithms for NP-complete graph problems do not apply to inverted indexing or substring matching. |
| **Matrix Chain Multiplication (MCM)** | Associative Dynamic Programming | Computes optimal parenthesization of matrix chains. IR retrieval queries compare terms and documents directly without tensor contraction chains. |
| **Optimal Binary Search Trees (OBST)** | Static Probability DP Trees | Search engine vocabularies are dynamic; an Inverted Index using Hash Tables ($O(1)$ expected lookup) strictly outperforms OBST ($O(\log N)$ with expensive $O(N^3)$ or $O(N^2)$ precomputation). |
| **Bitmask Dynamic Programming** | Exponential DP ($O(2^N \cdot \text{poly})$) | Bitmask state compression is limited to small inputs ($N \le 20$). Document collections contain thousands of words, making bitmask representations infeasible and mathematically irrelevant. |

---

## 5. Technology Stack & Environment

| Layer | Component | Version | Role in Project |
| :--- | :--- | :--- | :--- |
| **Backend** | Java Development Kit (JDK) | 25 LTS (Oracle) | Core runtime, collections, string manipulation, multithreading |
| **Framework** | Spring Boot | 4.0.8 | REST controllers, dependency injection, CORS handling |
| **Build Tool** | Apache Maven | Wrapper (`./mvnw.cmd`) | Dependency management, test execution, packaging |
| **Database** | MongoDB Community Server | 8.3.8 (Port 27017) | Durable storage of uploaded documents and metadata |
| **Frontend** | React | 18.x | Modular single-page application (SPA) |
| **Bundler** | Vite | 5.4.21 | Hot-module replacement and ultra-fast client-side builds |
| **Styling** | Pure CSS3 | Custom Responsive | Academic UI with glassmorphism, responsive tables, and badges |

---

## 6. Project Directory & Package Structure

```
DSA3/
├── ALGORITHM_MAPPING.md          <-- Complete syllabus-to-project mapping specification
├── README.md                     <-- Comprehensive academic documentation and viva guide
├── test-files/                   <-- Standard test corpus for benchmarking
│   ├── DSA.txt                   <-- Document on stacks, queues, algorithms, Big O
│   ├── Java.txt                  <-- Document on Java OOP, programming, language
│   └── Python.txt                <-- Document on Python ML, data science, algorithms
├── backend/
│   ├── pom.xml                   <-- Spring Boot 4.0.8 & MongoDB dependencies
│   ├── mvnw.cmd                  <-- Maven wrapper executable for Windows
│   └── src/
│       ├── main/
│       │   ├── java/com/dsa/minisearchengine/
│       │   │   ├── MiniSearchEngineApplication.java  <-- Main entrypoint
│       │   │   ├── algorithm/                        <-- Pure Java DSA Implementations
│       │   │   │   ├── AhoCorasick.java              <-- Multi-pattern Trie automaton
│       │   │   │   ├── CosineSimilarity.java         <-- Vector space document similarity
│       │   │   │   ├── DamerauLevenshtein.java       <-- Transposition-aware edit distance
│       │   │   │   ├── KasaiLCP.java                 <-- Linear-time LCP array construction
│       │   │   │   ├── KMPStringSearch.java          <-- Knuth-Morris-Pratt pattern search
│       │   │   │   ├── LevenshteinDistance.java      <-- Classic Wagner-Fischer 2D DP
│       │   │   │   ├── NaiveStringSearch.java        <-- Brute-force string matching
│       │   │   │   ├── RabinKarp.java                <-- Modular rolling hash string search
│       │   │   │   ├── RandomizedHash.java           <-- Universal randomized hashing
│       │   │   │   ├── RandomizedQuickSort.java      <-- Las Vegas randomized pivot ranking
│       │   │   │   ├── SuffixArray.java              <-- Suffix array + binary search
│       │   │   │   └── SuffixAutomaton.java          <-- Minimal DAWG for factor analysis
│       │   │   ├── controller/
│       │   │   │   ├── AlgorithmController.java      <-- Benchmark, multi-search, fuzzy endpoints
│       │   │   │   ├── DocumentController.java       <-- Upload, download, delete endpoints
│       │   │   │   └── SearchController.java         <-- Search with mode & sort parameters
│       │   │   ├── model/
│       │   │   │   ├── AdvancedAnalysisResult.java   <-- Suffix Array & Automaton DTO
│       │   │   │   ├── AlgorithmMatchResult.java     <-- Single algorithm benchmark metrics
│       │   │   │   ├── ComparisonReport.java         <-- Benchmark report across 4 algorithms
│       │   │   │   ├── Document.java                 <-- MongoDB Document entity
│       │   │   │   ├── FuzzySuggestionResult.java    <-- Spell check suggestion DTO
│       │   │   │   ├── MultiPatternResult.java       <-- Aho-Corasick multi-match DTO
│       │   │   │   ├── SearchResult.java             <-- Ranked search item with metadata
│       │   │   │   └── SimilarityResult.java         <-- Cosine similarity comparison DTO
│       │   │   ├── repository/
│       │   │   │   └── DocumentRepository.java       <-- Spring Data Mongo repository interface
│       │   │   └── service/
│       │   │       ├── AlgorithmComparisonService.java <-- Benchmark orchestrator
│       │   │       ├── DocumentAnalysisService.java    <-- Suffix & similarity orchestrator
│       │   │       ├── DocumentService.java            <-- MongoDB hydration & document CRUD
│       │   │       └── InvertedIndexService.java       <-- In-memory HashMap inverted index
│       │   └── resources/
│       │       └── application.properties        <-- MongoDB connection & multipart limits
│       └── test/java/com/dsa/minisearchengine/
│           ├── AlgorithmSuiteTest.java           <-- Comprehensive 9-part algorithm test suite
│           ├── InvertedIndexServiceTest.java     <-- 3-part indexing & hydration tests
│           └── MiniSearchEngineApplicationTests.java <-- Spring context load test
└── frontend/
    ├── package.json                              <-- React 18, Lucide icons, Vite
    ├── vite.config.js                            <-- Dev server configuration
    └── src/
        ├── App.jsx                               <-- Main application with 6 view tabs
        ├── App.css                               <-- Comprehensive CSS design system
        ├── components/
        │   ├── AdvancedAnalysisView.jsx          <-- Suffix Array & Automaton visualizer
        │   ├── AlgorithmComparisonView.jsx       <-- Naive vs KMP vs Z vs Rabin-Karp benchmark
        │   ├── DocumentList.jsx                  <-- Uploaded files table and preview modal
        │   ├── DocumentSimilarityView.jsx        <-- Pairwise Cosine similarity comparator
        │   ├── DocumentUpload.jsx                <-- Drag-and-drop file uploader
        │   ├── DsaComplexityModal.jsx            <-- Interactive modal explaining algorithm theory
        │   ├── FuzzySearchView.jsx               <-- Levenshtein & Damerau-Levenshtein tester
        │   ├── InvertedIndexVisualizer.jsx       <-- Interactive HashMap posting list inspector
        │   ├── MultiPatternSearchView.jsx        <-- Aho-Corasick multi-keyword runner
        │   ├── Navbar.jsx                        <-- Top navigation header with view tabs
        │   ├── SearchBar.jsx                     <-- Query input with Algorithm & Sort toggles
        │   └── SearchResults.jsx                 <-- Ranked results with relevance badges
        └── services/
            └── api.js                            <-- Axios client configured for all endpoints
```

---

## 7. Deep-Dive: Core Algorithms & Implementations

### 7.1 Inverted Index Service (`InvertedIndexService.java`)
- **Structure**: `HashMap<String, HashMap<String, Integer>>`
  - Outer Key: Canonical term (lowercased, punctuation-stripped).
  - Inner Key: Document ID.
  - Inner Value: Occurrence frequency of term in document.
- **Workflow**:
  1. Text is normalized: `text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").split("\\s+")`.
  2. Stop words (`"the"`, `"is"`, `"at"`, `"which"`, `"on"`) can be filtered or retained.
  3. Terms are inserted in $O(1)$ amortized time.
  4. Search lookups execute in $O(1)$ time, retrieving the exact posting list.

### 7.2 Knuth-Morris-Pratt Algorithm (`KMPStringSearch.java`)
- **Core Mechanism**: Precomputes the Longest Proper Prefix which is also a Suffix (LPS array) of the pattern.
- **Complexity**: $O(M)$ preprocessing, $O(N)$ text scanning. Total: $O(N + M)$ time, $O(M)$ space.
- **Advantage**: Backtracking in the pattern index occurs via `j = lps[j-1]`, guaranteeing the text pointer `i` never moves backwards.

### 7.3 Z-Algorithm (`ZAlgorithm.java`)
- **Core Mechanism**: Constructs a concatenated string $S = P + \text{"\$"} + T$ and calculates the Z-array, where $Z[i]$ is the length of the longest substring starting at $i$ that matches a prefix of $S$.
- **Complexity**: Strictly $O(N + M)$ time and $O(N + M)$ auxiliary space using a sliding match window $[L, R]$.
- **Match Condition**: Any index $i > |P|$ where $Z[i] == |P|$ represents an exact occurrence of $P$ at text index $i - |P| - 1$.

### 7.4 Rabin-Karp Rolling Hash (`RabinKarp.java`)
- **Core Mechanism**: Computes polynomial rolling fingerprints:
  $$H(S[0..m-1]) = \left( \sum_{i=0}^{m-1} S[i] \cdot B^{m-1-i} \right) \pmod P$$
  where $B = 256$ and $P = 1{,}000{,}000{,}007$ (a large prime).
- **Rolling Step**: Sliding the window right by 1 character takes $O(1)$:
  $$H_{\text{new}} = \left( (H_{\text{old}} - S[i] \cdot B^{m-1}) \cdot B + S[i+m] \right) \pmod P$$
- **Collision Handling**: On hash equality, explicit character verification is performed to completely avoid false positives.

### 7.5 Aho-Corasick Multi-Pattern Automaton (`AhoCorasick.java`)
- **Core Mechanism**:
  1. **Trie Construction**: All dictionary keywords are inserted into a prefix tree.
  2. **BFS Failure Function**: Computes suffix links for each node pointing to the longest proper suffix that exists in the Trie.
  3. **Dictionary Output Links**: Links each node to the nearest pattern end node reachable via failure links.
- **Complexity**: $O(L)$ build time, $O(N + Z)$ search time, where $L = \sum |P_i|$ and $Z$ is total matches.
- **Advantage**: Searches for 100 keywords in a single linear pass over the document without re-reading the text.

### 7.6 Levenshtein vs. Damerau-Levenshtein Distance (`LevenshteinDistance.java`, `DamerauLevenshtein.java`)
- **Levenshtein Distance**: Classic Wagner-Fischer dynamic programming matrix:
  $$D[i,j] = \min \begin{cases} D[i-1,j] + 1 & \text{(Deletion)} \\ D[i,j-1] + 1 & \text{(Insertion)} \\ D[i-1,j-1] + \text{cost} & \text{(Substitution)} \end{cases}$$
- **Damerau-Levenshtein Distance**: Adds a 4th operation for adjacent character transpositions:
  $$\text{if } i > 1 \text{ and } j > 1 \text{ and } s_1[i] == s_2[j-1] \text{ and } s_1[i-1] == s_2[j]:$$
  $$D[i,j] = \min(D[i,j], D[i-2, j-2] + 1)$$
- **Academic Demonstration**:
  - `pyhton` $\to$ `python`: Levenshtein distance = 2 (delete `h`, insert `h`).
  - `pyhton` $\to$ `python`: Damerau-Levenshtein distance = 1 (single transposition swap of adjacent `h` and `t`).

### 7.7 Vector Space Model & Cosine Similarity (`CosineSimilarity.java`)
- **Core Mechanism**: Documents are modeled as vectors in a $V$-dimensional vocabulary space:
  $$\text{Cosine Similarity}(\vec{A}, \vec{B}) = \frac{\vec{A} \cdot \vec{B}}{\|\vec{A}\|_2 \|\vec{B}\|_2} = \frac{\sum_{i=1}^V A_i B_i}{\sqrt{\sum_{i=1}^V A_i^2} \sqrt{\sum_{i=1}^V B_i^2}}$$
- **Output**: Returns a normalized similarity score in $[0.0, 1.0]$ and the exact list of shared intersection keywords.

### 7.8 Las Vegas Randomized QuickSort (`RandomizedQuickSort.java`)
- **Core Mechanism**: Standard QuickSort can degrade to $O(N^2)$ on pre-sorted or adversarial inputs when using a deterministic pivot (e.g., first or last element).
- **Las Vegas Randomization**: Selects a pivot index uniformly at random from $[low, high]$:
  $$\text{pivotIndex} = \text{ThreadLocalRandom.current().nextInt}(low, high + 1)$$
- **Guarantees**: Always produces a strictly sorted output (zero error probability) with guaranteed expected time complexity of $O(N \log N)$ across any input distribution.

### 7.9 Suffix Array & Kasai's LCP Algorithm (`SuffixArray.java`, `KasaiLCP.java`)
- **Suffix Array**: An integer array representing the lexicographical order of all suffixes of text $T$. Substrings can be searched using two binary searches (lower bound and upper bound) in $O(M \log N)$.
- **Kasai's Algorithm**: Calculates the Longest Common Prefix (LCP) between adjacent suffixes in the suffix array in strictly $O(N)$ linear time.
  - Key Theorem: If $h = LCP[rank[i]]$, then for suffix $i+1$, the LCP is at least $h - 1$:
    $$LCP[rank[i+1]] \ge LCP[rank[i]] - 1$$
- **Search Engine Utility**: Automatically locates the **Longest Repeated Substring (LRS)** in any document to detect duplicate phrases, boilerplate content, or plagiarism.

### 7.10 Suffix Automaton / DAWG (`SuffixAutomaton.java`)
- **Core Mechanism**: A minimal Directed Acyclic Word Graph (DAWG) that represents all $O(N^2)$ substrings of a text of length $N$ using only $O(N)$ states (at most $2N-1$ states) and $O(N)$ transitions (at most $3N-4$ transitions).
- **Online Construction**: Processes characters one-by-one in amortized $O(1)$ per character, achieving $O(N)$ overall build time.
- **Distinct Substring Counting**: Using topological dynamic programming over the DAG:
  $$dp[u] = 1 + \sum_{c \in \Sigma} dp[\delta(u, c)]$$
  computes the exact count of unique substrings without allocating or hashing substrings.

---

## 8. REST API Reference

### 8.1 Document Management APIs (`/api/documents`)
- `POST /api/documents/upload`: Multipart file upload (`.txt`).
- `GET /api/documents`: List all uploaded documents with metadata.
- `GET /api/documents/{id}`: Fetch document text content.
- `GET /api/documents/{id}/download`: Download raw text file.
- `DELETE /api/documents/{id}`: Delete document from MongoDB and purge from in-memory Inverted Index.

### 8.2 Search APIs (`/api/search`)
- `GET /api/search?query={term}&mode={mode}&sort={sort}`:
  - `query`: Search query (e.g., `java`).
  - `mode`: `INVERTED_INDEX` (default), `KMP`, `NAIVE`, `Z_ALGORITHM`, `RABIN_KARP`.
  - `sort`: `RANDOMIZED_QUICKSORT` (default) or `JAVA_SORT`.
- `GET /api/search/index`: Returns the full in-memory inverted index map for live inspection.

### 8.3 Advanced Algorithm APIs (`/api/algorithms`)
- `POST /api/algorithms/compare`:
  - Request: `{"documentId": "...", "pattern": "algorithm"}`
  - Response: Side-by-side benchmark of Naive, KMP, Z-Algorithm, and Rabin-Karp (character comparisons, match positions, execution time in nanoseconds/microseconds).
- `POST /api/algorithms/multi-search`:
  - Request: `{"patterns": ["stack", "queue", "algorithm", "python"]}`
  - Response: Aho-Corasick multi-pattern match distribution across all documents in a single text pass.
- `GET /api/algorithms/fuzzy-search?query={typo}&maxDistance={2}`:
  - Response: Suggested term, Levenshtein distance, Damerau-Levenshtein distance, similarity percentage, and auto-executed search results.
- `POST /api/algorithms/similarity`:
  - Request: `{"docId1": "...", "docId2": "..."}`
  - Response: Cosine similarity score, common overlapping terms, and unique word counts.
- `GET /api/algorithms/advanced-analysis/{documentId}`:
  - Response: Suffix Array sample, LCP array sample, Longest Repeated Substring (LRS), and exact distinct substring count via Suffix Automaton.

---

## 9. Prerequisites & Installation Guide

### Prerequisites
1. **Java JDK 25 LTS** (or JDK 17+): Verified with `java -version`.
2. **Node.js v18+ & npm**: Verified with `node -v` and `npm -v`.
3. **MongoDB Community Server 7.0+ / 8.0+**: Running locally on `mongodb://localhost:27017`.

---

## 10. Step-by-Step Execution Guide

### Step 1: Verify MongoDB is Running
Open PowerShell or Command Prompt:
```powershell
mongosh --eval "db.adminCommand('ping')"
```
Expected output: `{ ok: 1 }`.

### Step 2: Start the Spring Boot Backend Server
Navigate to the `backend` directory:
```powershell
cd C:\Users\Sidhartha\OneDrive\DSA3\backend
.\mvnw.cmd spring-boot:run
```
The server will start on **`http://localhost:8080`**.  
On startup, the console will display:
```
Inverted Index initialized with 3 documents and 32 unique words.
```

### Step 3: Start the React Frontend Development Server
Open a second PowerShell window:
```powershell
cd C:\Users\Sidhartha\OneDrive\DSA3\frontend
npm run dev
```
The client application will start on **`http://localhost:5173`**.

---

## 11. Automated Test Suite Execution

The backend contains a dedicated JUnit 5 test suite verifying every single algorithm against exact test cases:

```powershell
cd C:\Users\Sidhartha\OneDrive\DSA3\backend
.\mvnw.cmd test
```

### Test Coverage Summary:
- `AlgorithmSuiteTest.java`:
  - `testNaiveSearch`: Verifies pattern matches and character comparisons.
  - `testKMPSearch`: Verifies LPS computation and exact match positions.
  - `testZAlgorithm`: Verifies Z-box sliding window and prefix matching.
  - `testRabinKarp`: Verifies rolling hash modular arithmetic and collision resolution.
  - `testAhoCorasick`: Verifies multi-pattern Trie search across branching keywords.
  - `testLevenshteinAndDamerau`: Validates transposition advantage (`pyhton` $\to$ `python`).
  - `testCosineSimilarity`: Verifies identical documents yield $1.0$ and orthogonal documents yield $0.0$.
  - `testRandomizedQuickSort`: Verifies descending relevance ordering across arbitrary scores.
  - `testSuffixStructures`: Verifies Suffix Array, Kasai LCP longest repeated substring, and Suffix Automaton distinct factor counts.
- `InvertedIndexServiceTest.java`:
  - `testIndexAndSearch`: Verifies tokenization, posting list updates, and frequency counts.
  - `testDeleteDocument`: Verifies clean purging of indexed documents and orphaned words.
  - `testHydration`: Verifies database hydration upon server boot.
- `MiniSearchEngineApplicationTests.java`:
  - `contextLoads`: Verifies Spring Boot ApplicationContext initializes cleanly.

**Result**: `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0` (100% Passing).

---

## 12. Frontend Navigation & Demonstration Guide

The frontend features 6 dedicated tabs in the top navigation bar:

1. **Search Engine Tab**:
   - Query input with **Algorithm Selector** (Inverted Index, KMP, Z-Algorithm, Rabin-Karp, Naive) and **Sort Method** (Randomized QuickSort vs. Standard Sort).
   - Instant search results with document snippets, term occurrences, and relevance percentage badges.
   - Live Inverted Index visualizer showing the in-memory `HashMap` postings.
   - Document upload and management table (view, download, delete).
2. **Algorithm Benchmark Tab**:
   - Select any uploaded document and enter a test pattern (e.g., `Java`).
   - Runs **Naive**, **KMP**, **Z-Algorithm**, and **Rabin-Karp** side-by-side.
   - Displays a comparison table with: Match Count, Match Indices, Character Comparisons, and Execution Time in Nanoseconds / Microseconds.
3. **Aho-Corasick Multi-Search Tab**:
   - Enter multiple comma-separated keywords (e.g., `stack, queue, algorithm, python`).
   - Scans all documents simultaneously in $O(N + L + Z)$ time using the Trie automaton.
   - Displays per-document match distributions and execution times.
4. **Fuzzy Search & Spellcheck Tab**:
   - Enter misspelled queries (e.g., `pyhton`, `struture`, `javva`).
   - Compares **Levenshtein Distance** vs. **Damerau-Levenshtein Distance** with adjacent transposition analysis.
   - Shows similarity percentage and automatically fetches corrected results.
5. **Document Similarity Tab**:
   - Select two documents to compare using the Vector Space Model.
   - Calculates **Cosine Similarity** ($\%$) and highlights shared intersection vocabulary.
6. **Advanced Text Analysis Tab**:
   - Computes **Suffix Array**, **Kasai's LCP Array**, and **Suffix Automaton (DAWG)**.
   - Visualizes the top sorted suffixes, LCP values, the **Longest Repeated Substring (LRS)**, and total **Distinct Substrings**.
   - Includes an interactive **DSA Theory Modal** with LaTeX complexity breakdowns.

---

## 13. Viva Voce & Academic Defense Preparation

### 13.1 General & Architectural Questions

**Q1: Why did you build an Inverted Index instead of searching files directly with a loop?**  
*Answer*: Direct sequential search requires $O(N \times L)$ operations for every query, where $N$ is the number of documents and $L$ is their average length. As the corpus grows, latency scales linearly with data size. An **Inverted Index** precomputes a mapping from words to documents using a Hash Table (`HashMap<String, HashMap<String, Integer>>`), enabling search queries to execute in average $O(1)$ time regardless of corpus size.

**Q2: Why did you not use Elasticsearch or Lucene?**  
*Answer*: Elasticsearch and Apache Lucene are production-grade frameworks that encapsulate all indexing and search logic behind black-box APIs. For a B.Tech Data Structures and Algorithms project, using external search engines defeats the academic objective. By writing all data structures from scratch in pure Java, we demonstrate mastery of memory layouts, hash collisions, dynamic programming matrices, automata transitions, and algorithmic complexity.

**Q3: How does your Inverted Index handle document updates and deletions?**  
*Answer*: In `InvertedIndexService.java`:
- When a document is re-uploaded, `deleteDocument(fileName)` is called first to purge old posting entries before re-indexing.
- When a document is deleted, all postings matching the document name are removed from every word's inner map. If an inner map becomes empty, the word key itself is removed from the outer map using `index.remove(word)` to prevent memory leaks and orphaned entries.

**Q4: How does the in-memory index survive server restarts?**  
*Answer*: On startup, `DocumentService.java` implements a `@PostConstruct` hydration lifecycle hook. It queries MongoDB for all existing documents and streams them through `InvertedIndexService.addDocument()`, reconstructing the in-memory `HashMap` before the first HTTP request arrives.

---

### 13.2 String Matching & Automata Questions

**Q5: What is the theoretical advantage of KMP over Naive String Search?**  
*Answer*: In Naive search, when a mismatch occurs after matching $j$ characters, the text pointer restarts at $i - j + 1$, leading to $O((N-M+1)M)$ worst-case time (e.g., matching `AAAA` in `AAAAAAAAA`). **KMP** avoids text backtracking by utilizing the Longest Proper Prefix which is also a Suffix (LPS array). When a mismatch occurs at pattern index $j$, the text pointer $i$ never retreats; instead, the pattern pointer transitions to $j = LPS[j-1]$. This guarantees strict $O(N + M)$ worst-case execution.

**Q6: How does the Z-Algorithm achieve linear time complexity?**  
*Answer*: The Z-Algorithm maintains a match window $[L, R]$, which represents the interval of text currently known to match a prefix of the concatenated string $P + \text{"\$"} + T$. For each index $i$:
- If $i > R$, it computes $Z[i]$ naively and updates $[L, R]$.
- If $i \le R$, it looks at the previously computed value $k = i - L$. If $Z[k] < R - i + 1$, then $Z[i] = Z[k]$ in $O(1)$ without character comparisons.
- If $Z[k] \ge R - i + 1$, it extends the comparison beyond $R$ and advances $R$. Because $R$ only advances from $0$ to $|S|$, the total number of character comparisons is bounded by $2|S|$, proving $O(N + M)$ linear time.

**Q7: How does Rabin-Karp calculate rolling hashes in $O(1)$ time?**  
*Answer*: Rabin-Karp treats substrings of length $m$ as base-$B$ integers modulo a large prime $P$. When shifting the window from $S[i..i+m-1]$ to $S[i+1..i+m]$, the new hash is computed as:
$$H_{\text{new}} = \left( (H_{\text{old}} - S[i] \cdot B^{m-1}) \cdot B + S[i+m] \right) \pmod P$$
By precomputing $h = B^{m-1} \pmod P$, the high-order character is removed, the remaining value is multiplied by $B$, and the new low-order character is added, all in $O(1)$ arithmetic operations.

**Q8: What makes Aho-Corasick superior to running KMP multiple times?**  
*Answer*: If we want to find $K$ different patterns of length $M$ in a text of length $N$, running KMP sequentially takes $O(K \times (N + M))$ time. If $K = 100$, the text is scanned 100 times. **Aho-Corasick** constructs a multi-pattern Trie with failure transitions in $O(L)$ time, where $L = \sum |P_i|$. It then scans the text **once** in $O(N + Z)$ time, finding all occurrences of all $K$ patterns simultaneously.

---

### 13.3 Dynamic Programming & Spellcheck Questions

**Q9: Explain the difference between Levenshtein and Damerau-Levenshtein distance.**  
*Answer*: **Levenshtein Distance** allows three edit operations: insertion, deletion, and substitution. **Damerau-Levenshtein Distance** (specifically the Optimal String Alignment variant) adds a fourth operation: adjacent character transposition (e.g., swapping `te` to `et`).
- For the common typo `pyhton` $\to$ `python`:
  - Levenshtein requires **2 operations**: Delete `h` at index 2, then insert `h` at index 3 (or substitute `h` with `t`, and `t` with `h`).
  - Damerau-Levenshtein recognizes that `h` and `t` are adjacent transposed letters and requires only **1 operation**.
  This makes Damerau-Levenshtein significantly more accurate for natural human typing errors.

**Q10: What is the time and space complexity of your edit distance implementation?**  
*Answer*: Both algorithms use dynamic programming matrices of dimension $(M+1) \times (K+1)$, where $M$ is the length of the query and $K$ is the length of the dictionary word.
- Time Complexity: $O(M \times K)$ operations to fill the table.
- Space Complexity: $O(M \times K)$ space. (Can be optimized to $O(\min(M,K))$ using two rows if path reconstruction is not required).

---

### 13.4 Information Retrieval & Ranking Questions

**Q11: How does Cosine Similarity evaluate document relatedness?**  
*Answer*: In the Vector Space Model, each document is represented as a high-dimensional vector whose dimensions correspond to unique terms in the corpus vocabulary, and values represent term frequencies. The Cosine Similarity measures the cosine of the angle between two document vectors:
$$\cos(\theta) = \frac{\vec{A} \cdot \vec{B}}{\|\vec{A}\|_2 \|\vec{B}\|_2}$$
Because it normalizes by Euclidean length ($\|\vec{A}\|_2$), the score is invariant to document length. A 10-page document and a 1-page document on the same topic will have identical vector angles and a high cosine similarity close to $1.0$, whereas orthogonal documents with disjoint vocabularies yield $0.0$.

**Q12: Why did you use Randomized QuickSort instead of standard deterministic QuickSort?**  
*Answer*: Standard deterministic QuickSort (e.g., picking the first or last element as pivot) degrades to $O(N^2)$ time when the input is already sorted or nearly sorted, which commonly occurs when sorting ranking scores. **Randomized QuickSort** is a Las Vegas algorithm that selects the pivot index uniformly at random:
$$\text{pivotIndex} = \text{ThreadLocalRandom.current().nextInt}(low, high + 1)$$
This random choice guarantees that no input distribution can systematically trigger the worst-case partition. The expected time complexity is strictly $O(N \log N)$ for all inputs, while guaranteeing exact and correct sorting without any error probability.

---

### 13.5 Advanced Suffix Structures Questions

**Q13: What is a Suffix Array, and why is it useful in a search engine?**  
*Answer*: A **Suffix Array** is an integer array storing the starting indices of all suffixes of a text, sorted in lexicographical order. While an inverted index only indexes pre-tokenized words, a Suffix Array indexes **every possible substring** in the document. By applying binary search on the suffix array, any arbitrary substring of length $M$ can be located in $O(M \log N)$ time without full-text re-scanning.

**Q14: How does Kasai's Algorithm compute the LCP array in linear time?**  
*Answer*: A naive comparison of adjacent suffixes in the suffix array would take $O(N^2)$ time. Kasai's algorithm achieves $O(N)$ linear time by observing suffixes in their original text order (from index $0$ to $N-1$) rather than suffix array order. It leverages the mathematical invariant:
$$LCP[rank[i]] \ge LCP[rank[i-1]] - 1$$
Because the prefix match length drops by at most 1 when stripping the first character, the internal matching counter is decremented by at most 1 across outer iterations. The counter can increase at most $N$ times, strictly bounding the total comparisons to $2N = O(N)$.

**Q15: What is a Suffix Automaton, and how does it count distinct substrings?**  
*Answer*: A **Suffix Automaton** (or DAWG) is the minimal deterministic finite automaton (DFA) that accepts all suffixes of a string. Despite a text of length $N$ having $\frac{N(N+1)}{2} = O(N^2)$ substrings, the Suffix Automaton contains at most $2N-1$ states and $3N-4$ transitions. Each state in the automaton corresponds to an equivalence class of substrings that have the same set of end positions (`endpos`).
To count the total number of distinct substrings, we perform dynamic programming on the directed acyclic graph:
$$dp[u] = 1 + \sum_{c \in \Sigma} dp[\delta(u, c)]$$
The value $dp[\text{root}] - 1$ gives the exact count of unique substrings in $O(N)$ time without generating strings in memory.

---

## 14. License & Academic Disclaimer

This project is developed solely for academic coursework and evaluation for **Course 25CS2103E (Advanced Data Structures and Algorithms)** at the School of Computer Science & Engineering. All algorithm implementations are original works written for educational demonstrations.
