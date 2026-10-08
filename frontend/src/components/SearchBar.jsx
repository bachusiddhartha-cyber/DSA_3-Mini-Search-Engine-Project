import React, { useState } from 'react';
import { Search, X, Sparkles, SlidersHorizontal, ArrowUpDown } from 'lucide-react';

const SUGGESTED_KEYWORDS = ['java', 'python', 'algorithm', 'data', 'structures', 'stack', 'queue', 'object oriented'];

export default function SearchBar({ onSearch, loading }) {
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('INVERTED_INDEX');
  const [sortMethod, setSortMethod] = useState('RANDOMIZED_QUICKSORT');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (query.trim()) {
      onSearch(query.trim(), mode, sortMethod);
    }
  };

  const handleClear = () => {
    setQuery('');
    onSearch('', mode, sortMethod);
  };

  const handleChipClick = (keyword) => {
    setQuery(keyword);
    onSearch(keyword, mode, sortMethod);
  };

  const handleModeChange = (newMode) => {
    setMode(newMode);
    if (query.trim()) {
      onSearch(query.trim(), newMode, sortMethod);
    }
  };

  const handleSortChange = (newSort) => {
    setSortMethod(newSort);
    if (query.trim()) {
      onSearch(query.trim(), mode, newSort);
    }
  };

  return (
    <section className="card search-card">
      <form onSubmit={handleSubmit} className="search-form-full">
        <div className="search-input-wrapper">
          <Search className="search-icon" size={22} />
          <input
            type="text"
            className="search-input"
            placeholder="Search documents by word or phrase (e.g. java, python, object oriented)..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            disabled={loading}
          />
          {query && (
            <button 
              type="button" 
              className="btn-icon clear-btn" 
              onClick={handleClear}
              title="Clear search"
            >
              <X size={18} />
            </button>
          )}
          <button 
            type="submit" 
            className="btn btn-primary search-submit-btn" 
            disabled={!query.trim() || loading}
          >
            {loading ? 'Searching...' : 'Search'}
          </button>
        </div>

        {/* Algorithm & Ranking Controls Bar */}
        <div className="search-controls-bar">
          <div className="control-item">
            <span className="control-label">
              <SlidersHorizontal size={14} /> Search Algorithm:
            </span>
            <div className="algo-radio-group">
              {[
                { id: 'INVERTED_INDEX', label: 'Inverted Index (O(1))' },
                { id: 'KMP', label: 'KMP' },
                { id: 'Z_ALGORITHM', label: 'Z Algorithm' },
                { id: 'RABIN_KARP', label: 'Rabin-Karp' },
                { id: 'NAIVE', label: 'Naive' },
                { id: 'SUFFIX_ARRAY', label: 'Suffix Array' }
              ].map((opt) => (
                <button
                  key={opt.id}
                  type="button"
                  className={`algo-btn ${mode === opt.id ? 'active' : ''}`}
                  onClick={() => handleModeChange(opt.id)}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>

          <div className="control-item">
            <span className="control-label">
              <ArrowUpDown size={14} /> Ranking:
            </span>
            <div className="algo-radio-group">
              {[
                { id: 'RANDOMIZED_QUICKSORT', label: 'Randomized QuickSort' },
                { id: 'STANDARD_SORT', label: 'Standard Sort' }
              ].map((opt) => (
                <button
                  key={opt.id}
                  type="button"
                  className={`algo-btn ${sortMethod === opt.id ? 'active' : ''}`}
                  onClick={() => handleSortChange(opt.id)}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>
        </div>
      </form>

      {/* Suggested Quick Keywords */}
      <div className="search-suggestions">
        <span className="suggestion-label">
          <Sparkles size={14} /> Try searching:
        </span>
        <div className="suggestion-chips">
          {SUGGESTED_KEYWORDS.map((word) => (
            <button
              key={word}
              type="button"
              className={`chip ${query.toLowerCase() === word.toLowerCase() ? 'active' : ''}`}
              onClick={() => handleChipClick(word)}
            >
              {word}
            </button>
          ))}
        </div>
      </div>
    </section>
  );
}
