public class HashTable {
    private Object[] table;
    private boolean[] deleted;  // To handle deleted entries
    private CollisionResolutionPolicy crp;
    private int size;
    private int collisionCount;
    private long insertionTime;
    private long searchTime;
    private long deletionTime;
    private String techniqueName;  // Field to store the name of the technique

    public HashTable(int capacity, CollisionResolutionPolicy crp, String name) {
        this.table = new Object[capacity];
        this.deleted = new boolean[capacity];
        this.crp = crp;
        this.crp.setUp(capacity);
        this.size = 0;
        this.collisionCount = 0;
        this.insertionTime = 0;
        this.searchTime = 0;
        this.deletionTime = 0;
        this.techniqueName = name;
    }

    public boolean insert(Object key) {
        long startTime = System.nanoTime();
        int index = Math.abs(key.hashCode() % table.length);
        int attempts = 0;
        while (table[index] != null && !deleted[index] && attempts < table.length) {
            if (attempts > 0) {
                collisionCount++;
            }
            index = crp.probe(key, ++attempts);
        }
        if (attempts < table.length) {
            table[index] = key;
            deleted[index] = false;
            size++;
            long endTime = System.nanoTime();
            insertionTime += (endTime - startTime);
            return true;
        }
        long endTime = System.nanoTime();
        insertionTime += (endTime - startTime);
        return false; // table full
    }

    public Object search(Object key) {
        long startTime = System.nanoTime();
        int index = Math.abs(key.hashCode() % table.length);
        int attempts = 0;
        while (table[index] != null && attempts < table.length) {
            if (table[index].equals(key) && !deleted[index]) {
                long endTime = System.nanoTime();
                searchTime += (endTime - startTime);
                return table[index];
            }
            index = crp.probe(key, ++attempts);
        }
        long endTime = System.nanoTime();
        searchTime += (endTime - startTime);
        return null; // not found
    }

    public boolean delete(Object key) {
        long startTime = System.nanoTime();
        int index = Math.abs(key.hashCode() % table.length);
        int attempts = 0;
        while (table[index] != null && attempts < table.length) {
            if (table[index].equals(key) && !deleted[index]) {
                deleted[index] = true;
                size--;
                long endTime = System.nanoTime();
                deletionTime += (endTime - startTime);
                return true;
            }
            index = crp.probe(key, ++attempts);
        }
        long endTime = System.nanoTime();
        deletionTime += (endTime - startTime);
        return false; // not found
    }

    public int getSize() {
        return size;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public long getMemoryUsage() {
        // This is a basic implementation and does not necessarily reflect the actual memory usage in a JVM.
        // It considers only the array sizes and the Object references stored.
        long objectSize = table.length * 4;  // Rough estimate: 4 bytes for each reference
        long deletedSize = deleted.length;   // 1 byte per boolean value
        return objectSize + deletedSize;
    }

    public long getInsertionTime() {
        return insertionTime;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public long getDeletionTime() {
        return deletionTime;
    }

    public void resetMetrics() {
        insertionTime = 0;
        searchTime = 0;
        deletionTime = 0;
        collisionCount = 0;
    }

    @Override
    public String toString() {
        return techniqueName;  // Return the specific technique name
    }
}
