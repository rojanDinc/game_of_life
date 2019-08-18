package game_of_life.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClientSocket
 */
public class ClientSocket {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Initializes Output and Input streams.
     * 
     * @param socket connected socket.
     */
    public ClientSocket(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Returns the output stream
     * 
     * @return output stream
     */
    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * Returns the input stream
     * 
     * @return input stream
     */
    public ObjectInputStream getOis() {
        return ois;
    }

}