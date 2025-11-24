# Duck Duck Goose - Virtual Pet Game

Duck Duck Goose is a virtual pet application that provides players with the experience of caring for and interacting with a digital pet. Players manage their pets' vital statistics (Health, Hunger, Sleep, Happiness, Love) through various interactive commands and activities.

## Required Libraries and Third-Party Tools

- Java SE Development Kit (JDK) version 23 or newer
- Java Swing (included with standard JDK)
- JUnit 5 (used for testing, version 5.10.0)

## Building the Software

**Step-by-step guide:**

1. **Install JDK**
   - Download JDK 23 from [Oracle's official website](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html).
   - Follow installation instructions for your operating system.

2. **Set up Development Environment (VS Code)**
   - Install Visual Studio Code from [VS Code website](https://code.visualstudio.com/).
   - Install the Java Extension Pack for VS Code.

3. **Cloning Repository**
   ```bash
   git clone <repository_url>
   ```

4. **Open Project in VS Code**
   - Launch VS Code and open the cloned project directory.

5. **Compile Project**
   - Use VS Code's built-in terminal:
     ```bash
     javac -d bin src/**/*.java
     ```

6. **Running Tests**
   - Ensure JUnit 5 is configured in your VS Code project settings.
   - Run tests using:
     ```bash
     java -jar junit-platform-console-standalone-1.10.0.jar -cp bin --scan-class-path
     ```

## Running the Compiled Software

- Execute the main GUI class (`MainMenuGUI`) from the terminal:
  ```bash
  java -cp bin view.MainMenuGUI
  ```

## User Guide

### Starting the Game

- Upon launching, the main menu offers the following options:
  - **New Game**: Start a new pet.
  - **Load Game**: Continue from a previously saved state.
  - **Parental Controls**: Access restricted settings (password: `1234`).
  - **Tutorial**: View instructions and gameplay tips.
  - **Exit**: Close the game.

### Starting a New Game

- Choose a pet type: Baby Duck, Duck, or Goose.
- Once selected, the other pets become unavailable.
- Enter a name for your pet.
- Begin interacting through the main gameplay interface.

### Gameplay Commands

- **Sleep**: Pet sleeps, increasing energy over time. Pet wakes when energy is full.
- **Feed**: Opens inventory to select food; increases fullness.
- **Play**: Increases happiness; 10-second cooldown.
- **Exercise**: Increases health but reduces sleep and hunger.
- **Vet**: Fully restores health at the cost of 20 points; 45-second cooldown.
- **Gift**: Select a gift from the inventory to increase love and happiness.

### Game Mechanics

- All stats (except Health) drain passively.
- Health drains if Hunger reaches below 25% or Sleep hits zero.
- Zero Happiness triggers the Angry state (commands restricted to Play/Gift).
- Health zero means pet death; revival is available only via Parental Controls.

### Saving and Loading Games

- Use the Save button to select one of three save slots.
- Confirmation dialogues appear for overwriting or deleting saves.

## Parental Controls

- Accessed from the main menu (password: `1234`).
- Monitor and reset player's screen time.
- Set daily screen time limits.
- Revive dead pets using saved game states.

## Additional Notes

- All game data (inventory, pet states) saved locally as CSV files in `src/model/saveFiles`.
- Ensure asset files (`assets` folder) and fonts (`src/fonts/IrishGrover-Regular.ttf`) are correctly located relative to the executable.
- Proper GUI operation depends on consistent file path structure; do not alter folder layout unless necessary.

**Enjoy caring for your virtual pet and have fun playing Duck Duck Goose!**

