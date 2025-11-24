import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryObjectTest {

    @Test
    public void testConstructorAndGetters() {
        InventoryObject item = new InventoryObject("Iron Sword", "Weapon", 3, 150, 10);

        assertEquals("Iron Sword", item.getName());
        assertEquals("Weapon", item.getType());
        assertEquals(3, item.getAmount());
        assertEquals(150, item.getPrice());
        assertEquals(10, item.getStats());
    }

    @Test
    public void testSetQty() {
        InventoryObject item = new InventoryObject("Potion", "Consumable", 1, 50, 0);
        item.setQty(5);
        assertEquals(5, item.getAmount());
    }

    @Test
    public void testToStringFormat() {
        InventoryObject item = new InventoryObject("Shield", "Armor", 2, 100, 8);
        String expected = "Item{name='Shield',type='Armor',quantity=2,price=100,stats=8}";
        assertEquals(expected, item.toString());
    }
}
