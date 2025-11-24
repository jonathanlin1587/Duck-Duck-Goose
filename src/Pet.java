// Our Pet class, stores all the vital statistics of our pet

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Pet is a class that stores all the vital statistics of our pet
 * It also tracks cooldowns and the success of recent commands executed upon it
 *
 * @author Samuel Joseph Humphrey (shumph3)
 */
public class Pet {
  // Instance variables
  private boolean recentCommand;
  private int health;
  private int fullness;
  private int sleep;
  private int happiness;
  private int love;
  private String typeString;
  private petType pet;
  private String petName;
  private String status;
  private Map<String, LocalTime> cooldowns = new HashMap<>(); // Track cooldown times for commands.
  private boolean temporaryStateActive = false;

  /**
   * Constructs a Pet object with default (full) statistics
   *
   * @param typeString A string that tells the constructor the type of pet to be
   *                   created
   * @param petName    A string storing the name of the pet
   */
  public Pet(String typeString, String petName) {
    this.typeString = typeString;
    this.petName = petName;
    this.pet = new petType(typeString);
    this.health = pet.getMaxHealth();
    this.fullness = pet.getMaxFullness();
    this.sleep = pet.getMaxSleep();
    this.happiness = pet.getMaxHappiness();
    this.love = pet.getMaxLove();
    this.status = "normal";

  }

  /**
   * Constructs a Pet object from a save file
   *
   * @param saveFile A string storing the path to the save file
   */

