import org.junit.jupiter.api.*;
import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShopTest {
    private Shop shop;
    private Player mockPlayer;

    @BeforeEach
    public void setup() {
        shop = new Shop("testSave"); 
        mockPlayer = shop.getPlayer();
        mockPlayer.adjustScore(10);
    }

    @AfterEach
    void tearDown() {
        try (PrintWriter writer = new PrintWriter("src/model/saveFiles/testSave/testSave_pet.csv")) {
            writer.println("name,type,state,fullness,energy,health,love,happiness,score,time played");
            writer.println("null,null,null,0,0,0,0,0,0,16:27");
        } catch (IOException e) {
            fail("Failed to reset testSave_pet.csv: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter("src/model/saveFiles/testSave/testSave_inventory.csv", false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSuccessfulPurchase() {
        boolean success = shop.purchaseItem("Apple Pie", 2);
        assertTrue(success, "Purchase should succeed with enough score.");
    }

    @Test
    public void testFailedPurchaseDueToFunds() {
        boolean success = shop.purchaseItem("Beach Ball", 10);
        assertFalse(success, "Purchase should fail due to insufficient funds.");
    }

    @Test
    public void testInventoryUpdatedAfterPurchase() {
        mockPlayer.adjustScore(500);
        shop.purchaseItem("Popcorn", 5);
        boolean found = false;
        for (InventoryObject obj : shop.getPlayer().getInventory().getItems()) {
            if (obj.getName().equals("Popcorn")) {
                found = true;
                assertEquals(5, obj.getAmount());
            }
        }
        assertTrue(found, "Item should be in the inventory after purchase.");
    }
}
