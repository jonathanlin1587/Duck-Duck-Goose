import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

/**
 * The awayCalculator class is responsible for calculating the time a player has been away
 * and generating stories and inventory updates based on the elapsed time.
 * 
 * @author Samuel Joseph Humphrey (shumph3)
 * @author Jonathan Lin (jlin764)
 */
public class awayCalculator {

  // Array of item names that can be found while the player is away.
  private static final String[] ITEM_NAMES = {
      "Apple Pie", "French Fries", "Bread", "Jam", "Pancakes",
      "Popcorn", "Pinecone", "Sunflower", "Hat", "Bow", "Rubber Duck", "Beach Ball"
  };

  // Array of corresponding stories for each item.
  private static final String[] STORIES = {
      "Your pet found an apple pie while you were away!",
      "Your pet found french fries while you were away!",
      "Your pet found bread while you were away!",
      "Your pet found jam while you were away!",
      "Your pet found pancakes while you were away!",
      "Your pet found popcorn while you were away!",
      "Your pet found a pinecone while you were away!",
      "Your pet found a sunflower while you were away!",
      "Your pet found a hat while you were away!",
      "Your pet found a bow while you were away!",
      "Your pet found a rubber duck while you were away!",
      "Your pet found a beach ball while you were away!"
  };

  /**
   * Calculates the time (in minutes) the player has been away, using the "currenTime" from the last save file.
   *
   * @param player The player whose away time is being calculated.
   * @return The elapsed time in minutes since the player's last recorded time.
   */
  public static int calculateAwayTime(Player player) {
    // Parse the player's last recorded time.
    LocalTime lastTime = LocalTime.parse(player.getCurrentTime(), DateTimeFormatter.ofPattern("HH:mm"));
    LocalTime currentTime = LocalTime.now();

    // Convert times to minutes since midnight.
    int lastMinutes = lastTime.getHour() * 60 + lastTime.getMinute();
    int currentMinutes = currentTime.getHour() * 60 + currentTime.getMinute();

    // Calculate elapsed time, adjusting for day rollover if necessary.
    int elapsedTime = currentMinutes - lastMinutes;
    if (elapsedTime < 0)
      elapsedTime += 24 * 60; // adjust for day rollover

    return elapsedTime;
  }

  /**
   * Used to create a list of stories based on how long the player has been absent,
   * each story has a corresponding inventory item that is added to the players inventory.
   *
   * @param player The player for whom stories are being generated.
   * @return A list of up to 5 stories describing what happened while the player was away.
   */
  public static ArrayList<String> generateStories(Player player) {
    ArrayList<String> resultStories = new ArrayList<>();
    Inventory inventory = player.getInventory();
    int elapsedTime = calculateAwayTime(player);

    // If the player has been away for less than 10 minutes, nothing happens.
    if (elapsedTime < 10) {
      resultStories.add("Nothing happened while you were away.");
      return resultStories;
    }

    // Determine the number of items found based on elapsed time (1 item per 10 minutes).
    int itemsFound = elapsedTime / 10;
    Random rand = new Random();

    // Generate up to 5 stories and update the inventory.
    for (int i = 0; i < Math.min(itemsFound, 5); i++) {
      int randIndex = rand.nextInt(ITEM_NAMES.length);
      String foundItemName = ITEM_NAMES[randIndex];

      // Check if the item already exists in the inventory.
      InventoryObject foundItem = null;
      for (InventoryObject item : inventory.getItems()) {
        if (item.getName().equals(foundItemName)) {
          foundItem = item;
          break;
        }
      }

      // Add the item to the inventory, creating it if necessary.
      if (foundItem != null) {
        inventory.addItem(foundItem, 1);
      } else {
        foundItem = new InventoryObject(foundItemName, "Misc", 1, 0, 0);
        inventory.addItem(foundItem, 1);
      }

      // Add the corresponding story to the result list.
      resultStories.add(STORIES[randIndex]);
    }

    // Save the player's game state.
    player.saveGame(player.saveFile);
    return resultStories;
  }
}
