package game_of_life.views;

import java.util.ArrayList;
import java.util.Random;

import game_of_life.models.Cell;
import game_of_life.utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * AppController
 */
public class AppController extends Pane {
    @FXML
    private Pane mainPane, controlPanel;
    @FXML
    private GridPane grid;
    @FXML
    private Label clientsLbl;
    @FXML
    public Button startBtn, pauseBtn, restartBtn, stopBtn;

    int cols;
    int rows;
    // Rectangle size
    int size = 20;
    private ArrayList<ArrayList<Rectangle>> rects = new ArrayList<ArrayList<Rectangle>>();

    public AppController(int cols, int rows) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/window.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (Exception e) {
            System.err.println(e);
        }
        this.cols = cols;
        this.rows = rows;
        initRects();
    }

    /**
     * @param clientsLbl the clientsLbl to set
     */
    public void setClientsLbl(int number) {
        this.clientsLbl.setText(Integer.toString(number));
    }

    /**
     * @return the grid
     */
    public GridPane getGrid() {
        return grid;
    }

    // Update the UI
    public void display(ArrayList<ArrayList<Cell>> cells) {
        Platform.runLater(() -> {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    if ((cells.get(i).get(j).getState() == 1)) {
                        rects.get(i).get(j).setFill(Color.BLACK);
                    } else {
                        rects.get(i).get(j).setFill(Color.WHITE);
                    }
                }
            }
        });
    }

    /**
     * Initialize the two dimensional Rectangle array which is used for representing
     * the cells on the GUI
     */
    private void initRects() {
        for (int i = 0; i < cols; i++) {
            ArrayList<Rectangle> temp_rects = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                Rectangle rectangle = new Rectangle(size, size, Color.YELLOW);
                GridPane.setRowIndex(rectangle, i);
                GridPane.setColumnIndex(rectangle, j);
                temp_rects.add(rectangle);
                grid.getChildren().addAll(rectangle);
            }
            rects.add(temp_rects);
        }
    }
}