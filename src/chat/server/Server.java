package chat.server;

import chat.client.Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerThread st = new ServerThread();
        st.start();
    }
}
