public class DoubleHashing implements CollisionResolutionPolicy {

    private int prime;
    private int M;

    public DoubleHashing(int prime) {
        this.prime = prime;
    }

    @Override
    public int probe(Object key, int i) {
        int hash1 = Math.abs(key.hashCode() % M);
        int hash2 = Math.abs(prime - (key.hashCode() % prime));
        return (hash1 + i * hash2) % M;
    }

    @Override
    public void setUp(int tableSize) {
        M = tableSize;
    }

    @Override
    public String toString() {
        return String.format("Double Hashing using prime %d", prime);
    }
}
