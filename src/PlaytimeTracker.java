import java.time.LocalDate;
/**
 * The {@code PlaytimeTracker} class tracks playtime statistics including the total play time,
 * number of sessions, the date of the last session, and the time played on the current day.
 * <p>
 * All playtime values are measured in minutes. This class provides methods to record sessions,
 * retrieve overall and daily playtime statistics, calculate average session time, and reset all statistics.
 * </p>
 * 
 * @author Jessamine Li
 */
public class PlaytimeTracker {
    private int totalPlayTime; // Total playtime in minutes
    private int sessionCount; // Total number of sessions played
    private LocalDate lastSessionDate; // Last date a session was recorded
    private int timePlayedToday; // How many minutes of playtime were recorded today

    
    /**
     * Constructs a new {@code PlaytimeTracker} instance with all statistics initialized to zero.
     * <p>
     * The {@code lastSessionDate} is set to the current date.
     * </p>
     */
    public PlaytimeTracker() {
        this.totalPlayTime = 0;
        this.sessionCount = 0;
        this.lastSessionDate = LocalDate.now();
        this.timePlayedToday = 0;
    }

    /**
     * Records the end of a play session.
     * <p>
     * This method should be called at the end of each session with the session length (in minutes).
     * It updates the total play time, increments the session count, and updates today's play time.
     * If the current date differs from the last recorded session date, the daily play time is reset.
     * </p>
     *
     * @param sessionLength the duration of the session in minutes.
     */
    public void trackSession(int sessionLength) {
        LocalDate today = LocalDate.now(); // Get current date 
        if (!today.equals(lastSessionDate)) { // If today is different from the last session date, reset the daily playtime counter
           
            timePlayedToday = 0;
            lastSessionDate = today;
        }

        totalPlayTime += sessionLength; // Update overall total play time and today's play time
        timePlayedToday += sessionLength;
        sessionCount++; // Increment session counter
    }

    /**
     * Returns the total play time recorded.
     *
     * @return the total play time in minutes.
     */
    public int getTotalPlayTime() {
        return totalPlayTime;
    }

     /**
     * Returns the average duration of a session.
     * <p>
     * This is calculated by dividing the total play time by the number of sessions.
     * If no sessions have been recorded, the average session time is 0.
     * </p>
     *
     * @return the average session time in minutes.
     */
    public double getAverageSessionTime() {
        if (sessionCount == 0) return 0;
        return (double) totalPlayTime / sessionCount;
    }

    /**
     * Returns the play time recorded for the current day.
     * <p>
     * If no session has been recorded today, this method returns 0.
     * </p>
     *
     * @return the play time for the current day in minutes.
     */
    public int getTimePlayedToday() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastSessionDate)) { // Return 0 if current day is not the same as last session date
            return 0;
        }
        return timePlayedToday;
    }

    /**
     * Resets all tracked playtime statistics to their initial values.
     * <p>
     * This method sets the total play time, session count, and daily play time to 0,
     * and updates the last session date to the current date.
     * </p>
     */
    public void resetStats() {
        totalPlayTime = 0;
        sessionCount = 0;
        timePlayedToday = 0;
        lastSessionDate = LocalDate.now();
    }

    /**
     * Sets the total play time.
     *
     * @param totalPlayTime the total play time in minutes.
     */
    public void setTotalPlayTime(int totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

     /**
     * Returns the number of sessions recorded.
     *
     * @return the session count.
     */
    public int getSessionCount() {
        return sessionCount;
    }

    /**
     * Sets the number of sessions recorded.
     *
     * @param sessionCount the session count.
     */
    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

     /**
     * Returns the date of the last session recorded.
     *
     * @return the last session date.
     */
    public LocalDate getLastSessionDate() {
        return lastSessionDate;
    }

    /**
     * Sets the date of the last session recorded.
     *
     * @param lastSessionDate the date of the last session.
     */
    public void setLastSessionDate(LocalDate lastSessionDate) {
        this.lastSessionDate = lastSessionDate;
    }

    /**
     * Sets the play time for the current day.
     *
     * @param timePlayedToday the play time for today in minutes.
     */
    public void setTimePlayedToday(int timePlayedToday) {
        this.timePlayedToday = timePlayedToday;
    }

    /**
     * Returns a string representation of the playtime statistics.
     * <p>
     * The returned string includes the total play time, session count, last session date,
     * and the play time for the current day.
     * </p>
     *
     * @return a string representation of the current playtime statistics.
     */
    @Override
    public String toString() {
        return "PlayTimeTracker [totalPlayTime=" + totalPlayTime + ", sessionCount=" + sessionCount
                + ", lastSessionDate=" + lastSessionDate + ", timePlayedToday=" + timePlayedToday + "]";
    }
}
