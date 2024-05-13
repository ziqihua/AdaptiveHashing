public class LinearProbingSteps implements CollisionResolutionPolicy {
    private int c;
    private int M;

    public LinearProbingSteps(int c) {
        this.c = c;
    }

    @Override
    public int probe(Object key, int i) {
        int homePos = Math.abs(key.hashCode() % M);
        return (homePos + i * c) % M;  // Linear probing with custom steps
    }

    @Override
    public void setUp(int tableSize) {
        M = tableSize;
    }

    @Override
    public String toString() {
        return String.format("Linear Probing with steps of %d", c);
    }
}