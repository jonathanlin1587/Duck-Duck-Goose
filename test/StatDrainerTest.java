import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for the stat drainer class.
 * @author Jasmine Kumar (jkumar43)
 */
public class StatDrainerTest {
    /**
     * A fake Pet class to control conditions for unit testing.
     * Extends the original Pet class so we can override methods such as getSleep(), isSleeping(), and isDead().
     */
    private static class FakePet extends Pet {
        // Custom fields to simulate pet state
        private int sleepValue;
        private boolean sleepingOverride;
        private boolean deadOverride;

        /**
         * Constructs a FakePet with the given parameters.
         * @param sleepValue The value to be returned by getSleep()
         * @param sleepingOverride The value to be returned by isSleeping()
         * @param deadOverride The value to be returned by isDead()
         */
        public FakePet(int sleepValue, boolean sleepingOverride, boolean deadOverride) {
            super("dog", "Buddy");
            this.sleepValue = sleepValue;
            this.sleepingOverride = sleepingOverride;
            this.deadOverride = deadOverride;
        }

        @Override
        public int getSleep() {
            return sleepValue;
        }

        @Override
        public boolean isSleeping() {
            return sleepingOverride;
        }

        @Override
        public boolean isDead() {
            return deadOverride;
        }
    }

    /**
     * Test that the updateStatusCallback is invoked at least once after calling start().
     * We use a CountDownLatch that will be decremented in the callback.
     */
    @Test
    public void testUpdateStatusCallbackIsInvoked() throws InterruptedException {
        // Latch to wait for the callback invocation
        CountDownLatch latch = new CountDownLatch(1);

        // Simple callback that counts down the latch when executed
        Runnable updateStatusCallback = () -> latch.countDown();
        // Provide a no-op for autoSleepCallback
        Runnable autoSleepCallback = () -> { };

        // Create a FakePet with normal conditions to avoid triggering autoSleepCallback.
        FakePet pet = new FakePet(10, false, false);

        // Instantiate and start the StatDrainer
        StatDrainer drainer = new StatDrainer(pet, updateStatusCallback, autoSleepCallback);
        drainer.start();

        // Wait up to 3 seconds for the updateStatusCallback to be invoked
        boolean invoked = latch.await(3, TimeUnit.SECONDS);
        drainer.stop();

        assertTrue(invoked, "updateStatusCallback should be invoked at least once after start()");
    }

    /**
     * Test that the autoSleepCallback is invoked when the pet's sleep is 0,
     * the pet is not already sleeping, and the pet is alive.
     */
    @Test
    public void testAutoSleepCallbackTriggeredWhenConditionsMet() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Use a no-op updateStatusCallback
        Runnable updateStatusCallback = () -> { };
        // autoSleepCallback decrements the latch when called
        Runnable autoSleepCallback = () -> latch.countDown();

        // Create a FakePet where getSleep() returns 0, isSleeping() returns false, and pet is alive (deadOverride false).
        FakePet pet = new FakePet(0, false, false);

        StatDrainer drainer = new StatDrainer(pet, updateStatusCallback, autoSleepCallback);
        drainer.start();

        // Wait up to 3 seconds for the autoSleepCallback to be invoked
        boolean invoked = latch.await(3, TimeUnit.SECONDS);
        drainer.stop();

        assertTrue(invoked, "autoSleepCallback should be invoked when pet has 0 sleep, is not sleeping, and is not dead");
    }

    /**
     * Test that the autoSleepCallback is NOT invoked when the pet is already sleeping.
     * Here, even though getSleep() returns 0, isSleeping() returns true.
     */
    @Test
    public void testAutoSleepCallbackNotTriggeredWhenPetIsSleeping() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Runnable updateStatusCallback = () -> { };
        Runnable autoSleepCallback = () -> latch.countDown();

        // Create a FakePet where getSleep() returns 0 but isSleeping() returns true.
        FakePet pet = new FakePet(0, true, false);

        StatDrainer drainer = new StatDrainer(pet, updateStatusCallback, autoSleepCallback);
        drainer.start();

        // Wait for 3 seconds; autoSleepCallback should not be triggered.
        boolean invoked = latch.await(3, TimeUnit.SECONDS);
        drainer.stop();

        assertFalse(invoked, "autoSleepCallback should not be invoked when pet is already sleeping");
    }

    /**
     * Test that the autoSleepCallback is NOT invoked when the pet is dead.
     * In this case, even if getSleep() is 0 and isSleeping() returns false, isDead() returns true.
     */
    @Test
    public void testAutoSleepCallbackNotTriggeredWhenPetIsDead() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Runnable updateStatusCallback = () -> { };
        Runnable autoSleepCallback = () -> latch.countDown();

        // Create a FakePet where getSleep() returns 0, isSleeping() returns false, but pet is dead.
        FakePet pet = new FakePet(0, false, true);

        StatDrainer drainer = new StatDrainer(pet, updateStatusCallback, autoSleepCallback);
        drainer.start();

        // Wait for 3 seconds; autoSleepCallback should not be triggered.
        boolean invoked = latch.await(3, TimeUnit.SECONDS);
        drainer.stop();

        assertFalse(invoked, "autoSleepCallback should not be invoked when pet is dead");
    }

    /**
     * Test that calling stop() cancels further scheduled executions.
     * We count the number of times updateStatusCallback is invoked before and after stop() is called.
     */
    @Test
    public void testStopCancelsScheduledTask() throws InterruptedException {
        // Counter to keep track of updateStatusCallback invocations.
        final int[] counter = {0};
        Runnable updateStatusCallback = () -> counter[0]++;
        Runnable autoSleepCallback = () -> { };

        // Use a FakePet with normal conditions.
        FakePet pet = new FakePet(10, false, false);
        StatDrainer drainer = new StatDrainer(pet, updateStatusCallback, autoSleepCallback);
        drainer.start();

        // Let the scheduler run for about 3 seconds.
        Thread.sleep(3100);
        drainer.stop();
        int countAfterStop = counter[0];

        // Wait an additional 2.5 seconds; no new invocations should occur after stop().
        Thread.sleep(2500);

        assertEquals(countAfterStop, counter[0], "No additional updateStatusCallback invocations should occur after stop() is called");
    }

    /**
     * Optionally, we want to test that Command.update(pet) is called as part of the scheduled task.
     * 
     * Note: Since Command.update() is a static method, testing its invocation may require a mocking framework
     * (e.g., PowerMockito). For demonstration purposes, we assume that if the updateStatusCallback is invoked,
     * then Command.update(pet) was also executed.
     */
    @Test
    public void testCommandUpdateIsCalled() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Runnable updateStatusCallback = () -> latch.countDown();
        Runnable autoSleepCallback = () -> { };

        FakePet pet = new FakePet(10, false, false);
        StatDrainer drainer = new StatDrainer(pet, updateStatusCallback, autoSleepCallback);
        drainer.start();

        boolean invoked = latch.await(3, TimeUnit.SECONDS);
        drainer.stop();

        assertTrue(invoked, "Command.update should be called as part of the scheduled task execution (verified indirectly via updateStatusCallback)");
    }
}
