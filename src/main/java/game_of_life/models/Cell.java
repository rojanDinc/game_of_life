package game_of_life.models;

import java.io.Serializable;
import java.util.Random;

public class Cell implements Serializable {
    final static long serialVersionUID = 327984L;
    int state;

    public Cell() {
        Random random = new Random();
        state = random.nextInt(2);
    }

    public Cell(int state) {
        this.state = state;
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