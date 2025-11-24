import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * <p> ShopGUI represents the graphical user interface for the shop in the game.
 * It allows players to view and purchase items from the shop catalog. </p>
 * 
 * @author Jonathan Lin (jlin764)
 * @author Chelsea Ye (cye68)
 * 
 */
public class ShopGUI extends JFrame {
    private Shop shop;
    private Font customFont;
    private Player player;
    private JLabel scoreLabel;

    /**
     * Constructs the ShopGUI with the specified save file.
     *
     * @param saveFile The save file to load the shop and player data.
     */
    public ShopGUI(String saveFile) {
        // Initialize shop and player
        this.shop = new Shop(saveFile);
        this.player = shop.getPlayer();

        // Load custom font
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.BOLD, 36);
        }

        // Set up JFrame properties
        setTitle("Inventory");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set up UI background and layout
        UIPanelBuilder builder = new UIPanelBuilder();
        JLabel timeLabel = builder.createLiveClockLabel(customFont);
        UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "back");
        setContentPane(layout.mainPanel);

        // Add action listener for the home button
        layout.homeButton.addActionListener(e-> {
            new PetGUI(player, saveFile);
            dispose();
        });

        // Create and configure header panel
        JLabel label = new JLabel("Shop");
        label.setFont(customFont.deriveFont(60f));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(label, BorderLayout.CENTER);

        // Create and configure score label
        scoreLabel = new JLabel("Score: " + player.getScore());
        scoreLabel.setFont(customFont.deriveFont(20f));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        // Create catalog panel to display shop items
        JPanel catalogPanel = new JPanel(new GridLayout(0, 6, 40, 40));
        catalogPanel.setOpaque(false);
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 20, 120));

        // Populate catalog panel with items
        for (InventoryObject item : shop.getCatalog()) {
            int quantity = item.getAmount();
            if (quantity <= 0) continue; // Skip items with 0 quantity

            // Create item panel
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setOpaque(false);
            itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Create item button with icon
            JButton itemButton = new JButton();
            itemButton.setContentAreaFilled(false);
            itemButton.setBorderPainted(false);
            itemButton.setFocusPainted(false);
            itemButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Load and scale item image
            String imagePath = "assets/items/" + item.getName().toLowerCase().replace(" ", "_") + ".png";
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaledIcon = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            itemButton.setIcon(new ImageIcon(scaledIcon));

            // Create item name label
            JLabel nameLabel = new JLabel(item.getName());
            nameLabel.setFont(customFont.deriveFont(14f));
            nameLabel.setForeground(Color.BLACK);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add components to item panel
            itemPanel.add(Box.createVerticalStrut(5)); // Spacing above
            itemPanel.add(itemButton);
            itemPanel.add(Box.createVerticalStrut(4)); // Spacing between icon and text
            itemPanel.add(nameLabel);

            // Set tooltip for item button
            itemButton.setToolTipText(
                "<html><b>" + item.getName() + "</b><br/>" +
                "Type: " + item.getType() + "<br/>" +
                "Stats: " + item.getStats() + "<br/>" +
                "Price: " + item.getPrice() + "</html>"
            );

            // Add action listener for item button
            itemButton.addActionListener(e-> {
                BuyItemDialog dialog = new BuyItemDialog(this, item, customFont);
                dialog.setVisible(true); // Show modal dialog

                if (dialog.isConfirmed()) {
                    int qty = dialog.getQuantity();
                    if (shop.purchaseItem(item.getName(), qty)) {
                        JOptionPane.showMessageDialog(this, "Purchase successful!");
                        scoreLabel.setText("Score: " + player.getScore());
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough points to buy this item.");
                    }
                }
            });

            catalogPanel.add(itemPanel);
        }

        // Create outer wrapper panel
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setOpaque(false);
        outerWrapper.add(catalogPanel, BorderLayout.CENTER);
        outerWrapper.add(headerPanel, BorderLayout.NORTH);
        layout.mainPanel.add(outerWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    /******************************************************************************************************/
    /**
     * Inner class representing the dialog for buying items.
     * Note: This component of the code was generated with AI assistance from ChatGPT and Anima
     */
    class BuyItemDialog extends JDialog {
        private boolean confirmed = false;
        private int quantity;
        private JTextField qtyField;

        /**
         * Constructs a BuyItemDialog for the specified item.
         *
         * @param parent     The parent JFrame.
         * @param item       The item to be purchased.
         * @param customFont The custom font to be used in the dialog.
         */
        public BuyItemDialog(JFrame parent, InventoryObject item, Font customFont) {
            super(parent, true);
            setUndecorated(true);
            setSize(350, 220);
            setLocationRelativeTo(parent);

            // Create background panel with custom image
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

            // Add title label
            JLabel titleLabel = new JLabel("Buy " + item.getName());
            titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
            titleLabel.setBounds(50, 20, 250, 30);
            titleLabel.setForeground(Color.BLACK);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bgPanel.add(titleLabel);

            // Add quantity label and text field
            JLabel qtyLabel = new JLabel("Quantity:");
            qtyLabel.setFont(customFont.deriveFont(16f));
            qtyLabel.setBounds(50, 70, 80, 30);
            qtyLabel.setForeground(Color.BLACK);
            bgPanel.add(qtyLabel);

            qtyField = new JTextField("1");
            qtyField.setFont(customFont.deriveFont(16f));
            qtyField.setBounds(140, 70, 50, 30);
            bgPanel.add(qtyField);

            // Add cost label
            int price = item.getPrice();
            JLabel costLabel = new JLabel("Price: " + price * 1 + " total");
            costLabel.setFont(customFont.deriveFont(14f));
            costLabel.setBounds(50, 110, 200, 30);
            costLabel.setForeground(Color.BLACK);
            bgPanel.add(costLabel);

            // Update cost label when quantity changes
            qtyField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                private void updateCost() {
                    try {
                        int qty = Integer.parseInt(qtyField.getText().trim());
                        costLabel.setText("Price: " + (price * qty) + " total");
                    } catch (NumberFormatException ex) {
                        costLabel.setText("Price: 0 total");
                    }
                }

                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    updateCost();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    updateCost();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    updateCost();
                }
            });

            // Add confirm button
            ImageIcon rawConfirmIcon = new ImageIcon("assets/confirm.png");
            Image scaledConfirm = rawConfirmIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JButton confirmBtn = new JButton(new ImageIcon(scaledConfirm));
            confirmBtn.setBounds(85, 160, 40, 40);
            confirmBtn.setContentAreaFilled(false);
            confirmBtn.setBorderPainted(false);
            confirmBtn.setFocusPainted(false);
            confirmBtn.addActionListener(e-> {
                try {
                    quantity = Integer.parseInt(qtyField.getText().trim());
                    confirmed = true;
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            });
            bgPanel.add(confirmBtn);

            // Add cancel button
            ImageIcon rawCancelIcon = new ImageIcon("assets/cancel.png");
            Image scaledCancel = rawCancelIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JButton cancelBtn = new JButton(new ImageIcon(scaledCancel));
            cancelBtn.setBounds(210, 160, 40, 40);
            cancelBtn.setContentAreaFilled(false);
            cancelBtn.setBorderPainted(false);
            cancelBtn.setFocusPainted(false);
            cancelBtn.addActionListener(e-> {
                confirmed = false;
                dispose();
            });
            bgPanel.add(cancelBtn);

            setContentPane(bgPanel);
        }

        /**
         * Returns whether the purchase was confirmed.
         *
         * @return true if confirmed, false otherwise.
         */
        public boolean isConfirmed() {
            return confirmed;
        }

        /**
         * Returns the quantity of the item to be purchased.
         *
         * @return The quantity entered by the user.
         */
        public int getQuantity() {
            return quantity;
        }
    }
/******************************************************************************************************/
    /**
     * Main method to launch the ShopGUI.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShopGUI("save1");
        });
    }
}
