import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class BloomFilterTest {
    private BloomFilter bloomFilter;
    private Set<String> insertedItems;

    @Before
    public void setUp() {
        bloomFilter = new BloomFilter(1000, 10); // Example size and number of hashes
        insertedItems = new HashSet<>();
    }

    @Test
    public void testFalseNegatives() {
        String item = "nonexistent";
        assertFalse("Item should not be in the filter.", bloomFilter.mightContain(item));
    }

    @Test
    public void testNullInput() {
        try {
            bloomFilter.insert(null);
            fail("Should throw NullPointerException for null input");
        } catch (NullPointerException expected) {
            // Test passes
        }
    }

    @Test
    public void testEmptyStringInput() {
        String item = "";
        bloomFilter.insert(item);
        assertTrue("Empty string should be handled.", bloomFilter.mightContain(item));
    }


    @Test
    public void testSearchTimeMetrics() {
        bloomFilter.insert("testItem");
        bloomFilter.mightContain("testItem");
        assertTrue("Search time should be recorded.", bloomFilter.getSearchTime() > 0);
    }

    @Test
    public void testMemoryUsage() {
        long memoryUsage = bloomFilter.getMemoryUsage();
        assertTrue("Memory usage should be greater than zero.", memoryUsage > 0);
    }

    @Test
    public void testToStringOutput() {
        String output = bloomFilter.toString();
        assertNotNull("ToString should not return null.", output);
        assertTrue("ToString should contain class name.", output.contains("BloomFilter{"));
    }
}
