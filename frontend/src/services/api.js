/**
 * API Service for communicating with the Spring Boot Backend.
 * Supports standard document CRUD + Advanced DSA algorithm endpoints.
 */

const BASE_URL = '/api';

export const api = {
  // Search documents using specified algorithm mode and sorting method
  async search(query, mode = 'INVERTED_INDEX', sort = 'RANDOMIZED_QUICKSORT') {
    const params = new URLSearchParams({
      query: query || '',
      mode: mode || 'INVERTED_INDEX',
      sort: sort || 'RANDOMIZED_QUICKSORT',
    });
    const res = await fetch(`${BASE_URL}/search?${params.toString()}`);
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Search request failed');
    }
    return res.json();
  },

  // Side-by-side algorithm comparison benchmark (Naive, KMP, Z, Rabin-Karp)
  async compareAlgorithms(documentId, pattern) {
    const res = await fetch(`${BASE_URL}/algorithms/compare`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ documentId, pattern }),
    });
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'Algorithm comparison failed');
    }
    return data;
  },

  // Aho-Corasick Multi-Pattern Search
  async multiPatternSearch(patterns) {
    const res = await fetch(`${BASE_URL}/algorithms/multi-search`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ patterns }),
    });
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'Multi-pattern search failed');
    }
    return data;
  },

  // Fuzzy Search with Levenshtein & Damerau-Levenshtein suggestions
  async fuzzySearch(query) {
    const res = await fetch(`${BASE_URL}/algorithms/fuzzy-search?query=${encodeURIComponent(query)}`);
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'Fuzzy search failed');
    }
    return data;
  },

  // Document Cosine Similarity between two documents
  async calculateSimilarity(docId1, docId2) {
    const res = await fetch(`${BASE_URL}/algorithms/similarity`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ docId1, docId2 }),
    });
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'Similarity calculation failed');
    }
    return data;
  },

  // Advanced Substring Analysis (Suffix Array, Kasai LCP, Suffix Automaton)
  async getAdvancedAnalysis(documentId) {
    const res = await fetch(`${BASE_URL}/algorithms/advanced-analysis/${documentId}`);
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'Advanced analysis failed');
    }
    return data;
  },

  // Upload a text document (.txt)
  async uploadDocument(file) {
    const formData = new FormData();
    formData.append('file', file);

    const res = await fetch(`${BASE_URL}/documents/upload`, {
      method: 'POST',
      body: formData,
    });

    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'File upload failed');
    }
    return data;
  },

  // Get all documents metadata from MongoDB
  async getAllDocuments() {
    const res = await fetch(`${BASE_URL}/documents`);
    if (!res.ok) {
      throw new Error('Failed to fetch documents');
    }
    return res.json();
  },

  // Get a single document by ID
  async getDocumentById(id) {
    const res = await fetch(`${BASE_URL}/documents/${id}`);
    if (!res.ok) {
      throw new Error('Failed to retrieve document');
    }
    return res.json();
  },

  // Delete a document from MongoDB and remove from Inverted Index
  async deleteDocument(id) {
    const res = await fetch(`${BASE_URL}/documents/${id}`, {
      method: 'DELETE',
    });
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(data.message || 'Failed to delete document');
    }
    return data;
  },

  // Download document URL
  getDownloadUrl(id) {
    return `${BASE_URL}/documents/${id}/download`;
  },

  // Fetch live index stats & HashMap
  async getIndexStats() {
    const res = await fetch(`${BASE_URL}/index-stats`);
    if (!res.ok) {
      throw new Error('Failed to fetch index stats');
    }
    return res.json();
  },
};
