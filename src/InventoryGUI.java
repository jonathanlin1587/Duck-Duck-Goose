import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * <p>A GUI window that displays the player's inventory in a grid-based layout.
 * Allows interaction with items, such as using food or gifts on a pet.
 * Built using Java Swing and custom fonts/images for styling.</p>
 * 
 * <p>Clicking on an item opens a confirmation popup, and if confirmed,
 * updates the pet's state, inventory, and player's score accordingly.</p>
 * 
 * @author Chelsea Ye (cye68)
 */
public class InventoryGUI extends JFrame {
    private Inventory inventory;
    private Player player;
    private Font customFont;
    
    /**
     * Constructs the Inventory GUI using the player's save file.
     * Initializes the player's inventory, pet, and applies custom UI styling.
     * 
     * @param saveFile the name of the save file to load data from
     */
    public InventoryGUI(String saveFile) {

        this.player = new Player(saveFile);
        Pet pet = player.getPet();
        this.inventory = player.getInventory();

        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }


            setTitle("Inventory");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1080, 720);
            setLocationRelativeTo(null);
            setResizable(false);

            // Set up UI background
            UIPanelBuilder builder = new UIPanelBuilder();

            JLabel timeLabel = builder.createLiveClockLabel(customFont);
            UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "back");

            setContentPane(layout.mainPanel);

            layout.homeButton.addActionListener(e -> {
                new PetGUI(player, saveFile);
                dispose();
            });

            // Label
            JLabel label = new JLabel("Inventory");
            label.setFont(customFont.deriveFont(60f));
            label.setForeground(Color.WHITE); 
            label.setHorizontalAlignment(SwingConstants.CENTER); // centers text in label

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            headerPanel.add(label, BorderLayout.CENTER);

            // Inventory Grid Panel
            JPanel catalogPanel = new JPanel(new GridLayout(0, 6, 40, 40));
            catalogPanel.setOpaque(false);
            catalogPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 20, 120));

            for (InventoryObject item : inventory.getItems()) {
                int quantity = item.getAmount();
                if (quantity <= 0) continue; // Skip items with 0 qty
            
                // Outer panel for each item in the grid
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                itemPanel.setOpaque(false);
                itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Icon button
                JButton itemButton = new JButton();
                itemButton.setContentAreaFilled(false);
                itemButton.setBorderPainted(false);
                itemButton.setFocusPainted(false);
                itemButton.setBounds(0, 0, 80, 80);

                // Load and scale image
                String imagePath = "assets/items/" + item.getName().toLowerCase().replace(" ", "_") + ".png";
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledIcon = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                itemButton.setIcon(new ImageIcon(scaledIcon));

                // Quantity label
                JLabel qtyLabel = new JLabel("x" + quantity);
                qtyLabel.setFont(customFont.deriveFont(14f));
                qtyLabel.setForeground(Color.BLACK);
                qtyLabel.setBounds(60, 55, 30, 20); // bottom right corner-ish

                // Container to hold icon + quantity overlay
                JPanel iconWrapper = new JPanel(null);
                iconWrapper.setPreferredSize(new Dimension(80, 80));
                iconWrapper.setMaximumSize(new Dimension(80, 80));
                iconWrapper.setMinimumSize(new Dimension(80, 80));
                iconWrapper.setOpaque(false);
                
                iconWrapper.add(qtyLabel);
                iconWrapper.add(itemButton);

                // Label below
                JLabel nameLabel = new JLabel(item.getName());
                nameLabel.setFont(customFont.deriveFont(14f));
                nameLabel.setForeground(Color.BLACK);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Add vertical spacing and set size
                itemPanel.add(Box.createVerticalStrut(5)); // small spacing above
                itemPanel.add(iconWrapper);
                itemPanel.add(Box.createVerticalStrut(4)); // spacing between icon and text
                itemPanel.add(nameLabel);
            
                // Tooltip on hover
                itemButton.setToolTipText(
                    "<html><b>" + item.getName() + "</b><br/>" +
                    "Type: " + item.getType() + "<br/>" +
                    "Stats: " + item.getStats() + "<br/>" +
                    "Quantity: " + quantity + "</html>"
                );
            
                // Remove confirmation on click
                itemButton.addActionListener(e -> {
                    RemoveItemDialog dialog = new RemoveItemDialog(this, item.getName(), item.getType(), customFont);
                    dialog.setVisible(true); // pauses here until user closes it

                    if (dialog.isConfirmed()) {
                        if (item.getType().equals("Food")){
                            if (pet.isAngry() || pet.isDead() || pet.isSleeping()) {
                                ConfirmationDialog confirm = new ConfirmationDialog(this, "Action Failed", "Your pet is "+ pet.getState() + "!", 
                                "assets/confirm.png", null, customFont);
                                confirm.setAlwaysOnTop(true);
                                confirm.setVisible(true);
            
                            } else {
                                pet.changeFullness(item.getStats());
                                inventory.removeItem(item.getName());
                                player.adjustInventory(inventory);
                                player.adjustScore(3);
                            }
                        } else {
                            if (pet.isDead() || pet.isSleeping()) {
                                ConfirmationDialog confirm = new ConfirmationDialog(this, "Action Failed", "Your pet is "+ pet.getState() + "!", 
                                "assets/confirm.png", null, customFont);
                                confirm.setAlwaysOnTop(true);
                                confirm.setVisible(true);
                            } else {
                                pet.changeHappiness(item.getStats());
                                inventory.removeItem(item.getName());
                                player.adjustInventory(inventory);
                                player.adjustScore(2);
                            }                        
                        }
                        new PetGUI(player, saveFile);
                        dispose();
                    }
                });
            
                catalogPanel.add(itemPanel);
            }

   
            // Outer wrapper for content alignment
            JPanel outerWrapper = new JPanel(new BorderLayout());
            outerWrapper.setOpaque(false); // So background shows through
            outerWrapper.add(catalogPanel, BorderLayout.CENTER);
            outerWrapper.add(headerPanel, BorderLayout.NORTH);
            layout.mainPanel.add(outerWrapper, BorderLayout.CENTER);
            

            setVisible(true);

    }

    /**
     * A modal confirmation dialog that appears when a user clicks on an inventory item.
     * Asks the user to confirm using the item (e.g. feeding or gifting).
     * 
     * The dialog is styled with a custom background and image buttons for confirmation.
     */
    public class RemoveItemDialog extends JDialog {
        private boolean confirmed = false;
    
        /**
         * Constructs a custom confirmation dialog.
         * Displays a question based on the item name and type, with confirm/cancel buttons.
         * 
         * @param parent the parent JFrame (InventoryGUI)
         * @param itemName the name of the item being considered
         * @param itemType the type of the item (e.g. "Food" or "Gift")
         * @param customFont the custom font used for styling
         */
        public RemoveItemDialog(JFrame parent, String itemName, String itemType, Font customFont) {
            super(parent, true); // modal
            setUndecorated(true); // no default borders
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
    
            // Title label
            String actionName = switch (itemType){
                case "Food" -> "Feed";
                case "Gift" -> "Give";
                default -> null;
            };

            JLabel titleLabel = new JLabel(actionName + " " + itemName + "?");
            titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
            titleLabel.setBounds(50, 20, 250, 30);
            titleLabel.setForeground(Color.BLACK);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bgPanel.add(titleLabel);
    
            // Description label
            JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" + actionName+ " one " + itemName + " from<br>your inventory</div></html>");
            descLabel.setFont(customFont.deriveFont(14f));
            descLabel.setBounds(50, 60, 250, 60);
            descLabel.setHorizontalAlignment(SwingConstants.CENTER);
            descLabel.setForeground(Color.BLACK);
            bgPanel.add(descLabel);
    
            // Confirm button
            ImageIcon rawConfirmIcon = new ImageIcon("assets/confirm.png");
            Image scaledConfirm = rawConfirmIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JButton yesBtn = new JButton(new ImageIcon(scaledConfirm));
            yesBtn.setBounds(85, 140, 40, 40);
            yesBtn.setContentAreaFilled(false);
            yesBtn.setBorderPainted(false);
            yesBtn.setFocusPainted(false);
            yesBtn.addActionListener(e -> {
                confirmed = true;
                dispose();
            });
            bgPanel.add(yesBtn);


            // Cancel button
            ImageIcon rawCancelIcon = new ImageIcon("assets/cancel.png");
            Image scaledCancel = rawCancelIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JButton noBtn = new JButton(new ImageIcon(scaledCancel));
            noBtn.setBounds(210, 140, 40, 40);
            noBtn.setContentAreaFilled(false);
            noBtn.setBorderPainted(false);
            noBtn.setFocusPainted(false);
            noBtn.addActionListener(e -> {
                confirmed = false;
                dispose();
            });
            bgPanel.add(noBtn);
    
            setContentPane(bgPanel);
        }
    
        /**
         * Returns whether the user confirmed the item removal.
         * 
         * @return true if the user clicked the confirm button, false otherwise
         */
        public boolean isConfirmed() {
            return confirmed;
        }
    }
    

    /**
     * Launches the Inventory GUI using a test save file ("save1").
     * Should be called from the main method of the application or for testing.
     * Runs on the Event Dispatch Thread (EDT).
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Ensure Swing components are created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            new InventoryGUI("save1");
        });
    }
}