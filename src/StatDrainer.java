/**
 * This class was generated using ChatGPT
 */
import java.util.concurrent.Executors; 
import java.util.concurrent.ScheduledExecutorService; 
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

public class StatDrainer { 
    private final ScheduledExecutorService scheduler; 
    private final Pet pet;
    private final Runnable updateStatusCallback;
    private final Runnable autoSleepCallback;

    public StatDrainer(Pet pet, Runnable updateStatusCallback, Runnable autoSleepCallback) {
        this.pet = pet;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        this.updateStatusCallback = updateStatusCallback;
        this.autoSleepCallback = autoSleepCallback;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            Command.update(pet);

            SwingUtilities.invokeLater(() -> {
                updateStatusCallback.run();

                if (pet.getSleep() <= 0 && !pet.isSleeping() && !pet.isDead()) {
                    autoSleepCallback.run();
                }
            });

        }, 0, 2, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }
}
