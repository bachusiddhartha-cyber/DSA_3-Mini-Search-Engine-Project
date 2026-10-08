import React from 'react';
import { Award, FileText, Eye, Info, SearchX, CheckCircle, Percent } from 'lucide-react';

export default function SearchResults({ results, query, loading, hasSearched, onViewDocument, onSwitchToFuzzy }) {
  if (loading) {
    return (
      <section className="card results-card loading-card">
        <div className="spinner"></div>
        <p className="loading-text">Executing pattern matching and ranking results...</p>
      </section>
    );
  }

  if (!hasSearched) {
    return null;
  }

  if (results.length === 0) {
    return (
      <section className="card results-card no-results-card">
        <div className="no-results-content">
          <SearchX size={44} className="no-results-icon" />
          <h3>No matching documents found</h3>
          <p>
            No documents in the collection contain <strong>"{query}"</strong>.
          </p>
          <div className="fuzzy-callout">
            <p>Did you make a typo? Try checking with Dynamic Programming Fuzzy Search!</p>
            <button
              type="button"
              className="btn btn-secondary btn-sm"
              onClick={() => onSwitchToFuzzy(query)}
            >
              Check Fuzzy Match for "{query}"
            </button>
          </div>
        </div>
      </section>
    );
  }

  const totalOccurrences = results.reduce((acc, curr) => acc + curr.occurrences, 0);
  const algorithmName = results[0]?.algorithmUsed || 'Inverted Index';
  const sortMethod = results[0]?.sortMethodUsed || 'Randomized QuickSort';

  return (
    <section className="card results-card">
      <div className="results-header">
        <div>
          <h2>Search Results</h2>
          <p className="results-summary">
            Found <strong>{results.length}</strong> document{results.length > 1 ? 's' : ''} with <strong>{totalOccurrences}</strong> total matches for <em>"{query}"</em>
          </p>
        </div>
        <div className="results-algo-meta">
          <span className="badge-ranking">Algorithm: <strong>{algorithmName}</strong></span>
          <span className="badge-ranking">Sorted by: <strong>{sortMethod}</strong></span>
        </div>
      </div>

      <div className="results-list">
        {results.map((item, index) => (
          <div key={item.fileName + index} className="result-item">
            <div className="result-rank">
              <span className={`rank-badge rank-${index + 1}`}>
                #{index + 1}
              </span>
            </div>

            <div className="result-main">
              <div className="result-title-row">
                <div className="result-file-info">
                  <FileText size={18} className="file-icon" />
                  <h3 className="result-filename">{item.fileName}</h3>
                </div>
                <div className="metric-pills">
                  <div className="occurrence-pill">
                    <Award size={14} />
                    <span>Occurrences: <strong>{item.occurrences}</strong></span>
                  </div>
                  {item.relevanceScore > 0 && (
                    <div className="relevance-pill">
                      <Percent size={13} />
                      <span>Relevance: <strong>{item.relevanceScore}%</strong></span>
                    </div>
                  )}
                </div>
              </div>

              {item.snippet && (
                <p className="result-snippet">
                  "{item.snippet}"
                </p>
              )}
            </div>

            <div className="result-actions">
              {item.documentId && (
                <button
                  type="button"
                  className="btn btn-sm btn-outline"
                  onClick={() => onViewDocument(item.documentId)}
                  title="View complete document content"
                >
                  <Eye size={14} />
                  <span>View</span>
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}
