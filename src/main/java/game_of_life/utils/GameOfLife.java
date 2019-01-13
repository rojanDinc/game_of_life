package game_of_life.utils;

import java.util.ArrayList;

import game_of_life.models.Cell;

public class GameOfLife {

    public GameOfLife(ArrayList<ArrayList<Cell>> cells) {
        cells = initList();
    }

    public ArrayList<ArrayList<Cell>> generate(ArrayList<ArrayList<Cell>> cells) {
        ArrayList<ArrayList<Cell>> next = initList();

        for (int x = 1; x < Constants.columns - 1; x++) {
            for (int y = 1; y < Constants.rows - 1; y++) {
                Cell neighbors = new Cell(0);
                // Counting neighbors
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        neighbors.addState(cells.get(x + i).get(y + j).getState());
                    }
                }

                neighbors.subState(cells.get(x).get(y).getState());

                if ((cells.get(x).get(y).getState() == 1) && (neighbors.getState() < 2))
                    next.get(x).get(y).setState(0);
                else if ((cells.get(x).get(y).getState() == 1) && (neighbors.getState() > 3))
                    next.get(x).get(y).setState(0);
                else if ((cells.get(x).get(y).getState() == 0) && (neighbors.getState() == 3))
                    next.get(x).get(y).setState(1);
                else
                    next.get(x).get(y).setState(cells.get(x).get(y).getState());
            }
        }

        return next;
    }

    public ArrayList<ArrayList<Cell>> initList() {
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < Constants.columns; i++) {
            ArrayList<Cell> newCells = new ArrayList<>();
            for (int j = 0; j < Constants.rows; j++) {
                newCells.add(new Cell(0));
            }
            cells.add(newCells);
        }

        return cells;
    }
}