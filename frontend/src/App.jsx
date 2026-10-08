import React, { useState, useEffect, useCallback } from 'react';
import Navbar from './components/Navbar';
import UploadSection from './components/UploadSection';
import SearchBar from './components/SearchBar';
import SearchResults from './components/SearchResults';
import DocumentList from './components/DocumentList';
import ViewModal from './components/ViewModal';
import DsaVisualizerModal from './components/DsaVisualizerModal';
import DsaComplexityModal from './components/DsaComplexityModal';
import AlgorithmComparisonView from './components/AlgorithmComparisonView';
import MultiPatternSearchView from './components/MultiPatternSearchView';
import FuzzySearchView from './components/FuzzySearchView';
import DocumentSimilarityView from './components/DocumentSimilarityView';
import AdvancedAnalysisView from './components/AdvancedAnalysisView';
import { api } from './services/api';
import { Search, Zap, Network, HelpCircle, GitCompare, Microscope } from 'lucide-react';
import './App.css';

export default function App() {
  const [activeTab, setActiveTab] = useState('search');
  const [documents, setDocuments] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [currentQuery, setCurrentQuery] = useState('');
  const [currentMode, setCurrentMode] = useState('INVERTED_INDEX');
  const [currentSort, setCurrentSort] = useState('RANDOMIZED_QUICKSORT');
  const [hasSearched, setHasSearched] = useState(false);
  const [searchLoading, setSearchLoading] = useState(false);
  const [docsLoading, setDocsLoading] = useState(false);
  const [indexStats, setIndexStats] = useState(null);
  const [activeDocView, setActiveDocView] = useState(null);
  const [showDsaModal, setShowDsaModal] = useState(false);
  const [showComplexityModal, setShowComplexityModal] = useState(false);
  const [backendError, setBackendError] = useState('');

  // Fetch document list from MongoDB
  const loadDocuments = useCallback(async () => {
    try {
      setDocsLoading(true);
      const data = await api.getAllDocuments();
      setDocuments(data);
      setBackendError('');
    } catch (err) {
      console.error('Failed to load documents:', err);
      setBackendError('Cannot connect to backend server. Make sure Spring Boot is running on port 8080.');
    } finally {
      setDocsLoading(false);
    }
  }, []);

  // Fetch index stats & raw HashMap from backend
  const loadIndexStats = useCallback(async () => {
    try {
      const stats = await api.getIndexStats();
      setIndexStats(stats);
    } catch (err) {
      console.warn('Index stats not available:', err);
    }
  }, []);

  // Initial load
  useEffect(() => {
    loadDocuments();
    loadIndexStats();
  }, [loadDocuments, loadIndexStats]);

  // Execute search
  const handleSearch = async (query, mode = 'INVERTED_INDEX', sort = 'RANDOMIZED_QUICKSORT') => {
    if (!query || !query.trim()) {
      setSearchResults([]);
      setHasSearched(false);
      setCurrentQuery('');
      return;
    }

    try {
      setSearchLoading(true);
      setCurrentQuery(query);
      setCurrentMode(mode);
      setCurrentSort(sort);
      setHasSearched(true);
      const results = await api.search(query, mode, sort);
      setSearchResults(results);
    } catch (err) {
      console.error('Search failed:', err);
      setSearchResults([]);
    } finally {
      setSearchLoading(false);
    }
  };

  // View document content
  const handleViewDocument = async (docId) => {
    try {
      const doc = await api.getDocumentById(docId);
      setActiveDocView(doc);
    } catch (err) {
      alert('Could not retrieve document: ' + err.message);
    }
  };

  const handleUploadSuccess = () => {
    loadDocuments();
    loadIndexStats();
    if (currentQuery) {
      handleSearch(currentQuery, currentMode, currentSort);
    }
  };

  const handleDeleteSuccess = () => {
    loadDocuments();
    loadIndexStats();
    if (currentQuery) {
      handleSearch(currentQuery, currentMode, currentSort);
    }
  };

  const handleSwitchToFuzzy = (query) => {
    setActiveTab('fuzzy');
  };

  return (
    <div className="app-layout">
      <Navbar
        onOpenDsaModal={() => setShowDsaModal(true)}
        onOpenComplexityModal={() => setShowComplexityModal(true)}
        totalDocs={indexStats?.totalIndexedDocuments ?? documents.length}
        uniqueWords={indexStats?.totalUniqueWordsIndexed ?? 0}
      />

      {/* Main Tab Navigation Bar */}
      <nav className="tab-navigation">
        <div className="tab-container">
          <button
            className={`tab-btn ${activeTab === 'search' ? 'active' : ''}`}
            onClick={() => setActiveTab('search')}
          >
            <Search size={16} />
            <span>Search Engine</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'benchmark' ? 'active' : ''}`}
            onClick={() => setActiveTab('benchmark')}
          >
            <Zap size={16} />
            <span>Algorithm Benchmark</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'multi' ? 'active' : ''}`}
            onClick={() => setActiveTab('multi')}
          >
            <Network size={16} />
            <span>Multi-Pattern Search</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'fuzzy' ? 'active' : ''}`}
            onClick={() => setActiveTab('fuzzy')}
          >
            <HelpCircle size={16} />
            <span>Fuzzy / Spellcheck</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'similarity' ? 'active' : ''}`}
            onClick={() => setActiveTab('similarity')}
          >
            <GitCompare size={16} />
            <span>Document Similarity</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'advanced' ? 'active' : ''}`}
            onClick={() => setActiveTab('advanced')}
          >
            <Microscope size={16} />
            <span>Advanced Analysis</span>
          </button>
        </div>
      </nav>

      <main className="main-content">
        {backendError && (
          <div className="backend-offline-banner">
            <div className="banner-content">
              <strong>Spring Boot Backend Not Connected:</strong> {backendError}
              <button className="btn btn-sm btn-secondary" onClick={() => { loadDocuments(); loadIndexStats(); }}>
                Retry Connection
              </button>
            </div>
          </div>
        )}

        {/* TAB 1: MAIN SEARCH ENGINE */}
        {activeTab === 'search' && (
          <>
            <div className="hero-section">
              <h2>Mini Search Engine with Advanced DSA Algorithms</h2>
              <p>
                Query documents using <strong>Inverted Index (HashMap)</strong>, <strong>KMP</strong>, <strong>Z Algorithm</strong>, <strong>Rabin-Karp</strong>, or <strong>Suffix Array</strong>, ranked via <strong>Randomized QuickSort</strong>.
              </p>
            </div>

            <SearchBar onSearch={handleSearch} loading={searchLoading} />

            <SearchResults
              results={searchResults}
              query={currentQuery}
              loading={searchLoading}
              hasSearched={hasSearched}
              onViewDocument={handleViewDocument}
              onSwitchToFuzzy={handleSwitchToFuzzy}
            />

            <UploadSection onUploadSuccess={handleUploadSuccess} />

            <DocumentList
              documents={documents}
              loading={docsLoading}
              onRefresh={() => { loadDocuments(); loadIndexStats(); }}
              onViewDocument={handleViewDocument}
              onDeleteSuccess={handleDeleteSuccess}
            />
          </>
        )}

        {/* TAB 2: ALGORITHM BENCHMARK */}
        {activeTab === 'benchmark' && (
          <AlgorithmComparisonView documents={documents} />
        )}

        {/* TAB 3: MULTI-PATTERN SEARCH (AHO-CORASICK) */}
        {activeTab === 'multi' && (
          <MultiPatternSearchView onViewDocument={handleViewDocument} />
        )}

        {/* TAB 4: FUZZY SEARCH (LEVENSHTEIN / DAMERAU-LEVENSHTEIN) */}
        {activeTab === 'fuzzy' && (
          <FuzzySearchView onViewDocument={handleViewDocument} />
        )}

        {/* TAB 5: DOCUMENT SIMILARITY (COSINE SIMILARITY) */}
        {activeTab === 'similarity' && (
          <DocumentSimilarityView documents={documents} />
        )}

        {/* TAB 6: ADVANCED ANALYSIS (SUFFIX ARRAY, KASAI LCP, SUFFIX AUTOMATON) */}
        {activeTab === 'advanced' && (
          <AdvancedAnalysisView documents={documents} />
        )}
      </main>

      <footer className="footer">
        <p>
          Mini Search Engine Using Data Structures and Information Retrieval • B.Tech DSA-3 (25CS2103E) • Team 10
        </p>
      </footer>

      {/* Modal for viewing file contents */}
      {activeDocView && (
        <ViewModal
          document={activeDocView}
          onClose={() => setActiveDocView(null)}
        />
      )}

      {/* Modal for viewing live Inverted Index HashMap */}
      {showDsaModal && (
        <DsaVisualizerModal
          stats={indexStats}
          onClose={() => setShowDsaModal(false)}
        />
      )}

      {/* Modal for master complexity matrix */}
      {showComplexityModal && (
        <DsaComplexityModal
          onClose={() => setShowComplexityModal(false)}
        />
      )}
    </div>
  );
}
