package chat.server;

import chat.object.Object;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private Server server;

    public WorkerThread(Socket socket, Server server) {
        try {
            this.ous = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.server = server;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Object obj = (Object) this.ois.readObject();

                if(obj.getType().equals("Message")){
                    server.sendToAll(obj);
                }
                else if(obj.getType().equals("Client")){
                    if(obj.getAction()==1){
                        server.addClient(obj.getSender());
                    }
                    else{
                        server.removeClient(obj.getSender());
                    }
                    server.updateConnectedClient();
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