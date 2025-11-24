import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents the main menu GUI for the game "Duck Duck Goose".
 * Provides options to start a new game, load a game, view tutorials, access parental controls, or exit the game.
 * 
 * @author Jonathan Lin (jlin764)
 */
public class MainMenuGUI extends JFrame {

    private Font customFont; // Custom font for the UI

    /**
     * Constructs the main menu GUI and initializes its components.
     */
    public MainMenuGUI() {
        // Set up the main window properties
        setTitle("Duck Duck Goose - Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load and register the custom font
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // Set up the UI background and layout
        UIPanelBuilder builder = new UIPanelBuilder();
        JLabel timeLabel = builder.createLiveClockLabel(customFont); // Live clock label
        UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "home");

        setContentPane(layout.mainPanel); // Set the main panel as the content pane

        // Outer wrapper for aligning content
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setOpaque(false); // Make the wrapper transparent

        // Vertical layout for the title and buttons
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.setOpaque(false);
        verticalPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        verticalPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0)); // Add padding

        // Add the title image
        JLabel titleImage = new JLabel(new ImageIcon("assets/main_title.png"));
        titleImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        verticalPanel.add(titleImage);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 0))); // Add spacing

        // Add menu buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons for each menu option
        addMenuButton(buttonPanel, "assets/new_game.png", this::openNewGame);
        addMenuButton(buttonPanel, "assets/load_game.png", this::openLoadGame);
        addMenuButton(buttonPanel, "assets/tutorial.png", this::openTutorials);
        addMenuButton(buttonPanel, "assets/parent_controls.png", this::openParentalControls);
        addMenuButton(buttonPanel, "assets/exit.png", () -> System.exit(0));

        JLabel footer = new JLabel("<html>Developers: Samuel Humphrey, Jasmine Kumar, Jessamine Li, Jonathan Lin, Chelsea Ye"
        + "<br>Team 26, Winter term 2025"
        + "<br>Created as part of CS2212 at Western University</html>");
        footer.setFont(customFont);
        footer.setForeground(Color.WHITE);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        outerWrapper.add(footer, BorderLayout.SOUTH);

        verticalPanel.add(buttonPanel);
        outerWrapper.add(verticalPanel, BorderLayout.NORTH);
        layout.mainPanel.add(outerWrapper, BorderLayout.CENTER);

        setVisible(true); // Display the main menu
    }

    /**
     * Adds a menu button to the specified panel with hover and click effects.
     *
     * @param panel     The panel to which the button will be added.
     * @param imagePath The file path of the button's image.
     * @param action    The action to perform when the button is clicked.
     */
    private void addMenuButton(JPanel panel, String imagePath, Runnable action) {
        try {
            BufferedImage baseImg = ImageIO.read(new File(imagePath)); // Load the button image
            ImageIcon normalIcon = new ImageIcon(baseImg); // Normal state icon
            ImageIcon hoverIcon = new ImageIcon(applyBrightness(baseImg, 1.2f)); // Hover state icon

            JLabel buttonLabel = new JLabel(normalIcon); // Create a label for the button
            buttonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set hand cursor

            // Add mouse listeners for hover and click effects
            buttonLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    buttonLabel.setIcon(hoverIcon); // Change to hover icon
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    buttonLabel.setIcon(normalIcon); // Change back to normal icon
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run(); // Execute the button's action
                }
            });

            panel.add(buttonLabel); // Add the button to the panel
            panel.add(Box.createRigidArea(new Dimension(0, 15))); // Add spacing
        } catch (Exception ex) {
            ex.printStackTrace(); // Handle errors
        }
    }

    /**
     * Adjusts the brightness of an image by applying a scaling factor.
     *
     * @param original The original image to be adjusted.
     * @param scale    The brightness scale factor (e.g., 1.2f for 20% brighter).
     * @return The brightened image.
     */
    private BufferedImage applyBrightness(BufferedImage original, float scale) {
        RescaleOp op = new RescaleOp(scale, 0, null);
        return op.filter(original, null);
    }

    /**
     * Opens the load game screen. If parental restrictions are enabled and the user is not allowed to play,
     * a popup will be displayed instead.
     */
    private void openLoadGame() {
        ParentalSettings settings = new ParentalSettings();
        if (settings.isEnabled() && !settings.isAllowedToPlay()) {
            ParentalLimitations.showTimeLimitPopup(this); // Show restriction popup
            return;
        }
        dispose(); // Close the main menu
        new LoadGameGUI(); // Open the load game screen
    }

    /**
     * Opens the parental controls login screen, allowing access to parental settings.
     */
    private void openParentalControls() {
        dispose(); // Close the main menu
        new ParentalControlsLogin(); // Open the login screen
    }

    /**
     * Opens the tutorials screen, providing instructions or guidance for the game.
     */
    private void openTutorials() {
        dispose(); // Close the main menu
        new TutorialGUI(); // Open the tutorials screen
    }

    /**
     * Opens the new game screen. If parental restrictions are enabled and the user is not allowed to play,
     * a popup will be displayed instead.
     */
    private void openNewGame() {
        ParentalSettings settings = new ParentalSettings();
        if (settings.isEnabled() && !settings.isAllowedToPlay()) {
            ParentalLimitations.showTimeLimitPopup(this); // Show restriction popup
            return;
        }
        dispose(); // Close the main menu
        new NewGameGUI(); // Open the new game screen
    }

    /**
     * The main method to launch the main menu GUI.
     *
     * @param args Command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuGUI::new); // Launch the GUI on the Event Dispatch Thread
    }
}
