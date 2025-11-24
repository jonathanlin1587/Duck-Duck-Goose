import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.time.LocalTime;

public class ParentalSettingsTest {

    private static final String SETTINGS_FILE = "parental_settings.properties";
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @BeforeEach
    public void setUp() {
        // Delete the settings file if it exists, to ensure a clean slate.
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    public void delete() {
        // Clean up the settings file after tests.
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void DefaultConstructor() throws ParseException {
        // When no settings file exists, defaults should be used.
        ParentalSettings settings = new ParentalSettings();
        // Default: parental control disabled.
        assertFalse(settings.isEnabled(), "Parental control should be disabled by default");
        // Default start time should be "08:00" and end time "20:00".
        Date defaultStart = timeFormat.parse("08:00");
        Date defaultEnd = timeFormat.parse("20:00");
        assertEquals(timeFormat.format(defaultStart), timeFormat.format(settings.getStartTime()),
                     "Default start time should be 08:00");
        assertEquals(timeFormat.format(defaultEnd), timeFormat.format(settings.getEndTime()),
                     "Default end time should be 20:00");
    }

    @Test
    public void SetAndGetEnabled() {
        ParentalSettings settings = new ParentalSettings();
        settings.setEnabled(true);
        assertTrue(settings.isEnabled(), "Parental control should be enabled after setting true");
    }

    @Test
    public void StartAndEndTime() throws ParseException {
        ParentalSettings settings = new ParentalSettings();
        Date newStart = timeFormat.parse("09:30");
        Date newEnd = timeFormat.parse("19:30");
        settings.setStartTime(newStart);
        settings.setEndTime(newEnd);
        assertEquals(timeFormat.format(newStart), timeFormat.format(settings.getStartTime()),
                     "Start time should be updated to 09:30");
        assertEquals(timeFormat.format(newEnd), timeFormat.format(settings.getEndTime()),
                     "End time should be updated to 19:30");
    }

    @Test
    public void PlayWhenDisabled() {
        ParentalSettings settings = new ParentalSettings();
        settings.setEnabled(false);
        // When parental restrictions are disabled, play is always allowed.
        assertTrue(settings.isAllowedToPlay(), "Should be allowed to play when parental control is disabled");
    }

    
   
}