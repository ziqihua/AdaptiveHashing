import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class AdaptiveHashing {
    private List<String> dataset;
    private Map<String, HashTable> hashTables;
    private BloomFilter bloomFilter;
    private CuckooFilter cuckooFilter;
    private HashTable activeHashTable;

    private static final int HASH_TABLE_SIZE = 1000;
    private static final int BLOOM_FILTER_SIZE = 10000;
    private static final int BLOOM_FILTER_HASHES = 5;
    private static final int CUCKOO_FILTER_BUCKETS = 512;
    private static final int CUCKOO_FILTER_BUCKET_SIZE = 4;
    private static final int CUCKOO_FILTER_HASHES = 2;

    public AdaptiveHashing(List<String> dataset) {
        this.dataset = dataset;
        this.hashTables = new HashMap<>();
        initializeAllTechniques();
        preloadDataStructures();
    }

    private void initializeAllTechniques() {
        hashTables.put("DoubleHashing", new HashTable(HASH_TABLE_SIZE, new DoubleHashing(7), "DoubleHashing"));
        hashTables.put("LinearProbing", new HashTable(HASH_TABLE_SIZE, new LinearProbing(), "LinearProbing"));
        hashTables.put("LinearProbingSteps", new HashTable(HASH_TABLE_SIZE, new LinearProbingSteps(3), "LinearProbingSteps"));
        hashTables.put("QuadraticProbing", new HashTable(HASH_TABLE_SIZE, new QuadraticProbing(1,3), "QuadraticProbing"));
        hashTables.put("PseudoRandomProbing", new HashTable(HASH_TABLE_SIZE, new PseudoRandomProbing(), "PseudoRandomProbing"));

        bloomFilter = new BloomFilter(BLOOM_FILTER_SIZE, BLOOM_FILTER_HASHES);
        cuckooFilter = new CuckooFilter(CUCKOO_FILTER_BUCKETS, CUCKOO_FILTER_BUCKET_SIZE, CUCKOO_FILTER_HASHES);

        activeHashTable = hashTables.get("DoubleHashing"); // default active hash table
    }

    private void preloadDataStructures() {
        for (String item : dataset) {
            bloomFilter.insert(item);
            cuckooFilter.insert(item.getBytes());
            for (HashTable table : hashTables.values()) {
                table.insert(item);
            }
        }
    }

    public void benchmark() {
        // Simple benchmarking of search operations
        long startTime = System.nanoTime();
        for (String item : dataset) {
            mightContain(item);
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Total time for benchmarking search operations: " + duration + " ns");
    }

    public void insert(String item) {
        bloomFilter.insert(item);
        cuckooFilter.insert(item.getBytes());
        for (HashTable table : hashTables.values()) {
            table.insert(item);
        }
    }

    public boolean mightContain(String item) {
        boolean result = bloomFilter.mightContain(item) || cuckooFilter.mightContain(item.getBytes());
        for (HashTable table : hashTables.values()) {
            if (table.search(item) != null) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean delete(String item) {
        boolean result = cuckooFilter.delete(item.getBytes());
        for (HashTable table : hashTables.values()) {
            if (table.delete(item)) {
                result = true;
            }
        }
        return result;
    }

    public void switchToBestTechnique() {
        String bestTechnique = null;
        long bestPerformance = Long.MAX_VALUE;

        // Check performance of hash tables
        for (Map.Entry<String, HashTable> entry : hashTables.entrySet()) {
            long performance = entry.getValue().getInsertionTime() + entry.getValue().getSearchTime() + entry.getValue().getDeletionTime();
            if (performance < bestPerformance) {
                bestPerformance = performance;
                bestTechnique = entry.getKey();
            }
        }

        // Check performance of Bloom Filter (if you want to consider it for switching)
        long bloomPerformance = bloomFilter.getInsertionTime() + bloomFilter.getSearchTime(); // Bloom filters don't support deletion
        if (bloomPerformance < bestPerformance) {
            bestPerformance = bloomPerformance;
            bestTechnique = "BloomFilter";
        }

        // Check performance of Cuckoo Filter
        long cuckooPerformance = cuckooFilter.getInsertionTime() + cuckooFilter.getSearchTime() + cuckooFilter.getDeletionTime();
        if (cuckooPerformance < bestPerformance) {
            bestPerformance = cuckooPerformance;
            bestTechnique = "CuckooFilter";
        }

        // Apply the best technique
        if (bestTechnique.equals("BloomFilter") || bestTechnique.equals("CuckooFilter")) {
            System.out.println("Switching to " + bestTechnique);
            // You might set activeFilter here if you implement logic to handle filters as active techniques.
            // Example: activeFilter = (bestTechnique.equals("BloomFilter") ? bloomFilter : cuckooFilter);
        } else {
            activeHashTable = hashTables.get(bestTechnique);
            System.out.println("Switching to " + bestTechnique + " HashTable");
        }
    }


    public long getMemoryUsage() {
        long totalMemory = bloomFilter.getMemoryUsage() + cuckooFilter.getMemoryUsage();
        for (HashTable table : hashTables.values()) {
            totalMemory += table.getMemoryUsage();
        }
        return totalMemory;
    }

    @Override
    public String toString() {
        return String.format("AdaptiveHashing currently using %s", activeHashTable.toString());
    }

    public CuckooFilter getCuckooFilter() {
        return this.cuckooFilter;
    }

    public Map<String, HashTable> getHashTables() {
        return this.hashTables;
    }

    public void comparePerformanceMetrics() {
        System.out.println("Comparing Performance Metrics:");

        // Print insertion times
        System.out.println("Insertion Times:");
        System.out.println("Bloom Filter: " + bloomFilter.getInsertionTime() + " ns");
        System.out.println("Cuckoo Filter: " + cuckooFilter.getInsertionTime() + " ns");
        for (String key : hashTables.keySet()) {
            System.out.println(key + ": " + hashTables.get(key).getInsertionTime() + " ns");
        }

        // Print search times
        System.out.println("\nSearch Times:");
        System.out.println("Bloom Filter: " + bloomFilter.getSearchTime() + " ns");
        System.out.println("Cuckoo Filter: " + cuckooFilter.getSearchTime() + " ns");
        for (String key : hashTables.keySet()) {
            System.out.println(key + ": " + hashTables.get(key).getSearchTime() + " ns");
        }

        // Print deletion times
        System.out.println("\nDeletion Times:");
        System.out.println("Cuckoo Filter: " + cuckooFilter.getDeletionTime() + " ns");
        for (String key : hashTables.keySet()) {
            System.out.println(key + ": " + hashTables.get(key).getDeletionTime() + " ns");
        }
    }
}
