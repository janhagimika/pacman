package com.example.pacmangame;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PacManController {
    @FXML
    private Canvas gameCanvas;
    private PacMan pacman;
    private List<Maze> levels;
    private List<Ghost> ghosts;
    private long boostEndTime = 0;
    private int currentLevelIndex = 0;
    private int score = 0;

    public void initialize() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        levels = new ArrayList<>();

        levels.add(new Maze(new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 2, 1},
                {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 1, 3, 2, 3, 3, 3, 1, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1},
                {1, 2, 1, 3, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 3, 3, 2, 1, 2, 2, 2, 2, 2, 2, 2, 3, 2, 1, 1, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1, 3, 2, 1},
                {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                {1, 1, 1, 3, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        }, 40));

        levels.add(new Maze(new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1},
                {1, 2, 1, 3, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 3, 2, 1, 2, 2, 2, 1},
                {1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1},
                {1, 2, 1, 3, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 1, 1, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 3, 2, 2, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1, 3, 2, 1},
                {1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                {1, 1, 1, 3, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        }, 40));


        Maze initialMaze = levels.get(currentLevelIndex);
        gameCanvas.setWidth(initialMaze.getCellSize() * initialMaze.getGrid()[0].length);
        gameCanvas.setHeight(initialMaze.getCellSize() * initialMaze.getGrid().length);

        pacman = new PacMan(initialMaze.getCellSize(), initialMaze.getCellSize(), initialMaze.getCellSize(), 2.5);

        ghosts = new ArrayList<>();
        initializeGhostsForCurrentLevel(); // Start with 4 ghosts

        // Start the game loop
        startGameLoop(gc);

        // Set up key event listeners
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::handleKeyPress);
    }

    private void initializeGhostsForCurrentLevel() {
        int baseGhostCount = 4; // Start with 4 ghosts
        int additionalGhosts = currentLevelIndex; // Add 1 ghost per level beyond the first
        int totalGhosts = baseGhostCount + additionalGhosts;

        initializeGhosts(totalGhosts);
    }


    private void initializeGhosts(int numberOfGhosts) {
        ghosts.clear();
        Maze currentMaze = levels.get(currentLevelIndex);
        double cellSize = currentMaze.getCellSize();
        String[] ghostColors = {"red", "green", "orange", "yellow"}; // Ghost colors
        double minDistance = 5 * cellSize; // Minimum safe distance (5 cells away)

        for (int i = 0; i < numberOfGhosts; i++) {
            double ghostX = 0, ghostY = 0;
            boolean validPositionFound = false;

            while (!validPositionFound) {
                // Generate a random position in the grid
                int col = (int) (Math.random() * currentMaze.getGrid()[0].length);
                int row = (int) (Math.random() * currentMaze.getGrid().length);

                // Check if the position is valid (not a wall)
                if (!currentMaze.isWall(row, col)) {
                    // Calculate ghost's position in pixels
                    ghostX = col * cellSize;
                    ghostY = row * cellSize;

                    // Ensure the ghost is at least 'minDistance' away from Pac-Man
                    double distanceToPacMan = Math.sqrt(
                            Math.pow(ghostX - pacman.getX(), 2) + Math.pow(ghostY - pacman.getY(), 2)
                    );

                    if (distanceToPacMan >= minDistance) {
                        // Use a temporary ghost object to check if at least one move is valid
                        Ghost tempGhost = new Ghost(ghostX, ghostY, cellSize, 2, "orange", currentMaze);
                        if (tempGhost.canMove(ghostX + cellSize, ghostY, currentMaze) ||
                                tempGhost.canMove(ghostX - cellSize, ghostY, currentMaze) ||
                                tempGhost.canMove(ghostX, ghostY + cellSize, currentMaze) ||
                                tempGhost.canMove(ghostX, ghostY - cellSize, currentMaze)) {
                            validPositionFound = true; // All checks passed
                        }
                    }
                }
            }

            // Add the new ghost to the list with its corresponding color
            String color = ghostColors[i % ghostColors.length];
            Ghost ghost = new Ghost(ghostX, ghostY, cellSize, 2, color, currentMaze);
            ghost.snapToGridWithTolerance(currentMaze); // Align the ghost perfectly
            ghosts.add(ghost);
        }
    }





    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> pacman.queueDirection(0, -1);
            case DOWN -> pacman.queueDirection(0, 1);
            case LEFT -> pacman.queueDirection(-1, 0);
            case RIGHT -> pacman.queueDirection(1, 0);
        }
    }

    private void startGameLoop(GraphicsContext gc) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        timer.start();
    }

    private void update() {
        Maze currentMaze = levels.get(currentLevelIndex);


        pacman.allignAndQueueDirection(currentMaze); // alligning pacman and setting his queued direction

        // Predict position based on the current direction
        double nextX = pacman.getX() + pacman.getDirectionX() * pacman.getSpeed();
        double nextY = pacman.getY() + pacman.getDirectionY() * pacman.getSpeed();

        // Check collisions with walls for the current direction
        if (pacman.canMove(nextX, nextY, currentMaze)) {
            pacman.move(nextX, nextY, currentMaze);
        } else {
            pacman.resetDirection(); // Stop Pac-Man if a collision is detected
        }

        //consume pellets and power pellets
        int row = (int) (pacman.getY() / currentMaze.getCellSize());
        int col = (int) (pacman.getX() / currentMaze.getCellSize());
        if (currentMaze.isPellet(row, col) && pacman.collidesWithPellet(row, col, currentMaze)) {
            currentMaze.consume(row, col);
            score += 10;
        } else if (currentMaze.isPowerUp(row, col) && pacman.collidesWithPellet(row, col, currentMaze)) {
            currentMaze.consume(row, col);
            score += 50;
            boostEndTime = System.nanoTime() + 8_000_000_000L; // 8 seconds boost
            pacman.activatePowerUp();
            // Make all ghosts "blind"
            for (Ghost ghost : ghosts) {
                ghost.setBlind(true);
            }
        }

        //deactivate powerUp after 8 secs
        if (System.nanoTime() > boostEndTime && pacman.isPoweredUp()) {
            for (Ghost ghost : ghosts) {
                ghost.setBlind(false);
            }
            pacman.deactivatePowerUp(); // Deactivate boost
        }

        // Check for collision with ghosts
        if (pacman.isPoweredUp()) {
            List<Ghost> eatenGhosts = new ArrayList<>();
            for (Ghost ghost : ghosts) {
                if (ghost.collidesWith(pacman)) { // Using bounding box method
                    score += 200;
                    eatenGhosts.add(ghost);
                }
            }
            ghosts.removeAll(eatenGhosts);
        } else {
            for (Ghost ghost : ghosts) {
                if (ghost.collidesWith(pacman)) { // Using bounding box method
                    System.out.println("Game Over! Final Score: " + score);
                    System.exit(0);
                }
            }
        }


        // Move ghosts
        for (Ghost ghost : ghosts) {
            ghost.move(currentMaze);
        }

        // Check if all pellets are consumed
        if (isLevelCleared(currentMaze)) {
            if (currentLevelIndex + 1 < levels.size()) {
                currentLevelIndex++;
                Maze nextMaze = levels.get(currentLevelIndex);
                initializeGhostsForCurrentLevel();
                pacman.resetPosition(nextMaze.getCellSize(), nextMaze.getCellSize());
                gameCanvas.setWidth(nextMaze.getCellSize() * nextMaze.getGrid()[0].length);
                gameCanvas.setHeight(nextMaze.getCellSize() * nextMaze.getGrid().length);
            } else {
                System.out.println("Game Over! Final Score: " + score);
                System.exit(0);
            }
        }
    }


    private boolean isLevelCleared(Maze maze) {
        for (int[] row : maze.getGrid()) {
            for (int cell : row) {
                if (cell == 2 || cell == 3) return false; // Pellet or power-pellet exists
            }
        }
        return true;
    }

    private void render(GraphicsContext gc) {
        Maze currentMaze = levels.get(currentLevelIndex);

        // Clear the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Render maze and Pac-Man
        // Render maze, Pac-Man, and ghosts
        currentMaze.render(gc);
        pacman.render(gc);
        for (Ghost ghost : ghosts) {
            ghost.render(gc);
        }

        // Render score
        gc.setFill(Color.YELLOW);
        gc.fillText("SCORE: " + score, 10, gameCanvas.getHeight() - 5);
        gc.fillText("LEVEL: " + (currentLevelIndex + 1), 10, gameCanvas.getHeight() - 20);

        // Render boost state
        if (pacman.isPoweredUp()) {
            gc.setFill(Color.CYAN);
            gc.fillText("BOOST ACTIVE!", gameCanvas.getWidth() - 100, gameCanvas.getHeight() - 10);
        }
    }
}
