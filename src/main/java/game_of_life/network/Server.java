package game_of_life.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Future;

import game_of_life.models.Cell;
import game_of_life.utils.Constants;

public class Server implements Runnable {

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private ArrayList<Socket> clients = new ArrayList<>();
    private int clientCount = 0;

    public Server() {
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void run() {
        // Listen for new clients
        while (true) {
            try {
                clients.add(serverSocket.accept());
                this.clientCount++;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    private Future<ArrayList<ArrayList<Cell>>> dispatchCells(ArrayList<ArrayList<Cell>> cells) {
        try {
            for (int i = 0; i < clients.size(); i++) {
                Socket client = clients.get(i);
                ObjectOutputStream oot = new ObjectOutputStream(client.getOutputStream());

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * Calculate how many cells to send to each client
     */
    // private ArrayList<ArrayList<Cell>> calcDist(ArrayList<ArrayList<Cell>> cells,
    // int no_clients) {
    // int cols = cells.size();
    // int rows = cells.get(0).size();
    // for (int i = 0; i < no_clients; i++) {
    // int _cols = cols / no_clients;
    // int _rows = cols / no_clients;
    // }
    // }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + Constants.PORT, e);
        }
    }
}