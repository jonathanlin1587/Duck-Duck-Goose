import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;

//This GUI class was created using ChatGPT
public class ParentalLimitations extends JPanel {
    private JCheckBox enableRestrictionsCheckBox;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JButton saveButton;
    private Font customFont;
    
    private ParentalSettings settings;
    
    public ParentalLimitations() {
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        settings = new ParentalSettings();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        enableRestrictionsCheckBox = new JCheckBox("Enable Parental Limitations");
        enableRestrictionsCheckBox.setFont(customFont.deriveFont(50f));
        enableRestrictionsCheckBox.setSelected(settings.isEnabled());
        
        SpinnerDateModel startModel = new SpinnerDateModel();
        startTimeSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startEditor);
        startTimeSpinner.setPreferredSize(new Dimension(150, 50));
        startTimeSpinner.setValue(settings.getStartTime());
        
        SpinnerDateModel endModel = new SpinnerDateModel();
        endTimeSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endEditor);
        endTimeSpinner.setPreferredSize(new Dimension(150, 50));
        endTimeSpinner.setValue(settings.getEndTime());
        
        saveButton = new JButton("Save Settings");
        saveButton.setFont(customFont.deriveFont(22f));
        saveButton.setPreferredSize(new Dimension(300, 70));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(enableRestrictionsCheckBox, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Allowed Start Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        add(startTimeSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Allowed End Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        add(endTimeSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);
        
        // Save settings when the button is clicked.
        saveButton.addActionListener((ActionEvent e) -> {
            settings.setEnabled(enableRestrictionsCheckBox.isSelected());
            settings.setStartTime((Date) startTimeSpinner.getValue());
            settings.setEndTime((Date) endTimeSpinner.getValue());
            settings.saveSettings();
            JOptionPane.showMessageDialog(this, "Settings saved successfully.", 
                                          "Info", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    public static void showTimeLimitPopup(Component parent) {
        JPanel panel = new JPanel() {
            private final Image bgImage = new ImageIcon("assets/time_limit.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setPreferredSize(new Dimension(450, 250));
        JOptionPane.showMessageDialog(parent, panel, "Time Limit Reached", JOptionPane.PLAIN_MESSAGE);
    }
}
