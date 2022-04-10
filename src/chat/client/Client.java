package chat.client;

import chat.gui.ClientGUI;
import chat.object.Object;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private String userName;
    private ObjectOutputStream ous;
    private static ArrayList<String> clients = new ArrayList<>();
    Socket socket;

    public Client() {
         ClientGUI clientGUI = new ClientGUI(this);

        try {
            socket = new Socket("127.0.0.1", 9000);
            ous = new ObjectOutputStream(socket.getOutputStream());

            WorkerThread wt = new WorkerThread(new ObjectInputStream(socket.getInputStream()), this);
            wt.start();


        } catch (IOException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void sendMessage(String text){
        try {
            Object msgObj = new Object();
            msgObj.setSender(this.userName);
            msgObj.setType("Message");
            msgObj.setText(text);

            this.ous.writeObject(msgObj);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerClient(String userName){
        try {
            clients.add(userName);

            Object personObj = new Object();
            personObj.setSender(userName);
            personObj.setClients(clients);
            personObj.setType("Client");
            personObj.setAction(1);

            this.ous.writeObject(personObj);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectClient(String userName){
        try {
            clients.remove(userName);

            Object personObj = new Object();
            personObj.setSender(userName);
            personObj.setClients(clients);
            personObj.setType("Client");
            personObj.setAction(-1);

            this.ous.writeObject(personObj);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    public void setClients(ArrayList<String> clients) {
        Client.clients = clients;
    }

    public static void main(String[] args) {
        Client clientRunner = new Client();
    }
}
