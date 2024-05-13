import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CuckooFilterTest {
    private CuckooFilter cuckooFilter;

    @Before
    public void setUp() {
        // Initialize with a smaller size for easier testing
        cuckooFilter = new CuckooFilter(10, 4, 2);
    }

    @Test
    public void testInsert() {
        byte[] item = "test".getBytes();
        assertTrue("Insert should succeed.", cuckooFilter.insert(item));
        assertTrue("Item should be found after insertion.", cuckooFilter.mightContain(item));
    }

    @Test
    public void testInsertionTimeRecorded() {
        byte[] item = "test".getBytes();
        cuckooFilter.insert(item);
        assertTrue("Insertion time should be greater than zero.", cuckooFilter.getInsertionTime() > 0);
    }


    @Test
    public void testMemoryUsage() {
        long memoryUsage = cuckooFilter.getMemoryUsage();
        assertTrue("Memory usage should be greater than zero.", memoryUsage > 0);
    }

    @Test
    public void testToString() {
        assertNotNull("ToString should provide a non-null string.", cuckooFilter.toString());
    }



    @Test
    public void testSearchTimeRecording() {
        byte[] item = "searchTimeTest".getBytes();
        cuckooFilter.insert(item);
        cuckooFilter.mightContain(item);
        assertTrue("Search time should be recorded and greater than zero.", cuckooFilter.getSearchTime() > 0);
    }

    @Test
    public void testDeletionTimeRecording() {
        byte[] item = "deletionTimeTest".getBytes();
        cuckooFilter.insert(item);
        cuckooFilter.delete(item);
        assertTrue("Deletion time should be recorded and greater than zero.", cuckooFilter.getDeletionTime() > 0);
    }

    @Test
    public void testItemNotFoundAfterDeletion() {
        byte[] item = "tempItem".getBytes();
        cuckooFilter.insert(item);
        cuckooFilter.delete(item);
        assertFalse("Item should not be found after it has been deleted.", cuckooFilter.mightContain(item));
    }

    @Test
    public void testFalsePositive() {
        byte[] item1 = "item1".getBytes();
        byte[] item2 = "item2".getBytes(); // Assume item2 generates a hash collision with item1
        cuckooFilter.insert(item1);
        assertFalse("Item2 should not be falsely identified as present.", cuckooFilter.mightContain(item2));
    }

    @Test
    public void testCuckooFilterConstructor() {
        assertNotNull("CuckooFilter should not be null after instantiation", cuckooFilter);
    }

    @Test
    public void testDeleteExistingItem() {
        byte[] item = "testItem".getBytes();
        cuckooFilter.insert(item);
        assertTrue("Item should be present before deletion.", cuckooFilter.mightContain(item));
        assertTrue("Deletion should succeed for an existing item.", cuckooFilter.delete(item));
        assertFalse("Item should not be present after deletion.", cuckooFilter.mightContain(item));
    }

    @Test
    public void testDeleteNonExistingItem() {
        byte[] item = "nonExisting".getBytes();
        assertFalse("Deletion should fail for a non-existing item.", cuckooFilter.delete(item));
    }

    @Test
    public void testDeletionTimeRecorded() {
        byte[] item = "itemToTime".getBytes();
        cuckooFilter.insert(item);
        cuckooFilter.delete(item);
        assertTrue("Deletion time should be recorded and greater than zero.", cuckooFilter.getDeletionTime() > 0);
    }

    @Test
    public void testPlaceAndFindItemInBucket() {
        byte[] item = "testItem".getBytes();
        assertTrue("Item should be successfully placed in a bucket.", cuckooFilter.insert(item));
        assertTrue("Item should be found after placing it in a bucket.", cuckooFilter.mightContain(item));
    }

    @Test
    public void testMightContainWithInsertedItem() {
        byte[] item = "exist".getBytes();
        cuckooFilter.insert(item);
        assertTrue("mightContain should return true for an item that was inserted.", cuckooFilter.mightContain(item));
    }



    @Test
    public void testInsertWithCollisions() {
        for (int i = 0; i < 50; i++) {
            String itemString = "test" + i;  // Append index to make each item unique
            byte[] item = itemString.getBytes();

            boolean result = cuckooFilter.insert(item);
            assertTrue("Insert should handle collisions or be successful.", result || cuckooFilter.getCollisions() > 0);
        }

        assertTrue("Collisions should be counted.", cuckooFilter.getCollisions() > 0);
    }






}
