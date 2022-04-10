package chat.server;

import chat.object.Object;
import utils.CColors;
import utils.Dbg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server {
    private Hashtable<String, WorkerThread> clientList;
    private ArrayList<String> clients = new ArrayList<>();
    private ServerSocket server;

    public Server() {
        try {
            this.clientList = new Hashtable<>();
            this.server = new ServerSocket(9000);

            initiateServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateServer() {
        while(true) {
            try {
                Dbg.debugKu("Listening new connection");
                Socket socket = server.accept();
                WorkerThread wt = new WorkerThread(socket, this);
                String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

                Dbg.debugKu("WT running for clientId : " + clientId);
                clientList.put(clientId, wt);
                wt.start();

            } catch (IOException e) {
                System.out.println(CColors.RED + "Server STOPPED" + CColors.RESET);
                closeServerSocket();
            }
        }
    }

    public void closeServerSocket(){
        try{
            if(server != null){
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String userName){
        clients.add(userName);
        Dbg.debugKu("Connected Client :");

        for(int i = 0; i< clients.size(); i++)
            System.out.println((i+1)+ ". " + clients.get(i));
    }

    public void removeClient(String userName){
        clients.remove(userName);
    }

    public ArrayList<String> getClients(){
        return clients;
    }

    public void sendToAll(Object message) {
        // iterate through all clients
        Enumeration<String> clientKeys = clientList.keys();
        while (clientKeys.hasMoreElements()) {
            String clientId = clientKeys.nextElement();

            WorkerThread wt = clientList.get(clientId);

            // send the message
            wt.send(message);
        }
    }

    public void updateConnectedClient(){
        Enumeration<String> clientKeys = clientList.keys();
        Object obj = new Object();
        obj.setClients(clients);
        System.out.println("Client list: " + obj.getClients());
        obj.setType("ClientList");
        while (clientKeys.hasMoreElements()) {
            String clientId = clientKeys.nextElement();

            WorkerThread wt = clientList.get(clientId);

            // send the object
            wt.send(obj);
        }
    }

    public static void main(String[] args) {
        Server serverRunner = new Server();
    }
}