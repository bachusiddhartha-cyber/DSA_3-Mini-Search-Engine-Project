import React, { useState } from 'react';
import { GitCompare, PieChart, Layers, Info, ArrowRight } from 'lucide-react';
import { api } from '../services/api';

export default function DocumentSimilarityView({ documents }) {
  const [doc1Id, setDoc1Id] = useState(documents[0]?.id || '');
  const [doc2Id, setDoc2Id] = useState(documents[1]?.id || documents[0]?.id || '');
  const [similarity, setSimilarity] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleCompute = async (e) => {
    e.preventDefault();
    if (!doc1Id || !doc2Id) {
      setError('Please select two documents to compare.');
      return;
    }

    try {
      setLoading(true);
      setError('');
      const data = await api.calculateSimilarity(doc1Id, doc2Id);
      setSimilarity(data);
    } catch (err) {
      setError(err.message || 'Similarity calculation failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="tab-pane">
      <div className="card">
        <div className="card-header">
          <div className="header-left">
            <GitCompare className="header-icon" size={20} />
            <h2>Document Similarity (Vector Space Model & Cosine Metric)</h2>
          </div>
          <span className="badge-ranking">Module 1: Information Retrieval</span>
        </div>
        <p className="card-description">
          Evaluates content similarity between two documents by mapping word frequencies into an N-dimensional vector space and calculating the cosine of the angle between them.
        </p>

        <form onSubmit={handleCompute} className="similarity-form">
          <div className="similarity-select-grid">
            <div className="control-group">
              <label>Document 1:</label>
              <select
                className="select-input"
                value={doc1Id}
                onChange={(e) => setDoc1Id(e.target.value)}
              >
                {documents.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.fileName}
                  </option>
                ))}
              </select>
            </div>

            <div className="vs-badge">VS</div>

            <div className="control-group">
              <label>Document 2:</label>
              <select
                className="select-input"
                value={doc2Id}
                onChange={(e) => setDoc2Id(e.target.value)}
              >
                {documents.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.fileName}
                  </option>
                ))}
              </select>
            </div>

            <button type="submit" className="btn btn-primary similarity-submit-btn" disabled={loading}>
              <PieChart size={16} />
              <span>{loading ? 'Computing...' : 'Calculate Similarity'}</span>
            </button>
          </div>
        </form>

        {error && (
          <div className="alert alert-error">
            <span>{error}</span>
          </div>
        )}

        {similarity && (
          <div className="similarity-results-card">
            <div className="similarity-score-hero">
              <div className="score-circle">
                <span className="score-number">{similarity.similarityPercentage}%</span>
                <span className="score-label">Cosine Similarity</span>
              </div>

              <div className="similarity-summary-text">
                <h3>{similarity.doc1Name} ⟷ {similarity.doc2Name}</h3>
                <p>
                  {similarity.similarityPercentage > 70
                    ? 'High semantic and lexical overlap across documents.'
                    : similarity.similarityPercentage > 30
                    ? 'Moderate shared vocabulary.'
                    : 'Low lexical overlap; distinct topics.'}
                </p>
                <div className="vector-stats-row">
                  <span><strong>{similarity.doc1UniqueWords}</strong> words in Doc 1</span>
                  <span className="dot">•</span>
                  <span><strong>{similarity.doc2UniqueWords}</strong> words in Doc 2</span>
                  <span className="dot">•</span>
                  <span><strong>{similarity.commonWords.length}</strong> shared terms</span>
                </div>
              </div>
            </div>

            <div className="shared-vocabulary-section">
              <h4>Shared Vocabulary Terms ({similarity.commonWords.length}):</h4>
              {similarity.commonWords.length === 0 ? (
                <p className="empty-subtext">No overlapping terms between these two files.</p>
              ) : (
                <div className="shared-chips">
                  {similarity.commonWords.map((word) => (
                    <span key={word} className="shared-chip">
                      {word}
                    </span>
                  ))}
                </div>
              )}
            </div>

            <div className="viva-note" style={{ marginTop: '1.25rem' }}>
              <Info size={16} />
              <span>
                <strong>Mathematical Formulation:</strong> <code>Cosine Similarity = (A · B) / (||A|| * ||B||)</code>, where <code>A · B = ∑ TF(w, d1) * TF(w, d2)</code> and <code>||A|| = √(∑ TF(w, d1)²)</code>. Independent of document length variations!
              </span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
