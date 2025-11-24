/**
 * petType stores a set of default characteristics for each pet type
 * allowing these attributes to be easily accessed
 *
 * @author Samuel Joseph Humphrey (shumph3)
 */
public class petType {
  // Our instance variable
  private String type;
  private int maxHealth;
  private int maxFullness;
  private int maxSleep;
  private int maxHappiness;
  private int maxLove;
  private int fullnessRate;
  private int sleepRate;
  private int happinessRate;
  private int loveRate;

  // Allow for easy tuning of sleep and hunger variables
  private int sleepPenalty = 10;
  private int sleepRecovery = 5;
  private int hungerHealthPenalty = 2;
  private int hungerHappinessDrain = 2;

  /**
   * Constructs the pet type with the correct default values depending on
   * the string passed ("baby duck", "duck", or "goose")
   *
   * @param type the string storing the name of the petType
   */
  public petType(String type) {
    if (type.equals("goose")) {
      this.type = "goose";
      this.maxHealth = 100;
      this.maxFullness = 200;
      this.maxSleep = 200;
      this.maxHappiness = 50;
      this.maxLove = 200;
      this.fullnessRate = 1;
      this.sleepRate = 1;
      this.happinessRate = 3;
      this.loveRate = 3;

    }

    else if (type.equals("duck")) {
      this.type = "duck";
      this.maxHealth = 100;
      this.maxFullness = 100;
      this.maxSleep = 100;
      this.maxHappiness = 100;
      this.maxLove = 100;
      this.fullnessRate = 2;
      this.sleepRate = 2;
      this.happinessRate = 2;
      this.loveRate = 2;

    }

    else if (type.equals("baby duck")) {
      this.type = "baby duck";
      this.maxHealth = 100;
      this.maxFullness = 100;
      this.maxSleep = 200;
      this.maxHappiness = 100;
      this.maxLove = 200;
      this.fullnessRate = 4;
      this.sleepRate = 4;
      this.happinessRate = 1;
      this.loveRate = 1;

    }

  }

  /**
   * Used to get the name of the petType
   *
   * @return a string storing the name
   */
  public String getType() {
    return this.type;

  }

  /**
   * Used to get the max health of this petType
   *
   * @return an integer storing the max health
   */
  public int getMaxHealth() {
    return this.maxHealth;

  }

  /**
   * Used to get the max fullness of this petType
   *
   * @return an integer storing the max fullness
   */
  public int getMaxFullness() {
    return this.maxFullness;
  }

  /**
   * Used to get the max sleep of this petType
   *
   * @return an integer storing the max sleep
   */
  public int getMaxSleep() {
    return this.maxSleep;
  }

  /**
   * Used to get the max happiness of this petType
   *
   * @return an integer storing the max happiness
   */
  public int getMaxHappiness() {
    return this.maxHappiness;

  }

  /**
   * Used to get the max love of this petType
   *
   * @return an integer storing the max love
   */
  public int getMaxLove() {
    return this.maxLove;
  }

  /**
   * Used to get the fullness depletion rate of this petType
   *
   * @return an integer storing the rate
   */
  public int getFullnessRate() {
    return this.fullnessRate;

  }

  /**
   * Used to get the sleep depletion rate of this petType
   *
   * @return an integer storing the rate
   */
  public int getSleepRate() {
    return this.sleepRate;

  }

  /**
   * Used to get the happiness depletion rate of this petType
   *
   * @return an integer storing the rate
   */
  public int getHappinessRate() {
    return this.happinessRate;

  }

  /**
   * Used to get the love depletion rate of this petType
   *
   * @return an integer storing the rate
   */
  public int getLoveRate() {
    return this.loveRate;

  }

  /**
   * Used to get the penalty associated with lack of sleep for this petType
   *
   * @return the integer storing the penalty
   */
  public int getSleepPenalty() {
    return sleepPenalty;
  }

  /**
   * Used to get the sleep recovery rate for this petType
   *
   * @return the integer storing the rate
   */
  public int getSleepRecovery() {
    return sleepRecovery;
  }

  /**
   * Used to get the penalty associated with lack of food for this petType
   *
   * @return the integer storing the penalty
   */
  public int getHungerHealthPenalty() {
    return hungerHealthPenalty;
  }

  /**
   * Used to get the rate at which happiness drains when this petType is hungry
   *
   * @return the integer storing the rate
   */
  public int getHungerHappinessDrain() {
    return hungerHappinessDrain;
  }
}
