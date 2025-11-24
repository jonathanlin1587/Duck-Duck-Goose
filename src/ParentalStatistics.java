import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.Timer;

/**
 * The ParentalStatistics class is a JPanel that displays parental statistics
 * such as total play time, average session time, and daily totals. It also
 * includes a reset button to reset the statistics and a live updating timer.
 * 
 * @author Jessamine Li
 * 
 * The GUI Portion of this class was generated using ChatGPT
 */
public class ParentalStatistics extends JPanel {
    private JLabel totalPlayTimeLabel; // Label to display total play time
    private JLabel averageSessionLabel; // Label to display average session time
    private JTextArea dailyTotalsTextArea; // Text area to display daily totals
    private JScrollPane dailyTotalsScrollPane; // Scroll pane for daily totals text area
    private JButton resetButton; // Button to reset statistics
    private TimeCirclePanel circlePanel; // Custom panel to display time in a circular format
    
    private ParentalStatisticsModel model; // Model containing the statistics data
    private Timer updateTimer; // Timer for live updates
    private Font customFont; // Custom font for UI elements

    /**
     * Constructor for the ParentalStatistics class.
     * Initializes the UI components and sets up the layout.
     *
     * @param model The model containing the statistics data.
     */
    public ParentalStatistics(ParentalStatisticsModel model) {
        try {
            // Load and register custom font
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        this.model = model;
        setLayout(new BorderLayout());
        setBackground(new Color(255, 251, 240)); // Set background color

        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 195, 113));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Parental Statistics", SwingConstants.CENTER);
        titleLabel.setFont(customFont.deriveFont(28f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center panel for content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add labels and components to the content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Total play Time:"), gbc);
        gbc.gridy++;
        contentPanel.add(new JLabel(" "), gbc); // Spacing

        gbc.gridx = 1;
        gbc.gridy = 0;
        totalPlayTimeLabel = new JLabel();
        totalPlayTimeLabel.setFont(customFont.deriveFont(14f));
        contentPanel.add(totalPlayTimeLabel, gbc);

        // Add circular time display
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        circlePanel = new TimeCirclePanel();
        circlePanel.setPreferredSize(new Dimension(120, 120));
        contentPanel.add(circlePanel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        // Add average session time label
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Average play time:"), gbc);
        gbc.gridx = 1;
        averageSessionLabel = new JLabel();
        averageSessionLabel.setFont(customFont.deriveFont(14.0F));
        contentPanel.add(averageSessionLabel, gbc);

        // Add daily totals section
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(new JLabel("Daily Totals:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        dailyTotalsTextArea = new JTextArea(8, 25);
        dailyTotalsTextArea.setEditable(false);
        dailyTotalsTextArea.setFont(customFont.deriveFont(14f));
        dailyTotalsScrollPane = new JScrollPane(dailyTotalsTextArea);
        contentPanel.add(dailyTotalsScrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        // Add reset button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        ImageIcon icon = new ImageIcon("ResetStatistics.png");
        if (icon.getIconWidth() == -1) {
            // Fallback: show text button
            resetButton = new JButton("Reset Statistics");
            resetButton.setFont(customFont);
        } else {
            resetButton = new JButton(icon);
            resetButton.setContentAreaFilled(false);
            resetButton.setBorderPainted(false);
            resetButton.setFocusPainted(false);
            resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        resetButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to reset the statistics?",
                    "Confirm Reset", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.resetStatistics();
                updateView();
            }
        });

        contentPanel.add(resetButton, gbc);
        add(contentPanel, BorderLayout.CENTER);

        // Timer for live updates
        updateTimer = new Timer(1000, e -> updateView());
        updateTimer.start();

        updateView();
    }

    /**
     * Everything below this point was created by @author Jessamine Li
     */

    /**
     * Updates the view with the latest statistics from the model.
     */
    public void updateView() {
        long liveSessionSeconds = (System.currentTimeMillis() - model.getsessionStartTime()) / 1000;
        long liveTotalSeconds = model.getTotalPlayTimeSeconds() + liveSessionSeconds;
        totalPlayTimeLabel.setText(formatTime(liveTotalSeconds));

        int sessions = model.getSessionCount();
        long average = sessions == 0 ? liveSessionSeconds : model.getTotalPlayTimeSeconds() / sessions;
        averageSessionLabel.setText(formatTime(average));

        circlePanel.setTimeText(formatShortTime(liveTotalSeconds));
        updateDailyTotalsArea();
    }

    /**
     * Updates the daily totals text area with the latest data from the model.
     */
    private void updateDailyTotalsArea() {
        Map<String, Long> dailyMap = model.getDailyPlayTimeSeconds();
        List<String> dates = new ArrayList<>(dailyMap.keySet());
        Collections.sort(dates);
        StringBuilder sb = new StringBuilder();
        for (String date : dates) {
            sb.append(date).append(" : ").append(formatTime(dailyMap.get(date))).append("\n");
        }
        dailyTotalsTextArea.setText(sb.toString());
    }

    /**
     * Formats a time value in seconds into a string in the format HH:mm:ss.
     *
     * @param totalSeconds The total time in seconds.
     * @return The formatted time string.
     */
    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Formats a time value in seconds into a short string (e.g., "1h 30m").
     *
     * @param totalSeconds The total time in seconds.
     * @return The formatted short time string.
     */
    private String formatShortTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        if (hours == 0 && minutes == 0)
            return (totalSeconds % 60) + "s";
        else if (hours == 0)
            return String.format("%dm", minutes);
        else
            return String.format("%dh %dm", hours, minutes);
    }

    /**
     * A custom JPanel that displays time in a circular format.
     */
    private static class TimeCirclePanel extends JPanel {
        private String timeText = "0h 0m"; // Text to display inside the circle

        /**
         * Sets the time text to be displayed inside the circle.
         *
         * @param timeText The time text to display.
         */
        public void setTimeText(String timeText) {
            this.timeText = timeText;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int diameter = Math.min(getWidth(), getHeight());
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            g.setColor(Color.LIGHT_GRAY);
            g.fillOval(x, y, diameter, diameter);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(timeText);
            int textHeight = fm.getAscent();
            int textX = (getWidth() - textWidth) / 2;
            int textY = (getHeight() + textHeight) / 2 - 3;
            g.drawString(timeText, textX, textY);
        }
    }
}
