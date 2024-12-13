// Ghost class
package com.example.pacmangame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ghost {
    private double x;
    private double y;
    private final double size;
    private double speed;
    private double directionX;
    private double directionY;
    private Image normalImage;
    private Image blindImage;
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

        // Check if the ghost is at an intersection
        if (isAtIntersection(maze)) {
            snapToGridWithTolerance(maze);
            changeDirection(maze);
        }

        if (canMove(nextX, nextY, maze)) {
            this.x = nextX;
            this.y = nextY;
        } else {
            snapToGridWithTolerance(maze); // Align again if blocked
            changeDirection(maze);
        }
    }

    private boolean isAtIntersection(Maze maze) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Up, down, left, right
        double cellSize = maze.getCellSize();

        if (!isAlignedWithGrid(x, cellSize) || !isAlignedWithGrid(y, cellSize)) {
            return false;
        }

        int validMoves = 0;

        for (int[] dir : directions) {
            double nextX = x + dir[0] * speed;
            double nextY = y + dir[1] * speed;

            if (canMove(nextX, nextY, maze)) {
                System.out.println("Direction [" + dir[0] + ", " + dir[1] + "] is valid.");
                validMoves++;
            }
            else {
                System.out.println("Direction [" + dir[0] + ", " + dir[1] + "] is blocked.");
            }
        }

        // An intersection has at least 3 or more valid moves
        return validMoves >= 3; // Adjust threshold if necessary
    }




    private boolean canMove(double nextX, double nextY, Maze maze) {
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

        // Debug print for precise analysis
        /*System.out.printf("Checking move to (%.2f, %.2f): withinBounds=%b, TLWall=%b, TRWall=%b, BLWall=%b, BRWall=%b%n",
                nextX, nextY, withinBounds,
                maze.isWall(topLeftRow, topLeftCol),
                maze.isWall(topRightRow, topRightCol),
                maze.isWall(bottomLeftRow, bottomLeftCol),
                maze.isWall(bottomRightRow, bottomRightCol));*/

        return withinBounds && !isWall;
    }


    private static final double EPSILON = 1e-3;

    private boolean isAlignedWithGrid(double value, double cellSize) {
        return Math.abs(value % cellSize) < EPSILON;
    }


    private void snapToGridWithTolerance(Maze maze) {
        double cellSize = maze.getCellSize();

        // Snap horizontal position (x) if moving vertically
        if (directionY != 0) {
            x = Math.round(x / cellSize) * cellSize;
        }

        // Snap vertical position (y) if moving horizontally
        if (directionX != 0) {
            y = Math.round(y / cellSize) * cellSize;
        }

        // Ensure both alignments when standing still or changing direction
        if (directionX == 0 && directionY == 0) {
            x = Math.round(x / cellSize) * cellSize;
            y = Math.round(y / cellSize) * cellSize;
        }

        // Debugging output to confirm snapping
        System.out.printf("Snapped to grid: (%.2f, %.2f)%n", x, y);
    }

    private void changeDirection(Maze maze) {
        // Define directions and initialize weights
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Up, down, left, right
        List<int[]> weightedDirections = new ArrayList<>();

        // Current direction has lower weight
        for (int[] dir : directions) {
            if (dir[0] == directionX && dir[1] == directionY) {
                // Add current direction 1 time
                weightedDirections.add(dir);
            } else {
                // Add other directions multiple times for higher weight
                for (int i = 0; i < 3; i++) {
                    weightedDirections.add(dir);
                }
            }
        }

        // Shuffle weighted directions
        Collections.shuffle(weightedDirections);

        // Try to move in a valid direction
        for (int[] dir : weightedDirections) {
            double nextX = x + dir[0] * speed;
            double nextY = y + dir[1] * speed;

            if (canMove(nextX, nextY, maze)) {
                directionX = dir[0];
                directionY = dir[1];
                return;
            }
        }

        // Fallback in case no valid move is found
        directionX = 0;
        directionY = 0;
    }



    private void initializeDirection(Maze maze) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        List<int[]> list = new ArrayList<>(List.of(directions));
        Collections.shuffle(list); // Randomize directions
        System.out.println("Initializing ghost at (" + x + ", " + y + ")");

        for (int[] dir : directions) {
            double nextX = x + dir[0] * speed;
            double nextY = y + dir[1] * speed;


            if (canMove(nextX, nextY, maze)) {
                directionX = dir[0];
                directionY = dir[1];
                System.out.println("Initial direction set to: (" + directionX + ", " + directionY + ")");

                return;
            }
        }

        // Fallback if no direction is valid (shouldn't happen)
        directionX = 0;
        directionY = 0;
    }



    public boolean collidesWith(PacMan pacman) {
        double pacmanCenterX = pacman.getX() + pacman.getSize() / 2;
        double pacmanCenterY = pacman.getY() + pacman.getSize() / 2;

        double ghostCenterX = this.x + this.size / 2;
        double ghostCenterY = this.y + this.size / 2;

        double distanceX = Math.abs(pacmanCenterX - ghostCenterX);
        double distanceY = Math.abs(pacmanCenterY - ghostCenterY);
        double collisionThreshold = this.size / 2 + pacman.getSize() / 2;

        return distanceX < collisionThreshold && distanceY < collisionThreshold;
    }



    public void render(GraphicsContext gc) {
        if (isBlind) {
            gc.drawImage(blindImage, x, y, size, size);
        } else {
            gc.drawImage(normalImage, x, y, size, size);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void resetPosition(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }

    public void setBlind(boolean bool) {
        isBlind = bool;
    }
}