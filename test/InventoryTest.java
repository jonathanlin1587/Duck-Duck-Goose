import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private final String testFilePath = "src/model/saveFiles/testSave/testSave_inventory.csv";

    @BeforeEach
    public void setup() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/model/saveFiles/testSave/testSave_inventory.csv", false))) {
            writer.write("Sword,Weapon,2,100,10\n");
            writer.write("Potion,Consumable,5,20,0\n");
        }
    }

    @AfterEach
    public void cleanup() {
        try (FileWriter writer = new FileWriter(testFilePath, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInventoryFromCSV() {
        Inventory inventory = new Inventory(testFilePath);
        List<InventoryObject> items = inventory.getItems();
        assertEquals(2, items.size());
        assertEquals("Sword", items.get(0).getName());
        assertEquals(5, items.get(1).getAmount());
    }

    @Test
    public void testAddNewItem() {
        Inventory inventory = new Inventory(testFilePath);
        InventoryObject shield = new InventoryObject("Shield", "Armor", 0, 50, 5);
        inventory.addItem(shield, 1);
        assertEquals(3, inventory.getItems().size());
    }

    @Test
    public void testAddToExistingItem() {
        Inventory inventory = new Inventory(testFilePath);
        InventoryObject sword = new InventoryObject("Sword", "Weapon", 0, 100, 10);
        inventory.addItem(sword, 3);
        assertEquals(5, inventory.getItems().size() >= 1 ? inventory.getItems().get(0).getAmount() : 0);
    }

    @Test
    public void testRemoveItem() {
        Inventory inventory = new Inventory(testFilePath);
        inventory.removeItem("Potion");
        Optional<InventoryObject> potion = inventory.getItems().stream()
            .filter(i -> i.getName().equals("Potion")).findFirst();
        assertTrue(potion.isPresent());
        assertEquals(4, potion.get().getAmount());
    }
}
