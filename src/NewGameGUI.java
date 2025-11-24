import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * NewGameGUI is the graphical user interface that allows the player to start a new game
 * by choosing a pet (Baby Duck, Duck, or Goose), naming it, and initializing the game.
 * @author Jasmine Kumar (jkumar43)
 */
public class NewGameGUI extends JFrame {
  private Font customFont;
  private PetOptionPanel selectedPetPanel = null;

  /**
   * Constructor that sets up the entire new game screen, including font, layout, pet options, and selection logic.
   */
  public NewGameGUI() {
    setTitle("Duck Duck Goose - New Game");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setResizable(false);

    // Load custom font (same as MainMenuGUI)
    try {
      this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/IrishGrover-Regular.ttf"))
          .deriveFont(16f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(customFont);
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
      customFont = new Font("Serif", Font.BOLD, 36);
    }

    // Set up UI background using UIPanelBuilder.
    UIPanelBuilder builder = new UIPanelBuilder();
    JLabel timeLabel = builder.createLiveClockLabel(customFont);
    UIPanelBuilder.MainPanelComponents layout = builder.createMainPanel(customFont, timeLabel, "home");
    setContentPane(layout.mainPanel);

    layout.homeButton.addActionListener(e-> {
      dispose();
      new MainMenuGUI();
    });

    // ===== Center Panel: Pet Selection =====
    JPanel centerPanel = new JPanel();
    centerPanel.setOpaque(false); // Transparent so the background shows through
    centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

    // Create three pet option panels with updated descriptions using HTML formatting:
    PetOptionPanel petOption1 = new PetOptionPanel(
        "src/view/Pets/pet1/pet1_default.png",
        "Baby Duck",
        "<html><br><br>Due to its tender age, this duck requires a lot of care! It gets hungry really quickly and can't store much food at a time. " +
        "Lucky for us, this duck can keep itself busy, so its happiness doesn't drain very quickly.<br><br>" +
        "This pet is considered the <b>hard mode</b> of Duck Duck Goose.</html>",
        customFont
    );

    PetOptionPanel petOption2 = new PetOptionPanel(
        "src/view/Pets/pet2/pet2_default.png",
        "Duck",
        "<html><br><br>Our baby is all grown up! The adult duck boasts higher max stats and lower rates of depletion.<br><br>" +
        "This pet is considered the <b>normal mode</b> of Duck Duck Goose.</html>",
        customFont
    );

    PetOptionPanel petOption3 = new PetOptionPanel(
        "src/view/Pets/pet3/pet3_default.png",
        "Goose",
        "<html><br><br>As a migratory bird, the goose has become hardened to the adversities of long travel, boasting the best vital statistics in the game. " +
        "These same hardships have made it bitter and quick to anger... be careful!<br><br>" +
        "This pet is considered the <b>easy mode</b> of Duck Duck Goose.</html>",
        customFont
    );

    PetOptionPanel[] petPanels = { petOption1, petOption2, petOption3 };

    // Add mouse listeners to handle selection.
    for (PetOptionPanel petPanel : petPanels) {
      petPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          for (PetOptionPanel p : petPanels) {
            p.setSelected(false);
            p.setGreyed(true); // Grey out non-selected panels
          }
          petPanel.setSelected(true);
          petPanel.setGreyed(false); // Selected panel remains in full color
          selectedPetPanel = petPanel;
        }
      });
      centerPanel.add(petPanel);
    }
    layout.mainPanel.add(centerPanel, BorderLayout.CENTER);

    // Bottom Panel
    JPanel bottomPanel = new ImagePanel("assets/bar.png");
    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Select Button 
    ImageIcon selectIcon = new ImageIcon("assets/Frame 2.png");
    Image scaledSelect = selectIcon.getImage().getScaledInstance(300, 50, Image.SCALE_SMOOTH);
    JButton selectButton = new JButton(new ImageIcon(scaledSelect));
    selectButton.setContentAreaFilled(false);
    selectButton.setBorderPainted(false);
    selectButton.setFocusPainted(false);
    selectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Custom dialog for pet naming and game initialization.
    selectButton.addActionListener(e-> {
      if (selectedPetPanel != null) {
        NamePetDialog dialog = new NamePetDialog(this, customFont);
        dialog.setVisible(true); // Show the custom popup

        if (dialog.isConfirmed()) {
          // If user confirmed and name is valid
          String petName = dialog.getPetName();
          // Use NewGameInitializer to create a new game with the chosen pet type and name.
          NewGameInitializer.createNewGame(selectedPetPanel.getPetType().toLowerCase(), petName);
          dispose();
          
        }
        // If not confirmed or invalid, do nothing.
      } else {
        JOptionPane.showMessageDialog(
            NewGameGUI.this,
            "Please select a pet first.",
            "No Pet Selected",
            JOptionPane.WARNING_MESSAGE);
      }
    });
    bottomPanel.add(selectButton);
    layout.mainPanel.add(bottomPanel, BorderLayout.SOUTH);

    setVisible(true);
  }

  /**
   * ImagePanel is a custom JPanel that paints an image as its background.
   */
  class ImagePanel extends JPanel {
    private Image backgroundImage;

      /**
     * Constructs a panel with a background image.
     * @param imagePath Path to the image file.
     */
    public ImagePanel(String imagePath) {
      try {
        backgroundImage = ImageIO.read(new File(imagePath));
      } catch (Exception e) {
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
   * Represents a pet option panel with image, title, and description.
   * Handles selection and greying behavior for UI feedback.
   */
  class PetOptionPanel extends JPanel {
    private boolean greyed = false; // Flag for greyed state
    private String petType;
    private String petDescription;
    private InfoPanel infoPanel;

      /**
     * Constructs a new PetOptionPanel.
     * @param petImagePath Path to pet image.
     * @param petType Type of pet (e.g., "Duck").
     * @param petDescription Description (HTML supported).
     * @param font Font used for the panel.
     */
    public PetOptionPanel(String petImagePath, String petType, String petDescription, Font font) {
      this.petType = petType;
      this.petDescription = petDescription;
      setLayout(new BorderLayout());
      setOpaque(false); // Make the entire panel transparent

      // Center image of pet
      ImageIcon petIcon = new ImageIcon(petImagePath);
      Image scaledPetImage = petIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
      JLabel petImageLabel = new JLabel(new ImageIcon(scaledPetImage));
      petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
      petImageLabel.setOpaque(false);
      add(petImageLabel, BorderLayout.CENTER);

      // InfoPane that displays the rectangle background with pet type and description.
      infoPanel = new InfoPanel();
      add(infoPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets a pet panel as selected
     * @param selected a boolean indicaring the panel is marked as selected
     */
    public void setSelected(boolean selected) {
      if (selected) {
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
      } else {
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      }
      repaint();
    }

    /**
     * Repains a panel to have a grey border if it is marked as greyed.
     * @param greyed is a boolean indicating that a panel is greyed
     */
    public void setGreyed(boolean greyed) {
      this.greyed = greyed;
      repaint();
    }

    /**
     * Getter method for the pet's type from a given panel.
     * @return is a string indicating the pet's type
     */
    public String getPetType() {
      return petType;
    }

    public String getPetDescription() {
      return petDescription;
    }

    /**
     * Override paintChildren to add translucent overlay if greyed.
     */
    @Override
    protected void paintChildren(Graphics g) {
      super.paintChildren(g);
      if (greyed) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 100)); // Semi-transparent gray overlay
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
      }
    }


    /**
     * InfoPanel displays pet name and description over a background rectangle.
     */
    class InfoPanel extends JPanel {
      private Image rectangleImage;
      private JLabel titleLabel;
      private JLabel descriptionLabel;

      public InfoPanel() {
        // Load the rectangle image background.
        try {
          rectangleImage = ImageIO.read(new File("assets/Rectangle.png"));
        } catch (Exception e) {
          e.printStackTrace();
        }
        setLayout(new BorderLayout());
        setOpaque(false);

        // Adjust the preferred size if needed.
        setPreferredSize(new Dimension(300, 320));

        // Title label for the pet type, aligned at the top.
        titleLabel = new JLabel(petType);
        titleLabel.setFont(customFont);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);

        // Description label for the pet description; supports HTML formatting.
        descriptionLabel = new JLabel(petDescription);
        descriptionLabel.setFont(customFont.deriveFont(16f));
        descriptionLabel.setForeground(Color.BLACK);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);

        // Create a transparent panel to hold both labels with padding.
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textPanel.add(titleLabel);
        textPanel.add(descriptionLabel);

        add(textPanel, BorderLayout.NORTH);
      }

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rectangleImage != null) {
          g.drawImage(rectangleImage, 0, 0, getWidth(), getHeight(), this);
        }
      }
    }
  }


  /**
   * A popup dialog that allows the user to name their new pet.
   */
  class NamePetDialog extends JDialog {
    private boolean confirmed = false;
    private String petName;
    private JTextField nameField;

      /**
     * Constructs the naming dialog.
     * @param parent The parent frame.
     * @param customFont The font used throughout the dialog.
     */
    public NamePetDialog(JFrame parent, Font customFont) {
      super(parent, true); // modal dialog
      setUndecorated(true);
      setSize(400, 200);
      setLocationRelativeTo(parent);

      // Background panel.
      JPanel bgPanel = new JPanel() {
        Image bgImage = new ImageIcon("assets/popupBG.png").getImage();

        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
      };
      bgPanel.setLayout(null);
      bgPanel.setOpaque(false);

      // Title label.
      JLabel titleLabel = new JLabel("Name your pet");
      titleLabel.setFont(customFont.deriveFont(24f));
      titleLabel.setBounds(100, 10, 200, 30);
      titleLabel.setForeground(Color.BLACK);
      titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bgPanel.add(titleLabel);

      // Prompt label.
      JLabel promptLabel = new JLabel("Enter name:");
      promptLabel.setFont(customFont.deriveFont(16f));
      promptLabel.setBounds(80, 50, 250, 30);
      promptLabel.setForeground(Color.BLACK);
      bgPanel.add(promptLabel);

      // Name field.
      nameField = new JTextField();
      nameField.setFont(customFont.deriveFont(16f));
      nameField.setBounds(80, 80, 240, 30);
      bgPanel.add(nameField);

      // Confirm button.
      ImageIcon rawConfirmIcon = new ImageIcon("assets/confirm.png");
      Image scaledConfirm = rawConfirmIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
      JButton confirmBtn = new JButton(new ImageIcon(scaledConfirm));
      confirmBtn.setBounds(110, 130, 40, 40);
      confirmBtn.setContentAreaFilled(false);
      confirmBtn.setBorderPainted(false);
      confirmBtn.setFocusPainted(false);
      confirmBtn.addActionListener(e-> {
        String enteredName = nameField.getText().trim();
        if (enteredName.isEmpty()) {
          JOptionPane.showMessageDialog(
              this,
              "Please enter a name.",
              "No Name Entered",
              JOptionPane.WARNING_MESSAGE);
        } else {
          petName = enteredName;
          confirmed = true;
          dispose();
        }
      });
      bgPanel.add(confirmBtn);

      // Cancel button.
      ImageIcon rawCancelIcon = new ImageIcon("assets/cancel.png");
      Image scaledCancel = rawCancelIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
      JButton cancelBtn = new JButton(new ImageIcon(scaledCancel));
      cancelBtn.setBounds(230, 130, 40, 40);
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

    public boolean isConfirmed() {
      return confirmed;
    }

    public String getPetName() {
      return petName;
    }
  }
  
  /**
   * Launches the NewGameGUI for testing.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(NewGameGUI::new);
  }
}
