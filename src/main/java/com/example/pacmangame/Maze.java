package com.example.pacmangame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Maze {
    private final int[][] grid; // 1 = wall, 0 = path, 2 = pellet, 3 = power-up
    private final double cellSize;

    public Maze(int[][] grid, double cellSize) {
        this.grid = grid;
        this.cellSize = cellSize;
    }

    public void render(GraphicsContext gc) {

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 1) { // Wall
                    gc.setFill(Color.PURPLE);
                    gc.fillRect(col * cellSize, row * cellSize, cellSize, cellSize); // Ensure base wall drawing


                } else if (grid[row][col] == 2) { // Pellet
                    gc.setFill(Color.WHITE);
                    gc.fillOval(col * cellSize + cellSize / 4, row * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
                } else if (grid[row][col] == 3) { // Power Pellet
                    gc.setFill(Color.WHITE);
                    gc.fillOval(col * cellSize + cellSize / 4, row * cellSize + cellSize / 4, cellSize / 1.5, cellSize / 1.5);
                }
            }
        }
    }


    public boolean isWall(int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return true; // Treat out-of-bounds as walls
        }
        return grid[row][col] == 1; // 1 represents a wall
    }



    public boolean isPellet(int row, int col) {
        return grid[row][col] == 2;
    }

    public boolean isPowerUp(int row, int col) {
        return grid[row][col] == 3;
    }

    public double getCellSize() {
        return cellSize;
    }

    public void consume(int row, int col) {
        if (grid[row][col] == 2 || grid[row][col] == 3) {
            grid[row][col] = 0; // Remove pellet or power-up
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public double getHeight() {
        return cellSize;
    }

    public double getWidth() {
        return cellSize;
    }
}
