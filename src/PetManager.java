import java.util.HashMap;
import java.util.Map;

/**
 * The {@code PetManager} class is a utility class that manages {@link Pet} instances associated with unique slot identifiers.
 * <p>
 * It provides methods to retrieve and assign pets in a static collection.
 * When a pet is requested for a slot that does not yet have an associated {@code Pet}, a new {@code Pet} is created,
 * stored, and then returned.
 * </p>
 * @author Jessamine Li
 */
public class PetManager {
     /**
     * A static map that stores {@link Pet} instances by their slot identifiers.
     * The key is a {@code String} representing the slot, and the value is the corresponding {@code Pet}.
     */
    private static Map<String, Pet> petSlots = new HashMap<>();

    /**
     * Retrieves the {@link Pet} associated with the specified slot.
     * <p>
     * If no {@code Pet} is found for the given slot, a new {@code Pet} is created using the slot identifier,
     * added to the {@code petSlots} map, and then returned.
     * </p>
     *
     * @param slot the unique identifier for the pet slot.
     * @return the {@code Pet} instance associated with the specified slot.
     */
    public static Pet getPet(String slot) {
        return petSlots.computeIfAbsent(slot, s -> new Pet(s));
    }

       /**
     * Associates the specified {@link Pet} with the given slot.
     * <p>
     * If a {@code Pet} already exists for the specified slot, it is replaced with the new {@code Pet}.
     * </p>
     *
     * @param slot the unique identifier for the pet slot.
     * @param pet  the {@code Pet} instance to be associated with the specified slot.
     */
    public static void setPet(String slot, Pet pet) {
        petSlots.put(slot, pet);
    }
}
