import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * LoadGameGUI is a graphical user interface for loading saved games in the Duck Duck Goose application.
 * It allows users to select or delete.
 * 
 * @author Jonathan Lin (jlin764)
 */
public class LoadGameGUI extends JFrame {
    private Font customFont;


    /**
     * Constructor for LoadGameGUI. Initializes the GUI components and layout.
     * Sets up the entire GUI layout including fonts, panels, save buttons, and action buttons.
     */
    public LoadGameGUI() {
        // Set up the main frame properties
        setTitle("Duck Duck Goose - Load Game");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load custom font or fallback to Arial
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 24);
        }

        // Create the main panel and clock label
        UIPanelBuilder builder = new UIPanelBuilder();
        JLabel timeLabel = builder.createLiveClockLabel(customFont);
        UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "back");
        setContentPane(layout.mainPanel);

        // Add action listener to the home button
        layout.homeButton.addActionListener(e-> {
            dispose();
            new MainMenuGUI();
        });

        // Create the center panel for save buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Add top banner
        JLabel topBanner = new JLabel(new ImageIcon("assets/select_game.png"));
        topBanner.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(topBanner);
        centerPanel.add(Box.createVerticalStrut(30));

        // Add save buttons
        centerPanel.add(createSaveButton("assets/save1.png", new JLabel(), new JButton(), "Ducky", "save1"));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(createSaveButton("assets/save2.png", new JLabel(), new JButton(), "NO PET", "save2"));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(createSaveButton("assets/save3.png", new JLabel(), new JButton(), "NO PET", "save3"));

        layout.mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create the bottom panel for action buttons
        JPanel bottomPanel = new ImagePanel("assets/bar.png");
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        bottomPanel.setPreferredSize(new Dimension(1080, 80));

        // Add action buttons to the bottom panel
        bottomPanel.add(imageButton("assets/select.png", () -> System.out.println("Select clicked")));
        bottomPanel.add(imageButton("assets/delete.png", () -> System.out.println("Delete clicked")));
        bottomPanel.add(imageButton("assets/rename_pet.png", () -> System.out.println("Rename clicked")));

        layout.mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Creates a save button with an associated label and action.
     *
     * @param imagePath The path to the button's image.
     * @param textLabel The label to display text.
     * @param button    The button component.
     * @param text      The text to display on the label.
     * @param saveFile  The save file associated with the button.
     * @return A JPanel containing the button and label.
     */
    private JPanel createSaveButton(String imagePath, JLabel textLabel, JButton button, String text, String saveFile) {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(400, 80));
        panel.setMaximumSize(new Dimension(400, 80));
        panel.setOpaque(false);

        // Configure the button
        button.setIcon(new ImageIcon(imagePath));
        button.setBounds(0, 0, 400, 80);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(button);

        // Configure the label
        textLabel.setText(text);
        textLabel.setFont(customFont);
        textLabel.setForeground("NO PET".equals(text) ? Color.RED : Color.BLACK);
        textLabel.setBounds(0, 20, 400, 40);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(textLabel);

        // Add action listener to the button
        button.addActionListener(e-> {
            Player player = new Player(saveFile);
            ArrayList<String> awayStories = awayCalculator.generateStories(player);

            StringBuilder message = new StringBuilder();
            for (String story : awayStories) {
                message.append(story).append("\n");
            }

            JOptionPane.showMessageDialog(this, message.toString(),
                "While You Were Away...", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new PetGUI(player, saveFile); // Opens your game's GUI after loading
        });

        return panel;
    }

    /**
     * Creates an image button with a click action.
     *
     * @param imagePath The path to the button's image.
     * @param onClick   The action to perform when the button is clicked.
     * @return A JButton configured with the image and action.
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
     * ImagePanel is a custom JPanel that displays a background image.
     */
    class ImagePanel extends JPanel {
        private Image backgroundImage;

        /**
         * Constructor for ImagePanel.
         *
         * @param imagePath The path to the background image.
         */
        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null)
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Main method to launch the LoadGameGUI.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoadGameGUI::new);
    }
}
