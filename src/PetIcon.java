import javax.swing.*;

/**
 * A Swing component that visually represents a pet using an animated (.GIF) or static image (.png).
 * 
 * The icon changes based on the pet's state (e.g. default, hungry, angry, sleeping, etc.).
 * It supports multiple pet types (e.g. duck, goose) with their own image directories.
 * 
 * Extends {@link JLabel} to display the image within any Swing layout.
 * 
 * @author Chelsea Ye (cye68)
 */
class PetIcon extends JLabel {
    
    /** The currently displayed pet icon */
    private ImageIcon currentIcon;
    /** Base file path for this pet's image assets */
    private String petPath;
    
    /**
     * Constructs a PetIcon with the appropriate image path based on the given pet type.
     * Automatically loads the default image for the pet.
     * 
     * @param petType the type of pet (e.g., "duck", "goose", "baby duck")
     */
    public PetIcon(String petType) {
        this.petPath = switch (petType) {
            case "baby duck" -> "src/view/Pets/pet1/pet1_";
            case "duck" -> "src/view/Pets/pet2/pet2_";
            case "goose" -> "src/view/Pets/pet3/pet3_";
            default -> "src/view/Pets/default.png";
        };

        loadImage("default");
    }

    /**
     * Loads and sets the pet's image based on the current state.
     * The image path is constructed from the pet type and state.
     * 
     * @param state the current state of the pet (e.g., "default", "hungry", "dead")
     */
    private void loadImage(String state) {
        String imagePath = switch (state) {
            case "default" -> petPath+"default.GIF";
            case "hungry" -> petPath+"hungry.GIF";
            case "angry" -> petPath+"angry.GIF";
            case "exercise" -> petPath+"exercise.GIF";
            case "dead" -> petPath+"dead.png";
            case "sleep" -> petPath+"sleep.png";
            default -> null;
        };
        
        if (imagePath != null) {
            this.currentIcon = new ImageIcon(imagePath);
            setIcon(currentIcon);
            setSize(currentIcon.getIconWidth(), currentIcon.getIconHeight());
            repaint(); // Force redraw
        }
    }

    /**
     * Changes the displayed pet image by loading the icon corresponding to the specified state.
     * 
     * @param state the new state of the pet (e.g., "sleep", "angry", "exercise")
     */
    public void changePetIcon(String state) {
        loadImage(state);
    }
}
