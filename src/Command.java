//This class contains static methods used in updating the state and vital statistics of a pet.

/**
 * Command contains a set of static methods used to update the statistics and
 * status of
 * the pet object in a controlled manner
 *
 * @author Samuel Joseph Humphrey (shumph3)
 */
public class Command {

  /**
   * Used to update the status of the pet
   *
   * @param pet the pet to be updated
   * @return the updated pet
   */
  public static Pet updateStatus(Pet pet) {
    if (pet.isNormal())
      pet.setState("default");
    if (pet.isAngry())
      pet.setState("angry");
    if (pet.isHungry())
      pet.setState("hungry");
    if (pet.isSleeping())
      pet.setState("sleep");
    if (pet.isDead())
      pet.setState("dead");
    return pet;

  }

  /**
   * Used to feed the pet, increasing its hunger
   *
   * @param pet  the pet to be fed
   * @param food the InventoryObject the pet is fed with
   * @return the fed pet
   */
  public static Pet feed(Pet pet, InventoryObject food) {
    if (pet.isDead()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isSleeping()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isFull()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isAngry()) {
      pet.setRecentCommand(false);
      return pet;
    } else {
      pet.setRecentCommand(true);
      pet.changeFullness(Math.min(pet.getType().getMaxFullness() - pet.getFullness(), food.getStats()));
      pet = updateStatus(pet);
      return pet;
    }

  }

  /**
   * Used to put the pet to sleep, maxxing out its sleep
   *
   * @param pet the pet to be put to sleep
   * @return the slept pet
   */
  public static Pet sleep(Pet pet) {
    if (pet.isDead()) {
      pet.setRecentCommand(false);
      return pet;
    }
    // else if (pet.isSleeping())
    // return pet;
    else if (pet.isAngry()) {
      pet.setRecentCommand(false);
      return pet;
    } else {
      int amount = pet.getType().getSleepRate();
      pet.changeSleep(amount);
      pet.setSleep(Math.min(pet.getType().getMaxSleep(), pet.getSleep()));
      pet.setState("sleep");
      return pet;
    }

  }

  /**
   * Used to give gifts to the pet, increasing love and happiness
   *
   * @param pet  the pet to receive the gift
   * @param gift the InventoryObject to be gifted
   * @return the updated pet
   */
  public static Pet gift(Pet pet, InventoryObject gift) {
    if (pet.isDead()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isSleeping()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isHappy()) {
      pet.setRecentCommand(false);
      return pet;
    } else {
      pet.setRecentCommand(true);
      pet.changeHappiness(Math.min(pet.getType().getMaxHappiness() - pet.getHappiness(), gift.getStats()));
      pet.changeLove(Math.min(pet.getType().getMaxLove() - pet.getLove(), gift.getStats()));
      pet = updateStatus(pet);
      return pet;
    }

  }

  /**
   * Used to take the pet to the vet (maxing out it's health)
   *
   * @param pet the pet to be healed
   * @return the healed pet
   */
  public static Pet vet(Pet pet) {
    if (pet.isDead()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isAngry()) {
      pet.setRecentCommand(false);
      return pet;
    } else {
      // Check if vet is on cooldown.
      if (pet.isCommandOnCooldown("vet")) {
        pet.setRecentCommand(false);
        return pet; // Command handling outside this method should inform the user.
      }
      int amount = pet.getType().getMaxHealth() - pet.getHealth();
      pet.changeHealth(amount);

      // Set vet command cooldown to 45 seconds.
      pet.setCooldown("vet", 45);

      pet.setRecentCommand(true);
      pet = updateStatus(pet);
      return pet;
    }

  }

  /**
   * Used to play with the pet, increasing happiness and love
   *
   * @param pet the pet to be played with
   * @return the updated pet
   */
  public static Pet play(Pet pet) {
    if (pet.isDead()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isSleeping()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isHappy()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isLoved()) {
      pet.setRecentCommand(false);
      return pet;
    } else {

      // Check if play is on cooldown.
      if (pet.isCommandOnCooldown("play")) {
        pet.setRecentCommand(false);
        return pet; // Command handling outside this method should inform the user.
      }
      pet.changeHappiness(Math.min(pet.getType().getMaxHappiness() - pet.getHappiness(), 25));
      pet.changeLove(Math.min(pet.getType().getMaxLove() - pet.getLove(), 25));

      // Set play command cooldown to 15 seconds.
      pet.setCooldown("play", 10);
      pet.setRecentCommand(true);
      pet = updateStatus(pet);
      return pet;
    }

  }

