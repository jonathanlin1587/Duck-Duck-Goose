import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("testSave");
    }

    @AfterEach
    void tearDown() {
        try (PrintWriter writer = new PrintWriter("src/model/saveFiles/testSave/testSave_pet.csv")) {
            writer.println("name,type,state,fullness,energy,health,love,happiness,score,time played");
            writer.println("null,null,null,0,0,0,0,0,0,16:27");
        } catch (IOException e) {
            fail("Failed to reset testSave_pet.csv: " + e.getMessage());
        }
    }

    @Test
    void testSaveGame() {
        player.saveGame("testSave");
        File petFile = new File("src/model/saveFiles/testSave/testSave_pet.csv");
        File inventoryFile = new File("src/model/saveFiles/testSave/testSave_inventory.csv");
        assertTrue(petFile.exists());
        assertTrue(inventoryFile.exists());
    }

    @Test
    void testLoadGame() {
        player.saveGame("testSave");
        Player loadedPlayer = new Player("testSave");
        assertEquals(player.getScore(), loadedPlayer.getScore());
        assertEquals(player.getCurrentTime(), loadedPlayer.getCurrentTime());
    }

    @Test
    void testAdjustCurrentTime() {
        player.adjustCurrentTime();
        String expectedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        assertEquals(expectedTime, player.getCurrentTime());
    }

    @Test
    void testSaveToFile() {
        player.saveToFile("testSave");
        File petFile = new File("src/model/saveFiles/testSave/testSave_pet.csv");
        assertTrue(petFile.exists());
    }

    @Test
    void testGetPet() {
        assertNotNull(player.getPet());
    }

    @Test
    void testGetInventory() {
        assertNotNull(player.getInventory());
    }

    @Test
    void testGetScore() {
        assertEquals(0, player.getScore());
    }

    @Test
    void testGetCurrentTime() {
        assertNotNull(player.getCurrentTime());
    }

    @Test
    void testAdjustScore() {
        int initialScore = player.getScore();
        player.adjustScore(10);
        assertEquals(initialScore + 10, player.getScore());
    }

    @Test
    void testAdjustInventory() {
        Inventory newInventory = new Inventory("src/model/saveFiles/testSave/testSave_inventory.csv");
        player.adjustInventory(newInventory);
        assertEquals(newInventory, player.getInventory());
    }
}
