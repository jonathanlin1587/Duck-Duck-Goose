import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class CommandTest {
  
  /*
   * Used to test the updateStatus method
   * Ensures that the status is updated correctly based on
   * vital statistics
   */
  @Test
  public void testUpdateStatus() {
    Pet pet = new Pet("duck", "ducky");
    assertEquals(pet.getState(), "normal");

    pet.setHappiness(0);
    pet = Command.updateStatus(pet);
    assertEquals(pet.getState(), "angry");

    pet.setHappiness(100);
    pet.setFullness(0);
    pet = Command.updateStatus(pet);
    assertEquals(pet.getState(), "hungry");

    pet.setFullness(100);
    pet.setSleep(0);
    pet = Command.updateStatus(pet);
    assertEquals(pet.getState(), "sleep");

    pet.setSleep(100);
    pet.setHealth(0);
    pet = Command.updateStatus(pet);
    assertEquals(pet.getState(), "dead");

  }
  
  /*
   * Used to test the feed method
   * Ensures that  fullness is incremented the correct amount
   * and that the command can only be triggered when fullness
   * isn't maxxed out
   */
  @Test
  public void testFeed() {
    Pet pet = new Pet("duck", "ducky");
    InventoryObject food = new InventoryObject("food", "food", 1, 0, 10);
    pet.setFullness(50);
    pet = Command.feed(pet, food);
    assertTrue(pet.getRecentCommand());
    assertEquals(pet.getFullness(), 60);

    pet.setFullness(100);
    pet = Command.feed(pet, food);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getFullness(), 100);

    pet.setFullness(50);
    pet.setHappiness(0);
    pet = Command.feed(pet, food);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getFullness(), 50);

    pet.setHappiness(100);
    pet.setHealth(0);
    pet = Command.feed(pet, food);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getFullness(), 50);

  }

  /*
   * Used to test the sleep command
   * Ensures that sleep is increased by the sleep rate
   * and that the pet doesnt sleep when it is dead
   */
  @Test
  public void testSleep() {
    Pet pet = new Pet("duck", "ducky");
    pet.setSleep(0);
    pet = Command.sleep(pet);
    assertEquals(pet.getSleep(), pet.getType().getSleepRate());

    pet.setState("dead");
    pet.setSleep(50);
    pet = Command.sleep(pet);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getSleep(), 50 + pet.getType().getSleepRate());

  }

  /*
   *  Used to test the gift command
   *  Ensures that happiness and love is incremented correctly
   *  and that gifts cannot be given when the pet is fully 
   *  happy or loved
   */
  @Test
  public void testGift() {
    Pet pet = new Pet("duck", "ducky");
    InventoryObject gift = new InventoryObject("gift", "gift", 1, 0, 10);
    pet.setHappiness(50);
    pet.setLove(50);
    pet = Command.gift(pet, gift);
    assertTrue(pet.getRecentCommand());
    assertEquals(pet.getLove(), 60);
    assertEquals(pet.getHappiness(), 60);

    pet.setHappiness(100);
    pet.setLove(100);
    pet = Command.gift(pet, gift);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getLove(), 100);
    assertEquals(pet.getHappiness(), 100);

  }

  /*
   *  Used to test the vet method
   *  Ensures that vet maxes out the pets health
   *  and that the pet cannot be taken to the vet
   *  when healthy or dead
   */
  @Test
  public void testVet() {
    Pet pet = new Pet("duck", "ducky");
    pet.setHealth(50);
    pet = Command.vet(pet);
    assertTrue(pet.getRecentCommand());
    assertEquals(pet.getHealth(), 100);

    pet = Command.vet(pet);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getHealth(), 100);

    pet.setHealth(0);
    pet = Command.vet(pet);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getHealth(), 0);

  }

  /*
   *  Used to test the play command
   *  Ensures that love and happiness are incremented correctly
   *  and that the pet cannot be played with when fully happy
   *  or loved
   */
  @Test
  public void testPlay() {
    Pet pet = new Pet("duck", "ducky");
    pet.setHappiness(50);
    pet.setLove(50);
    pet = Command.play(pet);
    assertTrue(pet.getRecentCommand());
    assertEquals(pet.getLove(), 75);
    assertEquals(pet.getHappiness(), 75);

    pet.setHappiness(100);
    pet.setLove(100);
    pet = Command.play(pet);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getLove(), 100);
    assertEquals(pet.getHappiness(), 100);

  }

  /*
   *  Used to test the exercise command
   *  Ensures that health is incremented correctly
   *  and that the pet cannot exercise when hungry
   *  or completely healthy
   */
  @Test
  public void testExercise() {
    Pet pet = new Pet("duck", "ducky");
    pet.setHealth(50);
    pet = Command.exercise(pet);
    assertTrue(pet.getRecentCommand());
    assertEquals(pet.getHealth(), 57);
    assertEquals(pet.getSleep(), 95);
    assertEquals(pet.getFullness(), 95);

    pet.setFullness(0);
    pet = Command.exercise(pet);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getHealth(), 57);

    pet.setFullness(100);
    pet.setHealth(100);
    pet = Command.exercise(pet);
    assertFalse(pet.getRecentCommand());
    assertEquals(pet.getHealth(), 100);

  }

  /*
   * Used to test the update command
   * Ensures that stats deplete by the correct amounts
   * and that stats arent updated when the pet is dead
   */
  @Test
  public void testUpdate() {
    Pet pet = new Pet("duck", "ducky");
    pet = Command.update(pet);
    assertEquals(pet.getFullness(), pet.getType().getMaxFullness() - pet.getType().getFullnessRate());
    assertEquals(pet.getHappiness(), pet.getType().getMaxHappiness() - pet.getType().getHappinessRate());
    assertEquals(pet.getSleep(), pet.getType().getMaxSleep() - pet.getType().getSleepRate());
    assertEquals(pet.getLove(), pet.getType().getMaxLove() - pet.getType().getLoveRate());

    pet.setFullness(100);
    pet.setHappiness(100);
    pet.setSleep(100);
    pet.setLove(100);
    pet.setHealth(0);
    pet = Command.update(pet);
    assertEquals(pet.getFullness(), 100);
    assertEquals(pet.getHappiness(), 100);
    assertEquals(pet.getSleep(), 100);
    assertEquals(pet.getLove(), 100);

  }

  /*
   * Used to test the revive command
   * Ensures that all stats are fully replenished
   * and that the method can only be called when
   * the pet is dead
   */
  @Test
  public void testRevive() {
    Pet pet = new Pet("duck", "ducky");
    pet.setFullness(0);
    pet.setSleep(0);
    pet.setHappiness(0);
    pet.setLove(0);
    pet = Command.revive(pet);
    assertEquals(pet.getFullness(), 0);
    assertEquals(pet.getSleep(), 0);
    assertEquals(pet.getHappiness(), 0);
    assertEquals(pet.getLove(), 0);

    pet.setHealth(0);
    pet = Command.revive(pet);
    assertEquals(pet.getFullness(), 100);
    assertEquals(pet.getSleep(), 100);
    assertEquals(pet.getHappiness(), 100);
    assertEquals(pet.getLove(), 100);

  }

}
