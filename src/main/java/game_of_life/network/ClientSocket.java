package game_of_life.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClientSocket
 */
public class ClientSocket {

    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public ClientSocket(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

}