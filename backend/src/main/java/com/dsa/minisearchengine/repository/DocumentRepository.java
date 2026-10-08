package com.dsa.minisearchengine.repository;

import com.dsa.minisearchengine.model.DocumentModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB Repository for DocumentModel.
 * Provides standard CRUD methods and custom queries for filename lookups.
 */
@Repository
public interface DocumentRepository extends MongoRepository<DocumentModel, String> {

    Optional<DocumentModel> findByFileName(String fileName);

    boolean existsByFileName(String fileName);
}
