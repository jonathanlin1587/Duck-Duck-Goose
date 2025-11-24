import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * A GUI class for handling the save operation in the Duck Duck Goose game.
 * Players can select save slots, saving their current game state.
 * They can also use the delete option to clear a game file.
 * @author Jasmine Kumar (jkumar43)
 */
public class SaveGameGUI extends JFrame{
    private Player currentPlayer; 
    // Array to hold the save slot panels private 
    JPanel[] savePanels; // Which slot (1-indexed) is currently selected 
    private int selectedSaveSlot = -1; 
    private JButton selectButton;
    private Font customFont;

    /**
     * Constructs the Save Game GUI given a specific player.
     * @param player the Player object to be saved
     */
    public SaveGameGUI(Player player) {
    this.currentPlayer = player;
    
    // Set up the properties of the main window
    setTitle("Duck Duck Goose - Save Game");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Load custom font
    try {
        customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(24f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
    } catch (IOException | FontFormatException e) {
        e.printStackTrace();
        customFont = new Font("Arial", Font.BOLD, 24);
    }
    
    // Set up UI background (consists of save button and a clock) using UIPanelBuilder class
    UIPanelBuilder builder = new UIPanelBuilder();
    JLabel timeLabel = builder.createLiveClockLabel(customFont);
    UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "back");
    setContentPane(layout.mainPanel);
    
    layout.homeButton.addActionListener(e-> {
        dispose();
        new MainMenuGUI();
    });
    
    // Center panel for save file selection
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setOpaque(false);
    
    // Top banner for selecting the game file
    JLabel topBanner = new JLabel(new ImageIcon("assets/select_game.png"));
    topBanner.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(Box.createVerticalStrut(20));
    centerPanel.add(topBanner);
    centerPanel.add(Box.createVerticalStrut(30));
    
    // Create three save slot panels. We store them in an array so we can update borders.
    savePanels = new JPanel[3];
    // For illustration, the first save slot is labeled with a custom name ("Ducky") and others as "Empty" (or "NO PET")
    savePanels[0] = createSavePanel("assets/save1.png", 1);
    savePanels[1] = createSavePanel("assets/save2.png", 2);
    savePanels[2] = createSavePanel("assets/save3.png", 3);
    
    // Add panels to center panel with vertical spacing.
    for (JPanel panel : savePanels) {
        centerPanel.add(panel);
        centerPanel.add(Box.createVerticalStrut(10));
    }
    
    layout.mainPanel.add(centerPanel, BorderLayout.CENTER);
    
    // Bottom panel with buttons
    JPanel bottomPanel = new ImagePanel("assets/bar.png");
    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
    bottomPanel.setPreferredSize(new Dimension(1080, 80));
    
    // "Select" button to trigger saving to the chosen slot
    selectButton = imageButton("assets/select.png", () -> selectSaveSlot());
    bottomPanel.add(selectButton);
    // "Delete" button to clear a selected game file
    bottomPanel.add(imageButton("assets/delete.png", () -> deleteSaveSlot()));

    
    layout.mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    
    setVisible(true);
}

/**
 * Creates a panel that represents a single save spot.
 * @param imagePath the path to the background save slot icon
 * @param slotNumber is the number of the save slot.
 * @return is the created 
 */
private JPanel createSavePanel(String imagePath, int slotNumber) {
    JPanel panel = new JPanel(null);
    panel.setPreferredSize(new Dimension(400, 80));
    panel.setMaximumSize(new Dimension(400, 80));
    panel.setOpaque(false);

    ImageIcon icon = new ImageIcon(imagePath);
    JButton button = new JButton(icon);
    button.setBounds(0, 0, 400, 80);
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    panel.add(button);
    

    
    // When the panel (or its button) is clicked, update selection.
    MouseAdapter selectionListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedSaveSlot = slotNumber;
            updateSaveSlotBorders();
        }
    };
    panel.addMouseListener(selectionListener);
    button.addMouseListener(selectionListener);
    
    return panel;
}

/**
 * Updates the border of the save slot if it is marked as selected.
 */
