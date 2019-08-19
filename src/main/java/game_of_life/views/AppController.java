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
    public Button startBtn, restartBtn, stopBtn, addClientBtn;
    // Rectangle size
    private static final int size = 20;
    private ArrayList<ArrayList<Rectangle>> rects = new ArrayList<ArrayList<Rectangle>>();

    /**
     * <p>
     * Initializes the GUI by loading a fxml file and mapping the components to
     * variables. Also the initialization of UI rectangles are done.
     * </p>
     */
    public AppController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/window.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (Exception e) {
            System.err.println(e);
        }

        for (int i = 0; i < Constants.COLS; i++) {
            ArrayList<Rectangle> temp_rects = new ArrayList<>();
            for (int j = 0; j < Constants.ROWS; j++) {
                Rectangle rectangle = new Rectangle(size, size, Color.YELLOW);
                GridPane.setRowIndex(rectangle, i);
                GridPane.setColumnIndex(rectangle, j);
                temp_rects.add(rectangle);
                grid.getChildren().addAll(rectangle);
            }
            rects.add(temp_rects);
        }
    }

    /**
     * @param clientsLbl the Label to set
     */
    public void setClientsLbl(int number) {
        this.clientsLbl.setText(Integer.toString(number));
    }

    /**
     * @return the grid object
     */
    public GridPane getGrid() {
        return grid;
    }

    /**
     * Paints the UI with provided cells.
     * 
     * @param cells
     */
    public void display(ArrayList<ArrayList<Cell>> cells) {
        Random rand = new Random();
        Platform.runLater(() -> {
            for (int i = 0; i < Constants.COLS; i++) {
                for (int j = 0; j < Constants.ROWS; j++) {
                    if ((cells.get(i).get(j).getState() == 1)) {
                        rects.get(i).get(j).setFill(Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                    } else {
                        rects.get(i).get(j).setFill(Color.WHITE);
                    }
                }
            }
        });
    }
}