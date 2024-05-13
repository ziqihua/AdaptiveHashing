import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LinearProbingStepsTest {

    private LinearProbingSteps linearProbingSteps;
    private int stepFactor = 3;  // Custom step factor
    private int tableSize = 10;  // Example table size

    @Before
    public void setUp() {
        linearProbingSteps = new LinearProbingSteps(stepFactor);
        linearProbingSteps.setUp(tableSize);
    }

    @Test
    public void testProbe() {
        Object key = "testKey";  // Example key
        int homePosition = Math.abs(key.hashCode() % tableSize);

        // Test several values of i to simulate probing sequence with steps
        for (int i = 0; i < 5; i++) {
            int expectedPosition = (homePosition + i * stepFactor) % tableSize;
            assertEquals("Probe position should match expected position for i=" + i,
                    expectedPosition, linearProbingSteps.probe(key, i));
        }
    }

    @Test
    public void testSetUp() {
        int newTableSize = 20;
        linearProbingSteps.setUp(newTableSize);
        // We use reflection to access the private field M to verify it's set correctly
        try {
            java.lang.reflect.Field field = LinearProbingSteps.class.getDeclaredField("M");
            field.setAccessible(true);
            int M = (int) field.get(linearProbingSteps);
            assertEquals("Table size should be updated to new value", newTableSize, M);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Field access failed: " + e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String expectedDescription = "Linear Probing with steps of 3";
        assertEquals("toString should return the correct description", expectedDescription, linearProbingSteps.toString());
    }
}
