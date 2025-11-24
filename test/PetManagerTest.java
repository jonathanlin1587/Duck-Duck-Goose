import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PetManager} class.
 */
public class PetManagerTest {

    /**
     * Test that the same Pet is returned for the same slot.
     */
    @Test
    public void testGetPetReturnsSamePetForSameSlot() {
        String slot = "slot2";
        Pet pet1 = PetManager.getPet(slot);
        Pet pet2 = PetManager.getPet(slot);

        assertSame(pet1, pet2, "The same Pet instance should be returned for the same slot");
    }

    /**
     * Test that a Pet can be set for a slot and retrieved correctly.
     */
    @Test
    public void testSetPet() {
        String slot = "slot3";
        Pet newPet = new Pet("CustomPet");

        PetManager.setPet(slot, newPet);
        Pet retrievedPet = PetManager.getPet(slot);

        assertSame(newPet, retrievedPet, "The Pet set for the slot should be the same as the one retrieved");
    }

    /**
     * Test that setting a new Pet for a slot replaces the existing Pet.
     */
    @Test
    public void testSetPetReplacesExistingPet() {
        String slot = "slot4";
        Pet originalPet = PetManager.getPet(slot);
        Pet newPet = new Pet("ReplacedPet");

        PetManager.setPet(slot, newPet);
        Pet retrievedPet = PetManager.getPet(slot);

        assertNotSame(originalPet, retrievedPet, "The original Pet should be replaced");
        assertSame(newPet, retrievedPet, "The new Pet should be the one retrieved");
    }
}