  public Pet(String saveFile) {
    String saveFilePath = "src/model/saveFiles/" + saveFile + "/" + saveFile + "_pet.csv";
    try (Scanner sc = new Scanner(new File(saveFilePath))) {
      if (sc.hasNextLine())
        sc.nextLine(); // skip header
      if (sc.hasNextLine()) {
        String[] data = sc.nextLine().split(",");
        this.petName = data[0].trim();
        System.out.println(data[1]);
        this.typeString = data[1].trim();
        this.status = data[2].trim();
        this.fullness = Integer.parseInt(data[3].trim());
        this.sleep = Integer.parseInt(data[4].trim());
        this.health = Integer.parseInt(data[5].trim());
        this.love = Integer.parseInt(data[6].trim());
        this.happiness = Integer.parseInt(data[7].trim());
        this.pet = new petType(this.typeString);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Used to set the recentCommand boolean, used primarily by the Command class
   *
   * @param bool a boolean storing the new value
   */
  public void setRecentCommand(boolean bool) {
    this.recentCommand = bool;
  }

  /**
   * Used to set the value of the health statistic
   *
   * @param value an integer storing the new value
   */
  public void setHealth(int value) {
    this.health = value;

  }

  /**
   * Used to set the value of the fullness statistic
   *
   * @param value an integer storing the new value
   */
  public void setFullness(int value) {
    this.fullness = value;

  }

  /**
   * Used to set the value of the sleep statistic
   *
   * @param value an integer storing the new value
   */
  public void setSleep(int value) {
    this.sleep = value;

  }

  /**
   * Used to set the value of the happiness statistic
   *
   * @param value an integer storing the new value
   */
  public void setHappiness(int value) {
    this.happiness = value;

  }

  /**
   * Used to set the value of the love statistic
   *
   * @param value an integer storing the new value
   */
  public void setLove(int value) {
    this.love = value;
  }

  /**
   * Used to increment the health statistic
   *
   * @param amount an integer storing the increment
   */
  public void changeHealth(int amount) {
    this.health += amount;

  }

  /**
   * Used to increment the fullness statistic
   *
   * @param amount an integer storing the increment
   */
  public void changeFullness(int amount) {
    this.fullness += amount;

  }

  /**
   * Used to increment the sleep statistic
   *
   * @param amount an integer storing the increment
   */
  public void changeSleep(int amount) {
    this.sleep += amount;

  }

  /**
   * Used to increment the happiness statistic
   *
   * @param amount an integer storing the increment
   */
  public void changeHappiness(int amount) {
    this.happiness += amount;

  }

  /**
   * Used to increment the love statistic
   *
   * @param amount an integer storing the increment
   */
  public void changeLove(int amount) {
    this.love += amount;

  }

  /**
   * Used to set the state
   *
   * @param state a string storing the new state
   */
  public void setState(String state) {
    this.status = state;

  }

  /**
   * Used to get the boolean value of recentCommand
   *
   * @return the boolean value
   */
  public boolean getRecentCommand() {
    return this.recentCommand;

  }

  /**
   * Used to get the value of the health statistic
   *
   * @return the integer value
   */
  public int getHealth() {
    return this.health;

  }

  /**
   * Used to get the value of the fullness statistic
   *
   * @return the integer value
   */
  public int getFullness() {
    return this.fullness;

  }

  /**
   * Used to get the value of the health statistic
   *
   * @return the integer value
   */
  public int getSleep() {
    return this.sleep;

  }

  /**
   * Used to get the value of the happiness statistic
   *
   * @return the integer value
   */
  public int getHappiness() {
    return this.happiness;

  }

  /**
   * Used to get the value of the love statistic
   *
   * @return the integer value
   */
  public int getLove() {
    return this.love;

  }

  /**
   * Used to get the petType object of the pet
   *
   * @return the petType object
   */
  public petType getType() {
    return this.pet;

  }

  /**
   * Used to get the name of the pet type
   *
   * @return the string storing the name
   */
  public String getTypeString() {
    return this.typeString;

  }

  /**
   * Used to get the pet's name
   *
   * @return the string storing the name
   */
  public String getName() {
    return this.petName;

  }

  /**
   * Used to get the state of the pet
   *
   * @return the string storing the state
   */
  public String getState() {
    return this.status;

  }

  /**
   * Used to check if the pet is dead
   *
   * @return the related boolean
   */
  public boolean isDead() {
    if (this.health <= 0) {
      return true;
    }
    return false;

  }

  /**
   * Used to check if the pet is asleep
   *
   * @return the associated boolean
   */
  public boolean isSleeping() {
    if (this.sleep <= 0) {
      return true;
    } else if (this.status.equals("sleeping")) {
      return true;
    }

    return false;

  }

  /**
   * Used to check if the pet is angry (happiness <= 25%)
   *
   * @return the associated boolean
   */
  public boolean isAngry() {
    if (this.happiness <= (this.pet.getMaxHappiness() / 4)) {
      return true;
    }
    return false;

  }

  /**
   * Used to check if the pet is sick (health <= 25%)
   *
   * @return the associated boolean
   */
  public boolean isSick() {
    if (this.health <= (this.pet.getMaxHealth() / 4)) {
      return true;
    }
    return false;

  }

  /**
   * Used to check if the pet is hungry (fullness <= 25%)
   *
   * @return the associated boolean
   */
  public boolean isHungry() {
    if (this.fullness <= (this.pet.getMaxFullness() / 4)) {
      return true;
    }
    return false;

  }

  /**
   * Used to check if the pet is sleepy (sleepy <= 25%)
   *
   * @return the associated boolean
   */
  public boolean isSleepy() {
    if (this.sleep <= (this.pet.getMaxSleep() / 4)) {
      return true;
    }
    return false;

  }

  /**
   * Used to check if the pet's status is normal (none of the above)
   *
   * @return the associated boolean
   */
  public boolean isNormal() {
    // If none of the other states are met, the pet should be in a normal state
    if (!isDead() && !isSleeping() && !isAngry() && !isHungry()) {
      return true;
    }
    return false;

  }

  /**
   * Used to check if the pet is full (max hunger)
   *
   * @return the associated boolean
   */
  public boolean isFull() {
    if (this.fullness < pet.getMaxFullness()) {
      return false;
    }
    return true;

  }

  /**
   * Used to check if the pet is healthy (max health)
   *
   * @return the associated boolean
   */
  public boolean isHealthy() {
    if (this.health < pet.getMaxHealth()) {
      return false;
    }
    return true;

  }

  /**
   * Used to check if the pet is happy (max happiness)
   *
   * @return the associated boolean
   */
  public boolean isHappy() {
    if (this.happiness < this.pet.getMaxHappiness()) {
      return false;
    }
    return true;

  }

  /**
   * Used to check if the pet is loved (max love)
   *
   * @return the associated boolean
   */
  public boolean isLoved() {
    if (this.love < this.pet.getMaxLove()) {
      return false;
    }
    return true;

  }

  // These three functions were generated by ChatGPT

  /**
   * Checks if a given command is still on cooldown.
   * 
   * @param command the name of the command to check
   * @return true if the current time is before the cooldown end time; false
   *         otherwise.
   */
  public boolean isCommandOnCooldown(String command) {
    return cooldowns.containsKey(command) && cooldowns.get(command).isAfter(LocalTime.now());
  }

  /**
   * Sets a cooldown for a given command.
   * 
   * @param command the name of the command
   * @param seconds the duration of the cooldown in seconds
   */
  public void setCooldown(String command, int seconds) {
    cooldowns.put(command, LocalTime.now().plusSeconds(seconds));
  }

  /**
   * Gets the remaining cooldown time for a command in seconds.
   * 
   * @param command the name of the command
   * @return the number of seconds remaining on the cooldown; 0 if no cooldown.
   */
  public int getRemainingCooldown(String command) {
    if (!cooldowns.containsKey(command))
      return 0;
    return Math.max(0, (int) java.time.Duration.between(LocalTime.now(), cooldowns.get(command)).getSeconds());
  }

  public void setTemporaryState(String state, int durationSeconds) {
    if (temporaryStateActive)
      return;

    temporaryStateActive = true;
    setState(state);

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.schedule(() -> {
      temporaryStateActive = false;
      //Command.updateStatus(this); // Recalculate state after animation
      scheduler.shutdown();
    },
        durationSeconds, TimeUnit.SECONDS);
  }

  public boolean isTemporaryStateActive() {
    return temporaryStateActive;
  }
}
