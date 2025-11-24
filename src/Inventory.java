/**
 * <p> Manages a player's inventory of items.
 * Loads item data from a CSV file, allows adding and removing items, and can save the inventory back to a file. </p>
 * @author Chelsea Ye (cye68)
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Inventory {
    private ArrayList<InventoryObject> items = new ArrayList<>();

    /**
     * <p> Constructs an Inventory by loading items from the specified CSV file. </p>
     *
     * @param filePath the path to the CSV file containing inventory data
     */
    public Inventory(String filePath) {
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
                items.add(item);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p> Adds a quantity of the specified item to the inventory. 
     * If the item already exists, increases its quantity. </p>
     *
     * @param item the item to add
     * @param qty  the quantity to add
     */
    public void addItem(InventoryObject item, int qty) {
        Iterator<InventoryObject> it = items.iterator();
        while (it.hasNext()) {
            InventoryObject current = it.next();
            if (current.getName().equals(item.getName())) {
                current.setQty(current.getAmount() + qty);
                return;
            }
        }

        item.setQty(qty);
        items.add(item);
    }

    /**
     * Removes one unit of the item with the given name from the inventory.
     *
     * @param name the name of the item to remove
     */
    public void removeItem(String name) {
        Iterator<InventoryObject> it = items.iterator();
        while (it.hasNext()) {
            InventoryObject item = it.next();
            if (item.getName().equals(name)) {
                int qty = item.getAmount();
                item.setQty(qty-1);
            }
        }
    }

    /**
     * <p> Returns the list of items currently in the inventory. </p>
     *
     * @return an ArrayList of InventoryObject
     */
    public ArrayList<InventoryObject> getItems() {
        return items;
    }

    /**
     * <p> Saves the current state of the inventory to a CSV file. </p>
     *
     * @param filePath the path to the file where the inventory should be saved
     */
    public void saveInventory(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
        for (InventoryObject item : items) {
            String line = String.format("%s,%s,%d,%d,%d",
                item.getName(),
                item.getType(),
                item.getAmount(),
                item.getPrice(),
                item.getStats()
            );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}