package game_of_life.utils;

import game_of_life.models.Cell;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Game logic class
 */
public class Game {
    private int cols = Constants.COLS;
    private int rows = Constants.ROWS;

    /**
     * Empty Contstructor
     */
    public Game() {
    }

    /**
     * Parameterized constructor
     * 
     * @param cols amount of columns the display is initialized with
     * @param rows amount of rows the display is initialized with
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
    }

    public ArrayList<ArrayList<Cell>> compute(ArrayList<ArrayList<Cell>> cells) {
        ArrayList<ArrayList<Cell>> next = initList();
        for (int i = 0; i < next.size(); i++) {
            for (int j = 0; j < next.get(0).size(); j++) {
                Cell state = cells.get(i).get(j);
                // Count the cell's neighbors
                Cell neighbors = countNeighbors(cells, i, j);

                // Rules
                /**
                 * 1. Any live cell with fewer than two live neighbors dies, as if by
                 * underpopulation.
                 */
                if (state.getState() == 1 && neighbors.getState() < 2) {
                    next.get(i).get(j).setState(0);
                }
                /**
                 * 2. Any live cell with two or three live neighbors lives on to the next
                 * generation.
                 */
                else if (state.getState() == 1 && (neighbors.getState() == 2 || neighbors.getState() == 3)) {
                    next.get(i).get(j).setState(1);
                }
                /**
                 * 3. Any live cell with more than three live neighbors dies, as if by
                 * overpopulation.
                 */
                else if (state.getState() == 1 && neighbors.getState() > 3) {
                    next.get(i).get(j).setState(0);
                }
                /**
                 * 4. Any dead cell with exactly three live neighbors becomes a live cell, as if
                 * by reproduction.
                 */
                else if (state.getState() == 0 && neighbors.getState() == 3) {
                    next.get(i).get(j).setState(1);
                }

            }
        }
        return next;
    }

    /**
     * <p>
     * Get the current cells position to calculate the cells neighbours and return a
     * new cell with a new state depending on its current neighbours and replace
     * this with the initial cell.
     * </p>
     * 
     * @param cells 2D ArrayList with the current cells
     * @param x     x position to count neighbours from
     * @param y     y position to count neighbours from
     * @return a new cell with a new state
     */
    private Cell countNeighbors(ArrayList<ArrayList<Cell>> cells, int x, int y) {
        Cell c = new Cell(
                IntStream.range(-1, 2)
                        .map(i -> IntStream.range(-1, 2)
                                .map(j -> cells.get((x + i + cols) % cols).get((y + j + rows) % rows).getState()).sum())
                        .sum());
        c.subState(cells.get(x).get(y));
        return c;
    }

    /**
     * Generate an empty 2D ArrayList with Cell objects
     * 
     * @return the 2D ArrayList
     */
    private ArrayList<ArrayList<Cell>> initList() {
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < cols; i++) {
            ArrayList<Cell> newCells = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                newCells.add(new Cell(0));
            }
            cells.add(newCells);
        }

        return cells;
    }
}