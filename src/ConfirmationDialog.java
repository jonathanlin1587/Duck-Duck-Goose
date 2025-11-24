
import javax.swing.*;
import java.awt.*;
/**
 * This class was generated using ChatGPT
 */
public class ConfirmationDialog extends JDialog {
    private boolean confirmed = false;

    public ConfirmationDialog(JFrame parent, String title, String message, String confirmIconPath, String cancelIconPath, Font customFont) {
        super(parent, true);
        setUndecorated(true);
        setSize(350, 220);
        setLocationRelativeTo(parent);

        // Background panel
        JPanel bgPanel = new JPanel() {
            Image bg = new ImageIcon("assets/popupBG.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);
        bgPanel.setOpaque(false);

        // Title Label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        titleLabel.setBounds(50, 20, 250, 30);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bgPanel.add(titleLabel);

        // Message Label
        JLabel msgLabel = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>");
        msgLabel.setFont(customFont.deriveFont(14f));
        msgLabel.setBounds(50, 60, 250, 60);
        msgLabel.setForeground(Color.BLACK);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bgPanel.add(msgLabel);

    // Check if a cancel icon was provided.
    if (cancelIconPath != null && !cancelIconPath.isEmpty()) {
        // Confirm Button
        JButton confirmBtn = new JButton(new ImageIcon(new ImageIcon(confirmIconPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        confirmBtn.setBounds(85, 140, 50, 50);
        confirmBtn.setContentAreaFilled(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        bgPanel.add(confirmBtn);

        // Cancel Button
        JButton cancelBtn = new JButton(new ImageIcon(new ImageIcon(cancelIconPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        cancelBtn.setBounds(215, 140, 50, 50);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        bgPanel.add(cancelBtn);
    } else {
        
        // Only one button - center it horizontally.
        JButton confirmBtn = new JButton(new ImageIcon(new ImageIcon(confirmIconPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        // Center: (350 - 50) / 2 = 150
        confirmBtn.setBounds(150, 140, 50, 50);
        confirmBtn.setContentAreaFilled(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        bgPanel.add(confirmBtn);
    }

        setContentPane(bgPanel);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
