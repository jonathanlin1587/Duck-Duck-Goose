import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for the Pet class.
 */
public class PetTest {

    /**
     * Test the default constructor for a "duck" pet.
     * For "duck" (per petType), maxHealth, maxFullness, maxSleep, maxHappiness, and maxLove are all 100.
     */
    @Test
    public void testDefaultConstructorDuck() {
        Pet pet = new Pet("duck", "TestDuck");
        // Verify initial values
        assertEquals("TestDuck", pet.getName());
        assertEquals("duck", pet.getTypeString());
        assertEquals("normal", pet.getState());
        assertEquals(100, pet.getHealth());
        assertEquals(100, pet.getFullness());
        assertEquals(100, pet.getSleep());
        assertEquals(100, pet.getHappiness());
        assertEquals(100, pet.getLove());
    }

    /**
     * Test setter and increment (change) methods.
     */
    @Test
    public void testSettersAndIncrements() {
        Pet pet = new Pet("duck", "TestDuck");
        // Set new values
        pet.setHealth(80);
        pet.setFullness(50);
        pet.setSleep(60);
        pet.setHappiness(70);
        pet.setLove(90);
        assertEquals(80, pet.getHealth());
        assertEquals(50, pet.getFullness());
        assertEquals(60, pet.getSleep());
        assertEquals(70, pet.getHappiness());
        assertEquals(90, pet.getLove());
        
        // Increment values
        pet.changeHealth(10);     // 80 + 10 = 90
        pet.changeFullness(5);    // 50 + 5 = 55
        pet.changeSleep(5);       // 60 + 5 = 65
        pet.changeHappiness(-10); // 70 - 10 = 60
        pet.changeLove(10);       // 90 + 10 = 100
        assertEquals(90, pet.getHealth());
        assertEquals(55, pet.getFullness());
        assertEquals(65, pet.getSleep());
        assertEquals(60, pet.getHappiness());
        assertEquals(100, pet.getLove());
    }

    /**
     * Test that a pet is marked as dead when its health is 0.
     */
    @Test
    public void testIsDead() {
        Pet pet = new Pet("duck", "TestDuck");
        assertFalse(pet.isDead());
        pet.setHealth(0);
        assertTrue(pet.isDead());
    }

    /**
     * Test the isSleeping() method.
     * A pet should be sleeping if its sleep is <= 0 or if its status is "sleeping".
     */
    @Test
    public void testIsSleeping() {
        Pet pet = new Pet("duck", "TestDuck");
        // Initially, sleep = 100 and state "normal" so not sleeping.
        assertFalse(pet.isSleeping());
        pet.setSleep(0);
        assertTrue(pet.isSleeping());
        pet.setSleep(50);
        pet.setState("sleeping");
        assertTrue(pet.isSleeping());
    }

