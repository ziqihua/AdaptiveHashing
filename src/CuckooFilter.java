import java.util.BitSet;
import java.util.Random;
import java.util.Arrays;

public class CuckooFilter {
    private static final int MAX_KICKS = 500;
    private byte[][][] buckets;
    private int numBuckets;
    private int bucketSize;
    private int numHashes;
    private Random rand = new Random();

    private long insertionTime;
    private long searchTime;
    private long deletionTime;
    private int collisions;

    public CuckooFilter(int numBuckets, int bucketSize, int numHashes) {
        this.numBuckets = numBuckets;
        this.bucketSize = bucketSize;
        this.numHashes = numHashes;
        this.buckets = new byte[numBuckets][bucketSize][];
        this.insertionTime = 0;
        this.searchTime = 0;
        this.deletionTime = 0;
        this.collisions = 0;
    }

    public boolean insert(byte[] item) {
        long startTime = System.nanoTime();
        int bucket = getHash(item, 0) % numBuckets;
        int altBucket = getHash(item, 1) % numBuckets;

        if (placeItemInBucket(item, bucket) || placeItemInBucket(item, altBucket)) {
            long endTime = System.nanoTime();
            insertionTime += (endTime - startTime);
            return true;
        }

        int currentBucket = rand.nextBoolean() ? bucket : altBucket;
        for (int i = 0; i < MAX_KICKS; i++) {
            int slot = rand.nextInt(bucketSize);
            byte[] evictedItem = buckets[currentBucket][slot];
            buckets[currentBucket][slot] = item;
            item = evictedItem;

            currentBucket = (getHash(item, 0) % numBuckets == currentBucket) ?
                    (getHash(item, 1) % numBuckets) : (getHash(item, 0) % numBuckets);

            if (placeItemInBucket(item, currentBucket)) {
                long endTime = System.nanoTime();
                insertionTime += (endTime - startTime);
                return true;
            }
        }

        long endTime = System.nanoTime();
        insertionTime += (endTime - startTime);
        collisions++;
        return false;
    }

    public boolean mightContain(byte[] item) {
        long startTime = System.nanoTime();
        int bucket = getHash(item, 0) % numBuckets;
        int altBucket = getHash(item, 1) % numBuckets;

        boolean found = findItemInBucket(item, bucket) || findItemInBucket(item, altBucket);
        long endTime = System.nanoTime();
        searchTime += (endTime - startTime);
        return found;
    }

    public boolean delete(byte[] item) {
        long startTime = System.nanoTime();
        int bucket = getHash(item, 0) % numBuckets;
        int altBucket = getHash(item, 1) % numBuckets;

        boolean result = removeItemFromBucket(item, bucket) || removeItemFromBucket(item, altBucket);
        long endTime = System.nanoTime();
        deletionTime += (endTime - startTime);
        return result;
    }

    private int getHash(byte[] item, int seed) {
        int hash = Arrays.hashCode(item) + seed;
        return Math.abs(hash) % numBuckets; // Ensuring the hash value is non-negative
    }

    public boolean placeItemInBucket(byte[] item, int bucket) {
        for (int i = 0; i < bucketSize; i++) {
            if (buckets[bucket][i] == null) {
                buckets[bucket][i] = item;
                return true;
            }
        }
        return false;
    }

    private boolean findItemInBucket(byte[] item, int bucket) {
        for (int i = 0; i < bucketSize; i++) {
            if (Arrays.equals(item, buckets[bucket][i])) {
                return true;
            }
        }
        return false;
    }

    private boolean removeItemFromBucket(byte[] item, int bucket) {
        for (int i = 0; i < bucketSize; i++) {
            if (Arrays.equals(item, buckets[bucket][i])) {
                buckets[bucket][i] = null;
                return true;
            }
        }
        return false;
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

    public int getCollisions() {
        return collisions;
    }

    public long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    @Override
    public String toString() {
        return "CuckooFilter{" +
                "numBuckets=" + numBuckets +
                ", bucketSize=" + bucketSize +
                ", numHashes=" + numHashes +
                ", insertionTime=" + insertionTime +
                ", searchTime=" + searchTime +
                ", deletionTime=" + deletionTime +
                ", collisions=" + collisions +
                ", memoryUsage=" + getMemoryUsage() +
                '}';
    }
}
