package game_of_life.utils;

import game_of_life.models.Cell;
import java.util.ArrayList;

public class GameOfLife {
    int cols = 32;
    int rows = 24;

    public GameOfLife() {
    }

    public GameOfLife(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
    }

    public ArrayList<ArrayList<Cell>> compute(ArrayList<ArrayList<Cell>> cells) {
        ArrayList<ArrayList<Cell>> next = initList();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Cell state = cells.get(i).get(j);
                // Count the cell's neighbors
                Cell neighbors = countNeighbors(cells, i, j);

                if (state.getState() == 0 && neighbors.getState() == 3) {
                    next.get(i).get(j).setState(1);
                } else if (state.getState() == 1 && (neighbors.getState() < 2 || neighbors.getState() > 3)) {
                    next.get(i).get(j).setState(0);
                } else {
                    next.get(i).get(j).setState(state.getState());
                }
            }
        }
        return next;
    }

    private Cell countNeighbors(ArrayList<ArrayList<Cell>> cells, int x, int y) {
        Cell sum = new Cell(0);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int col = (x + i + cols) % cols;
                int row = (y + j + rows) % rows;
                sum.addState(cells.get(col).get(row));
            }
        }
        sum.subState(cells.get(x).get(y));
        return sum;
    }

    /**
     * Generate an empty 2D ArrayList with Cell objects
     */
    public ArrayList<ArrayList<Cell>> initList() {
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