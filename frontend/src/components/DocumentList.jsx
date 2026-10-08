import React, { useState } from 'react';
import { FileText, Trash2, Eye, Download, HardDrive, RefreshCw } from 'lucide-react';
import { api } from '../services/api';

export default function DocumentList({ documents, loading, onRefresh, onViewDocument, onDeleteSuccess }) {
  const [deletingId, setDeletingId] = useState(null);
  const [deleteError, setDeleteError] = useState('');

  const handleDelete = async (doc) => {
    const confirmed = window.confirm(`Are you sure you want to delete "${doc.fileName}"?\n\nThis will remove it from MongoDB and purge all its words from the Inverted Index.`);
    if (!confirmed) return;

    try {
      setDeletingId(doc.id);
      setDeleteError('');
      await api.deleteDocument(doc.id);
      if (onDeleteSuccess) {
        onDeleteSuccess(doc.fileName);
      }
    } catch (err) {
      setDeleteError(`Failed to delete "${doc.fileName}": ${err.message}`);
    } finally {
      setDeletingId(null);
    }
  };

  const formatFileSize = (bytes) => {
    if (!bytes || bytes === 0) return '0 B';
    if (bytes < 1024) return `${bytes} B`;
    const kb = (bytes / 1024).toFixed(1);
    return `${kb} KB`;
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      const d = new Date(dateString);
      return d.toLocaleDateString() + ' ' + d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } catch {
      return dateString;
    }
  };

  return (
    <section className="card documents-card">
      <div className="card-header">
        <div className="header-left">
          <HardDrive className="header-icon" size={20} />
          <h2>Uploaded Documents</h2>
          <span className="count-badge">{documents.length}</span>
        </div>
        <button 
          className="btn btn-sm btn-outline refresh-btn" 
          onClick={onRefresh} 
          disabled={loading}
          title="Refresh document list from MongoDB"
        >
          <RefreshCw size={14} className={loading ? 'spin-icon' : ''} />
          <span>Refresh</span>
        </button>
      </div>

      <p className="card-description">
        All documents stored in MongoDB database <code>mini_search_engine</code>.
      </p>

      {deleteError && (
        <div className="alert alert-error">
          <span>{deleteError}</span>
        </div>
      )}

      {documents.length === 0 ? (
        <div className="empty-documents">
          <FileText size={40} className="empty-icon" />
          <p>No documents uploaded yet.</p>
          <span>Use the upload box above to add text files into the search engine.</span>
        </div>
      ) : (
        <div className="table-responsive">
          <table className="doc-table">
            <thead>
              <tr>
                <th>File Name</th>
                <th>File Size</th>
                <th>Upload Date</th>
                <th className="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {documents.map((doc) => (
                <tr key={doc.id}>
                  <td className="doc-name-cell">
                    <FileText size={16} className="table-file-icon" />
                    <strong>{doc.fileName}</strong>
                  </td>
                  <td>{formatFileSize(doc.fileSize)}</td>
                  <td>{formatDate(doc.uploadDate)}</td>
                  <td className="text-right">
                    <div className="table-actions">
                      <button
                        type="button"
                        className="btn-action view"
                        onClick={() => onViewDocument(doc.id)}
                        title="View document text"
                      >
                        <Eye size={15} />
                        <span>View</span>
                      </button>

                      <a
                        href={api.getDownloadUrl(doc.id)}
                        className="btn-action download"
                        title="Download file"
                        download={doc.fileName}
                      >
                        <Download size={15} />
                        <span>Download</span>
                      </a>

                      <button
                        type="button"
                        className="btn-action delete"
                        onClick={() => handleDelete(doc)}
                        disabled={deletingId === doc.id}
                        title="Delete from MongoDB and Inverted Index"
                      >
                        <Trash2 size={15} />
                        <span>{deletingId === doc.id ? 'Deleting...' : 'Delete'}</span>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
