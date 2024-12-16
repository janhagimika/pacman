# Pac-Man Game

A 2D Pac-Man game implemented in Java using JavaFX. The game features Pac-Man, ghosts, pellets, power-ups, and dynamic maze progression.

---

## Features

- **Classic Gameplay**: Navigate Pac-Man through the maze, eating pellets and power-ups while avoiding ghosts.
- **Dynamic Ghost Behavior**: Ghosts follow a pathfinding algorithm with randomness to challenge the player.
- **Power-Ups**: Pac-Man can eat power-ups to make ghosts vulnerable for a limited time.
- **Level Progression**: Multiple levels with unique maze layouts and increasing ghost counts.
- **Collision Detection**: Accurate detection for Pac-Man's interaction with ghosts, pellets, and power-ups.

---

## How to Play

Use arrow keys to control Pac-Man:

↑ **Up**  
↓ **Down**  
← **Left**  
→ **Right**

### Objective:
- Eat all pellets to clear the level.
- Avoid ghosts unless Pac-Man has eaten a power-up.
- Gain bonus points by eating vulnerable ghosts.
- Progress to the next level after clearing all pellets.

---

## Running the Game

### Prerequisites
- Java 17 or higher installed.
- Maven installed (optional, for building).

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/janhagimika/pacman-game
   ```
2. Navigate to the project directory:
   ```bash
   cd pacman-game
   ```
3. Build the application:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn javafx:run
   ```

---

## Gameplay Details

### Power-Up Behavior
- Eating a power-up makes ghosts vulnerable for **8 seconds**.
- While vulnerable, ghosts can be eaten for bonus points.
- After 8 seconds, ghosts return to their normal behavior.

### Scoring
- Eating a pellet: **10 points**
- Eating a power-up: **50 points**
- Eating a vulnerable ghost: **200 points**

### Level Progression
- Ghost count increases with each level.
- Unique mazes for each level add variety and challenge.

---

## Known Issues & Future Enhancements

### Collision Detection Improvement
- Fine-tuning of Pac-Man and ghost collision boundaries for better accuracy.

### Additional Features
- **Ghost AI Enhancements**: Smarter pathfinding behavior for chasing Pac-Man.
- **Pause/Resume**: Add functionality to pause and resume the game.
- **High Scores**: Store and display the highest scores achieved by players.

---

## Notes on Project Structure

### Key Classes
- **PacMan.java**: Handles Pac-Man's movement, rendering, and power-up logic.
- **Ghost.java**: Implements ghost movement, behavior, and rendering.
- **Maze.java**: Represents the game grid, including walls, pellets, and power-ups.
- **PacManController.java**: Manages game logic, collisions, score tracking, and rendering.
- **PacManGameApplication.java**: Entry point to start the game.

### Resources
- **Images**: Contains sprite assets for Pac-Man and ghosts.
- **FXML**: JavaFX layout files for rendering the game.

---

## Global Exception Handling

All errors during initialization or gameplay return meaningful logs to the console for debugging purposes.

---

## Known Vulnerabilities

The project is a demo game and does not include external security features. For production or multiplayer adaptations, additional security measures should be applied.

---

## Notes
- This project was built for learning purposes and demonstrates fundamental JavaFX concepts and game development principles.

