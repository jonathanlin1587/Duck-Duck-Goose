import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Unit tests for the NewGameInitializer class.
 * @author Jasmine Kumar (jkumar43)
 */
public class NewGameInitializerTest {
    public class NewGameInitializerHardcodedTest {
        // Hardcoded paths for the temporary folder and files.
        private final String tempFolderPath = "src/model/saveFiles/temp";
        private final String tempPetPath = tempFolderPath + "/temp_pet.csv";
        private final String tempInventoryPath = tempFolderPath + "/temp_inventory.csv";
    
        /**
         * Tests that createNewGame creates the expected files at the hardcoded locations.
         *
         * @throws IOException if file operations fail.
         * @throws InterruptedException if the thread sleep is interrupted.
         */
        @Test
        public void testCreateNewGameHardcodedPaths() throws IOException, InterruptedException {
            // Use "goose" as the pet type and "HardCodedPet" as the pet name.
            NewGameInitializer.createNewGame("goose", "HardCodedPet");
    
            // Allow a brief pause to ensure file operations have completed.
            Thread.sleep(1000);
    
            // Verify that the temporary folder exists.
            File tempFolder = new File(tempFolderPath);
            assertTrue(tempFolder.exists(), "Temporary folder should exist at " + tempFolderPath);
    
            // Verify that the temp pet file exists.
            File tempPetFile = new File(tempPetPath);
            assertTrue(tempPetFile.exists(), "Temp pet file should exist at " + tempPetPath);
    
            // Verify that the temp inventory file exists.
            File tempInventoryFile = new File(tempInventoryPath);
            assertTrue(tempInventoryFile.exists(), "Temp inventory file should exist at " + tempInventoryPath);
    
            // Read the pet data from the copied temp pet file.
            List<String> lines = Files.readAllLines(Paths.get(tempPetPath));
            // Expect at least two lines: a header and a data line.
            assertTrue(lines.size() >= 2, "Temp pet file should contain a header and at least one data line.");
    
            String dataLine = lines.get(1);
            // The data line should contain the pet name "HardCodedPet" and pet type "goose".
            assertTrue(dataLine.contains("HardCodedPet"), "Pet data should contain the pet name 'HardCodedPet'.");
            assertTrue(dataLine.contains("goose"), "Pet data should contain the pet type 'goose'.");
        }
    }
}