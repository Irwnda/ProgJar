package chat.server;

import chat.object.Object;

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
                Object message = (Object) this.ois.readObject();
                if(message.getType().equals("Message")){
                    serverThread.sendToAll(message);
                }
                else if(message.getType().equals("Client")){
                    if(message.getAction()==1){
                        serverThread.addClient(message.getSender());
                    }
                    else{
                        serverThread.removeClient(message.getSender());
                    }
                    serverThread.updateConnectedClient();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Object obj) {
        try {
            this.ous.writeObject(obj);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}