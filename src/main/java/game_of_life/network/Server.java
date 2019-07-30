package game_of_life.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import game_of_life.models.Cell;
import game_of_life.utils.ClientCalculation;
import game_of_life.utils.Constants;

public class Server implements Runnable {

    private ServerSocket serverSocket = null;
    private ArrayList<Socket> clients = new ArrayList<>();
    private int clientCount = 0;
    private ExecutorService service;

    public Server() {
        try {
            serverSocket = new ServerSocket(Constants.PORT);
            service = Executors.newSingleThreadExecutor();
        } catch (Exception e) {
            throw new RuntimeException("Cannot open port " + Constants.PORT, e);
        }
    }

    @Override
    public void run() {
        // Listen for new clients
        while (true) {
            try {
                clients.add(serverSocket.accept());
                synchronized (this) {
                    this.clientCount++;
                }
            } catch (Exception e) {
            }
        }
    }

    private synchronized int getClientCount() {
        return this.clientCount;
    }

    public ArrayList<ArrayList<Cell>> calculate(ArrayList<ArrayList<Cell>> cells) throws Exception {
        // 1. Get all cells
        final ArrayList<ClientCalculation> batches = new ArrayList<>(getClientCount());
        // 2. Split cells into batches
        int cols_quotient = Constants.COLS / getClientCount(); // 10
        int rows_quotient = Constants.ROWS / getClientCount(); // 8
        for (int i = 0; i < getClientCount(); i++) {
            if (i < (getClientCount() - 1)) {
                batches.add(new ClientCalculation(i * cols_quotient, Constants.COLS - 1, i * rows_quotient,
                        Constants.ROWS - 1, cells));
            } else {
                batches.add(new ClientCalculation(i * cols_quotient, (i + 1) * cols_quotient - 1, i * rows_quotient,
                        (i + 1) * rows_quotient - 1, cells));
            }
        }
        // 3. Distribute batches to clients
        List<Future<ClientCalculation>> allFutures = new ArrayList<>();
        for (int i = 0; i < getClientCount(); i++) {
            allFutures.add(service.submit(new Task(clients.get(i), batches.get(i))));
        }
        // 4. Wait for batches to be calculated
        final ArrayList<ClientCalculation> newBatches = new ArrayList<>(getClientCount());
        for (int i = 0; i < allFutures.size(); i++) {
            Future<ClientCalculation> future = allFutures.get(i);
            try {
                ClientCalculation result = future.get(); // blocking
                newBatches.add(result);
            } catch (Exception e) {
                throw e;
            }
        }
        // 5. Aggregate batches into 2D array
        final ArrayList<ArrayList<Cell>> newCells = new ArrayList<ArrayList<Cell>>();
        newBatches.forEach(batch -> {
            for (int col = batch.getStartCol(); col < batch.getEndCol(); col++) {
                ArrayList<Cell> _temp = new ArrayList<>();
                for (int row = batch.getStartRow(); row < batch.getEndRow(); row++) {
                    _temp.add(batch.getCellBatch().get(col).get(row));
                }
                newCells.set(col, _temp);
            }
        });
        // 6. Return array
        return newCells;
    }

    static class Task implements Callable<ClientCalculation> {
        private Socket client;
        private ClientCalculation cellBatch;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        @Override
        public ClientCalculation call() throws Exception {
            try {
                oos = new ObjectOutputStream(this.client.getOutputStream());
                ois = new ObjectInputStream(this.client.getInputStream());
                oos.writeObject(cellBatch);
                // Wait for calculation of batch to return from client
                ClientCalculation newCellBatch = (ClientCalculation) ois.readObject();
                return newCellBatch;
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    oos.close();
                    ois.close();
                    client = null;
                } catch (IOException e) {
                    throw e;
                }
            }
        }

        public Task(Socket client, ClientCalculation cellBatch) {
            this.client = client;
            this.cellBatch = cellBatch;
        }

    }
}