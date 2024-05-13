import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdaptiveHashingTest {
    private AdaptiveHashing adaptiveHashing;
    private List<String> dataset;

    @Before
    public void setUp() {
        dataset = new ArrayList<>();
        dataset.add("apple");
        dataset.add("banana");
        dataset.add("cherry");
        dataset.add("date");
        dataset.add("elderberry");

        adaptiveHashing = new AdaptiveHashing(dataset);
    }

    @Test
    public void testInsert() {
        String newItem = "fig";
        adaptiveHashing.insert(newItem);
        assertTrue(adaptiveHashing.mightContain(newItem));
    }

    @Test
    public void testMightContain() {
        String existingItem = "apple";
        String nonExistingItem = "grape";

        assertTrue(adaptiveHashing.mightContain(existingItem));
        assertFalse(adaptiveHashing.mightContain(nonExistingItem));
    }

    @Test
    public void testDelete() {
        String itemToDelete = "banana";
        boolean deleted = adaptiveHashing.delete(itemToDelete);

        assertTrue(deleted);

        boolean presentInCuckooFilter = adaptiveHashing.getCuckooFilter().mightContain(itemToDelete.getBytes());

        boolean presentInHashTables = false;
        for (HashTable table : adaptiveHashing.getHashTables().values()) {
            if (table.search(itemToDelete) != null) {
                presentInHashTables = true;
                break;
            }
        }

        boolean deletionSuccessful = !presentInCuckooFilter && !presentInHashTables;

        if (!deletionSuccessful) {
            fail("Deletion was not successful for item: " + itemToDelete);
        }
    }

    @Test
    public void testSwitchToBestTechnique() {
        adaptiveHashing.switchToBestTechnique();
    }

    @Test
    public void testGetMemoryUsage() {
        long memoryUsage = adaptiveHashing.getMemoryUsage();
        assertTrue(memoryUsage > 0);
    }

    @Test
    public void testToString() {
        String expectedString = String.format("AdaptiveHashing currently using %s", adaptiveHashing.getHashTables().get("DoubleHashing").toString());
        assertEquals(expectedString, adaptiveHashing.toString());
    }

    @Test
    public void testGetCuckooFilter() {
        assertNotNull(adaptiveHashing.getCuckooFilter());
    }

    @Test
    public void testGetHashTables() {
        assertNotNull(adaptiveHashing.getHashTables());
        assertEquals(5, adaptiveHashing.getHashTables().size());
    }

    @Test
    public void testComparePerformanceMetrics() {
        adaptiveHashing.comparePerformanceMetrics();
    }
}