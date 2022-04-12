package chat.server;

import chat.object.Object;
import chat.object.Person;
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
    private ArrayList<Person> clients = new ArrayList<>();
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

    public void addClient(Person client){
        clients.add(client);
    }

    public void removeClient(Person person){
        clients.remove(person);
    }

    public ArrayList<Person> getClients(){
        return clients;
    }

    public void setClients(ArrayList<Person> clients) {
        this.clients = clients;
    }

    public void sendToAll(Object obj) {
        Enumeration<String> clientKeys = clientList.keys();

        while (clientKeys.hasMoreElements()) {
            String clientId = clientKeys.nextElement();

            WorkerThread wt = clientList.get(clientId);
            wt.send(obj);
        }
    }

    public void sendToAClient(Object obj, String clientId, String receiver) {
        String receiverId = "";
        String[] clientKey = clientList.keySet().toArray(new String[0]);
        for(int i=0; i<clients.size(); i++){
            if(clients.get(i).getUserName().equals(receiver)){
                receiverId = clientKey[i];
                System.out.println(receiver+": "+clientKey[i]);
            }
        }
        WorkerThread wt = clientList.get(clientId);
        wt.send(obj);
        wt = clientList.get(receiverId);
        wt.send(obj);

    }

    public void updateConnectedClient(){
        Object obj = new Object();
        obj.setClients(clients);
        obj.setType("Client");
        sendToAll(obj);

        System.out.println("Client list: " + obj.getClientsList());

    }

    public static void main(String[] args) {
        Server serverRunner = new Server();
    }

}