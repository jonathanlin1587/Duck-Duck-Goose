import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class PlaytimeTrackerTest {

    @Test
    public void testConstructor() {
        PlaytimeTracker tracker = new PlaytimeTracker();
        assertEquals(0, tracker.getTotalPlayTime(), "Total play time should be 0 on initialization");
        assertEquals(0, tracker.getSessionCount(), "Session count should be 0 on initialization");
        assertEquals(0, tracker.getTimePlayedToday(), "Time played today should be 0 on initialization");
        assertEquals(LocalDate.now(), tracker.getLastSessionDate(), "Last session date should be today on initialization");
    }


    @Test
    public void testGetAverageSessionTime() {
        PlaytimeTracker tracker = new PlaytimeTracker();
        assertEquals(0, tracker.getAverageSessionTime(), "Average session time should be 0 if no sessions have been recorded");
    }

    
    @Test
    public void testResetStats() {
        PlaytimeTracker tracker = new PlaytimeTracker();
        tracker.trackSession(40);
        tracker.trackSession(20);
        tracker.resetStats();
        assertEquals(0, tracker.getTotalPlayTime(), "Total play time should be reset to 0");
        assertEquals(0, tracker.getSessionCount(), "Session count should be reset to 0");
        assertEquals(0, tracker.getTimePlayedToday(), "Time played today should be reset to 0");
        assertEquals(LocalDate.now(), tracker.getLastSessionDate(), "Last session date should be reset to today");
    }

  
}