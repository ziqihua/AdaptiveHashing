import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;


public class BloomFilter {
    private BitSet hashes;
    private int numHashes;
    private int size;
    private long insertionTime;
    private long searchTime;
    private int collisions;
    private Set<Integer> usedHashes;

    public BloomFilter(int size, int numHashes) {
        this.size = size;
        this.numHashes = numHashes;
        this.hashes = new BitSet(size);
        this.usedHashes = new HashSet<>();
        this.insertionTime = 0;
        this.searchTime = 0;
        this.collisions = 0;
    }

    //Each method in this class performs a specific function. For instance, the insert method is used to add elements,
    // while also logging the time taken for insertion and any collisions that may occur.
    public void insert(String item) {
        long startTime = System.nanoTime();
        int[] positions = getPositions(item);
        for (int position : positions) {
            if (usedHashes.contains(position)) {
                collisions++;
            } else {
                usedHashes.add(position);
            }
            hashes.set(position);
        }
        long endTime = System.nanoTime();
        insertionTime += (endTime - startTime);
    }

    // The mightContain method is used to check if an element might be present in the set,
    // and it records the time taken for this query.
    public boolean mightContain(String item) {
        long startTime = System.nanoTime();
        int[] positions = getPositions(item);
        for (int position : positions) {
            if (!hashes.get(position)) {
                long endTime = System.nanoTime();
                searchTime += (endTime - startTime);
                return false;
            }
        }
        long endTime = System.nanoTime();
        searchTime += (endTime - startTime);
        return true;
    }

    // Additionally, there are helper methods designed to identify the positions where elements should be marked,
    // as well as methods related to performance monitoring, which gather data on time and memory usage.
    private int[] getPositions(String item) {
        int[] positions = new int[numHashes];
        for (int i = 0; i < numHashes; i++) {
            positions[i] = Math.abs((item.hashCode() + i) % size);
        }
        return positions;
    }

    public long getInsertionTime() {
        return insertionTime;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public int getNumberOfCollisions() {
        return collisions;
    }

    public long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    @Override
    public String toString() {
        return "BloomFilter{" +
                "size=" + size +
                ", numHashes=" + numHashes +
                ", insertionTime=" + insertionTime +
                ", searchTime=" + searchTime +
                ", collisions=" + collisions +
                ", memoryUsage=" + getMemoryUsage() +
                '}';
    }
}

