import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PetTypeTest {

  /*
   * Used to test the petType constructor
   * Ensures that all statistics are initialized correctly
   */
  @Test
  public void testConstructor() {
    petType goose = new petType("goose");
    assertEquals(goose.getType(), "goose");
    assertEquals(goose.getMaxHealth(), 100);
    assertEquals(goose.getMaxFullness(), 200);
    assertEquals(goose.getMaxSleep(), 200);
    assertEquals(goose.getMaxHappiness(), 50);
    assertEquals(goose.getMaxLove(), 200);
    assertEquals(goose.getFullnessRate(), 1);
    assertEquals(goose.getSleepRate(), 1);
    assertEquals(goose.getHappinessRate(), 3);
    assertEquals(goose.getLoveRate(), 3);

    petType duck = new petType("duck");
    assertEquals(duck.getType(), "duck");
    assertEquals(duck.getMaxHealth(), 100);
    assertEquals(duck.getMaxFullness(), 100);
    assertEquals(duck.getMaxSleep(), 100);
    assertEquals(duck.getMaxHappiness(), 100);
    assertEquals(duck.getMaxLove(), 100);
    assertEquals(duck.getFullnessRate(), 2);
    assertEquals(duck.getSleepRate(), 2);
    assertEquals(duck.getHappinessRate(), 2);
    assertEquals(duck.getLoveRate(), 2);

    petType babyDuck = new petType("baby duck");
    assertEquals(babyDuck.getType(), "baby duck");
    assertEquals(babyDuck.getMaxHealth(), 100);
    assertEquals(babyDuck.getMaxFullness(), 100);
    assertEquals(babyDuck.getMaxSleep(), 200);
    assertEquals(babyDuck.getMaxHappiness(), 100);
    assertEquals(babyDuck.getMaxLove(), 200);
    assertEquals(babyDuck.getFullnessRate(), 4);
    assertEquals(babyDuck.getSleepRate(), 4);
    assertEquals(babyDuck.getHappinessRate(), 1);
    assertEquals(babyDuck.getLoveRate(), 1);

  }

  /*
   * Used to test the getter methods for petType
   */
  @Test
  public void testGetters() {
    petType type = new petType("duck");
    assertEquals(type.getType(), "duck");
    assertEquals(type.getMaxHealth(), 100);
    assertEquals(type.getMaxFullness(), 100);
    assertEquals(type.getMaxSleep(), 100);
    assertEquals(type.getMaxHappiness(), 100);
    assertEquals(type.getMaxLove(), 100);
    assertEquals(type.getFullnessRate(), 2);
    assertEquals(type.getSleepRate(), 2);
    assertEquals(type.getHappinessRate(), 2);
    assertEquals(type.getLoveRate(), 2);
    assertEquals(type.getSleepPenalty(), 10);
    assertEquals(type.getSleepRecovery(), 5);
    assertEquals(type.getHungerHealthPenalty(), 2);
    assertEquals(type.getHungerHappinessDrain(), 2);

  }

}
