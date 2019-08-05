package game_of_life.models;

import java.io.Serializable;
import java.util.UUID;

public class Cell implements Serializable {
    final static long serialVersionUID = 327984L;
    private int state;
    private String id;

    public Cell(int state) {
        this.state = state;
        id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            Cell c = (Cell) obj;
            return this.state == c.getState();
        }
        return false;
    }

    /**
     * @param cell the cell to add
     * @return current state
     */
    public int addState(Cell cell) {
        state += cell.state;
        return state;
    }

    /**
     * @param cell the cell to subtract with
     * @return current state
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

    public String getId() {
        return id;
    }
}