import React, { useState, useRef } from 'react';
import { UploadCloud, CheckCircle, AlertCircle, FileText, X } from 'lucide-react';
import { api } from '../services/api';

export default function UploadSection({ onUploadSuccess }) {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [isDragOver, setIsDragOver] = useState(false);
  const fileInputRef = useRef(null);

  const validateAndSetFile = (file) => {
    setSuccessMessage('');
    setErrorMessage('');

    if (!file) return;

    if (!file.name.toLowerCase().endsWith('.txt') && !file.name.toLowerCase().endsWith('.text')) {
      setErrorMessage('Only plain text files (.txt) are allowed. Executables or scripts are rejected.');
      setSelectedFile(null);
      return;
    }

    if (file.size === 0) {
      setErrorMessage('The selected file is empty. Please select a text file with content.');
      setSelectedFile(null);
      return;
    }

    setSelectedFile(file);
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    validateAndSetFile(file);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  const handleDragLeave = () => {
    setIsDragOver(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragOver(false);
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      validateAndSetFile(e.dataTransfer.files[0]);
    }
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile) {
      setErrorMessage('Please select a file to upload first.');
      return;
    }

    try {
      setUploading(true);
      setErrorMessage('');
      setSuccessMessage('');

      const result = await api.uploadDocument(selectedFile);
      setSuccessMessage(`File "${result.document?.fileName || selectedFile.name}" uploaded successfully! Inverted Index updated.`);
      setSelectedFile(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }

      if (onUploadSuccess) {
        onUploadSuccess();
      }
    } catch (err) {
      setErrorMessage(err.message || 'File upload failed. Please try again.');
    } finally {
      setUploading(false);
    }
  };

  const clearSelection = () => {
    setSelectedFile(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
    setErrorMessage('');
  };

  return (
    <section className="card upload-card">
      <div className="card-header">
        <UploadCloud className="header-icon" size={20} />
        <h2>Upload Document</h2>
      </div>

      <p className="card-description">
        Upload <code>.txt</code> files to store them in MongoDB and index all words into the in-memory Inverted Index.
      </p>

      <form onSubmit={handleUpload}>
        <div 
          className={`dropzone ${isDragOver ? 'drag-over' : ''} ${selectedFile ? 'has-file' : ''}`}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onDrop={handleDrop}
          onClick={() => !selectedFile && fileInputRef.current?.click()}
        >
          <input 
            type="file" 
            ref={fileInputRef} 
            onChange={handleFileChange} 
            accept=".txt,.text" 
            style={{ display: 'none' }}
          />

          {!selectedFile ? (
            <div className="dropzone-prompt">
              <UploadCloud size={40} className="dropzone-icon" />
              <p><strong>Click to choose a file</strong> or drag & drop here</p>
              <span className="file-hint">Supported format: Plain text (.txt)</span>
            </div>
          ) : (
            <div className="selected-file-badge">
              <FileText size={24} className="file-icon" />
              <div className="file-details">
                <span className="file-name">{selectedFile.name}</span>
                <span className="file-size">({(selectedFile.size / 1024).toFixed(1)} KB)</span>
              </div>
              <button 
                type="button" 
                className="btn-icon" 
                onClick={(e) => { e.stopPropagation(); clearSelection(); }}
                title="Remove selection"
              >
                <X size={18} />
              </button>
            </div>
          )}
        </div>

        <div className="upload-actions">
          <button 
            type="button" 
            className="btn btn-secondary" 
            onClick={() => fileInputRef.current?.click()}
            disabled={uploading}
          >
            {selectedFile ? 'Change File' : 'Choose File'}
          </button>

          <button 
            type="submit" 
            className="btn btn-primary" 
            disabled={!selectedFile || uploading}
          >
            {uploading ? (
              <>
                <span className="spinner-sm"></span> Indexing & Uploading...
              </>
            ) : (
              'Upload'
            )}
          </button>
        </div>
      </form>

      {/* Success alert */}
      {successMessage && (
        <div className="alert alert-success">
          <CheckCircle size={18} />
          <span>{successMessage}</span>
        </div>
      )}

      {/* Error alert */}
      {errorMessage && (
        <div className="alert alert-error">
          <AlertCircle size={18} />
          <span>{errorMessage}</span>
        </div>
      )}
    </section>
  );
}
