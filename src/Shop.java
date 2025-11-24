/**
 * Represents a shop that sells items to a player.
 * Loads available items from a catalog and allows the player to purchase items using their score.
 * Accesses the players inventory and adds purchased items to the player's inventory.
 * @author Chelsea Ye (cye68)
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Shop {
    private ArrayList<InventoryObject> catalog = new ArrayList<>();
    private Inventory playerInventory;
    private Player player;
    final String filePath = "src/model/saveFiles/Inventory_Catalog.csv";
 
    /**
     * Constructs a Shop with a player's save file and loads item catalog from CSV.
     *
     * @param saveFile the name of the player's save directory (used to load inventory and player data)
     */
    public Shop(String saveFile) {
        this.playerInventory = new Inventory("src/model/saveFiles/"+saveFile+"/"+saveFile+"_inventory.csv");
        this.player = new Player(saveFile);
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read each line in the CSV
            while ((line = br.readLine()) != null) {
                // Skip empty or malformed lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split the line by commas
                String[] parts = line.split(",");

                // Basic validation of columns
                if (parts.length < 5) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // Parse the fields (adjust parsing logic for your CSV structure)
                String name = parts[0].trim();
                String type = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());
                int price = Integer.parseInt(parts[3].trim());
                int stats = Integer.parseInt(parts[4].trim());

                InventoryObject item = new InventoryObject(name, type, quantity, price, stats);
                catalog.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    /**
     * Returns the catalog of items available for purchase.
     *
     * @return an ArrayList of InventoryObject representing the shop's catalog
     */
    public ArrayList<InventoryObject> getCatalog() {
        return catalog;
    }

    
    /**
     * Returns the player associated with this shop.
     *
     * @return the Player object
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * <p> Attempts to purchase an item from the catalog.
     * If the player has enough score, the item is added to their inventory and score is reduced.
     * If the player does not have sufficient score the method returns false. </p>
     *
     * @param name the name of the item to purchase
     * @param qty  the quantity of the item to purchase
     * @return true if the purchase was successful, false otherwise
     */
    public boolean purchaseItem(String name, int qty) {
        Iterator<InventoryObject> it = catalog.iterator();
        while (it.hasNext()) {
            InventoryObject current = it.next();
            if (current.getName().equals(name)) {
                int price = current.getPrice();

                if (player.getScore() >= price*qty) {
                    playerInventory.addItem(current, qty);
                    player.adjustInventory(playerInventory);
                    player.adjustScore(-(price*qty));
                    return true;
                } else {
                    return false;                
                }
            }
        }
        return false;
        
    }
}
