import React, { useState } from 'react';
import { X, Cpu, Search, CheckCircle, Database, Hash, ArrowRight } from 'lucide-react';

export default function DsaVisualizerModal({ stats, onClose }) {
  const [filterTerm, setFilterTerm] = useState('');

  if (!stats) return null;

  const rawIndex = stats.rawIndex || {};
  const terms = Object.keys(rawIndex).sort();

  const filteredTerms = filterTerm
    ? terms.filter((t) => t.toLowerCase().includes(filterTerm.toLowerCase()))
    : terms;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-container dsa-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div className="modal-title">
            <Cpu size={22} className="modal-dsa-icon" />
            <div>
              <h3>Inverted Index DSA Inspector</h3>
              <p className="modal-subtitle">
                Live visualization of in-memory <code>HashMap&lt;String, HashMap&lt;String, Integer&gt;&gt;</code>
              </p>
            </div>
          </div>
          <button className="btn-icon modal-close" onClick={onClose} title="Close">
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          {/* Key Metrics Banner */}
          <div className="dsa-metrics-grid">
            <div className="metric-box">
              <span className="metric-val">{stats.totalUniqueWordsIndexed || 0}</span>
              <span className="metric-label">Unique Terms (Outer Map Keys)</span>
            </div>
            <div className="metric-box">
              <span className="metric-val">{stats.totalIndexedDocuments || 0}</span>
              <span className="metric-label">Indexed Documents</span>
            </div>
            <div className="metric-box">
              <span className="metric-val">O(1)</span>
              <span className="metric-label">Avg. Term Lookup Time</span>
            </div>
            <div className="metric-box">
              <span className="metric-val">O(K log K)</span>
              <span className="metric-label">Result Ranking via Comparator</span>
            </div>
          </div>

          {/* DSA Architecture Explainer */}
          <div className="dsa-explainer-card">
            <h4><Hash size={16} /> Data Structure Architecture</h4>
            <div className="dsa-flow-diagram">
              <div className="flow-step">
                <strong>WORD (Key)</strong>
                <span>String</span>
              </div>
              <ArrowRight size={18} className="flow-arrow" />
              <div className="flow-step">
                <strong>DOCUMENT (Inner Key)</strong>
                <span>fileName</span>
              </div>
              <ArrowRight size={18} className="flow-arrow" />
              <div className="flow-step">
                <strong>OCCURRENCES (Value)</strong>
                <span>Integer (Frequency)</span>
              </div>
            </div>
          </div>

          {/* Live HashMap Contents Table */}
          <div className="dsa-index-viewer">
            <div className="dsa-filter-bar">
              <h4>Live In-Memory Postings ({filteredTerms.length} terms)</h4>
              <div className="mini-filter">
                <Search size={14} />
                <input
                  type="text"
                  placeholder="Filter indexed terms..."
                  value={filterTerm}
                  onChange={(e) => setFilterTerm(e.target.value)}
                />
              </div>
            </div>

            {filteredTerms.length === 0 ? (
              <p className="no-terms">No terms match your filter or index is empty.</p>
            ) : (
              <div className="dsa-terms-table-wrapper">
                <table className="dsa-table">
                  <thead>
                    <tr>
                      <th style={{ width: '30%' }}>Word Token (Key)</th>
                      <th style={{ width: '70%' }}>Document Postings List <code>Map&lt;fileName, count&gt;</code></th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredTerms.map((term) => {
                      const postings = rawIndex[term] || {};
                      return (
                        <tr key={term}>
                          <td>
                            <span className="term-badge">{term}</span>
                          </td>
                          <td>
                            <div className="postings-container">
                              {Object.entries(postings).map(([docName, count]) => (
                                <span key={docName} className="posting-chip">
                                  <span className="posting-doc">{docName}</span>
                                  <span className="posting-arrow">→</span>
                                  <span className="posting-count">{count}</span>
                                </span>
                              ))}
                            </div>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>
            Close Inspector
          </button>
        </div>
      </div>
    </div>
  );
}
