public interface CollisionResolutionPolicy {
    int probe(Object key, int i);
    void setUp(int tableSize);
}