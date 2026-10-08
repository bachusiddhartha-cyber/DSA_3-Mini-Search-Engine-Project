package com.dsa.minisearchengine.service;

import com.dsa.minisearchengine.algorithm.*;
import com.dsa.minisearchengine.model.AlgorithmMatchResult;
import com.dsa.minisearchengine.model.DocumentModel;
import com.dsa.minisearchengine.model.SearchResult;
import com.dsa.minisearchengine.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service orchestrating MongoDB persistence, Inverted Index operations,
 * and algorithm-driven search and ranking workflows.
 */
@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final InvertedIndexService invertedIndexService;
    private final TextProcessingService textProcessingService;

    public DocumentService(DocumentRepository documentRepository,
                           InvertedIndexService invertedIndexService,
                           TextProcessingService textProcessingService) {
        this.documentRepository = documentRepository;
        this.invertedIndexService = invertedIndexService;
        this.textProcessingService = textProcessingService;
    }

    /**
     * Initializes the in-memory Inverted Index on application startup by reading
     * all stored documents from MongoDB. This ensures the index survives application restarts.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeIndexOnStartup() {
        log.info("Connecting to MongoDB and hydrating Inverted Index...");
        try {
            List<DocumentModel> documents = documentRepository.findAll();
            for (DocumentModel doc : documents) {
                invertedIndexService.addDocument(doc.getFileName(), doc.getContent());
            }
            log.info("Inverted Index initialized with {} documents and {} unique words.",
                    documents.size(), invertedIndexService.getTotalUniqueWords());
        } catch (Exception e) {
            log.warn("Could not populate Inverted Index on startup: {}", e.getMessage());
        }
    }

    /**
     * Handles file upload:
     * 1. Validates the file (non-empty, text format, no executables).
     * 2. Reads file content.
     * 3. Saves or updates document metadata and content in MongoDB.
     * 4. Updates the Inverted Index HashMap.
     */
    public DocumentModel uploadDocument(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty. Please select a valid text file.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            throw new IllegalArgumentException("File must have a valid name.");
        }

        String cleanFileName = originalName.trim();
        String lowerName = cleanFileName.toLowerCase();

        // Security check: restrict to plain text files (.txt) and reject executable extensions
        if (lowerName.endsWith(".exe") || lowerName.endsWith(".bat") || lowerName.endsWith(".cmd") ||
            lowerName.endsWith(".sh") || lowerName.endsWith(".bin") || lowerName.endsWith(".dll") ||
            lowerName.endsWith(".jar") || lowerName.endsWith(".js") || lowerName.endsWith(".py")) {
            throw new IllegalArgumentException("Security violation: Executable or script files are not allowed.");
        }

        if (!lowerName.endsWith(".txt") && !lowerName.endsWith(".text") && !lowerName.endsWith(".md")) {
            throw new IllegalArgumentException("Unsupported file type. Please upload a plain text file (.txt).");
        }

        // Read content in UTF-8
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        // Check if document with same filename exists in MongoDB
        Optional<DocumentModel> existingDocOpt = documentRepository.findByFileName(cleanFileName);
        DocumentModel document;

        if (existingDocOpt.isPresent()) {
            // Update existing document
            document = existingDocOpt.get();
            document.setFileSize(file.getSize());
            document.setFileType(file.getContentType() != null ? file.getContentType() : "text/plain");
            document.setContent(content);
            document.setUploadDate(new Date());
        } else {
            // Create new document
            document = new DocumentModel(
                    cleanFileName,
                    originalName,
                    file.getContentType() != null ? file.getContentType() : "text/plain",
                    file.getSize(),
                    content,
                    new Date()
            );
        }

        // Save to MongoDB
        DocumentModel savedDoc = documentRepository.save(document);

        // Update in-memory Inverted Index
        invertedIndexService.addDocument(savedDoc.getFileName(), savedDoc.getContent());

        log.info("Document '{}' uploaded and indexed successfully.", cleanFileName);
        return savedDoc;
    }

    /**
     * Retrieves all documents from MongoDB.
     */
    public List<DocumentModel> getAllDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Retrieves a single document by its MongoDB ID.
     */
    public Optional<DocumentModel> getDocumentById(String id) {
        return documentRepository.findById(id);
    }

    /**
     * Deletes a document:
     * 1. Removes from MongoDB.
     * 2. Purges all references from the in-memory Inverted Index.
     */
    public boolean deleteDocument(String id) {
        Optional<DocumentModel> docOpt = documentRepository.findById(id);
        if (docOpt.isPresent()) {
            DocumentModel doc = docOpt.get();
            String fileName = doc.getFileName();

            // Delete from MongoDB
            documentRepository.deleteById(id);

            // Update Inverted Index: remove file references and clear orphaned words
            invertedIndexService.removeDocument(fileName);

            log.info("Document '{}' (ID: {}) deleted and removed from Inverted Index.", fileName, id);
            return true;
        }
        return false;
    }

    /**
     * Performs keyword or phrase search across all documents using the requested algorithm mode
     * and sorting mechanism.
     *
     * @param query Search keyword or phrase
     * @param mode Algorithm mode: INVERTED_INDEX, NAIVE, KMP, Z_ALGORITHM, RABIN_KARP, SUFFIX_ARRAY
     * @param sortMethod Sorting mode: RANDOMIZED_QUICKSORT or STANDARD_SORT
     * @return Ranked list of SearchResult items
     */
    public List<SearchResult> search(String query, String mode, String sortMethod) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String cleanQuery = query.trim();
        String selectedMode = (mode != null && !mode.isEmpty()) ? mode.toUpperCase() : "INVERTED_INDEX";
        String selectedSort = (sortMethod != null && !sortMethod.isEmpty()) ? sortMethod.toUpperCase() : "RANDOMIZED_QUICKSORT";

        List<DocumentModel> allDocs = documentRepository.findAll();
        List<SearchResult> results = new ArrayList<>();

        if ("INVERTED_INDEX".equals(selectedMode)) {
            // Default fast Inverted Index search
            Map<String, String> docIdMap = new HashMap<>();
            Map<String, String> docContentMap = new HashMap<>();
            for (DocumentModel doc : allDocs) {
                docIdMap.put(doc.getFileName(), doc.getId());
                docContentMap.put(doc.getFileName(), doc.getContent());
            }
            results = invertedIndexService.search(cleanQuery, docIdMap, docContentMap);
            for (SearchResult r : results) {
                r.setAlgorithmUsed("Inverted Index (HashMap O(1))");
            }
        } else {
            // Document scan using selected string algorithm
            String lowerQuery = cleanQuery.toLowerCase();

            for (DocumentModel doc : allDocs) {
                String lowerText = doc.getContent().toLowerCase();
                AlgorithmMatchResult matchResult;

                switch (selectedMode) {
                    case "NAIVE":
                        matchResult = NaiveStringSearch.search(lowerText, lowerQuery);
                        break;
                    case "KMP":
                        matchResult = KMPStringSearch.search(lowerText, lowerQuery);
                        break;
                    case "Z_ALGORITHM":
                        matchResult = ZAlgorithm.search(lowerText, lowerQuery);
                        break;
                    case "RABIN_KARP":
                        matchResult = RabinKarp.search(lowerText, lowerQuery);
                        break;
                    case "SUFFIX_ARRAY":
                        SuffixArray sa = new SuffixArray(lowerText);
                        matchResult = sa.search(lowerQuery);
                        break;
                    default:
                        matchResult = KMPStringSearch.search(lowerText, lowerQuery);
                        break;
                }

                if (matchResult.getMatchCount() > 0) {
                    String snippet = textProcessingService.generateSnippet(doc.getContent(), cleanQuery);
                    results.add(new SearchResult(
                            doc.getFileName(),
                            doc.getId(),
                            matchResult.getMatchCount(),
                            0, // calculated below
                            snippet,
                            matchResult.getAlgorithmName(),
                            selectedSort
                    ));
                }
            }
        }

        if (results.isEmpty()) {
            return results;
        }

        // Calculate simple, explainable Relevance Score (%):
        // Normalize against highest occurrence document in the result set
        int maxOccurrences = results.stream().mapToInt(SearchResult::getOccurrences).max().orElse(1);
        for (SearchResult r : results) {
            int score = (int) Math.round(((double) r.getOccurrences() / maxOccurrences) * 100.0);
            r.setRelevanceScore(Math.max(1, score));
            r.setSortMethodUsed(selectedSort);
        }

        // Apply selected sorting algorithm
        if ("RANDOMIZED_QUICKSORT".equals(selectedSort)) {
            // DSA: Las Vegas Randomized Quicksort
            RandomizedQuickSort.sortDescending(results);
        } else {
            // Standard Java TimSort via Comparator
            results.sort((a, b) -> Integer.compare(b.getOccurrences(), a.getOccurrences()));
        }

        return results;
    }

    /**
     * Backward-compatible search method defaulting to Inverted Index and Randomized QuickSort.
     */
    public List<SearchResult> search(String query) {
        return search(query, "INVERTED_INDEX", "RANDOMIZED_QUICKSORT");
    }

    /**
     * Returns stats for index inspection in the UI or viva.
     */
    public Map<String, Object> getIndexStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDocumentsInDb", documentRepository.count());
        stats.put("totalIndexedDocuments", invertedIndexService.getTotalIndexedDocuments());
        stats.put("totalUniqueWordsIndexed", invertedIndexService.getTotalUniqueWords());
        stats.put("rawIndex", invertedIndexService.getRawIndex());
        stats.put("randomHashMultiplier", RandomizedHash.getMultiplier());
        stats.put("randomHashOffset", RandomizedHash.getOffset());
        return stats;
    }
}
