import React, { useState } from 'react';
import { Network, Search, Layers, FileText, Info, Sparkles } from 'lucide-react';
import { api } from '../services/api';

export default function MultiPatternSearchView({ onViewDocument }) {
  const [inputPatterns, setInputPatterns] = useState('java, python, stack, queue');
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    const list = inputPatterns
      .split(',')
      .map((s) => s.trim())
      .filter((s) => s.length > 0);

    if (list.length === 0) {
      setError('Please provide at least one pattern keyword.');
      return;
    }

    try {
      setLoading(true);
      setError('');
      const data = await api.multiPatternSearch(list);
      setResults(data);
    } catch (err) {
      setError(err.message || 'Multi-pattern search failed.');
    } finally {
      setLoading(false);
    }
  };

  const handlePreset = (preset) => {
    setInputPatterns(preset);
  };

  return (
    <div className="tab-pane">
      <div className="card">
        <div className="card-header">
          <div className="header-left">
            <Network className="header-icon" size={20} />
            <h2>Multi-Keyword Search (Aho-Corasick Automaton)</h2>
          </div>
          <span className="badge-ranking">Module 2: Aho-Corasick Automaton</span>
        </div>
        <p className="card-description">
          Searches for multiple keywords simultaneously across all documents in a <strong>single pass</strong> using a Trie augmented with failure and dictionary links in <code>O(n + matches)</code> time.
        </p>

        <form onSubmit={handleSearch} className="multi-search-form">
          <div className="multi-input-container">
            <label>Enter Comma-Separated Keywords / Patterns:</label>
            <div className="search-input-wrapper">
              <input
                type="text"
                className="search-input"
                placeholder="e.g. java, python, stack, queue"
                value={inputPatterns}
                onChange={(e) => setInputPatterns(e.target.value)}
              />
              <button type="submit" className="btn btn-primary search-submit-btn" disabled={loading}>
                <Search size={16} />
                <span>{loading ? 'Scanning...' : 'Scan Documents'}</span>
              </button>
            </div>
          </div>

          <div className="search-suggestions">
            <span className="suggestion-label">
              <Sparkles size={14} /> Quick Presets:
            </span>
            <div className="suggestion-chips">
              {[
                'java, stack, queue',
                'python, machine learning, data',
                'searching, sorting, algorithms',
                'object oriented, programming'
              ].map((p) => (
                <button
                  key={p}
                  type="button"
                  className="chip"
                  onClick={() => handlePreset(p)}
                >
                  {p}
                </button>
              ))}
            </div>
          </div>
        </form>

        {error && (
          <div className="alert alert-error">
            <span>{error}</span>
          </div>
        )}

        {results && (
          <div className="multi-results-section">
            <div className="results-header">
              <div>
                <h3>Matching Documents ({results.length})</h3>
                <p className="results-summary">
                  Scanned across collection simultaneously via Aho-Corasick Trie Automaton.
                </p>
              </div>
            </div>

            {results.length === 0 ? (
              <div className="no-results-card">
                <p>None of the uploaded documents contain any of the specified keywords.</p>
              </div>
            ) : (
              <div className="multi-results-grid">
                {results.map((item) => (
                  <div key={item.documentId} className="multi-doc-card">
                    <div className="multi-card-header">
                      <div className="multi-file-title">
                        <FileText size={18} className="file-icon" />
                        <strong>{item.fileName}</strong>
                      </div>
                      <span className="total-matches-pill">
                        Total: <strong>{item.totalMatches}</strong>
                      </span>
                    </div>

                    <div className="patterns-breakdown">
                      {Object.entries(item.patternCounts).map(([pattern, count]) => (
                        <div
                          key={pattern}
                          className={`pattern-occurrence-chip ${count > 0 ? 'has-hits' : 'zero-hits'}`}
                        >
                          <span className="pat-name">{pattern}</span>
                          <span className="pat-arrow">→</span>
                          <span className="pat-count">{count}</span>
                        </div>
                      ))}
                    </div>

                    <div className="multi-card-footer">
                      <span className="time-sub">
                        Scanned in {(item.executionTimeNanos / 1000).toFixed(1)} µs
                      </span>
                      <button
                        type="button"
                        className="btn btn-sm btn-outline"
                        onClick={() => onViewDocument(item.documentId)}
                      >
                        View Content
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}

            <div className="viva-note" style={{ marginTop: '1.25rem' }}>
              <Info size={16} />
              <span>
                <strong>Viva Insight:</strong> Unlike running KMP N times for N patterns (which costs <code>O(N * TextLength)</code>), Aho-Corasick constructs a Trie with failure transitions to match all N patterns in a single pass of <code>O(TextLength + TotalMatches)</code>!
              </span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
