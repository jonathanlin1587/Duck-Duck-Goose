
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;

public class RevivePetPanelTest {

    private final String testFilePath = "src/model/saveFiles/testSave/testSave_pet.csv";

    @BeforeEach
    public void setUp() {
        try (FileWriter writer = new FileWriter(testFilePath, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @AfterEach
    public void tearDown() throws IOException {
        try (PrintWriter writer = new PrintWriter("src/model/saveFiles/testSave/testSave_pet.csv")) {
            writer.println("name,type,state,fullness,energy,health,love,happiness,score,time played");
            writer.println("null,null,null,0,0,0,0,0,0,16:27");
        } catch (IOException e) {
            fail("Failed to reset testSave_pet.csv: " + e.getMessage());
        }
    }

    @Test
    public void testPetStaysDeadWithoutReviveCall() {
        Pet pet = new Pet("duck", "Fluffy");
        pet.setHealth(0); 
        PetManager.setPet("testSave", pet);
        Pet retrieved = PetManager.getPet("testSave");
        assertTrue(retrieved.isDead(), "Expected pet to be dead before revival.");
    }

    @Test
    public void testReviveDeadPet() {
        Pet pet = new Pet("duck", "Fluffy");
        pet.setHealth(0); 
        pet.setFullness(20);
        pet.setSleep(10);
        pet.setHappiness(5);
        pet.setLove(0);

        Command.revive(pet);

        assertFalse(pet.isDead());
        assertEquals(100, pet.getHealth());
        assertEquals(100, pet.getFullness());
        assertEquals(100, pet.getSleep());
        assertEquals(100, pet.getHappiness());
        assertEquals(100, pet.getLove());
    }

    @Test
    public void testReviveAlivePet() {
        Pet pet = new Pet("goose", "Buddy");
        pet.setHealth(60);
        pet.setFullness(40);
        pet.setSleep(70);
        pet.setHappiness(80);
        pet.setLove(90);

        Command.revive(pet);

        assertFalse(pet.isDead(), "Pet should remain alive");
        assertEquals(60, pet.getHealth(), "Health should not change for alive pet");
        assertEquals(40, pet.getFullness(), "Fullness should not change for alive pet");
        assertEquals(70, pet.getSleep(), "Sleep should not change for alive pet");
        assertEquals(80, pet.getHappiness(), "Happiness should not change for alive pet");
        assertEquals(90, pet.getLove(), "Love should not change for alive pet");
    }

}