    /**
     * Test the isAngry() method.
     * For "duck", maxHappiness is 100, so if happiness <= 25 then the pet is angry.
     */
    @Test
    public void testIsAngry() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setHappiness(20);
        assertTrue(pet.isAngry());
        pet.setHappiness(30);
        assertFalse(pet.isAngry());
    }

    /**
     * Test the isSick() method.
     * For "duck", if health <= 25 then the pet is sick.
     */
    @Test
    public void testIsSick() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setHealth(20);
        assertTrue(pet.isSick());
        pet.setHealth(30);
        assertFalse(pet.isSick());
    }

    /**
     * Test the isHungry() method.
     * For "duck", if fullness <= 25 then the pet is hungry.
     */
    @Test
    public void testIsHungry() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setFullness(20);
        assertTrue(pet.isHungry());
        pet.setFullness(30);
        assertFalse(pet.isHungry());
    }

    /**
     * Test the isSleepy() method.
     * For "duck", if sleep <= 25 then the pet is sleepy.
     */
    @Test
    public void testIsSleepy() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setSleep(20);
        assertTrue(pet.isSleepy());
        pet.setSleep(30);
        assertFalse(pet.isSleepy());
    }


    /**
     * Test the isFull() method.
     * A pet is full if its fullness is equal to or greater than its max fullness.
     */
    @Test
    public void testIsFull() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setFullness(100);
        assertTrue(pet.isFull());
        pet.setFullness(99);
        assertFalse(pet.isFull());
    }

    /**
     * Test the isHealthy() method.
     * A pet is healthy if its health is at maximum.
     */
    @Test
    public void testIsHealthy() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setHealth(100);
        assertTrue(pet.isHealthy());
        pet.setHealth(99);
        assertFalse(pet.isHealthy());
    }

    /**
     * Test the isHappy() method.
     * A pet is happy if its happiness is at maximum.
     */
    @Test
    public void testIsHappy() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setHappiness(100);
        assertTrue(pet.isHappy());
        pet.setHappiness(99);
        assertFalse(pet.isHappy());
    }

    /**
     * Test the isLoved() method.
     * A pet is loved if its love value is at maximum.
     */
    @Test
    public void testIsLoved() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setLove(100);
        assertTrue(pet.isLoved());
        pet.setLove(99);
        assertFalse(pet.isLoved());
    }

    /**
     * Test the cooldown methods.
     * Verifies that setting a cooldown correctly marks a command as on cooldown,
     * and that after waiting the cooldown expires.
     */
    @Test
    public void testCooldownMethods() throws InterruptedException {
        Pet pet = new Pet("duck", "TestDuck");
        // Initially, there is no cooldown for "test"
        assertFalse(pet.isCommandOnCooldown("test"));
        assertEquals(0, pet.getRemainingCooldown("test"));
        
        // Set a cooldown for 2 seconds
        pet.setCooldown("test", 2);
        // Immediately, the command should be on cooldown
        assertTrue(pet.isCommandOnCooldown("test"));
        int remaining = pet.getRemainingCooldown("test");
        assertTrue(remaining > 0 && remaining <= 2);
        
        // Wait for 3 seconds so the cooldown expires
        TimeUnit.SECONDS.sleep(3);
        assertFalse(pet.isCommandOnCooldown("test"));
        assertEquals(0, pet.getRemainingCooldown("test"));
    }

    /**
     * Test the temporary state mechanism.
     * setTemporaryState should mark the temporary flag as active, and then
     * after the specified duration, the temporary flag should be reset.
     */
    @Test
    public void testTemporaryState() throws InterruptedException {
        Pet pet = new Pet("duck", "TestDuck");
        // Initially, no temporary state
        assertFalse(pet.isTemporaryStateActive());
        // Set a temporary state for 1 second
        pet.setTemporaryState("tempState", 1);
        // Immediately, temporary state should be active
        assertTrue(pet.isTemporaryStateActive());
        // Wait for 2 seconds for the temporary state to end
        TimeUnit.SECONDS.sleep(2);
        assertFalse(pet.isTemporaryStateActive());
    }

    /**
     * Test setting and getting the recentCommand flag.
     */
    @Test
    public void testRecentCommand() {
        Pet pet = new Pet("duck", "TestDuck");
        pet.setRecentCommand(true);
        assertTrue(pet.getRecentCommand());
        pet.setRecentCommand(false);
        assertFalse(pet.getRecentCommand());
    }

    // ----- File-based constructor tests -----

    // Variables for the file-based constructor test.
    private final String testSaveFolder = "src/model/saveFiles/testPet";
    private final String testSaveFile = testSaveFolder + "/testPet_pet.csv";

    /**
     * Before file-based tests, create the directory and write a test CSV file.
     */
    @BeforeEach
    public void setUpFileTest() throws IOException {
        File folder = new File(testSaveFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // Write a header and one data line with known values.
        try (FileWriter writer = new FileWriter(testSaveFile)) {
            writer.write("name,type,state,fullness,energy,health,love,happiness\n");
            // Data line: petName, type, state, fullness, sleep, health, love, happiness.
            writer.write("TestFilePet, duck, normal, 80, 90, 70, 60, 50");
        }
    }


    /**
     * Test the file-based constructor.
     * It should correctly parse the CSV file and set the pet's fields.
     */
    @Test
    public void testFileBasedConstructor() {
        // Create a Pet using the file-based constructor.
        Pet pet = new Pet("testPet");
        // Verify that the values match those written in the CSV file.
        assertEquals("TestFilePet", pet.getName());
        assertEquals("duck", pet.getTypeString());
        assertEquals("normal", pet.getState());
        assertEquals(80, pet.getFullness());
        assertEquals(90, pet.getSleep());
        assertEquals(70, pet.getHealth());
        assertEquals(60, pet.getLove());
        assertEquals(50, pet.getHappiness());
    }
}