private void updateSaveSlotBorders() {
    for (int i = 0; i < savePanels.length; i++) {
        if ((i + 1) == selectedSaveSlot) {
            savePanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 30));
        } else {
            savePanels[i].setBorder(BorderFactory.createEmptyBorder());
        }
    }
    repaint();
}
/**
 * Handles the save operation then the select button is clicked.
 * Saves the current game state to the selected slot after confirmation.
 */
private void selectSaveSlot() {
    // Check if a slot is selecter
    if (selectedSaveSlot == -1) {
        JOptionPane.showMessageDialog(this, "Please select a save file first.", "No Save Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }
    String saveFileString = "save" + selectedSaveSlot;
    File petFile = new File("src/model/saveFiles/" + saveFileString + "/" + saveFileString + "_pet.csv");

    // Check if the slot has saved data already.
    if (petFile.exists() && petFile.length() > 0) {
        ConfirmationDialog overwriteDialog = new ConfirmationDialog(
            this, 
            "Overwrite Save?",
            "This save already exists.<br/>Overwrite it?",
            "assets/confirm.png",
            "assets/cancel.png",
            customFont
        );

        overwriteDialog.setVisible(true);
        if (!overwriteDialog.isConfirmed()) return; // Abort the select operation if the overwrite is not confirmed
    }

    // Save the current game state
    currentPlayer.saveToFile(saveFileString);
    
    // Show success message using custom confirmation dialog
    ConfirmationDialog saveCompleteDialog = new ConfirmationDialog(
        this, 
        "Game Saved!",
        "Game successfully saved to<br/>" + saveFileString + ".",
        "assets/confirm.png",
        null,  
        customFont
    );
    saveCompleteDialog.setVisible(true);

    // Return to main menu
    dispose();
    new MainMenuGUI();
}


/**
 * Handles the deletion of a selected game slot after confirmation.
 */
private void deleteSaveSlot() {
    if (selectedSaveSlot == -1) {
        ConfirmationDialog saveCompleteDialog = new ConfirmationDialog(
            this, 
            "Warning!",
            "Please select a save file first.",
            "assets/confirm.png",
            null,  
            customFont
        );
        saveCompleteDialog.setVisible(true);
        JOptionPane.showMessageDialog(this, "Please select a save file first.", "No Save Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }
    String saveFileString = "save" + selectedSaveSlot;

    ConfirmationDialog deleteDialog = new ConfirmationDialog(
        this,
        "Delete Save?",
        "Are you sure you want to permanently delete<br/>save file " + saveFileString + "?",
        "assets/confirm.png",
        "assets/cancel.png",
        customFont
    );
    deleteDialog.setVisible(true);

    if (deleteDialog.isConfirmed()) {
        File petFile = new File("src/model/saveFiles/" + saveFileString + "/" + saveFileString + "_pet.csv");
        File invFile = new File("src/model/saveFiles/" + saveFileString + "/" + saveFileString + "_inventory.csv");
        
        if (petFile.exists()) petFile.delete();
        if (invFile.exists()) invFile.delete();

        ConfirmationDialog deletedConfirmDialog = new ConfirmationDialog(
            this,
            "Deleted!",
            "Save file " + saveFileString + "<br/>successfully deleted.",
            "assets/confirm.png",
            null,
            customFont
        );
        deletedConfirmDialog.setVisible(true);
    }
}

/**
 * Utility method to create a button with an image background.
 * @param imagePath the path to the button's bavkground image
 * @param onClick a Runnable to execute when a button is click
 * @return the configured JButton with image background 
 */
private JButton imageButton(String imagePath, Runnable onClick) {
    JButton button = new JButton(new ImageIcon(imagePath));
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.addActionListener(e-> onClick.run());
    return button;
}

/**
 * Inner class for panels that have a background image.
 */
class ImagePanel extends JPanel {
    private Image backgroundImage;
    
    /**
     * Creates a panel with the specified background image.
     * @param imagePath the path to the background image
     */
    public ImagePanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOpaque(false); // Transparent panel
    }
    /**
     * Paint the background image on the panel.
     * @param g the graphics object to paint the component with
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
/**
 * Main method for testing the SaveGameGUI independently.
 * @param args
 */
public static void main(String[] args) {
    // For testing, we construct a new Player with an existing save file string.
    Player player = new Player("save1");
    SwingUtilities.invokeLater(() -> new SaveGameGUI(player));
}

}
