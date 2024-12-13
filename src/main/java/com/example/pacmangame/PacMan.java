package com.example.pacmangame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class PacMan {
    private double x;
    private double y;
    private final double size;
    private double speed;
    private double directionX = 0;
    private double directionY = 0;

    private double rotationAngle = 0;

    private double queuedDirectionX = 0;
    private double queuedDirectionY = 0;

    private boolean poweredUp = false;
    private long powerUpEndTime = 0;

    public void queueDirection(double dx, double dy) {
        queuedDirectionX = dx;
        queuedDirectionY = dy;
    }

    public boolean hasQueuedDirection() {
        return queuedDirectionX != 0 || queuedDirectionY != 0;
    }

    public void applyQueuedDirection() {
        directionX = queuedDirectionX;
        directionY = queuedDirectionY;
        queuedDirectionX = 0;
        queuedDirectionY = 0;

        // Update rotation angle based on direction
        if (directionX == 1 && directionY == 0) {
            rotationAngle = 0; // Right
        } else if (directionX == -1 && directionY == 0) {
            rotationAngle = 180; // Left
        } else if (directionX == 0 && directionY == -1) {
            rotationAngle = 270; // Up
        } else if (directionX == 0 && directionY == 1) {
            rotationAngle = 90; // Down
        }
    }

    public PacMan(double startX, double startY, double size, double speed) {
        this.x = startX;
        this.y = startY;
        this.size = size;
        this.speed = speed;
    }

    public void resetDirection() {
        directionX = 0;
        directionY = 0;
    }
    public void resetPosition(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        resetDirection(); // Reset movement direction as well
    }

    public void activatePowerUp(long powerUpEndTime) {
        this.poweredUp = true;
        this.powerUpEndTime = powerUpEndTime;
    }


    public boolean isPoweredUp() {
        if (poweredUp && System.currentTimeMillis() > powerUpEndTime) {
            poweredUp = false;
        }
        return poweredUp;
    }

    public boolean collidesWithPellet(int pelletRow, int pelletCol, Maze maze) {
        double cellSize = maze.getCellSize();

        // Calculate Pac-Man's center position
        double pacmanCenterX = this.x + this.size / 2;
        double pacmanCenterY = this.y + this.size / 2;

        // Calculate the center position of the pellet
        double pelletCenterX = pelletCol * cellSize + cellSize / 2;
        double pelletCenterY = pelletRow * cellSize + cellSize / 2;

        // Check if the distance between the centers is within the collision threshold
        double distanceX = Math.abs(pacmanCenterX - pelletCenterX);
        double distanceY = Math.abs(pacmanCenterY - pelletCenterY);
        double collisionThreshold = this.size / 2; // Only Pac-Man's size is relevant here

        return distanceX < collisionThreshold && distanceY < collisionThreshold;
    }


    public boolean canMove(double nextX, double nextY, Maze maze) {
        double cellSize = maze.getCellSize();

        // Calculate the positions of Pac-Man's corners
        double pacmanRight = nextX + getSize() - 1;
        double pacmanBottom = nextY + getSize() - 1;

        // Top-left corner
        int topLeftRow = (int) (nextY / cellSize);
        int topLeftCol = (int) (nextX / cellSize);

        // Top-right corner
        int topRightRow = topLeftRow;
        int topRightCol = (int) (pacmanRight / cellSize);

        // Bottom-left corner
        int bottomLeftRow = (int) (pacmanBottom / cellSize);
        int bottomLeftCol = topLeftCol;

        // Bottom-right corner
        int bottomRightRow = bottomLeftRow;
        int bottomRightCol = topRightCol;

        // Check all four corners against walls
        return !(maze.isWall(topLeftRow, topLeftCol) ||
                maze.isWall(topRightRow, topRightCol) ||
                maze.isWall(bottomLeftRow, bottomLeftCol) ||
                maze.isWall(bottomRightRow, bottomRightCol));
    }

    public double getDirectionX() {
        return directionX;
    }
    public double getDirectionY() {
        return directionY;
    }
    public double getSpeed() {
        return speed;
    }
    public double getSize() {
        return size;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.save(); // Save the current transformation
        gc.translate(x + size / 2, y + size / 2); // Translate to the center of Pac-Man
        gc.rotate(rotationAngle); // Apply rotation
        gc.fillArc(-size / 2, -size / 2, size, size, 30, 300, ArcType.ROUND); // Draw Pac-Man
        gc.restore(); // Restore the original transformation
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double nextX) {
        this.x = nextX;
    }

    public void setY(double nextY) {
        this.y = nextY;
    }

    public double getQueuedDirectionX() {
        return queuedDirectionX;
    }

    public double getQueuedDirectionY() {
        return queuedDirectionY;
    }

    public void move(double nextX, double nextY, Maze maze) {
        setX(nextX);
        setY(nextY);
    }

    public void deactivatePowerUp() {
        poweredUp = false;
    }

    public void allignAndQueueDirection(Maze currentMaze) {
        double cellSize = currentMaze.getCellSize();

        // Calculate Pac-Man's current cell
        int currentRow = (int) (getY() / currentMaze.getCellSize());
        int currentCol = (int) (getX() / currentMaze.getCellSize());

        // Snap Pac-Man to the center of the cell if aligned
        double centerX = currentCol * cellSize + cellSize / 2 - getSize() / 2;
        double centerY = currentRow * cellSize + cellSize / 2 - getSize() / 2;

        if (Math.abs(getX() - centerX) < getSpeed() &&
                Math.abs(getY() - centerY) < getSpeed()) {
            setX(centerX);
            setY(centerY);

            // Apply queued direction if possible
            if (hasQueuedDirection()) {
                double queuedNextX = getX() + getQueuedDirectionX() * getSpeed();
                double queuedNextY = getY() + getQueuedDirectionY() * getSpeed();
                if (canMove(queuedNextX, queuedNextY, currentMaze)) {
                    applyQueuedDirection();
                }
            }
        }
    }
}
