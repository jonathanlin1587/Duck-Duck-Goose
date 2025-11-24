import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ParentalStatisticsModelTest {

    private static final String STATISTICS_FILE = "statistics.properties";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        // Remove any existing statistics file to ensure a clean slate.
        File file = new File(STATISTICS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up the file after each test.
        File file = new File(STATISTICS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void ConstructorDefaults() {
        // When no file exists, defaults should be used.
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        assertEquals(0, model.getTotalPlayTimeSeconds(), "Total play time should be 0 by default");
        assertEquals(0, model.getSessionCount(), "Session count should be 0 by default");
        assertTrue(model.getDailyPlayTimeSeconds().isEmpty(), "Daily play time map should be empty by default");
        assertEquals(0, model.getOffTimeSeconds(), "Off time should be 0 when no last close time is recorded");
        
        // Check that sessionStartTime is set to the current time (allowing for small time differences)
        long now = System.currentTimeMillis();
        assertTrue(Math.abs(model.getsessionStartTime() - now) < 1000, "Session start time should be near current time");
    }

    @Test
    public void GetsessionStartTime() {
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        long sessionStart = model.getsessionStartTime();
        long now = System.currentTimeMillis();
        // Allowing a small margin for processing time.
        assertTrue(Math.abs(sessionStart - now) < 1000, "Session start time should be within 1 second of the current time");
    }

    @Test
    public void ResetStatistics() throws InterruptedException {
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        // Simulate a session.
        Thread.sleep(1100);
        model.endSession();
        
        // Confirm that statistics have been updated.
        assertTrue(model.getTotalPlayTimeSeconds() > 0, "Total play time should be greater than 0 after a session");
        assertTrue(model.getSessionCount() > 0, "Session count should be greater than 0 after a session");
        assertFalse(model.getDailyPlayTimeSeconds().isEmpty(), "Daily play time map should not be empty after a session");
        
        // Reset the statistics.
        model.resetStatistics();
        assertEquals(0, model.getTotalPlayTimeSeconds(), "Total play time should be reset to 0");
        assertEquals(0, model.getSessionCount(), "Session count should be reset to 0");
        assertTrue(model.getDailyPlayTimeSeconds().isEmpty(), "Daily play time map should be empty after reset");
        
        // Verify that sessionStartTime is updated (close to current time).
        long now = System.currentTimeMillis();
        assertTrue(Math.abs(model.getsessionStartTime() - now) < 1000, "Session start time should be updated after reset");
    }

    @Test
    public void GetTotalPlayTimeSeconds() throws InterruptedException {
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        // Initially, total play time is 0.
        assertEquals(0, model.getTotalPlayTimeSeconds(), "Initial total play time should be 0");
        
        // After ending a session, total play time should increase.
        Thread.sleep(1100);
        model.endSession();
        assertTrue(model.getTotalPlayTimeSeconds() >= 1, "Total play time should be at least 1 second after a session");
    }

    @Test
    public void GetSessionCount() throws InterruptedException {
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        assertEquals(0, model.getSessionCount(), "Initial session count should be 0");
        
        // End a session and verify the count.
        Thread.sleep(1100);
        model.endSession();
        assertEquals(1, model.getSessionCount(), "Session count should be 1 after one session end");
        
        // End another session.
        Thread.sleep(1100);
        model.endSession();
        assertEquals(2, model.getSessionCount(), "Session count should be 2 after two session ends");
    }

    @Test
    public void getPlayTime() throws InterruptedException {
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        // Initially, the daily play time map should be empty.
        assertTrue(model.getDailyPlayTimeSeconds().isEmpty(), "Daily play time map should initially be empty");
        
        // End a session.
        Thread.sleep(1100);
        model.endSession();
        
        // Verify that today's key is present and has a non-zero value.
        String todayKey = LocalDate.now().format(dateFormatter);
        assertTrue(model.getDailyPlayTimeSeconds().containsKey(todayKey),
                   "Daily play time map should contain today's date after session end");
        assertTrue(model.getDailyPlayTimeSeconds().get(todayKey) >= 1,
                   "Daily play time for today should be at least 1 second");
    }

    @Test
    public void getDownTime() throws Exception {
        // To test offTimeSeconds, simulate a previous session by writing a properties file with a known lastCloseTime.
        Properties props = new Properties();
        long fakeLastCloseTime = System.currentTimeMillis() - 5000; // 5 seconds in the past
        props.setProperty("totalPlayTimeSeconds", "100");
        props.setProperty("sessionCount", "2");
        props.setProperty("lastCloseTime", String.valueOf(fakeLastCloseTime));
        // No daily data is necessary for this test.
        try (FileOutputStream fos = new FileOutputStream(STATISTICS_FILE)) {
            props.store(fos, "Test Statistics");
        }
        
        // Instantiate a new model; it should load the fake last close time.
        ParentalStatisticsModel model = new ParentalStatisticsModel();
        long offTime = model.getOffTimeSeconds();
        // Since 5 seconds have passed since fakeLastCloseTime, off time should be at least 5 seconds (a bit more due to processing delay).
        assertTrue(offTime >= 5, "Off time should be at least 5 seconds based on the simulated last close time");
    }
}