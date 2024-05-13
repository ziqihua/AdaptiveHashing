import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HashTableTest {

    private HashTable hashTable;
    private int capacity = 10;

    @Before
    public void setUp() {
        CollisionResolutionPolicy crp = new DoubleHashing(5);
        hashTable = new HashTable(capacity, crp, "DoubleHashing");
    }

    @Test
    public void testInsert() {
        assertTrue("Insert should return true", hashTable.insert("Item1"));
        assertEquals("Size should be 1 after insertion", 1, hashTable.getSize());
    }


    @Test
    public void testSearch() {
        Object item = "SearchItem";
        hashTable.insert(item);
        assertEquals("Search should return the inserted item", item, hashTable.search(item));
    }

    @Test
    public void testDelete() {
        Object item = "DeleteItem";
        hashTable.insert(item);
        assertTrue("Delete should return true", hashTable.delete(item));
        assertNull("Search should return null for deleted item", hashTable.search(item));
    }

    @Test
    public void testTableFull() {
        // Fill the table
        for (int i = 0; i < capacity; i++) {
            assertTrue("Insert should succeed until table is full", hashTable.insert("Item" + i));
        }
        assertFalse("Insert should fail when table is full", hashTable.insert("ExtraItem"));
    }

    @Test
    public void testMemoryUsage() {
        hashTable.insert("MemoryTest");
        assertTrue("Memory usage should be a positive number", hashTable.getMemoryUsage() > 0);
    }


    @Test
    public void testTiming() {
        long startTime = System.nanoTime();
        hashTable.insert("TimingItem");
        long endTime = System.nanoTime();
        assertTrue("Insertion time should be positive", hashTable.getInsertionTime() > 0);
        assertTrue("Insertion time should be within the elapsed time", hashTable.getInsertionTime() <= (endTime - startTime));

        startTime = System.nanoTime();
        hashTable.search("TimingItem");
        endTime = System.nanoTime();
        assertTrue("Search time should be positive", hashTable.getSearchTime() > 0);
        assertTrue("Search time should be within the elapsed time", hashTable.getSearchTime() <= (endTime - startTime));

        startTime = System.nanoTime();
        hashTable.delete("TimingItem");
        endTime = System.nanoTime();
        assertTrue("Deletion time should be positive", hashTable.getDeletionTime() > 0);
        assertTrue("Deletion time should be within the elapsed time", hashTable.getDeletionTime() <= (endTime - startTime));
    }

    @Test
    public void testResetMetrics() {
        hashTable.insert("ResetItem");
        long insertionTime = hashTable.getInsertionTime();
        hashTable.resetMetrics();
        assertEquals("Insertion time should be reset to 0", 0, hashTable.getInsertionTime());
        assertEquals("Search time should be reset to 0", 0, hashTable.getSearchTime());
        assertEquals("Deletion time should be reset to 0", 0, hashTable.getDeletionTime());
        assertEquals("Collision count should be reset to 0", 0, hashTable.getCollisionCount());
    }

    @Test
    public void testToString() {
        assertEquals("toString should return the technique name", "DoubleHashing", hashTable.toString());
    }
}
