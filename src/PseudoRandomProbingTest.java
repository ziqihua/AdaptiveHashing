import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PseudoRandomProbingTest {

    private PseudoRandomProbing pseudoRandomProbing;
    private int tableSize = 10;

    @Before
    public void setUp() {
        pseudoRandomProbing = new PseudoRandomProbing();
        pseudoRandomProbing.setUp(tableSize);
    }

    @Test
    public void testProbe() {
        Object key = "testKey";
        int homePosition = Math.abs(key.hashCode() % tableSize);

        List<Integer> expectedPermutation = new ArrayList<>();
        for (int i = 0; i < tableSize; i++) {
            expectedPermutation.add(i);
        }
        Collections.shuffle(expectedPermutation, new Random(0));

        for (int i = 0; i < tableSize; i++) {
            int expectedPosition = (homePosition + expectedPermutation.get(i % tableSize)) % tableSize;
            assertEquals("Probe position should match expected position for i=" + i,
                    expectedPosition, pseudoRandomProbing.probe(key, i));
        }
    }


    @Test
    public void testToString() {
        assertEquals("toString should return 'Pseudo-Random Probing'", "Pseudo-Random Probing", pseudoRandomProbing.toString());
    }
}
