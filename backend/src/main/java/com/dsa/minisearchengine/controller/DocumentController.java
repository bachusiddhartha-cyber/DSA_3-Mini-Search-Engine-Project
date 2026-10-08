package com.dsa.minisearchengine.controller;

import com.dsa.minisearchengine.model.DocumentModel;
import com.dsa.minisearchengine.service.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for document management operations.
 */
@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "*"})
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Uploads and indexes a text file.
     * POST /api/documents/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            DocumentModel saved = documentService.uploadDocument(file);
            response.put("success", true);
            response.put("message", "File uploaded successfully");
            response.put("document", saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Failed to read file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves metadata for all uploaded documents.
     * GET /api/documents
     */
    @GetMapping
    public ResponseEntity<List<DocumentModel>> getAllDocuments() {
        List<DocumentModel> docs = documentService.getAllDocuments();
        return ResponseEntity.ok(docs);
    }

    /**
     * Retrieves a single document by ID (includes text content).
     * GET /api/documents/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable String id) {
        Optional<DocumentModel> doc = documentService.getDocumentById(id);
        if (doc.isPresent()) {
            return ResponseEntity.ok(doc.get());
        }
        Map<String, String> error = new HashMap<>();
        error.put("message", "Document not found with ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Downloads the document file.
     * GET /api/documents/{id}/download
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String id) {
        Optional<DocumentModel> docOpt = documentService.getDocumentById(id);
        if (docOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DocumentModel doc = docOpt.get();
        byte[] contentBytes = doc.getContent().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(contentBytes);
    }

    /**
     * Deletes a document by ID and purges its entries from the Inverted Index.
     * DELETE /api/documents/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        boolean deleted = documentService.deleteDocument(id);
        if (deleted) {
            response.put("success", true);
            response.put("message", "Document deleted successfully and removed from inverted index");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Document not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
