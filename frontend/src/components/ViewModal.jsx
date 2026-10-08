import React from 'react';
import { X, FileText, Download, Calendar, HardDrive } from 'lucide-react';
import { api } from '../services/api';

export default function ViewModal({ document, onClose }) {
  if (!document) return null;

  const formatFileSize = (bytes) => {
    if (!bytes || bytes === 0) return '0 B';
    if (bytes < 1024) return `${bytes} B`;
    return `${(bytes / 1024).toFixed(1)} KB`;
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
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-container" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div className="modal-title">
            <FileText size={20} className="modal-file-icon" />
            <div>
              <h3>{document.fileName}</h3>
              <div className="modal-meta">
                <span><Calendar size={13} /> {formatDate(document.uploadDate)}</span>
                <span><HardDrive size={13} /> {formatFileSize(document.fileSize)}</span>
              </div>
            </div>
          </div>
          <button className="btn-icon modal-close" onClick={onClose} title="Close">
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          <div className="content-box">
            <pre className="document-content-text">
              {document.content || '(File is empty)'}
            </pre>
          </div>
        </div>

        <div className="modal-footer">
          <a 
            href={api.getDownloadUrl(document.id)} 
            className="btn btn-primary"
            download={document.fileName}
          >
            <Download size={15} />
            <span>Download File</span>
          </a>
          <button className="btn btn-secondary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
