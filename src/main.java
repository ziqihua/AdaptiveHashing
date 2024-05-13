import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) throws IOException {
        List<String> movieTitles = Files.lines(Paths.get("src/movie_titles.txt")).collect(Collectors.toList());
        AdaptiveHashing adaptiveHashing = new AdaptiveHashing(movieTitles);

        // Perform benchmarking after initialization
        adaptiveHashing.benchmark();

        // Interactive CLI for user actions
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("Available Commands: insert <title>, delete <title>, search <title>, compare, exit");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim(); // Keep the original case for the title
            command = input.toLowerCase();     // Convert to lower case for command processing
            if (command.startsWith("insert ")) {
                String title = input.substring(7).trim();
                adaptiveHashing.insert(title);
                System.out.println("Inserted: " + title);

            } else if (command.startsWith("delete ")) {
                String title = input.substring(7).trim();
                boolean deleted = adaptiveHashing.delete(title);
                System.out.println(deleted ? "Deleted: " + title : "Not Found: " + title);

            } else if (command.startsWith("search ")) {
                String title = input.substring(6).trim();
                boolean present = adaptiveHashing.mightContain(title);
                System.out.println(present ? "Present: " + title : "Not Present: " + title);

            } else if (command.equals("compare")) {
                adaptiveHashing.comparePerformanceMetrics();
                // Decide which hash table or filter has the best performance metrics
                adaptiveHashing.switchToBestTechnique();
                System.out.println("Switched to the best technique based on the latest metrics.");

            } else if (command.equals("exit")) {
                break;

            } else {
                System.out.println("Unknown command. Try: insert <title>, delete <title>, search <title>, compare, exit");
            }
        }
        scanner.close();
        // Print final memory usage and performance
        System.out.println("\nFinal Adaptive Hashing Structure:");
        System.out.println(adaptiveHashing);
        System.out.println("Memory Usage: " + adaptiveHashing.getMemoryUsage() + " bytes");
    }
}
