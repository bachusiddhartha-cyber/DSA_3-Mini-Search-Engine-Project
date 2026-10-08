import React from 'react';
import { Search, Database, Cpu, BookOpen, Layers, Award } from 'lucide-react';

export default function Navbar({ onOpenDsaModal, onOpenComplexityModal, totalDocs, uniqueWords }) {
  return (
    <header className="navbar">
      <div className="navbar-container">
        <div className="navbar-brand">
          <div className="brand-icon">
            <Search size={24} />
          </div>
          <div className="brand-text">
            <h1>Mini Search Engine</h1>
            <p className="subtext">
              Data Structures & Information Retrieval (25CS2103E)
            </p>
          </div>
        </div>

        <div className="navbar-stats">
          <div className="stat-pill" title="Active in-memory documents">
            <Layers size={14} />
            <span><strong>{totalDocs}</strong> Docs</span>
          </div>
          <div className="stat-pill" title="Unique words in Inverted Index">
            <Cpu size={14} />
            <span><strong>{uniqueWords}</strong> Terms</span>
          </div>
          <div className="stat-pill status-online" title="MongoDB Database Status">
            <Database size={14} />
            <span>MongoDB: Connected</span>
          </div>

          <button 
            className="btn btn-outline dsa-inspect-btn" 
            onClick={onOpenDsaModal}
            title="Inspect live Inverted Index HashMap"
          >
            <Cpu size={15} />
            <span>Index HashMap</span>
          </button>

          <button 
            className="btn btn-outline dsa-inspect-btn dsa-complexity-btn" 
            onClick={onOpenComplexityModal}
            title="Open Master DSA Complexity Table for Viva"
          >
            <BookOpen size={15} />
            <span>Syllabus Matrix</span>
          </button>
        </div>
      </div>
    </header>
  );
}
