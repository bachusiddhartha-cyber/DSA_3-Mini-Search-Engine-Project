package com.dsa.minisearchengine.algorithm;

import com.dsa.minisearchengine.model.SearchResult;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ============================================================================
 * DSA ALGORITHM: RANDOMIZED QUICKSORT
 * ============================================================================
 * 
 * Syllabus Module 6: Randomized Algorithms (Las Vegas Algorithm)
 * 
 * Algorithm Concept:
 * Standard Quicksort suffers from an O(N^2) worst-case time complexity when the input
 * is already sorted or reverse-sorted and a deterministic pivot (such as the first
 * or last element) is chosen.
 * 
 * Randomized Quicksort eliminates adversarial worst-case inputs by selecting a pivot
 * uniformly at random from the subarray [low, high]. This guarantees an EXPECTED
 * running time of O(N log N) regardless of the initial ordering of the elements.
 * 
 * It is a Las Vegas randomized algorithm because:
 * 1. The result is always 100% correct and deterministic.
 * 2. Only the running time is a random variable with low variance around O(N log N).
 * 
 * In this search engine, it ranks search results by occurrences in descending order.
 * ============================================================================
 */
public class RandomizedQuickSort {

    /**
     * Sorts the search results list in descending order of occurrences.
     */
    public static void sortDescending(List<SearchResult> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        quickSort(list, 0, list.size() - 1);
    }

    private static void quickSort(List<SearchResult> list, int low, int high) {
        if (low < high) {
            int pIndex = randomizedPartition(list, low, high);
            quickSort(list, low, pIndex - 1);
            quickSort(list, pIndex + 1, high);
        }
    }

    private static int randomizedPartition(List<SearchResult> list, int low, int high) {
        // Pick a random pivot index between low and high inclusive
        int randomPivot = ThreadLocalRandom.current().nextInt(low, high + 1);
        swap(list, randomPivot, high);
        return partitionDescending(list, low, high);
    }

    private static int partitionDescending(List<SearchResult> list, int low, int high) {
        int pivotValue = list.get(high).getOccurrences();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // Descending order: higher occurrences come first
            if (list.get(j).getOccurrences() >= pivotValue) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    private static void swap(List<SearchResult> list, int i, int j) {
        SearchResult temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
