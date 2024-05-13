import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PseudoRandomProbing implements CollisionResolutionPolicy {
    private List<Integer> permutation;
    private int M;

    public PseudoRandomProbing() {
        // Constructor remains empty until we know the table size
    }

    @Override
    public int probe(Object key, int i) {
        int homePos = Math.abs(key.hashCode() % M);
        return (homePos + permutation.get(i % M)) % M;
    }

    @Override
    public void setUp(int tableSize) {
        this.M = tableSize;
        this.permutation = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            permutation.add(i);
        }
        Collections.shuffle(permutation, new Random(0)); // Use a fixed seed for reproducibility
    }

    @Override
    public String toString() {
        return "Pseudo-Random Probing";
    }
}
