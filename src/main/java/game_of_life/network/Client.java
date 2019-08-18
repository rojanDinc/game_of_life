package game_of_life.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import game_of_life.models.*;
import game_of_life.utils.Constants;
import game_of_life.utils.Game;

/**
 * Client
 */
public class Client implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Game game;

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            while (socket.isConnected()) {
                CellBatch batch = (CellBatch) ois.readObject();
                game = new Game(batch.getEndCol(), batch.getEndRow());
                ArrayList<ArrayList<Cell>> newCells = game.compute(batch.getCellBatch());
                batch.setCellBatch(newCells);
                oos.writeObject(batch);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public Client() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.PORT);
            System.out.println("Connected to server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}