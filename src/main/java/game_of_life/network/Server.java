package game_of_life.network;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import game_of_life.models.Cell;
import game_of_life.models.CellBatch;
import game_of_life.utils.Constants;

public class Server implements Runnable {

    private ServerSocket serverSocket = null;
    private ArrayList<ClientSocket> clients = new ArrayList<>();
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
                clients.add(new ClientSocket(serverSocket.accept()));
                synchronized (this) {
                    this.clientCount += 1;
                    System.out.println("Client added! Count: " + (this.clientCount));
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
        final int totClients = getClientCount();
        // final ArrayList<PacketBatch> batches = new ArrayList<>(totClients);
        final CellBatch[] batches = new CellBatch[totClients];
        // 2. Split cells into batches
        final int rows_quotient = Constants.ROWS / totClients; // 8
        for (int i = 0; i < totClients; i++) {
            if (i < (totClients - 1)) {
                batches[i] = new CellBatch(0, Constants.COLS, i * rows_quotient, (i + 1) * rows_quotient, cells);
            } else {
                batches[i] = new CellBatch(0, Constants.COLS, i * rows_quotient, Constants.ROWS, cells);
            }
        }
        // 3. Distribute batches to clients
        List<Future<CellBatch>> allFutures = new ArrayList<>();
        for (int i = 0; i < totClients; i++) {
            allFutures.add(service.submit(new Task(clients.get(i), batches[i])));
        }
        // 4. Wait for batches to be calculated
        final CellBatch[] newBatches = new CellBatch[totClients];
        for (int i = 0; i < allFutures.size(); i++) {
            Future<CellBatch> future = allFutures.get(i);
            try {
                CellBatch result = future.get(); // blocking
                newBatches[i] = result;
            } catch (Exception e) {
                throw e;
            }
        }
        // 5. Aggregate batches into 2D array
        final Cell[][] newCells = new Cell[Constants.COLS][Constants.ROWS];
        for (int c = 0; c < totClients; c++) {
            CellBatch pb = newBatches[c];
            for (int col = 0; col < Constants.COLS; col++) {
                for (int row = pb.getStartRow(); row < pb.getEndRow(); row++) {
                    newCells[col][row] = pb.getCellBatch().get(col).get(row);
                }
            }
        }

        // 6. Return array
        List<ArrayList<Cell>> lists = new ArrayList<ArrayList<Cell>>();
        for (Cell[] var : newCells) {
            lists.add(new ArrayList<Cell>(Arrays.asList(var)));
        }
        return to2DArrayList(lists);
    }

    private <T> ArrayList<ArrayList<T>> to2DArrayList(List<ArrayList<T>> twoDimenArr) {
        ArrayList<ArrayList<T>> results = new ArrayList<ArrayList<T>>();
        twoDimenArr.forEach(arr -> results.add(new ArrayList<T>(arr)));
        return results;
    }

    private class Task implements Callable<CellBatch> {
        private CellBatch cellBatch;
        private ClientSocket client;

        @Override
        public CellBatch call() throws Exception {
            try {
                client.getOos().writeObject(cellBatch);
                // Wait for calculation of batch to return from client
                CellBatch newCellBatch = (CellBatch) client.getOis().readObject();
                return newCellBatch;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        public Task(ClientSocket client, CellBatch cellBatch) {
            this.client = client;
            this.cellBatch = cellBatch;
        }

    }
}