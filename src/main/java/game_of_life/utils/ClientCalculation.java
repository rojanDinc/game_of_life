package game_of_life.utils;

import java.io.Serializable;
import java.util.ArrayList;

import game_of_life.models.Cell;

/**
 * ClientInstruction
 */
public class ClientCalculation implements Serializable {
    private static final long serialVersionUID = 1L;
    private int startCol, endCol;
    private int startRow, endRow;
    private ArrayList<ArrayList<Cell>> cellBatch;

    public ClientCalculation(int startCol, int endCol, int startRow, int endRow, ArrayList<ArrayList<Cell>> cellBatch) {
        this.startCol = startCol;
        this.endCol = endCol;
        this.startRow = startRow;
        this.endRow = endRow;
        this.cellBatch = cellBatch;
    }

    public ArrayList<ArrayList<Cell>> getCellBatch() {
        return cellBatch;
    }

    public void setCellBatch(ArrayList<ArrayList<Cell>> cells) {
        this.cellBatch = cells;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

}