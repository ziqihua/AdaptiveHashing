import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QuadraticProbingTest {

    private QuadraticProbing quadraticProbing;
    private int c1 = 1;  // Quadratic probing constant c1
    private int c2 = 3;  // Quadratic probing constant c2
    private int tableSize = 10;  // Example table size for testing

    @Before
    public void setUp() {
        quadraticProbing = new QuadraticProbing(c1, c2);
        quadraticProbing.setUp(tableSize);
    }

    @Test
    public void testProbe() {
        Object key = "testKey";  // Example key
        int homePosition = Math.abs(key.hashCode() % tableSize);

        // Test several values of i to simulate probing sequence
        for (int i = 0; i < 5; i++) {
            int expectedPosition = (homePosition + c1 * i + c2 * i * i) % tableSize;
            assertEquals("Probe position should match expected position for i=" + i,
                    expectedPosition, quadraticProbing.probe(key, i));
        }
    }

    @Test
    public void testSetUp() {
        int newTableSize = 20;
        quadraticProbing.setUp(newTableSize);
        // We use reflection to access the private field M to verify it's set correctly
        try {
            java.lang.reflect.Field field = QuadraticProbing.class.getDeclaredField("M");
            field.setAccessible(true);
            int M = (int) field.get(quadraticProbing);
            assertEquals("Table size should be updated to new value", newTableSize, M);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Field access failed: " + e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String expectedDescription = "Quadratic Probing with constants (1, 3)";
        assertEquals("toString should return the correct description", expectedDescription, quadraticProbing.toString());
    }
}
