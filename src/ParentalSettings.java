import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

/**
 * The {@code ParentalSettings} class manages parental control settings for the pet game.
 * <p>
 * This class allows for enabling or disabling restrictions the allowed play period per day
 * by specifying start and end times. Settings are saved in a parental_settings.properties file.
 * </p>
 * @author Jessamine Li
 */
public class ParentalSettings {
    private boolean enabled; // Boolean indicates if parental restrictions have been enabled or not
    private Date startTime; // The start time of the allowed play period
    private Date endTime; // The end time of the allowed play period

    private static final String SETTINGS_FILE = "parental_settings.properties"; // File name where the data of end and start time is stored
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Formatter for time values 

    /**
     * Constructs a new {@code ParentalSettings} instance with default values.
     * <p>
     * The default start time is set to 08:00 and the default end time is set to 20:00.
     * Parental restrictions are disabled by default. Existing settings are loaded
     * from the parental_settings.properties file, if available.
     * </p>
     */
    public ParentalSettings() {
        // Set default times
        try {
            startTime = timeFormat.parse("08:00"); // Start time is set to 08:00
            endTime = timeFormat.parse("20:00"); // End time is set to 20:00
        } catch (ParseException e) {
            e.printStackTrace();
        }
        enabled = false; // Set boolean to false so that parental settings are not enabled
        loadSettings();
    }

    /**
     * Checks whether parental restrictions are enabled.
     *
     * @return {@code true} if parental restrictions are enabled; {@code false} otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the parental restrictions enabled state.
     *
     * @param enabled {@code true} to enable parental restrictions; {@code false} to disable.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

     /**
     * Returns the start time of the allowed play period.
     *
     * @return the start time as a {@code Date} object.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the allowed play period.
     *
     * @param startTime the new start time as a {@code Date} object.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

     /**
     * Returns the end time of the allowed play period.
     *
     * @return the end time as a {@code Date} object.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the allowed play period.
     *
     * @param endTime the new end time as a {@code Date} object.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Determines if play is allowed at the current time.
     * <p>
     * If parental restrictions are disabled, the method returns {@code true}.
     * Otherwise, it checks whether the current time falls within the allowed play period.
     * The allowed period can either be a standard period within a single day or span midnight.
     * </p>
     *
     * @return {@code true} if the current time is within the allowed period or if restrictions are disabled;
     *         {@code false} otherwise.
     */
    public boolean isAllowedToPlay() {
        if (!enabled) {
            return true; // If parental limitations are not enabled, return true 
        }
        LocalTime current = LocalTime.now();
        LocalTime start = LocalTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        LocalTime end = LocalTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        
        if (!start.isAfter(end)) {
            // Return true only if the current time is not before the start time and not after the end time
            return !current.isBefore(start) && !current.isAfter(end);
        } else {
            // Allowed period spans midnight: allowed if current is after start OR before end.
            return !current.isBefore(start) || !current.isAfter(end);
        }
    }

     /**
     * Loads the parental control settings from the properties file.
     * <p>
     * If the file exists, the method reads the "enabled", "startTime", and "endTime" properties
     * and updates the corresponding fields. Default values are used if any property is missing.
     * </p>
     */
    public void loadSettings() {
        // Create a new Properties object to hold the settings
        Properties props = new Properties();
        File file = new File(SETTINGS_FILE);
         // Check if the settings file exists
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                // Load properties from the file
                props.load(fis);
                enabled = Boolean.parseBoolean(props.getProperty("enabled", "false"));
                String startStr = props.getProperty("startTime", "08:00");
                String endStr = props.getProperty("endTime", "20:00");
                startTime = timeFormat.parse(startStr);
                endTime = timeFormat.parse(endStr);
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

     /**
     * Saves the current parental control settings to the properties file.
     * <p>
     * The settings saved include the enabled state, start time, and end time.
     * </p>
     */
     public void saveSettings() {
         // Create a new Properties object to store the current settings
        Properties props = new Properties();
        props.setProperty("enabled", String.valueOf(enabled)); // Set the "enabled" property to the string representation of the enabled flag
        props.setProperty("startTime", timeFormat.format(startTime)); // Format the start time using the specified time format and store it as the "startTime" property
        props.setProperty("endTime", timeFormat.format(endTime)); 
        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            props.store(fos, "Parental Limitations Settings");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}