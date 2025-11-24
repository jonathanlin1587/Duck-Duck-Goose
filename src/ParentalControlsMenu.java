import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * GUI class for the Parental Controls Menu in the Duck Duck Goose application.
 * Provides options for viewing screen time, setting screen time limits, and reviving pets.
 * 
 * @author Jessamine Li
 * @author Jonathan LIn
 */
public class ParentalControlsMenu extends JFrame {

    private Font customFont;

    /**
     * Constructor for the ParentalControlsMenu class.
     * Sets up the main window, loads custom fonts, and initializes the UI components.
     */
    public ParentalControlsMenu() {
        setTitle("Duck Duck Goose - Parental Controls Menu");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Load and register the custom font
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

        // Configure the home button
        layout.homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layout.homeButton.addActionListener(e -> {
            dispose();
            new MainMenuGUI(); // Replace with your actual main menu class
        });

        // Button panel with vertical BoxLayout for parental controls
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        // Add image buttons for each control
        addImageButton(buttonPanel, "assets/View screen time.png", this::openParentalStatistics);
        addImageButton(buttonPanel, "assets/Set Screen time.png", this::openParentalLimitations);
        addImageButton(buttonPanel, "assets/Revive pet button.png", this::openRevivePetPanel);

        // Wrap the button panel in a container panel with FlowLayout to center it horizontally
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        outerWrapper.add(centerPanel, BorderLayout.CENTER);
        layout.mainPanel.add(outerWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Adds an image-based button to the specified panel.
     *
     * @param panel     The panel to which the button will be added.
     * @param assetPath The file path of the button's image.
     * @param action    The action to be performed when the button is clicked.
     */
    private void addImageButton(JPanel panel, String assetPath, Runnable action) {
        ImageIcon icon = new ImageIcon(assetPath);
        JButton button = new JButton(icon);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center within the box layout
        button.addActionListener(e -> action.run());
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    /**
     * Opens the Parental Statistics window.
     * Displays an error message if the statistics model is not initialized.
     */
    private void openParentalStatistics() {
        ParentalStatisticsModel model = Main.getStatsModel();

        if (model == null) {
            // For debugging: if the model is null, warn the user.
            JOptionPane.showMessageDialog(this,
                "Persistent statistics model is not initialized. Please launch the application from Main.java.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Stats model: " + Main.getStatsModel());

        JFrame statsFrame = new JFrame("Parental Statistics");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.add(new ParentalStatistics(model));
        statsFrame.pack();
        statsFrame.setLocationRelativeTo(this);
        statsFrame.setVisible(true);
    }

    /**
     * Opens the Parental Limitations window.
     */
    private void openParentalLimitations() {
        JFrame limitsFrame = new JFrame("Parental Limitations");
        limitsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        limitsFrame.add(new ParentalLimitations()); // Replace with your actual class
        limitsFrame.pack();
        limitsFrame.setLocationRelativeTo(this);
        limitsFrame.setVisible(true);
    }

    /**
     * Opens the Revive Pet panel.
     */
    private void openRevivePetPanel() {
        JFrame reviveFrame = new JFrame("Revive Pet");
        reviveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reviveFrame.setContentPane(new RevivePetPanel());
        reviveFrame.setSize(300, 500); // Set a fixed size for the panel
        reviveFrame.setLocationRelativeTo(null);
        reviveFrame.setVisible(true);
    }

    /**
     * Main method to launch the Parental Controls Menu.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParentalControlsMenu::new);
    }

    /**
     * Panel for the field/sky background.
     */
    class FieldBackgroundPanel extends JPanel {
        private final Image backgroundImage;

        /**
         * Constructor for the FieldBackgroundPanel class.
         *
         * @param imagePath The file path of the background image.
         */
        public FieldBackgroundPanel(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image scaled to fill the entire panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Panel for the orange bar at the top.
     */
    class BarPanel extends JPanel {
        private final Image barImage;

        /**
         * Constructor for the BarPanel class.
         *
         * @param imagePath The file path of the bar image.
         */
        public BarPanel(String imagePath) {
            this.barImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the bar image stretched to fill this panel
            g.drawImage(barImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
