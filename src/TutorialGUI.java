import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

/**
 * TutorialGUI represents the tutorial screen for the "Duck Duck Goose" application.
 * It displays a scrollable tutorial image, a live clock, and a home button to navigate back to the main menu.
 * 
 * @author Jonathan Lin (jlin764)
 */
public class TutorialGUI extends JFrame {

    private JLabel timeLabel; // Label to display the current time
    private Font customFont;  // Custom font used throughout the GUI

    /**
     * Constructor for the TutorialGUI class.
     * Initializes the GUI components and sets up the layout, key bindings, and live clock.
     */
    public TutorialGUI() {
        setTitle("Duck Duck Goose - Tutorial");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load custom font or fallback to default font
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf"))
                    .deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.BOLD, 36);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // === Top Bar ===
        JPanel topBar = new ImagePanel("assets/bar.png");
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Home button to navigate back to the main menu
        ImageIcon rawIcon = new ImageIcon("assets/home.png");
        Image scaled = rawIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton homeButton = new JButton(new ImageIcon(scaled));
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e-> goToMainMenu());

        // Label to display the current time
        timeLabel = new JLabel();
        timeLabel.setFont(customFont);
        timeLabel.setForeground(Color.BLACK);
        topBar.add(homeButton, BorderLayout.WEST);
        topBar.add(timeLabel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // === Tutorial Scrollable Image ===
        ImageIcon tutorialImage = new ImageIcon("assets/tutorials.png");
        JLabel imageLabel = new JLabel(tutorialImage);
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); // Smooth scrolling
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === Key Bindings ===
        scrollPane.setFocusable(true);
        scrollPane.requestFocusInWindow();

        InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = scrollPane.getActionMap();

        // Key binding for scrolling down
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "scrollDown");
        actionMap.put("scrollDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JScrollBar vBar = scrollPane.getVerticalScrollBar();
                vBar.setValue(vBar.getValue() + 50);
            }
        });

        // Key binding for scrolling up
        inputMap.put(KeyStroke.getKeyStroke("UP"), "scrollUp");
        actionMap.put("scrollUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JScrollBar vBar = scrollPane.getVerticalScrollBar();
                vBar.setValue(vBar.getValue() - 50);
            }
        });

        // Key binding for navigating back to the main menu
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "goHome");
        actionMap.put("goHome", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                goToMainMenu();
            }
        });

        // === Live Clock ===
        Timer timer = new Timer(1000, e-> updateTime());
        timer.start();
        updateTime();

        setVisible(true);
    }

    /**
     * Updates the time label with the current time.
     */
    private void updateTime() {
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        timeLabel.setText("Time: " + currentTime);
    }

    /**
     * Navigates back to the main menu by disposing of the current frame and opening the MainMenuGUI.
     */
    private void goToMainMenu() {
        dispose();
        new MainMenuGUI();
    }

    /**
     * Custom JPanel class to display a background image.
     */
    class ImagePanel extends JPanel {
        private Image backgroundImage; // Background image for the panel

        /**
         * Constructor for the ImagePanel class.
         * Loads the background image from the specified file path.
         *
         * @param imagePath Path to the background image file.
         */
        public ImagePanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    /**
     * Main method to launch the TutorialGUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TutorialGUI::new);
    }
}