  /**
   * Used to exercise the pet, increasing health and decreasing hunger and sleep
   *
   * @param pet the pet to be exercised
   */
  public static Pet exercise(Pet pet) {
    if (pet.isDead()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isSleeping()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isHungry()) {
      pet.setRecentCommand(false);
      return pet;
    } else if (pet.isHealthy()) {
      pet.setRecentCommand(false);
      return pet;
    } else {
      pet.setRecentCommand(true);
      pet.changeHealth(Math.min(pet.getType().getMaxHealth() - pet.getHealth(), 7));
      pet.changeSleep(-5);
      pet.setSleep(Math.max(0, pet.getSleep()));
      pet.changeFullness(-5);
      pet.setFullness(Math.max(0, pet.getFullness()));
      pet.setState("exercising");
      return pet;
    }

  }

  /**
   * Updates the pet's stats (fullness, happiness, sleep, love) and applies
   * state-specific logic.
   * This method integrates regular stat drain with state transitions.
   * 
   * @param pet the Pet object to be update
   * @return the updated pet
   */
  public static Pet update(Pet pet) {
    if (pet.isDead())
      return pet;

    boolean isActiveState = pet.getState().equals("sleep") || pet.getState().equals("exercising");

        // Only drain stats if the pet is not sleeping
    if (!"sleep".equals(pet.getState())) {
      pet.changeFullness(-pet.getType().getFullnessRate());
      pet.changeHappiness(-pet.getType().getHappinessRate());
      pet.changeLove(-pet.getType().getLoveRate());
      pet.changeSleep(-pet.getType().getSleepRate());
    }

    pet.setFullness(Math.max(0, pet.getFullness()));
    pet.setHappiness(Math.max(0, pet.getHappiness()));
    pet.setSleep(Math.max(0, pet.getSleep()));
    pet.setLove(Math.max(0, pet.getLove()));

    handleHungryState(pet, isActiveState);
    handleAngryState(pet, isActiveState);

    pet = updateStatus(pet);
    return pet;
  }

  /**
   * Used to handle hunger penalties to health and happiness
   * 
   * @param pet           the pet object to be handled
   * @param isActiveState the boolean indicating if hunger is the current state
   */
  private static void handleHungryState(Pet pet, boolean isActiveState) {
    if (pet.getFullness() == 0 && !"hungry".equals(pet.getState()) && !pet.isSleeping()) {
      pet.setState("hungry");
    }
    // Apply hunger penalties only when not active (e.g., not sleeping/exercising)
    if ("hungry".equals(pet.getState()) && !isActiveState) {
      pet.changeHappiness(-pet.getType().getHungerHappinessDrain());
      pet.changeHealth(-pet.getType().getHungerHealthPenalty());
    }
  }

  /**
   * Used to handle the angry state
   *
   * @param pet           the pet object to be handled
   * @param isActiveState the boolean indicating if anger is the current state
   */
  private static void handleAngryState(Pet pet, boolean isActiveState) {
    if (pet.getHappiness() == 0 && !"angry".equals(pet.getState()) && !pet.isSleeping()) {
      pet.setState("angry");
    } else if ("angry".equals(pet.getState()) && pet.getHappiness() >= pet.getType().getMaxHappiness() / 2) {
      pet.setState("default");
    }
    // Optionally, you can also conditionally apply anger penalties if needed.
  }

  /**
   * Used to revive the pet (refilling all vital statistics)
   *
   * @param pet the pet object to be revived
   * @return the revived pet
   */
  public static Pet revive(Pet pet) {
    if (pet.isDead()) {
      pet.setState("alive"); // Set the pet's state to "alive"
      pet.setHealth(100); // Reset health to maximum
      pet.setFullness(100); // Reset fullness to maximum
      pet.setSleep(100); // Reset sleep to maximum
      pet.setHappiness(100); // Reset happiness to maximum
      pet.setLove(100); // Reset love to maximum
    }
    return pet; // Return the revived pet
  }

}
