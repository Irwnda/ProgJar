package chat.server;

import chat.object.Message;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private ServerThread serverThread;

    public WorkerThread(Socket socket, ServerThread serverThread) {
        try {
            this.ous = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.serverThread = serverThread;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Message message = (Message) this.ois.readObject();

                switch (message.getAction()) {
                    case 1 -> serverThread.addClient(message.getSender());
                    case 0 -> serverThread.sendToAll(message);
                    case -1 -> serverThread.removeClient(message.getSender());
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Message message) {
        try {
            this.ous.writeObject(message);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}