import React, { useState } from 'react';
import { Microscope, Play, Layers, Code, CheckCircle, XCircle, Info } from 'lucide-react';
import { api } from '../services/api';

export default function AdvancedAnalysisView({ documents }) {
  const [selectedDocId, setSelectedDocId] = useState(documents[0]?.id || '');
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [subQuery, setSubQuery] = useState('');

  const handleAnalyze = async (e) => {
    e.preventDefault();
    if (!selectedDocId) return;

    try {
      setLoading(true);
      setError('');
      const data = await api.getAdvancedAnalysis(selectedDocId);
      setAnalysis(data);
    } catch (err) {
      setError(err.message || 'Advanced analysis failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="tab-pane">
      <div className="card">
        <div className="card-header">
          <div className="header-left">
            <Microscope className="header-icon" size={20} />
            <h2>Advanced Substring Analysis (Suffix Array, Kasai LCP & Suffix Automaton)</h2>
          </div>
          <span className="badge-ranking">Module 2: Advanced Suffix Structures</span>
        </div>
        <p className="card-description">
          Demonstrates advanced string indexing data structures: <strong>Suffix Array</strong>, <strong>Kasai's linear-time LCP</strong> algorithm to detect the Longest Repeated Substring, and a minimal <strong>Suffix Automaton (DAWG)</strong> to count distinct substrings.
        </p>

        <form onSubmit={handleAnalyze} className="advanced-form">
          <div className="control-group-inline">
            <label>Select Document for Structural Analysis:</label>
            <select
              className="select-input"
              value={selectedDocId}
              onChange={(e) => setSelectedDocId(e.target.value)}
            >
              {documents.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.fileName} ({d.fileSize} bytes)
                </option>
              ))}
            </select>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              <Play size={16} />
              <span>{loading ? 'Analyzing...' : 'Run Advanced Analysis'}</span>
            </button>
          </div>
        </form>

        {error && (
          <div className="alert alert-error">
            <span>{error}</span>
          </div>
        )}

        {analysis && (
          <div className="advanced-results-section">
            {/* Top Stat Cards */}
            <div className="dsa-metrics-grid">
              <div className="metric-box">
                <span className="metric-val">{analysis.textLength}</span>
                <span className="metric-label">Document Length (N)</span>
              </div>
              <div className="metric-box">
                <span className="metric-val">{analysis.distinctSubstringsCount.toLocaleString()}</span>
                <span className="metric-label">Distinct Substrings (Suffix Automaton)</span>
              </div>
              <div className="metric-box">
                <span className="metric-val">{analysis.lrsLength} chars</span>
                <span className="metric-label">Longest Repeated Substring Length</span>
              </div>
              <div className="metric-box">
                <span className="metric-val">{(analysis.analysisTimeNanos / 1000).toFixed(1)} µs</span>
                <span className="metric-label">Total Execution Time</span>
              </div>
            </div>

            {/* Kasai LCP Longest Repeated Substring Feature */}
            <div className="feature-highlight-card">
              <h4>
                <Code size={18} /> Kasai LCP: Longest Repeated Substring
              </h4>
              <div className="lrs-display-box">
                {analysis.longestRepeatedSubstring ? (
                  <>
                    <p className="lrs-text">"{analysis.longestRepeatedSubstring}"</p>
                    <span className="lrs-meta">
                      Length: <strong>{analysis.lrsLength}</strong> characters • Found in linear time <code>O(N)</code> using Kasai's algorithm.
                    </span>
                  </>
                ) : (
                  <p className="empty-subtext">No repeated substrings detected in this text.</p>
                )}
              </div>
            </div>

            {/* Suffix Array & LCP Sample Table */}
            <div className="suffix-array-preview">
              <h4>Suffix Array & LCP Sample (First {analysis.suffixArraySample.length} Entries)</h4>
              <div className="table-responsive">
                <table className="doc-table compact-table">
                  <thead>
                    <tr>
                      <th style={{ width: '15%' }}>Rank (i)</th>
                      <th style={{ width: '65%' }}>Suffix Array (SA[i] → Suffix Preview)</th>
                      <th style={{ width: '20%' }}>LCP[i]</th>
                    </tr>
                  </thead>
                  <tbody>
                    {analysis.suffixArraySample.map((entry, idx) => (
                      <tr key={idx}>
                        <td><code>#{idx}</code></td>
                        <td><span className="sa-entry">{entry}</span></td>
                        <td>
                          <span className="lcp-pill">LCP = {analysis.lcpArraySample[idx] || 0}</span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            <div className="viva-note" style={{ marginTop: '1.25rem' }}>
              <Info size={16} />
              <span>
                <strong>Viva Theoretical Highlight:</strong> Kasai's algorithm computes the LCP array in strictly <code>O(N)</code> time by observing that the LCP of successive suffixes can decrease by at most 1. Suffix Automaton represents all <code>O(N²)</code> substrings using at most <code>2N-1</code> states in <code>O(N)</code> time!
              </span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
