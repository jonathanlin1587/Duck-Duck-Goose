import javax.swing.SwingUtilities;

/**
 * The Main class serves as the entry point for the application.
 * It initializes the parental statistics model, checks parental settings,
 * and starts the main menu GUI if usage is allowed. It also sets up a shutdown hook
 * to properly end the session when the application terminates.
 * @author Jessamine Li
 */
public class Main {
    private static ParentalStatisticsModel statsModel; //The parental statistics model for tracking usage statistics


    /**
     * The main method that serves as the entry point of the application.
     * It performs the following actions:
     * <ul>
     *   <li>Initializes the parental statistics model.</li>
     *   <li>Checks the parental settings to determine if playing is allowed.</li>
     *   <li>If not allowed, displays a time limit popup and exits the application.</li>
     *   <li>If allowed, starts the main menu GUI on the Event Dispatch Thread.</li>
     *   <li>Adds a shutdown hook to end the session and record statistics upon termination.</li>
     * </ul>
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        statsModel = new ParentalStatisticsModel();
        ParentalSettings settings = new ParentalSettings();
        if (!settings.isAllowedToPlay()) {
            ParentalLimitations.showTimeLimitPopup(null);
            System.exit(0);
        }

        SwingUtilities.invokeLater(MainMenuGUI::new);  // Launch the main menu GUI 

        Runtime.getRuntime().addShutdownHook(new Thread(() -> statsModel.endSession()));
    }

     /**
     * Retrieves the current instance of the {@code ParentalStatisticsModel}.
     * <p>
     * If the model is {@code null}, a new instance is created (this is primarily
     * for testing purposes).
     * </p>
     *
     * @return the current {@code ParentalStatisticsModel} instance.
     */
    public static ParentalStatisticsModel getStatsModel() {
        if (statsModel == null) {
            System.out.println("Persistent model was null; creating a new instance for testing.");
            statsModel = new ParentalStatisticsModel();
        }
        return statsModel;
        
    }
}
