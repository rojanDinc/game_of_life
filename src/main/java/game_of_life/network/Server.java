package game_of_life.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
            service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
                    System.out.println("Client added! Count: " + (this.clientCount + 1));
                }
            } catch (Exception e) {
            }
        }
    }

    public synchronized int getClientCount() {
        return this.clientCount;
    }

    public synchronized ArrayList<ArrayList<Cell>> calculate(ArrayList<ArrayList<Cell>> cells) throws Exception {
        // 1. Get all cells
        final ArrayList<ClientCalculation> batches = new ArrayList<>(getClientCount());
        // 2. Split cells into batches
        int cols_quotient = Constants.COLS / getClientCount(); // 10
        int rows_quotient = Constants.ROWS / getClientCount(); // 8
        for (int i = 0; i < getClientCount(); i++) {
            if (i < (getClientCount() - 1)) {
                batches.add(new ClientCalculation(i * cols_quotient, (i + 1) * cols_quotient, i * rows_quotient,
                        (i + 1) * rows_quotient, cells));
            } else {
                batches.add(new ClientCalculation(i * cols_quotient, Constants.COLS, i * rows_quotient, Constants.ROWS,
                        cells));
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
        final Cell[][] newCells = new Cell[32][24];
        // newBatches.forEach(batch -> {
        // for (int col = batch.getStartCol(); col < batch.getEndCol(); col++) {
        // ArrayList<Cell> _temp = new ArrayList<>();
        // for (int row = batch.getStartRow(); row < batch.getEndRow(); row++) {
        // _temp.add(batch.getCellBatch().get(col).get(row));
        // }
        // newCells.set(col, _temp);
        // }
        // });
        for (ClientCalculation batch : newBatches) {
            for (int col = batch.getStartCol(); col < batch.getEndCol(); col++) {
                for (int row = batch.getStartRow(); row < batch.getEndRow(); row++) {
                    // _temp.add(batch.getCellBatch().get(col).get(row));
                    newCells[col][row] = batch.getCellBatch().get(col).get(row);
                }
            }
        }
        // 6. Return array
        final List<List<Cell>> finalCells = Arrays.stream(newCells).map(Arrays::asList).collect(Collectors.toList());
        return to2DArrayList(finalCells);
    }

    private <T> ArrayList<ArrayList<T>> to2DArrayList(List<List<T>> twoDimenArr) {
        ArrayList<ArrayList<T>> results = new ArrayList<ArrayList<T>>();
        twoDimenArr.forEach(arr -> results.add(new ArrayList<T>(arr)));
        return results;
    }

    private class Task implements Callable<ClientCalculation> {
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
                e.printStackTrace();
                throw e;
            } finally {
                try {
                    // oos.reset();
                    // ois.reset();
                    oos.close();
                    ois.close();
                    oos = null;
                    ois = null;
                } catch (IOException e) {
                    e.printStackTrace();
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