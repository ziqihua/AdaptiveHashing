import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DoubleHashingTest {

    private DoubleHashing doubleHashing;
    private int prime = 7;  // A small prime number for testing
    private int tableSize = 10;  // Example table size

    @Before
    public void setUp() {
        doubleHashing = new DoubleHashing(prime);
        doubleHashing.setUp(tableSize);  // Set up the table size
    }

    @Test
    public void testProbe() {
        Object key = "testKey";  // Example key
        int expectedInitialPosition = Math.abs(key.hashCode() % tableSize);
        int hash2 = Math.abs(prime - (key.hashCode() % prime));

        // Test several values of i to simulate probing sequence
        for (int i = 0; i < 5; i++) {
            int expectedPosition = (expectedInitialPosition + i * hash2) % tableSize;
            assertEquals("Probe position should match expected position for i=" + i,
                    expectedPosition, doubleHashing.probe(key, i));
        }
    }

    @Test
    public void testSetUp() {
        int newTableSize = 20;
        doubleHashing.setUp(newTableSize);
        // We use reflection to access the private field M to verify it's set correctly
        try {
            java.lang.reflect.Field field = DoubleHashing.class.getDeclaredField("M");
            field.setAccessible(true);
            int M = (int) field.get(doubleHashing);
            assertEquals("Table size should be updated to new value", newTableSize, M);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToString() {
        String expected = "Double Hashing using prime 7";
        assertEquals("toString should return the correct description", expected, doubleHashing.toString());
    }
}
