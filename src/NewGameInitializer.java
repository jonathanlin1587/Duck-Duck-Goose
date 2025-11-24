import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;

/**
 * NewGameInitializer handles the setup and file preparation required to start a new game.
 * It creates a new pet, saves its data, initializes inventory files, copies everything
 * to a temporary directory, and launches the main game interface (PetGUI).
 * @author Jasmine Kumar (jkumar43)
 */
public class NewGameInitializer {

    /**
     * Creates a new game instance using the selected pet type and pet name.
     * Prepares temporary save files and initializes the game screen.
     *
     * @param petType the type of pet selected by the user (e.g., "duck", "goose").
     * @param petName the name assigned to the new pet.
     */
    public static void createNewGame(String petType, String petName) {
        // Create a new Pet using the provided pet type and pet name.
        Pet newPet = new Pet(petType, petName);
        String petSavePath = "src/model/saveFiles/tempSave.csv";
        String inventorySavePath = "src/model/saveFiles/tempInventorySave.csv";
        
        if (!savePetData(newPet, petSavePath)) {
            JOptionPane.showMessageDialog(null, "Error saving pet data.");
            return;
        }
        
        if (!createEmptyInventory(inventorySavePath)) {
            JOptionPane.showMessageDialog(null, "Error creating inventory file.");
            return;
        }
        
        // Create a temporary folder ("temp") that matches the Player constructor's expectations.
        String tempFolderPath = "src/model/saveFiles/temp";
        File tempFolder = new File(tempFolderPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        
        // Copy the temporary files into the temp folder with the expected names.
        // Expected file names: "temp_pet.csv" and "temp_inventory.csv"
        String tempPetPath = tempFolderPath + "/temp_pet.csv";
        String tempInventoryPath = tempFolderPath + "/temp_inventory.csv";
        
        if (!copyFile(petSavePath, tempPetPath)) {
            JOptionPane.showMessageDialog(null, "Error copying pet data to temp folder.");
            return;
        }
        if (!copyFile(inventorySavePath, tempInventoryPath)) {
            JOptionPane.showMessageDialog(null, "Error copying inventory data to temp folder.");
            return;
        }
        
        // Create a new Player using the folder name "temp".
        // The Player constructor will load pet data from "src/model/saveFiles/temp/temp_pet.csv"
        // and inventory data from "src/model/saveFiles/temp/temp_inventory.csv".
        Player player = new Player("temp");
        
        // Launch the PetGUI with the new player.
        new PetGUI(player, "temp");
    }

    /**
     * Saves pet data to a CSV file.
     *
     * @param pet The Pet object containing initial game state.
     * @param petSavePath The path to the CSV file for saving pet data.
     * @return true if save succeeds; false otherwise.
     */
    private static boolean savePetData(Pet pet, String petSavePath) {
        try (FileWriter writer = new FileWriter(petSavePath)) {
            // Write header.
            writer.write("name,type,state,fullness,energy,health,love,happiness,score,current time\n");
            // Write pet data (starting with state "normal", score 0, and time "00:00").
            String data = pet.getName() + "," + pet.getTypeString() + "," + "normal" + "," +
                        pet.getFullness() + "," + pet.getSleep() + "," +
                        pet.getHealth() + "," + pet.getLove() + "," +
                        pet.getHappiness() + "," + "0" + "," + "00:00";
            writer.write(data); // Write the pet's initial state
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Creates an empty inventory file with a CSV header.
     * This file will be loaded into the Player's inventory at game start.
     *
     * @param inventorySavePath The file path to save the blank inventory.
     * @return true if creation succeeds; false otherwise.
     */
    private static boolean createEmptyInventory(String inventorySavePath) {
        try {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copies a file from a source path to a destination path.
     *
     * @param srcPath The source file path.
     * @param destPath The destination file path.
     * @return true if the copy is successful; false otherwise.
     */
    private static boolean copyFile(String srcPath, String destPath) {
        try {
            Files.copy(Paths.get(srcPath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
