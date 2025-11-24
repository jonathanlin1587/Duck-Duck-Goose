import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class awayCalculatorTest {

    private static final String TEST_SAVE = "testAwaySave";

    private Player player;

    @BeforeEach
    public void setUp() throws IOException {
        // Create test folder and files
        String savePath = "src/model/saveFiles/" + TEST_SAVE;
        new java.io.File(savePath).mkdirs();

        // Set pet save data with a known timestamp
        try (FileWriter fw = new FileWriter(savePath + "/" + TEST_SAVE + "_pet.csv")) {
            fw.write("name,type,state,fullness,energy,health,love,happiness,score,time played\n");
            fw.write("Quackers,duck,normal,50,50,50,50,50,0," + LocalTime.now().minusMinutes(30).format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        // Start with an empty inventory
        try (FileWriter fw = new FileWriter(savePath + "/" + TEST_SAVE + "_inventory.csv")) {}

        player = new Player(TEST_SAVE);
    }

    @AfterEach
    public void tearDown() {
        // Clean up test save files
        java.io.File dir = new java.io.File("src/model/saveFiles/" + TEST_SAVE);
        for (java.io.File f : Objects.requireNonNull(dir.listFiles())) {
            f.delete();
        }
        dir.delete();
    }

    @Test
    public void testCalculateAwayTimeWithinDay() {
        int elapsed = awayCalculator.calculateAwayTime(player);
        assertTrue(elapsed >= 29 && elapsed <= 31, "Away time should be around 30 minutes.");
    }

    @Test
    public void testGenerateStoriesReturnsUpToFive() {
        List<String> stories = awayCalculator.generateStories(player);
        assertTrue(stories.size() <= 5, "Should return up to 5 stories.");
        for (String story : stories) {
            assertNotNull(story);
            assertFalse(story.isBlank());
        }
    }

    @Test
    public void testGenerateStoriesAddsItemsToInventory() {
        List<String> stories = awayCalculator.generateStories(player);
        assertFalse(player.getInventory().getItems().isEmpty(), "Inventory should contain items.");
    }

    @Test
    public void testGenerateStoriesNothingHappenedMessage() throws IOException {
        // Override save file to simulate <10 minutes
        try (FileWriter fw = new FileWriter("src/model/saveFiles/" + TEST_SAVE + "/" + TEST_SAVE + "_pet.csv")) {
            fw.write("name,type,state,fullness,energy,health,love,happiness,score,time played\n");
            fw.write("Quackers,duck,normal,50,50,50,50,50,0," + LocalTime.now().minusMinutes(5).format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        player = new Player(TEST_SAVE); // reload player with updated time

        List<String> stories = awayCalculator.generateStories(player);
        assertEquals(1, stories.size());
        assertEquals("Nothing happened while you were away.", stories.get(0));
    }

    @Test
    public void testStackingExistingItem() {
        // Force adding the same item twice to simulate stacking
        player.getInventory().addItem(new InventoryObject("Pinecone", "Misc", 1, 0, 0), 1);
        int before = player.getInventory().getItems().stream()
                .filter(item -> item.getName().equals("Pinecone"))
                .mapToInt(InventoryObject::getAmount)
                .sum();

        awayCalculator.generateStories(player); // may or may not add Pinecone again

        int after = player.getInventory().getItems().stream()
                .filter(item -> item.getName().equals("Pinecone"))
                .mapToInt(InventoryObject::getAmount)
                .sum();

        assertTrue(after >= before, "Pinecone quantity should not decrease.");
    }
}
