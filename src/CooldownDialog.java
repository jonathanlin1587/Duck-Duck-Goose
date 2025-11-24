import javax.swing.*;
import java.awt.*;

/**
 * A dialog window to inform the player that a command is on cooldown.
 * The dialog displays the command name and the remaining time until the command is available.
 * It uses similar styling to the RemoveItemDialog in InventoryGUI.
 * @author Jasmine Kumar (jkumar43)
 */
public class CooldownDialog extends JDialog {
    /**
     * Constructs a {@code CooldownDialog} to inform the user that an action is on cooldown.
     *
     * @param parent           The parent frame that opened this dialog.
     * @param actionName       The name of the action (e.g., "Feed", "Play") that is currently on cooldown.
     * @param secondsRemaining The number of seconds remaining before the action becomes available again.
     * @param customFont       A custom font to be used for the label text.
     */
    public CooldownDialog(JFrame parent, String actionName, int secondsRemaining, Font customFont) {
        super(parent, true);
        setUndecorated(true);  // Removes the default window borders, similar to the remove dialog.
        setSize(350, 180);
        setLocationRelativeTo(parent);

        // Background panel using an image asset.
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

        // Message label indicating the command is on cooldown.
        JLabel msgLabel = new JLabel("<html><center>" + actionName + " is on cooldown!<br/>Wait " + secondsRemaining + " seconds.</center></html>");
        msgLabel.setFont(customFont.deriveFont(16f));
        msgLabel.setBounds(40, 40, 270, 60);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        msgLabel.setForeground(Color.BLACK);
        bgPanel.add(msgLabel);

        // Confirmation button with a checkmark image (no cancel/x button).
        ImageIcon checkIcon = new ImageIcon(new ImageIcon("assets/confirm.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton okBtn = new JButton(checkIcon);
        okBtn.setBounds(155, 110, 40, 40);
        okBtn.setContentAreaFilled(false);
        okBtn.setBorderPainted(false);
        okBtn.addActionListener(e -> dispose());
        bgPanel.add(okBtn);

        setContentPane(bgPanel);
    }
}
