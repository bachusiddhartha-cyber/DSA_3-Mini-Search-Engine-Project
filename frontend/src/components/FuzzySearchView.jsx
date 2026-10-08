import React, { useState } from 'react';
import { HelpCircle, Search, ArrowRight, Check, Sparkles, Info, Eye } from 'lucide-react';
import { api } from '../services/api';

export default function FuzzySearchView({ onViewDocument }) {
  const [query, setQuery] = useState('pyhton');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    try {
      setLoading(true);
      setError('');
      const data = await api.fuzzySearch(query.trim());
      setResult(data);
    } catch (err) {
      setError(err.message || 'Fuzzy search failed.');
    } finally {
      setLoading(false);
    }
  };

  const handleChip = (typo) => {
    setQuery(typo);
  };

  return (
    <div className="tab-pane">
      <div className="card">
        <div className="card-header">
          <div className="header-left">
            <HelpCircle className="header-icon" size={20} />
            <h2>Fuzzy Search & Spellcheck (DP Edit Distance)</h2>
          </div>
          <span className="badge-ranking">Module 3: Dynamic Programming</span>
        </div>
        <p className="card-description">
          Uses <strong>Levenshtein Distance</strong> (Wagner-Fischer 2D DP) and <strong>Damerau-Levenshtein Distance</strong> (supporting adjacent transpositions) to suggest correct terms when typos occur.
        </p>

        <form onSubmit={handleSearch} className="search-form">
          <div className="search-input-wrapper">
            <Search className="search-icon" size={20} />
            <input
              type="text"
              className="search-input"
              placeholder="Type a word with a typo (e.g. pyhton, jav, algoritm)..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />
          </div>
          <button type="submit" className="btn btn-primary search-submit-btn" disabled={loading || !query.trim()}>
            {loading ? 'Analyzing DP...' : 'Fuzzy Match'}
          </button>
        </form>

        <div className="search-suggestions">
          <span className="suggestion-label">
            <Sparkles size={14} /> Common Typos to Test:
          </span>
          <div className="suggestion-chips">
            {['pyhton', 'jav', 'algoritm', 'stak', 'queu', 'structur'].map((t) => (
              <button
                key={t}
                type="button"
                className={`chip ${query === t ? 'active' : ''}`}
                onClick={() => handleChip(t)}
              >
                {t}
              </button>
            ))}
          </div>
        </div>

        {error && (
          <div className="alert alert-error">
            <span>{error}</span>
          </div>
        )}

        {result && (
          <div className="fuzzy-results-box">
            {result.suggestedTerm ? (
              <>
                <div className="suggestion-card">
                  <div className="suggestion-header">
                    <span className="did-you-mean-label">Did you mean:</span>
                    <strong className="suggested-word">{result.suggestedTerm}</strong>
                    <span className="similarity-tag">{result.similarityScore}% Match</span>
                  </div>

                  <div className="dp-metrics-row">
                    <div className="dp-metric-cell">
                      <span className="dp-metric-title">Levenshtein Distance:</span>
                      <strong className="dp-metric-val">{result.levenshteinDistance}</strong>
                      <span className="dp-metric-sub">(Insertions, Deletions, Substitutions)</span>
                    </div>

                    <div className="dp-metric-cell highlight-damerau">
                      <span className="dp-metric-title">Damerau-Levenshtein:</span>
                      <strong className="dp-metric-val">{result.damerauDistance}</strong>
                      <span className="dp-metric-sub">(Includes Adjacent Transpositions)</span>
                    </div>
                  </div>

                  {result.damerauDistance < result.levenshteinDistance && (
                    <div className="transposition-alert">
                      <Check size={16} />
                      <span>
                        <strong>Transposition Detected!</strong> Damerau-Levenshtein corrected the adjacent letter swap in 1 edit step instead of 2.
                      </span>
                    </div>
                  )}
                </div>

                <div className="matching-docs-section">
                  <h4>Documents Containing "{result.suggestedTerm}" ({result.searchResults.length}):</h4>
                  {result.searchResults.length === 0 ? (
                    <p className="empty-subtext">No documents indexed with this word.</p>
                  ) : (
                    <div className="results-list">
                      {result.searchResults.map((item, idx) => (
                        <div key={item.fileName} className="result-item">
                          <span className="rank-badge rank-1">#{idx + 1}</span>
                          <div className="result-main">
                            <div className="result-title-row">
                              <strong>{item.fileName}</strong>
                              <span className="occurrence-pill">
                                Occurrences: <strong>{item.occurrences}</strong>
                              </span>
                            </div>
                            {item.snippet && <p className="result-snippet">"{item.snippet}"</p>}
                          </div>
                          {item.documentId && (
                            <button
                              type="button"
                              className="btn btn-sm btn-outline"
                              onClick={() => onViewDocument(item.documentId)}
                            >
                              <Eye size={14} /> View
                            </button>
                          )}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </>
            ) : (
              <div className="no-results-card">
                <p>No close vocabulary words found for query "{result.originalQuery}".</p>
              </div>
            )}

            <div className="viva-note" style={{ marginTop: '1.25rem' }}>
              <Info size={16} />
              <span>
                <strong>Viva Talking Point:</strong> Standard Levenshtein treats adjacent swap 'pyhton' as 2 operations (delete 'h' + insert 'h', or 2 substitutions). Damerau-Levenshtein adds a fourth recurrence clause: <code>dp[i][j] = min(..., dp[i-2][j-2] + 1)</code>, resolving transpositions in just 1 edit step!
              </span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
