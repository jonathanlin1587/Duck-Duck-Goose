import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

/**
 * ParentalControlsLogin is a GUI class that provides a login screen for accessing parental controls.
 * It includes password authentication, a live clock, and key bindings for user convenience.
 * 
 * This GUI class was created using ChatGPT
 */
public class ParentalControlsLogin extends JFrame {
    private static final String PASSWORD = "1234"; // Default password for parental controls
    private JLabel timeLabel; // Label to display the current time
    private JPasswordField passwordField; // Password input field
    private Font customFont; // Custom font for UI elements

    /**
     * Constructor for ParentalControlsLogin.
     * Initializes the GUI components and sets up the layout.
     */
    public ParentalControlsLogin() {
        setTitle("Duck Duck Goose - Parental Controls Login");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load custom font
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // Set up the main panel with a background image
        JPanel mainPanel = new ImagePanel("assets/background.png");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Create the top bar with a home button and a clock
        JPanel topBar = new ImagePanel("assets/bar.png");
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Home button to return to the main menu
        ImageIcon rawIcon = new ImageIcon("assets/home.png");
        Image scaled = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton homeButton = new JButton(new ImageIcon(scaled));
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> {
            dispose();
            new MainMenuGUI();
        });

        // Clock label to display the current time
        timeLabel = new JLabel();
        timeLabel.setFont(customFont);
        topBar.add(homeButton, BorderLayout.WEST);
        topBar.add(timeLabel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Create the login panel
        JPanel loginWrapper = new JPanel();
        loginWrapper.setLayout(new BoxLayout(loginWrapper, BoxLayout.Y_AXIS));
        loginWrapper.setOpaque(false);

        try {
            // Password input field with a styled background
            BufferedImage loginImage = ImageIO.read(new File("assets/password.png"));
            ImageIcon loginIcon = new ImageIcon(loginImage);
            JLabel backgroundLabel = new JLabel(loginIcon);
            backgroundLabel.setLayout(new BoxLayout(backgroundLabel, BoxLayout.Y_AXIS));
            backgroundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            backgroundLabel.add(Box.createVerticalStrut(115)); // Vertical spacing inside the image

            // Configure the password field
            passwordField = new JPasswordField(15);
            passwordField.setMaximumSize(new Dimension(301, 55));
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add a black border and padding to center the text
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(12, 42, 5, 5)
            ));

            backgroundLabel.add(passwordField);
            loginWrapper.add(Box.createVerticalGlue());
            loginWrapper.add(backgroundLabel);
            loginWrapper.add(Box.createVerticalStrut(20));

            // Login button with hover effect
            BufferedImage loginImg = ImageIO.read(new File("assets/login.png"));
            ImageIcon loginButtonIcon = new ImageIcon(loginImg);
            ImageIcon hoverIcon = new ImageIcon(applyBrightness(loginImg, 1.2f));

            JLabel loginButton = new JLabel(loginButtonIcon);
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            loginButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    loginButton.setIcon(hoverIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    loginButton.setIcon(loginButtonIcon);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    submitPassword();
                }
            });

            loginWrapper.add(loginButton);
            loginWrapper.add(Box.createVerticalStrut(20));
            loginWrapper.add(Box.createVerticalGlue());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mainPanel.add(loginWrapper, BorderLayout.CENTER);

        // Start a timer to update the clock every second
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime();

        // Add key bindings for Enter and Escape keys
        addKeyBindings();

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
     * Adjusts the brightness of an image.
     *
     * @param original The original image.
     * @param scale    The brightness scale factor.
     * @return A new BufferedImage with adjusted brightness.
     */
    private BufferedImage applyBrightness(BufferedImage original, float scale) {
        RescaleOp op = new RescaleOp(scale, 0, null);
        return op.filter(original, null);
    }

    /**
     * Adds key bindings to the login screen.
     * Pressing ENTER submits the password.
     * Pressing ESCAPE exits the login screen to the main menu.
     */
    private void addKeyBindings() {
        JRootPane rootPane = this.getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        // Bind the ENTER key to submit the password
        im.put(KeyStroke.getKeyStroke("ENTER"), "submitAction");
        am.put("submitAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPassword();
            }
        });

        // Bind the ESCAPE key to exit to the main menu
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "exitAction");
        am.put("exitAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitToMainMenu();
            }
        });
    }

    /**
     * Checks the password and, if correct, proceeds to the parental controls menu.
     * Otherwise, shows an error message.
     */
    private void submitPassword() {
        if (PASSWORD.equals(new String(passwordField.getPassword()))) {
            dispose();
            new ParentalControlsMenu();
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exits the login screen and goes back to the main menu.
     */
    private void exitToMainMenu() {
        dispose();
        new MainMenuGUI();
    }

    /**
     * Utility panel for displaying an image as the background.
     */
    static class ImagePanel extends JPanel {
        private Image backgroundImage;

        /**
         * Constructor for ImagePanel.
         *
         * @param imagePath The path to the background image.
         */
        public ImagePanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            setOpaque(false);
        }

        /**
         * Paints the background image onto the panel.
         *
         * @param g The Graphics object used for painting.
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
     * Main method to launch the ParentalControlsLogin GUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParentalControlsLogin::new);
    }
}