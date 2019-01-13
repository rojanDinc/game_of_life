package game_of_life.models;

import java.io.Serializable;

public class Cell implements Serializable {
    final static long serialVersionUID = 327984L;
    int state;

    public Cell(int state) {
        this.state = state;
    }

    public int addState(int state) {
        int s = this.state += state;
        return s;
    }

    public int subState(int state) {
        int s = this.state += state;
        return s;
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