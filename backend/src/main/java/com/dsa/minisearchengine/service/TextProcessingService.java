package com.dsa.minisearchengine.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for text processing, tokenization, and normalization.
 * 
 * DSA Concept: String Processing
 * - Time Complexity of tokenization: O(L) where L is the character length of the text.
 * - Converts all text to lowercase to ensure case-insensitive matching.
 * - Removes non-alphanumeric punctuation and delimiters.
 * - Splits normalized text into discrete word tokens.
 */
@Service
public class TextProcessingService {

    /**
     * Extracts normalized words from raw document content.
     *
     * Example:
     * Input:  "Java is Easy! Java is powerful."
     * Output: ["java", "is", "easy", "java", "is", "powerful"]
     *
     * @param content Raw text content of the document
     * @return List of normalized word tokens
     */
    public List<String> extractWords(String content) {
        List<String> words = new ArrayList<>();
        if (content == null || content.trim().isEmpty()) {
            return words;
        }

        // 1. Convert text to lowercase
        String lower = content.toLowerCase();

        // 2. Replace all punctuation and special characters with spaces
        // Keeps letters, digits, and whitespace
        String cleaned = lower.replaceAll("[^a-z0-9\\s]", " ");

        // 3. Split by one or more whitespace characters
        String[] tokens = cleaned.split("\\s+");

        // 4. Filter out empty words
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                words.add(trimmed);
            }
        }

        return words;
    }

    /**
     * Normalizes a search query string into clean tokens.
     */
    public List<String> extractQueryWords(String query) {
        return extractWords(query);
    }

    /**
     * Generates a short contextual snippet around the matched keyword in the original content.
     * Useful for search result display.
     */
    public String generateSnippet(String content, String keyword) {
        if (content == null || keyword == null || keyword.trim().isEmpty()) {
            return "";
        }

        String lowerContent = content.toLowerCase();
        String lowerKeyword = keyword.toLowerCase().trim();
        int index = lowerContent.indexOf(lowerKeyword);

        if (index == -1) {
            // Return first 100 characters if keyword wasn't found directly
            return content.length() > 100 ? content.substring(0, 100) + "..." : content;
        }

        int start = Math.max(0, index - 40);
        int end = Math.min(content.length(), index + lowerKeyword.length() + 60);

        String prefix = start > 0 ? "..." : "";
        String suffix = end < content.length() ? "..." : "";

        return prefix + content.substring(start, end).replaceAll("\\r?\\n", " ").trim() + suffix;
    }
}
