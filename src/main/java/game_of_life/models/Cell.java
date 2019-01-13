package game_of_life.models;

import java.io.Serializable;

public class Cell implements Serializable {
    final static long serialVersionUID = 327984L;
    int state;

    public Cell(int state) {
        this.state = state;
    }

    /**
     * @param cell the cell to add
     */
    public int addState(Cell cell) {
        state += cell.state;
        return state;
    }

    /**
     * @param cell the cell to subtract
     */
    public int subState(Cell cell) {
        state -= cell.state;
        return state;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
    }
}