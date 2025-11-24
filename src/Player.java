import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Represents a player in the game, managing their pet, inventory, score, and game state.
 * 
 * @author Jonathan Lin (jlin764)
 */
public class Player {
    // Fields to store the player's save file, pet, inventory, score, and current time
    protected String saveFile;
    protected Pet pet;
    protected Inventory inventory;
    protected int score;
    protected String currentTime;

    /**
     * Constructs a Player object and loads the game state from the specified save file.
     *
     * @param saveFile The name of the save file to load.
     */
    public Player(String saveFile) {
        this.saveFile = saveFile;
        loadGame(saveFile);
    }

    /**
     * Saves the current game state to the specified save file.
     *
     * @param saveFile The name of the save file to save the game state to.
     */
    public void saveGame(String saveFile) {
        String saveDirPath = "src/model/saveFiles/" + saveFile;
        File saveDir = new File(saveDirPath);
        if (!saveDir.exists()) {
            saveDir.mkdirs(); // Ensure the directory exists
        }

        String inventorySavePath = saveDirPath + "/" + saveFile + "_inventory.csv";
        String petSavePath = saveDirPath + "/" + saveFile + "_pet.csv";

        savePet(petSavePath); // Save pet data
        inventory.saveInventory(inventorySavePath); // Save inventory data
    }

    /**
     * Saves the pet's state to a CSV file.
     * Helper method to {@link saveGame}
     *
     * @param petSavePath The file path where the pet's state will be saved.
     */
    private void savePet(String petSavePath) {
        try (FileWriter writer = new FileWriter(petSavePath)) {
            // Write the header for the CSV file
            writer.write("name,type,state,fullness,energy,health,love,happiness,score,time played\n");
            StringBuilder petLine = new StringBuilder();

            // Append pet attributes and player score/time to the CSV line
            petLine.append(pet.getName()).append(",");
            petLine.append(pet.getTypeString()).append(",");
            petLine.append(pet.getState()).append(",");
            petLine.append(pet.getFullness()).append(",");
            petLine.append(pet.getSleep()).append(",");
            petLine.append(pet.getHealth()).append(",");
            petLine.append(pet.getLove()).append(",");
            petLine.append(pet.getHappiness()).append(",");
            petLine.append(score).append(",");
            petLine.append(currentTime);

            writer.write(petLine.toString()); // Write the pet data to the file

            System.out.print(petLine); // Print the pet data to the console
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }

    /**
     * Loads the game state from the specified save file.
     *
     * @param saveFile The name of the save file to load the game state from.
     */
    void loadGame(String saveFile) {
        String saveFilePath = "src/model/saveFiles/" + saveFile + "/" + saveFile + "_pet.csv";
        String inventorySavePath = "src/model/saveFiles/" + saveFile + "/" + saveFile + "_inventory.csv";

        pet = new Pet(saveFile); // Initialize the pet
        inventory = new Inventory(inventorySavePath); // Initialize the inventory

        try (Scanner sc = new Scanner(new File(saveFilePath))) {
            if (sc.hasNextLine())
                sc.nextLine(); // Skip the header line
            if (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(","); // Read the pet data
                this.score = Integer.parseInt(data[8]); // Load the score
                this.currentTime = data[9]; // Load the current time
            } else {
                // Initialize default values if no data is found
                this.score = 0;
                adjustCurrentTime(); // Set to the current time
                saveGame(saveFile); // Save the initialized state
            }
        } catch (Exception e) {
            // Handle missing or corrupted save files
            System.err.println("Save file missing or corrupted, initializing defaults.");
            this.score = 0;
            adjustCurrentTime(); // Set to the current time
            saveGame(saveFile); // Save the initialized state
        }
    }

    /**
     * Updates the current time to the system's current time.
     */
    public void adjustCurrentTime() {
        currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Saves the current game state to the save file after updating the current time.
     *
     * @param saveFile The name of the save file to save the game state to.
     */
    public void saveToFile(String saveFile) {
        adjustCurrentTime(); // Update the current time
        saveGame(saveFile); // Save the game state
    }

    /**
     * Gets the player's pet.
     *
     * @return The player's pet.
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Gets the player's inventory.
     *
     * @return The player's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the player's score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the current time as a string.
     *
     * @return The current time in "HH:mm" format.
     */
    public String getCurrentTime() {
        return currentTime;
    }

    /**
     * Adjusts the player's score by the specified amount and saves the game state.
     *
     * @param amount The amount to adjust the score by.
     */
    public void adjustScore(int amount) {
        this.score += amount;
        saveGame(saveFile);
    }

    /**
     * Updates the player's inventory and saves the game state.
     *
     * @param inventory The new inventory to set for the player.
     */
    public void adjustInventory(Inventory inventory) {
        this.inventory = inventory;
        saveGame(saveFile);
    }
}
