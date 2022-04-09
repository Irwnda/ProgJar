package chat.server;

import chat.object.Object;
import utils.Dbg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ServerThread extends Thread {
    private Hashtable<String, WorkerThread> clientList;
    private ArrayList<String> Clients = new ArrayList<>();
    private ServerSocket server;

    public ServerThread() {
        try {
            this.clientList = new Hashtable<>();
            this.server = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // listen for a new connection
        while(true) {
            try {
                // accept a new connection
                Socket socket = this.server.accept();

                // create a new WorkerThread
                WorkerThread wt = new WorkerThread(socket, this);

                // start the new thread
                wt.start();

                // store the new thread to the hash table
                String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

                this.clientList.put(clientId, wt);
            } catch (IOException e) {
                closeServerSocket();
            }
        }
    }

    public void closeServerSocket(){
        try{
            if(this.server != null){
                this.server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String userName){
        this.Clients.add(userName);
        Dbg.debugKu("Connected Client:");
        for(int i = 0; i<Clients.size(); i++)
            System.out.println(Clients.get(i));
    }

    public void removeClient(String userName){
        this.Clients.remove(userName);
    }

    public ArrayList<String> getClients(){
        return this.Clients;
    }

    public void sendToAll(Object message) {
        // iterate through all clients
        Enumeration<String> clientKeys = this.clientList.keys();
        while (clientKeys.hasMoreElements()) {
            String clientId = clientKeys.nextElement();

            WorkerThread wt = this.clientList.get(clientId);

            // send the message
            wt.send(message);
        }
    }

    public void updateConnectedClient(){
        Enumeration<String> clientKeys = this.clientList.keys();
        Object obj = new Object();
        obj.setClients(Clients);
        System.out.println("Client list: " + obj.getClients());
        obj.setType("ClientList");
        while (clientKeys.hasMoreElements()) {
            String clientId = clientKeys.nextElement();

            WorkerThread wt = this.clientList.get(clientId);

            // send the object
            wt.send(obj);
        }
    }
}