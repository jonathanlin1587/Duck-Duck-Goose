import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The PetGUI class takes care of displaying the GUI components of the pet screen including the command, inventory, shop, and save buttons
 * as well as the various pet icons, status bars, and the player's score.
 * @author Jasmine Kumar (jkumar43)
 */
public class PetGUI extends JFrame {
    public static final int WIDTH = 1081;
    public static final int HEIGHT = 721;
    public static final int MAX_FILL_WIDTH = 192;

    private JLabel scoreLabel;
    private Player currentPlayer;
    private String saveFile;
    private Font customFont;
    private PetIcon petIcon;
    private String lastState = "";

    // Status bars for the pet's vital stats – stored so they can be updated
    private StatusBar loveBar;
    private StatusBar healthBar;
    private StatusBar fullnessBar;
    private StatusBar sleepBar;
    private StatusBar happinessBar;
    private ScheduledExecutorService sleepExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService exerciseExecutor = Executors.newSingleThreadScheduledExecutor();

    public PetGUI(Player player, String saveFile) {
        this.saveFile = saveFile;
        currentPlayer = player;

        setTitle("Pet GUI");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // Import custom font "Irish Grover"
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }


        // Set up UI background
        UIPanelBuilder builder = new UIPanelBuilder();

        JLabel timeLabel = builder.createLiveClockLabel(customFont);
        UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "save");

        // Create a transparent overlay panel for manual positioning
        JPanel overlayPanel = new JPanel(null);
        overlayPanel.setOpaque(false);
        layout.mainPanel.add(overlayPanel, BorderLayout.CENTER); 

        setContentPane(layout.mainPanel);

        // Add an action listener to the "home" button (called that because of the UI Builder class, is a save button)
        layout.homeButton.addActionListener(e-> {
            saveAction();
        });


        // Select correct pet image based on type
        String petType = player.getPet().getTypeString().toLowerCase();

        // Add the right pet icon to the screen 
        this.petIcon = new PetIcon(petType);
        petIcon.setBounds(230, 170, 360, 360);
        overlayPanel.add(petIcon);

        // Panel for the score
        JPanel scorePanel = new JPanel(null);
        scorePanel.setOpaque(false);
        scorePanel.setBounds(848, 68, 196, 61);

        // Score text along with a banner in the background
        JLabel scoreBanner = new JLabel(new ImageIcon("src/view/gameAssets/score.png"));
        scoreBanner.setBounds(0, 0, 220, 69);
        scoreLabel = new JLabel("Score: " + currentPlayer.getScore());
        scoreLabel.setFont(customFont.deriveFont(20f));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(70,20, 150, 24);
        scorePanel.add(scoreLabel);
        scorePanel.add(scoreBanner);
        overlayPanel.add(scorePanel);

        // Shop Button
        JButton shopButton = new JButton(new ImageIcon("src/view/gameAssets/Shop button.png"));
        shopButton.setBounds(7, 0, 193, 99);
        shopButton.setOpaque(false);
        shopButton.setBorderPainted(false);
        shopButton.setContentAreaFilled(false);
        overlayPanel.add(shopButton);
        shopButton.addActionListener(e-> showShopWindow());

        // Inventory Button
        JButton inventoryButton = new JButton(new ImageIcon("src/view/gameAssets/Inventory button.png"));
        inventoryButton.setBounds(206, 0, 188, 100);
        inventoryButton.setOpaque(false);
        inventoryButton.setBorderPainted(false);
        inventoryButton.setContentAreaFilled(false);
        overlayPanel.add(inventoryButton);
        inventoryButton.addActionListener(e-> showInventoryWindow());

        // Status Bars
        JPanel statusBarsPanel = new JPanel(null);
        statusBarsPanel.setBounds(789, 180, 257, 276);
        statusBarsPanel.setOpaque(false);
        overlayPanel.add(statusBarsPanel);

        // Create status bars using the createStatusBar function 
        loveBar = createStatusBar(
            "barBackground.png", "heartIcon.png", 0, 0,
            (double) currentPlayer.getPet().getLove() / currentPlayer.getPet().getType().getMaxLove()
        );
        healthBar = createStatusBar(
            "barBackground-1.png", "healthIcon.png", 0, 55,
            (double) currentPlayer.getPet().getHealth() / currentPlayer.getPet().getType().getMaxHealth()
        );
        fullnessBar = createStatusBar(
            "barBackground-2.png", "fullnessIcon.png", 0, 110,
            (double) currentPlayer.getPet().getFullness() / currentPlayer.getPet().getType().getMaxFullness()
        );
        sleepBar = createStatusBar(
            "barBackground-3.png", "energyIcon.png", 0, 165,
            (double) currentPlayer.getPet().getSleep() / currentPlayer.getPet().getType().getMaxSleep()
        );
        happinessBar = createStatusBar(
            "barBackground-4.png", "happinessIcon.png", 0, 224,
            (double) currentPlayer.getPet().getHappiness() / currentPlayer.getPet().getType().getMaxHappiness()
        );

        // Add actual bars to the status bar panel
        statusBarsPanel.add(loveBar);
        statusBarsPanel.add(healthBar);
        statusBarsPanel.add(fullnessBar);
        statusBarsPanel.add(sleepBar);
        statusBarsPanel.add(happinessBar);


        // Start status drain (This instantiation of StatDrainer was generated by ChatGPT)
        /******************************************************************************************************/
        StatDrainer drainer = new StatDrainer(
            currentPlayer.getPet(),
            () -> {
                updateStatusBars();
                updatePetIcon();
            },
            () -> SwingUtilities.invokeLater(this::sleepAction) // New callback to trigger auto sleep
        );
        drainer.start();
        //************************************************************************************************** */


        // Command Buttons
        JPanel commandButtonsPanel = new JPanel(null);
        commandButtonsPanel.setBounds(120, 480, 775, 118);
        commandButtonsPanel.setOpaque(false);
        
        overlayPanel.add(commandButtonsPanel);

        int buttonSpacing = 16;
        int xOffset = 0;

        // Array of command button assets to streamline creation better
        String[] buttonNames = {
            "sleep button.png", "play button.png", "exercise button.png",
            "feed button.png", "vet button.png", "gift button.png"
        };

        // Corresponding array of action listeners
        ActionListener[] actions = {
            e-> sleepAction(), e-> playAction(), e-> exerciseAction(),
            e-> feedAction(), e-> vetAction(), e-> giftAction()
        };

        //  Create and add command buttons to the screen
        for (int i = 0; i < buttonNames.length; i++) {
            JButton btn = createCommandButton(buttonNames[i], xOffset, 0, 115, 118, actions[i]);
            commandButtonsPanel.add(btn);
            xOffset += 115 + buttonSpacing;
        }

        // Add keyboard shortcuts to some of the buttons
        addKeyBindings();
        setVisible(true);
    }

    /**
     * This function creates a command button with the appropriate asset, spacing and action listener.
     * @param imageFile name of the image corresponding to the button
     * @param x x-cordinate of the button
     * @param y y-coordinate of the button
     * @param width width of the button
     * @param height height of the button
     * @param action command corresponding to the command button
     * @return
     */
    private JButton createCommandButton(String imageFile, int x, int y, int width, int height, ActionListener action) {
        JButton button = new JButton(new ImageIcon("src/view/gameAssets/" + imageFile));
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(action);
        return button;
    }

    /**
     * This function creates a custom status bar with a corresponding image, and fills it with the appropriate fill percentage and x and y bounds.
     * @param barImg the image corresponding to the bar background
     * @param iconImg the icon of the status bar
     * @param x the x-coordinate of the status bar
     * @param y the y-coordinate of the status bar
     * @param fill the fill percentage of the status bar (0-100%)
     * @return
     */
    private StatusBar createStatusBar(String barImg, String iconImg, int x, int y, double fill) {
        StatusBar bar = new StatusBar("src/view/gameAssets/" + barImg, "src/view/gameAssets/" + iconImg);
        bar.setBounds(x, y, 257, 52);
        bar.setFillPercentage(fill);
        return bar;
    }

    /**
     * This function can be used to update the player's score and it adjusts the text on the score banner in addition to the actual player score.
     * @param newScore the new player score
     */
    public void updatePlayerScore(int newScore) {
        currentPlayer.adjustScore(newScore);
        scoreLabel.setText("Score: " + currentPlayer.getScore());
    }

    /**
     * Helper function that helps to compartmentalize the addition of keyboard bindings to actions such feed, gift, save, and sleep
     */
    private void addKeyBindings() {
        JRootPane rootPane = this.getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        // Feed command can be activated with F
        im.put(KeyStroke.getKeyStroke("F"), "feedAction");
        am.put("feedAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                feedAction();
            }
        });

        // Gift command can be activated with G
        im.put(KeyStroke.getKeyStroke("G"), "giftAction");
        am.put("giftAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                giftAction();
            }
        });

        // Save action can be activated with the escape key
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "saveAction");
        am.put("saveAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        });

        // Sleep command can be activated with the S key
        im.put(KeyStroke.getKeyStroke("S"), "sleepAction");
        am.put("sleepAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                sleepAction();
            }
        });
    }

    /**
     * This method was created with the help of ChatGPT generated code.
     */
    //******************************************************************************************** */
    private void sleepAction() { 

    sleepExecutor = Executors.newSingleThreadScheduledExecutor();

    currentPlayer.getPet().setState("sleep");
    petIcon.changePetIcon("sleep");

    sleepExecutor.scheduleAtFixedRate(() -> {
        Pet pet = currentPlayer.getPet();
        if (pet.getSleep() < pet.getType().getMaxSleep()) {
            Command.sleep(pet);
            SwingUtilities.invokeLater(this::updateStatusBars);
        } else {
            pet.setState("default");
            petIcon.changePetIcon("default");
            sleepExecutor.shutdown();
        }
    }, 0, 1, TimeUnit.SECONDS);
    
    updatePlayerScore(5);
    System.out.println("Sleep action triggered (auto/manual).");
    }
    //*********************************************************************************************** */

    /**
     * This method checks if the pllay action is on cooldown, if it is a dialog window with remaining cooldown is displayed, 
     * and if not the play command is executed and the status bars along with the pet icon are updated.
     */
    private void playAction() { 

    // Check if the play command is on cooldown.
    if (currentPlayer.getPet().isCommandOnCooldown("play")) {
        int remaining = currentPlayer.getPet().getRemainingCooldown("play");
        // Open a dialog informing the user that the command is on cooldown.
        new CooldownDialog(this, "Play", remaining, customFont).setVisible(true);
        return;
    }

        // Play is invalid if happiness is already full
        if (currentPlayer.getPet().getHappiness() == currentPlayer.getPet().getType().getMaxHappiness()) {
            commandInvalidDialog("play"); // Open dialog window saying that play is invalid
        } else {                                    
            Command.play(currentPlayer.getPet());   // Otherwise use the play command and update pet, score, and status bars
            updatePlayerScore(5);
            updateStatusBars();
            updatePetIcon();
        }
        System.out.println("Play action triggered."); 
    }

    /**
     * This is the method corresponding to the exercise button. 
     * This code was created with the help of ChatGPT generated code.
     */
    //************************************************************************************************************************* */
    private void exerciseAction() { 
        // Open invalid command dialog window if a state is preventing the pet from exercise
        if (currentPlayer.getPet().isDead() || currentPlayer.getPet().isSleeping() || currentPlayer.getPet().isHungry() || currentPlayer.getPet().isHealthy()) {
            commandInvalidDialog("exercise");
            return;
        }

        exerciseExecutor = Executors.newSingleThreadScheduledExecutor();
    
        petIcon.changePetIcon("exercise");
    
        exerciseExecutor.scheduleAtFixedRate(new Runnable() {
            private int exerciseCount = 0;
    
            @Override
            public void run() {
                if (exerciseCount < 3) {
                    Command.exercise(currentPlayer.getPet());
                    SwingUtilities.invokeLater(() -> {
                        updateStatusBars();
                        updatePetIcon();
                    });
                    exerciseCount++;
                } else {
                    currentPlayer.getPet().setState("default");
                    SwingUtilities.invokeLater(() -> {
                        updateStatusBars();
                        petIcon.changePetIcon("default");
                    });
                    exerciseExecutor.shutdown();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        
        updatePlayerScore(4);
        
        System.out.println("Exercise action triggered."); 
    }
    //************************************************************************************************************************* */

    /**
     * This action corresponds to the feed button and invokes the feed command on the current pet.
     */
    private void feedAction() {
        // Open the inventory to let the user select a food item then update the pet and status bars
        currentPlayer.saveToFile(saveFile);
        new InventoryGUI(saveFile);
        dispose();
        updateStatusBars();
        updatePetIcon();
         System.out.println("Feed action triggered."); 
        }

    /**
     * A function to create a more customized Confirmation dialog window with a custom message of  "You cannot use <command> right not..."
     * @param command is the command to be displayed as invalid.
     */
    private void commandInvalidDialog(String command){
        ConfirmationDialog ConfirmDialog = new ConfirmationDialog(
            this,
            "Command Invalid!",
            "You cannot use " + command + " command right now...",
            "assets/confirm.png",
            null,
            customFont
        );
        ConfirmDialog.setVisible(true);
    }
    /**
    * This function correcponds to the vet button.
    */
    private void vetAction() {
        // Check if the vet command is on cooldown.
        if (currentPlayer.getPet().isCommandOnCooldown("vet")) {
            int remaining = currentPlayer.getPet().getRemainingCooldown("vet");
            // Open a dialog informing the user that the command is on cooldown.
            new CooldownDialog(this, "Vet", remaining, customFont).setVisible(true);
            return;
        }

        // Only use the vet command if the pet's state allows for it, otherwise call a dialog window
        if (!currentPlayer.getPet().getRecentCommand() || currentPlayer.getPet().getHealth() == currentPlayer.getPet().getType().getMaxHealth()) {
            commandInvalidDialog("vet");
        } else {
            Command.vet(currentPlayer.getPet());
            updatePlayerScore(-20);
            updateStatusBars();
            updatePetIcon();
            System.out.println("Vet action triggered."); 
        }
        }

    /**
     * This function corresponds to the gift command and opens the inventory screen.
     */
    private void giftAction() { 
        // Open the inventory to let the user select a gift item then update the pet and status bars
        currentPlayer.saveToFile(saveFile); //Save current data before closing screen
        new InventoryGUI(saveFile);
        dispose();

        // Update the status bar and icon after
        updateStatusBars();
        updatePetIcon();
        System.out.println("Gift action triggered."); 
    }

    /**
     * Updates the pet icon based on the pet's type and current state (state must be different in order to update to prevent the animation from glitching).
     **/
    private void updatePetIcon() {
        String currentState = currentPlayer.getPet().getState().toLowerCase(); // Only update the icon if the state has actually changed. 
        if (!currentState.equals(lastState)) {            
            petIcon.changePetIcon(currentState); 
            lastState = currentState; 
        }
        
    }

    /**  
     * Updates each status bar's fill percentage based on the pet's current stats.
     **/
    private void updateStatusBars() {
        double loveFill = (double) currentPlayer.getPet().getLove() / currentPlayer.getPet().getType().getMaxLove(); // Calculat stat fill percentage using the pet's max stat
        loveBar.setFillPercentage(loveFill);                                                                         // Set the status bar to the corresponding fill
        double healthFill = (double) currentPlayer.getPet().getHealth() / currentPlayer.getPet().getType().getMaxHealth();
        healthBar.setFillPercentage(healthFill);
        double fullnessFill = (double) currentPlayer.getPet().getFullness() / currentPlayer.getPet().getType().getMaxFullness();
        fullnessBar.setFillPercentage(fullnessFill);
        double sleepFill = (double) currentPlayer.getPet().getSleep() / currentPlayer.getPet().getType().getMaxSleep();
        sleepBar.setFillPercentage(sleepFill);
        double happinessFill = (double) currentPlayer.getPet().getHappiness() / currentPlayer.getPet().getType().getMaxHappiness();
        happinessBar.setFillPercentage(happinessFill);
    }

    /**
     * Action corresponding to the save button. Creates and opens a new save game screen and closes the pet GUI.
     */
    private void saveAction() {
        SaveGameGUI saveGUI = new SaveGameGUI(currentPlayer);
        saveGUI.setVisible(true);  // Open the save GUI
        dispose();  // Close current PetGUI after saving
    }

    /**
     * Action corresponding to the inventory button, saves current game and opens the inventory screen.
     */
    private void showInventoryWindow() {
        // Launch the InventoryGUI, which will build the UI based on the inventory data
        currentPlayer.saveToFile(saveFile);
        new InventoryGUI(saveFile);
        dispose();
    }

    /**
     * Action corresponding to the shop button, saves current game and opens the inventory screen.
     */
    private void showShopWindow() {
        // Launch the ShopGUI, which will build the UI based on the shop data
        currentPlayer.saveToFile(saveFile);
        new ShopGUI(saveFile);
        dispose();
    }
    

     /** The status bar class represents a status bar created as a custom JPanel.
     * The panel contains a background image, an icon image, as well as a fill panel that changes color depending on its fill percentage.
     */
    class StatusBar extends JPanel {
        private JPanel fillPanel;
        private final int fillX = 58;
        private final int fillY = 17;
        private final int fillHeight = 18;
        private double fillPercentage = 1.0;

        /**
         * Constructor for StatusBar, requires a background image as well as the image of the status bar icon.
         */
        public StatusBar(String bgImageFile, String iconImageFile) {
            setLayout(null);
            setOpaque(false);
        
            // Fill bar first so we can layer it on top later
            fillPanel = new JPanel();
            fillPanel.setBackground(Color.GREEN);
            fillPanel.setBounds(fillX, fillY, (int)(MAX_FILL_WIDTH * fillPercentage), fillHeight);
            add(fillPanel); // add first
        
            JLabel backgroundLabel = new JLabel(new ImageIcon(bgImageFile));
            backgroundLabel.setBounds(48, 0, 209, 52);
            add(backgroundLabel);
        
            JLabel iconLabel = new JLabel(new ImageIcon(iconImageFile));
            iconLabel.setBounds(0, 4, 40, 40);
            add(iconLabel);
        
            // Adjust the z-order to bring fillPanel to the front
            setComponentZOrder(fillPanel, 0); // top
            setComponentZOrder(iconLabel, 1); // middle
            setComponentZOrder(backgroundLabel, 2); // bottom
        }

        /**
         * Function that adjusts the green (or red) fill of the staus bar
         * @param percentage is the new fill percentage of the of the status bar (0-1)
         */
        public void setFillPercentage(double percentage) {
            fillPercentage = Math.max(0.0, Math.min(1.0, percentage)); // Ensure percentage is  a double from 0-1
            int newWidth = (int)(MAX_FILL_WIDTH * fillPercentage); // Calculate the new wi
            fillPanel.setBounds(fillX, fillY, newWidth, fillHeight);
            
            // Change color based on percentage threshold (25% or less becomes red)
            if (fillPercentage <= 0.25) {
                fillPanel.setBackground(Color.RED);
            } else {
                fillPanel.setBackground(Color.GREEN);
            }
            fillPanel.repaint();
        }
    }


    /**
     * A main method to test the PetGUI.
     * save1 is used as the default. 
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Player player = new Player("save1");
            new PetGUI(player, "save1");
        });
    }
    

}
