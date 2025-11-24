import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The {@code ParentalStatisticsModel} class tracks game play statistics including total playtime,
 * session counts, daily screen time, and downttime between sessions.
 * <p>
 * Statistics are loaded from and saved to the statistics.properties file. 
 * </p>
 * @author Jessamine Li
 */

public class ParentalStatisticsModel {
    private long totalPlayTimeSeconds; // Total play time across sessions
    private int sessionCount; // Counts how many sessions have been played
    private long sessionStartTime; // Time when game starts
    private Map<String, Long> dailyPlayTimeSeconds; // key: yyyy-MM-dd, value: seconds played
    private long sessionEndTime; // The timestamp (in milliseconds) when the game was last closed. 
    private long offTimeSeconds;      // Downtime in seconds (time between last close and current start)

    private static final String STATISTICS_FILE = "statistics.properties"; // Name of file where the statisitics are saved
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formatter for dates in year/month/day
 /**
     * Constructs a new {@code ParentalStatisticsModel} instance.
     * <p>
     * This constructor initializes statistics, loads previous data from the properties file,
     * calculates the off time if a last close time is available, and records the start time of the current session.
     * </p>
     */
    
    public ParentalStatisticsModel() {
        totalPlayTimeSeconds = 0;
        sessionCount = 0;
        dailyPlayTimeSeconds = new HashMap<>();
        loadStatistics();

        long currentTimeMillis = System.currentTimeMillis();
        // If a last close time was recorded, compute downtime
        if (sessionEndTime > 0) {
            offTimeSeconds = (currentTimeMillis - sessionEndTime) / 1000;
        } else {
            offTimeSeconds = 0;
        }
        // Record the start of the current session in a new varibale
        sessionStartTime = currentTimeMillis;
    }

     /**
     * Ends the current game session.
     * <p>
     * This method calculates the duration of the current session, updates the overall and daily playtime totals,
     * increments the session count, updates the last close time, and saves the updated statistics.
     * </p>
     */
    public void endSession() {
        long sessionEnd = System.currentTimeMillis(); // Find current time when sessions ends
        long sessionSeconds = (sessionEnd  - sessionStartTime) / 1000; // Calculate how long the game was played for in seconds
        totalPlayTimeSeconds += sessionSeconds; // Update total play time with new session time
        sessionCount++;

        
        String todayKey = LocalDate.now().format(dateFormatter);
        long previous = dailyPlayTimeSeconds.getOrDefault(todayKey, 0L); // Get current play time for today 
        dailyPlayTimeSeconds.put(todayKey, previous + sessionSeconds);

     
        sessionEndTime = sessionEnd ;    // Update lastCloseTimeMillis to the current time

        saveStatistics(); // Save updated stats to statisitics.properties file
    }

    /**
     * Returns the start time of the current session.
     *
     * @return the session start time in milliseconds.
     */
    public long getsessionStartTime() {
        return sessionStartTime;
    }

    /**
     * Resets all recorded statistics.
     * <p>
     * This method clears the total play time, session count, and daily playtime records,
     * resets the current session start time, and saves the reset statistics.
     * </p>
     */
     public void resetStatistics() {
        totalPlayTimeSeconds = 0;
        sessionCount = 0;
        dailyPlayTimeSeconds.clear();
        sessionStartTime = System.currentTimeMillis();
        saveStatistics();
    }

    /**
     * Returns the total play time in seconds.
     *
     * @return the total number of seconds played.
     */ 
    public long getTotalPlayTimeSeconds() {
        return totalPlayTimeSeconds;
    }

     /**
     * Returns the total number of game sessions recorded.
     *
     * @return the session count.
     */ 
    public int getSessionCount() {
        return sessionCount;
    }

    /**
     * Returns the map of daily playtime statistics.
     * <p>
     * The map key is a date string in the format "yyyy-MM-dd" and the value is the play time (in seconds)
     * recorded for that day.
     * </p>
     *
     * @return a map containing daily play time in seconds.
     */
    public Map<String, Long> getDailyPlayTimeSeconds() {
        return dailyPlayTimeSeconds;
    }

    /**
     * Returns the off time in seconds.
     * <p>
     * Off time is the downtime calculated at startup, representing the time between when the game was last closed
     * and the current session start.
     * </p>
     *
     * @return the off time in seconds.
     */
    public long getOffTimeSeconds() {
        return offTimeSeconds;
    }

      /**
     * Loads the game statistics from the properties file.
     * <p>
     * This method reads the stored values for total play time, session count, daily playtime,
     * and the last close time from the {@code statistics.properties} file.
     * </p>
     */
    private void loadStatistics() {
        Properties props = new Properties();
        File file = new File(STATISTICS_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis); // Load properties from file
                totalPlayTimeSeconds = Long.parseLong(props.getProperty("totalPlayTimeSeconds", "0")); // Update the total play time
                sessionCount = Integer.parseInt(props.getProperty("sessionCount", "0")); // Update session count
                // Iterate over properties to load daily play time values
                for (String key : props.stringPropertyNames()) {
                    if (key.startsWith("day.")) {
                        String dateKey = key.substring(4);
                        long seconds = Long.parseLong(props.getProperty(key, "0")); // Parse the play time in seconds for the given day
                        dailyPlayTimeSeconds.put(dateKey, seconds);
                    }
                }
                // Load last close time if present
                sessionEndTime = Long.parseLong(props.getProperty("lastCloseTime", "0"));
            } catch (IOException ex) { // Catch any exceptions in case of IO erros
                ex.printStackTrace();
            }
        }
    }

     /**
     * Saves the current game statistics to the properties file.
     * <p>
     * The method stores the total play time, session count, daily playtime entries,
     * and the last close time in the {@code statistics.properties} file.
     * </p>
     */
    private void saveStatistics() {
        Properties props = new Properties();
        props.setProperty("totalPlayTimeSeconds", String.valueOf(totalPlayTimeSeconds)); // Store total play time in seconds
        props.setProperty("sessionCount", String.valueOf(sessionCount)); // Store total session count
        for (Map.Entry<String, Long> entry : dailyPlayTimeSeconds.entrySet()) {
            props.setProperty("day." + entry.getKey(), String.valueOf(entry.getValue()));
        }
       
        props.setProperty("lastCloseTime", String.valueOf(sessionEndTime)); // Store last end session time 
        try (FileOutputStream fos = new FileOutputStream(STATISTICS_FILE)) {
            props.store(fos, "Parental Statistics");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}