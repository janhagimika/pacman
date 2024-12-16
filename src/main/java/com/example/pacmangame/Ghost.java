// Ghost class
package com.example.pacmangame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ghost {
    private double x;
    private double y;
    private final double size;
    private final double speed;
    private double directionX;
    private double directionY;
    private final Image normalImage;
    private final Image blindImage;
    private boolean isBlind;

    public Ghost(double x, double y, double size, double speed, String color, Maze maze) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.isBlind = false;

        // Load images based on the color passed
        this.normalImage = new Image(getClass().getResourceAsStream("/com/example/pacmangame/images/" + color + "Ghost.png"));
        this.blindImage = new Image(getClass().getResourceAsStream("/com/example/pacmangame/images/blueGhost.png")); // Blue for blind state

        // Initialize direction
        initializeDirection(maze);
    }

    public void move(Maze maze) {
        double nextX = x + directionX * speed;
        double nextY = y + directionY * speed;
        snapToGridWithTolerance(maze);
        if (isAtIntersection(maze)) {
            System.out.println("is at intersection");
             // Align the ghost to the grid
            changeDirection(maze);        // Choose a new direction
            // Move the ghost slightly to ensure it's no longer at the intersection
            x += directionX * speed; // Move halfway in the new direction
            y += directionY * speed;
            return; // Exit to prevent further processing in the same frame
        }

        // Check if the ghost can continue moving in the current direction
        if (canMove(nextX, nextY, maze)) {
            this.x = nextX;
            this.y = nextY; // Move the ghost normally
        } else {
            // Blocked path: attempt to change direction only if not already at an intersection
            if (!isAtIntersection(maze)) {
                changeDirection(maze);
            }
        }
        handleStuckState(maze);

    }
    private void handleStuckState(Maze maze) {
        if (directionX == 0 && directionY == 0) {
            changeDirection(maze); // Force a direction change
            System.out.println("Ghost was stuck and forced to change direction.");
        }
    }

    private boolean isAtIntersection(Maze maze) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Up, down, left, right
        double cellSize = maze.getCellSize();

        // Ensure ghost is perfectly aligned with the grid
        if (!isAlignedWithGrid(x, cellSize) || !isAlignedWithGrid(y, cellSize)) {
            return false;
        }

        // Count the number of valid moves
        int validMoves = 0;
        for (int[] dir : directions) {
            double testX = x + dir[0] * cellSize; // Use cellSize to test intersections, not speed
            double testY = y + dir[1] * cellSize;
            if (canMove(testX, testY, maze)) {
                validMoves++;
            }
        }

        return validMoves >= 3; // Intersection if 3 or more valid moves
    }

    public boolean canMove(double nextX, double nextY, Maze maze) {
        double cellSize = maze.getCellSize();

        // Calculate positions of the corners
        int topLeftRow = (int) (nextY / cellSize);
        int topLeftCol = (int) (nextX / cellSize);
        int topRightRow = topLeftRow;
        int topRightCol = (int) ((nextX + size - 1) / cellSize);
        int bottomLeftRow = (int) ((nextY + size - 1) / cellSize);
        int bottomLeftCol = topLeftCol;
        int bottomRightRow = bottomLeftRow;
        int bottomRightCol = topRightCol;

        boolean withinBounds = topLeftRow >= 0 && topLeftRow < maze.getHeight() &&
                topLeftCol >= 0 && topLeftCol < maze.getWidth() &&
                bottomRightRow >= 0 && bottomRightRow < maze.getHeight() &&
                bottomRightCol >= 0 && bottomRightCol < maze.getWidth();

        boolean isWall = maze.isWall(topLeftRow, topLeftCol) ||
                maze.isWall(topRightRow, topRightCol) ||
                maze.isWall(bottomLeftRow, bottomLeftCol) ||
                maze.isWall(bottomRightRow, bottomRightCol);
        return withinBounds && !isWall;
    }

    private static final double EPSILON = 1e-1;

    private boolean isAlignedWithGrid(double value, double cellSize) {
        return Math.abs(value % cellSize) < EPSILON;
    }

    public void snapToGridWithTolerance(Maze maze) {
        double cellSize = maze.getCellSize();

        // Snap horizontal position (x) if moving vertically
        if (directionY != 0 && !isAlignedWithGrid(x, cellSize)) {
            x = Math.round(x / cellSize) * cellSize;
        }

        // Snap vertical position (y) if moving horizontally
        if (directionX != 0 && !isAlignedWithGrid(y, cellSize)) {
            y = Math.round(y / cellSize) * cellSize;
        }
    }

    private void changeDirection(Maze maze) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Up, down, left, right
        List<int[]> validDirections = new ArrayList<>();
        List<int[]> weightedDirections = new ArrayList<>();

        // Collect valid directions
        for (int[] dir : directions) {
            double testX = x + dir[0] * maze.getCellSize(); // Test full cell steps
            double testY = y + dir[1] * maze.getCellSize();
            if (canMove(testX, testY, maze)) {
                validDirections.add(dir);
            }
        }

        // Add weighted probability for valid directions
        for (int[] dir : validDirections) {
            if (dir[0] == -directionX && dir[1] == -directionY) {
                // Add the reverse direction once (low probability)
                weightedDirections.add(dir);
            } else {
                // Add other directions three times (higher probability)
                for (int i = 0; i < 3; i++) {
                    weightedDirections.add(dir);
                }
            }
        }

        // Choose a random direction based on the weighted list
        if (!weightedDirections.isEmpty()) {
            int[] chosenDirection = weightedDirections.get((int) (Math.random() * weightedDirections.size()));
            directionX = chosenDirection[0];
            directionY = chosenDirection[1];
        } else {
            // No valid moves; stop the ghost
            directionX = 0;
            directionY = 0;
        }
    }

    private void initializeDirection(Maze maze) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        List<int[]> list = new ArrayList<>(List.of(directions));
        Collections.shuffle(list); // Randomize directions

        for (int[] dir : directions) {
            double nextX = x + dir[0] * speed;
            double nextY = y + dir[1] * speed;


            if (canMove(nextX, nextY, maze)) {
                directionX = dir[0];
                directionY = dir[1];
                return;
            }
        }

        // Fallback if no direction is valid (shouldn't happen)..set the wrong one to be changed in changeDirection
        directionX = 0;
        directionY = 0;
        snapToGridWithTolerance(maze);
        changeDirection(maze);
    }

    public boolean collidesWith(PacMan pacman) {
        return this.getBounds().intersects(pacman.getBounds());
    }

    public javafx.geometry.Rectangle2D getBounds() {
        return new javafx.geometry.Rectangle2D(x, y, size, size);
    }


    public void render(GraphicsContext gc) {
        if (isBlind) {
            gc.drawImage(blindImage, x, y, size, size);
        } else {
            gc.drawImage(normalImage, x, y, size, size);
        }
    }

    public void setBlind(boolean bool) {
        isBlind = bool;
    }
}