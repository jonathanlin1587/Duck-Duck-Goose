import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

/**
 * A utility class that builds and returns styled UI components for consistent layout across game screens.
 * 
 * Provides methods for:
 * <p>Creating a live digital clock JLabel</p>
 * <p>Building a main panel with a background image, time label, and home/back/save buttons</p>
 * 
 * Includes inner classes for holding UI component bundles and drawing image backgrounds.
 * 
 * @author Note: This class was generated mostly with Anima and ChatGPT
 * 
 */
public class UIPanelBuilder extends JFrame {

    /**
     * Creates a JLabel that displays the current system time in HH:mm format.
     * The label updates every second using a Swing Timer.
     *
     * @param font the font to use for displaying the time
     * @return a JLabel that shows the current time
     */
    public JLabel createLiveClockLabel(Font font) {
        JLabel clockLabel = new JLabel();
        clockLabel.setFont(font);
        clockLabel.setForeground(Color.BLACK);
    
        Timer timer = new Timer(1000, e-> {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            clockLabel.setText("Time: " + currentTime);
        });
        timer.start();
    
        // Initial update
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        clockLabel.setText("Time: " + currentTime);
    
        return clockLabel;
    }

    /**
     * Creates a JPanel with a background image, a top bar containing a clock label, and a navigation button (home/back/save).
     *
     * @param font the font to apply to the time label
     * @param timeLabel a JLabel showing the live clock (provided externally)
     * @param icon the type of icon for the navigation button ("home", "back", or "save")
     * @return a MainPanelComponents object containing the panel, top bar, and button
     */
    public MainPanelComponents createMainPanel(Font font, JLabel timeLabel, String icon) {
        
        String iconPath = "";
        switch (icon) {
            case "home":
                iconPath = "assets/home.png";
                break;
            case "back":
                iconPath = "assets/back.png";
                break;
            case "save":
                iconPath = "src/view/gameAssets/save.png";
            default:
                break;
        }
        
        // Background Panel
        JPanel mainPanel = new ImagePanel("assets/background.png");
        mainPanel.setLayout(new BorderLayout());

        // Top bar
        JPanel topBar = new ImagePanel("assets/bar.png");
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Time label (injected reference)
        timeLabel.setFont(font);
        timeLabel.setForeground(Color.BLACK);
        topBar.add(timeLabel, BorderLayout.EAST);

        // Home/back button
        ImageIcon rawIcon = new ImageIcon(iconPath);
        Image scaled = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton homeButton = new JButton(new ImageIcon(scaled));
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        topBar.add(homeButton, BorderLayout.WEST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        return new MainPanelComponents(mainPanel, topBar, homeButton);

    }

    /**
     * A container class for returning related UI components from the main panel creation method.
     */
    public static class MainPanelComponents {
        public JPanel mainPanel;
        public JPanel topBar;
        public JButton homeButton;
    
        /**
         * Constructs a bundle of UI components used by the main panel.
         *
         * @param mainPanel the main container panel
         * @param topBar the top bar with clock and navigation
         * @param homeButton the button on the left side of the top bar
         */
        public MainPanelComponents(JPanel mainPanel, JPanel topBar, JButton homeButton) {
            this.mainPanel = mainPanel;
            this.topBar = topBar;
            this.homeButton = homeButton;
        }
    }

    /**
     * A custom JPanel that draws a scaled background image to fill the panel.
     * Used for setting UI backgrounds with transparency support.
     */
    public static class ImagePanel extends JPanel {
        private Image backgroundImage;
    
        /**
         * Constructs an ImagePanel that loads a background image from the given path.
         *
         * @param imagePath the path to the background image file
         */
        public ImagePanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setOpaque(false);
        }
    
        /**
         * Paints the background image, scaled to the size of the panel.
         *
         * @param g the Graphics context used for drawing
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

}
