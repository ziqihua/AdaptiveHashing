public class LinearProbing implements CollisionResolutionPolicy {
    private int M;

    @Override
    public int probe(Object key, int i) {
        int homePos = Math.abs(key.hashCode() % M);
        return (homePos + i) % M;  // Linear probing increment
    }

    @Override
    public void setUp(int tableSize) {
        M = tableSize;
    }

    @Override
    public String toString() {
        return "Linear Probing";
    }
}