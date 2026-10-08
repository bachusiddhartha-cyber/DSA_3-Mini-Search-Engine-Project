import React, { useState } from 'react';
import { Play, Zap, Clock, Hash, CheckCircle, Info } from 'lucide-react';
import { api } from '../services/api';

const ALGORITHM_THEORY = {
  'Naive Search': {
    time: 'O(n * m)',
    space: 'O(1)',
    desc: 'Sliding window character-by-character scan without preprocessing.'
  },
  'KMP Search': {
    time: 'O(n + m)',
    space: 'O(m)',
    desc: 'Uses precomputed Longest Prefix Suffix (LPS) failure table to skip redundant checks.'
  },
  'Z Algorithm': {
    time: 'O(n + m)',
    space: 'O(n + m)',
    desc: 'Constructs Z-array on pattern + "$" + text using [L, R] active matching interval.'
  },
  'Rabin-Karp': {
    time: 'O(n + m) avg',
    space: 'O(1)',
    desc: 'Polynomial rolling hash modulo 10^9+7 with character-by-character collision verification.'
  }
};

export default function AlgorithmComparisonView({ documents }) {
  const [selectedDocId, setSelectedDocId] = useState(documents[0]?.id || '');
  const [pattern, setPattern] = useState('data');
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleRun = async (e) => {
    e.preventDefault();
    if (!selectedDocId || !pattern.trim()) {
      setError('Please select a document and enter a pattern.');
      return;
    }

    try {
      setLoading(true);
      setError('');
      const data = await api.compareAlgorithms(selectedDocId, pattern.trim());
      setReport(data);
    } catch (err) {
      setError(err.message || 'Comparison failed.');
    } finally {
      setLoading(false);
    }
  };

  const maxTime = report?.results?.reduce((max, r) => Math.max(max, r.executionTimeNanos), 1) || 1;

  return (
    <div className="tab-pane">
      <div className="card">
        <div className="card-header">
          <div className="header-left">
            <Zap className="header-icon" size={20} />
            <h2>String Algorithm Benchmark & Comparison</h2>
          </div>
          <span className="badge-ranking">Module 2: String Algorithms</span>
        </div>
        <p className="card-description">
          Runs <strong>Naive</strong>, <strong>KMP</strong>, <strong>Z Algorithm</strong>, and <strong>Rabin-Karp</strong> side-by-side on real document content with nanosecond precision (<code>System.nanoTime()</code>) and character comparison tracking.
        </p>

        <form onSubmit={handleRun} className="benchmark-form">
          <div className="benchmark-controls">
            <div className="control-group">
              <label>Select Target Document:</label>
              <select
                className="select-input"
                value={selectedDocId}
                onChange={(e) => setSelectedDocId(e.target.value)}
              >
                {documents.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.fileName} ({(d.fileSize / 1024).toFixed(1)} KB)
                  </option>
                ))}
              </select>
            </div>

            <div className="control-group">
              <label>Target Pattern:</label>
              <input
                type="text"
                className="text-input"
                placeholder="e.g. data, java, algorithm"
                value={pattern}
                onChange={(e) => setPattern(e.target.value)}
              />
            </div>

            <button type="submit" className="btn btn-primary run-benchmark-btn" disabled={loading}>
              <Play size={16} />
              <span>{loading ? 'Running Benchmark...' : 'Run Benchmark'}</span>
            </button>
          </div>

          <div className="quick-patterns">
            <span className="file-hint">Quick test patterns:</span>
            {['data', 'java', 'algorithm', 'structure', 'learning', 'programming'].map((p) => (
              <button
                key={p}
                type="button"
                className="chip"
                onClick={() => setPattern(p)}
              >
                {p}
              </button>
            ))}
          </div>
        </form>

        {error && (
          <div className="alert alert-error">
            <span>{error}</span>
          </div>
        )}

        {report && (
          <div className="benchmark-results">
            <div className="benchmark-meta-banner">
              <div>
                <strong>Document:</strong> {report.documentName} ({report.documentLength} chars)
                <span className="meta-sep">•</span>
                <strong>Pattern:</strong> "{report.pattern}" ({report.patternLength} chars)
              </div>
              <div className="fastest-pill">
                <CheckCircle size={15} /> Fastest: <strong>{report.fastestAlgorithm}</strong>
              </div>
            </div>

            <div className="table-responsive">
              <table className="doc-table benchmark-table">
                <thead>
                  <tr>
                    <th>Algorithm</th>
                    <th>Matches Found</th>
                    <th>Comparisons</th>
                    <th>Execution Time</th>
                    <th>Speed Visualizer</th>
                    <th>Theoretical Complexity</th>
                  </tr>
                </thead>
                <tbody>
                  {report.results.map((res) => {
                    const isFastest = res.algorithmName === report.fastestAlgorithm;
                    const theory = ALGORITHM_THEORY[res.algorithmName] || {};
                    const pct = Math.max(12, Math.round((res.executionTimeNanos / maxTime) * 100));

                    return (
                      <tr key={res.algorithmName} className={isFastest ? 'fastest-row' : ''}>
                        <td>
                          <strong>{res.algorithmName}</strong>
                          {isFastest && <span className="winner-tag">Fastest</span>}
                        </td>
                        <td>
                          <span className="match-pill">{res.matchCount}</span>
                        </td>
                        <td>
                          <span className="comp-value">{res.characterComparisons.toLocaleString()}</span>
                        </td>
                        <td>
                          <span className="time-value">
                            {res.executionTimeMicros < 1000
                              ? `${res.executionTimeMicros.toFixed(2)} µs`
                              : `${(res.executionTimeMicros / 1000).toFixed(2)} ms`}
                          </span>
                          <span className="time-sub">({res.executionTimeNanos.toLocaleString()} ns)</span>
                        </td>
                        <td style={{ width: '25%' }}>
                          <div className="speed-bar-container">
                            <div
                              className={`speed-bar ${isFastest ? 'speed-bar-winner' : ''}`}
                              style={{ width: `${pct}%` }}
                            ></div>
                          </div>
                        </td>
                        <td>
                          <code>{theory.time}</code>
                          <span className="theory-desc">{theory.desc}</span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>

            <div className="viva-note">
              <Info size={16} />
              <span>
                <strong>Viva Talking Point:</strong> KMP and Z Algorithm guarantee linear worst-case time <code>O(n+m)</code> without backtracking. Rabin-Karp uses rolling polynomial hashes to evaluate windows in <code>O(1)</code> average time, verifying characters only upon a hash match.
              </span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
