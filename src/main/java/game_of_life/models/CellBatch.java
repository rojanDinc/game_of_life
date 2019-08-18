package game_of_life.models;

import java.io.Serializable;
import java.util.ArrayList;

import game_of_life.models.Cell;

/**
 * ClientInstruction
 */
public class CellBatch implements Serializable {
    private static final long serialVersionUID = 1L;
    private int startCol, endCol;
    private int startRow, endRow;
    private ArrayList<ArrayList<Cell>> cellBatch;

    /**
     * Cellbatch constructor which takes in multiple parameters for initialization
     * of variables.
     * 
     * @param startCol  start column
     * @param endCol    end column
     * @param startRow  start row
     * @param endRow    end row
     * @param cellBatch cell batch
     */
    public CellBatch(int startCol, int endCol, int startRow, int endRow, ArrayList<ArrayList<Cell>> cellBatch) {
        this.startCol = startCol;
        this.endCol = endCol;
        this.startRow = startRow;
        this.endRow = endRow;
        this.cellBatch = cellBatch;
    }

    /**
     * Get the cell batch
     * 
     * @return the cell batch
     */
    public ArrayList<ArrayList<Cell>> getCellBatch() {
        return cellBatch;
    }

    /**
     * Set the cell batch
     * 
     * @param cells the batch to replace the current with
     */
    public void setCellBatch(ArrayList<ArrayList<Cell>> cells) {
        this.cellBatch = cells;
    }

    /**
     * Get the column to start the calculation from
     * 
     * @return start column
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * Get the column to end the calculation on
     * 
     * @return end column
     */
    public int getEndCol() {
        return endCol;
    }

    /**
     * Get the row to start the calculation from
     * 
     * @return start row
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Get the row to start the calculation on
     * 
     * @return start row
     */
    public int getEndRow() {
        return endRow;
    }

}