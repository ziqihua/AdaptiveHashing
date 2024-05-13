import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.*;

public class mainTest {
    private List<String> movieTitles;
    private AdaptiveHashing adaptiveHashing;

    @Before
    public void setUp() throws IOException {
        movieTitles = Files.lines(Paths.get("src/movie_titles.txt")).collect(Collectors.toList());
        adaptiveHashing = new AdaptiveHashing(movieTitles);
    }



    @Test
    public void testInsert() {
        String newTitle = "New Movie Title";
        insertTitle(newTitle);
        assertTrue(adaptiveHashing.mightContain(newTitle));
    }

    @Test
    public void testDelete() {
        String existingTitle = movieTitles.get(0);
        boolean deleted = deleteTitle(existingTitle);
        assertTrue(deleted);

        boolean presentInCuckooFilter = adaptiveHashing.getCuckooFilter().mightContain(existingTitle.getBytes());

        boolean presentInHashTables = false;
        for (HashTable table : adaptiveHashing.getHashTables().values()) {
            if (table.search(existingTitle) != null) {
                presentInHashTables = true;
                break;
            }
        }

        boolean deletionSuccessful = !presentInCuckooFilter && !presentInHashTables;

        if (!deletionSuccessful) {
            fail("Deletion was not successful for item: " + existingTitle);
        }
    }


    @Test
    public void testCompare() {
        comparePerformanceMetrics();
    }

    @Test
    public void testInteractiveMode() {
        String input = "insert New Movie\nsearch New Movie\ndelete New Movie\ncompare\nexit";
        InputStream mockedInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(mockedInput);

        runInteractiveMode();
    }

    private void insertTitle(String title) {
        adaptiveHashing.insert(title);
    }

    private boolean deleteTitle(String title) {
        return adaptiveHashing.delete(title);
    }

    private boolean searchTitle(String title) {
        return adaptiveHashing.mightContain(title);
    }

    private void comparePerformanceMetrics() {
        adaptiveHashing.comparePerformanceMetrics();
        adaptiveHashing.switchToBestTechnique();
    }

    private void runInteractiveMode() {
    }

    @Test
    public void testBenchmark() {
        long startTime = System.nanoTime();
        adaptiveHashing.benchmark();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        assertTrue(duration > 0);
    }

    @Test
    public void testMainMethodWithValidInput() {
        String input = "insert New Movie\nsearch New Movie\ndelete New Movie\ncompare\nexit";
        ByteArrayInputStream mockedInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(mockedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream mockedOutput = new PrintStream(outputStream);

        System.setOut(mockedOutput);

        try {
            main.main(new String[0]);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Inserted: New Movie"));
        assertTrue(output.contains("Present: New Movie"));
        assertTrue(output.contains("Deleted: New Movie") || output.contains("Not Found: New Movie"));
        assertTrue(output.contains("Switched to the best technique based on the latest metrics."));
        assertTrue(output.contains("Final Adaptive Hashing Structure:"));
        assertTrue(output.contains("Memory Usage:"));
    }

    @Test
    public void testMainMethodWithInvalidInput() {
        String input = "invalid command\nexit";
        ByteArrayInputStream mockedInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(mockedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream mockedOutput = new PrintStream(outputStream);
        System.setOut(mockedOutput);

        try {
            main.main(new String[0]);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Unknown command. Try: insert <title>, delete <title>, search <title>, compare, exit"));
    }
}
