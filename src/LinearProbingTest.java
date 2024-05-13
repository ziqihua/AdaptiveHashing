import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LinearProbingTest {

    private LinearProbing linearProbing;
    private int tableSize = 10;  // Example table size for testing

    @Before
    public void setUp() {
        linearProbing = new LinearProbing();
        linearProbing.setUp(tableSize);
    }

    @Test
    public void testProbe() {
        Object key = "testKey";  // Example key
        int homePosition = Math.abs(key.hashCode() % tableSize);

        // Test several values of i to simulate probing sequence
        for (int i = 0; i < 5; i++) {
            int expectedPosition = (homePosition + i) % tableSize;
            assertEquals("Probe position should match expected position for i=" + i,
                    expectedPosition, linearProbing.probe(key, i));
        }
    }

    @Test
    public void testSetUp() {
        int newTableSize = 20;
        linearProbing.setUp(newTableSize);
        // We use reflection to access the private field M to verify it's set correctly
        try {
            java.lang.reflect.Field field = LinearProbing.class.getDeclaredField("M");
            field.setAccessible(true);
            int M = (int) field.get(linearProbing);
            assertEquals("Table size should be updated to new value", newTableSize, M);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Field access failed: " + e.getMessage());
        }
    }

    @Test
    public void testToString() {
        assertEquals("toString should return 'Linear Probing'", "Linear Probing", linearProbing.toString());
    }
}
