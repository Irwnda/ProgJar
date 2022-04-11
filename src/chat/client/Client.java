package chat.client;

import chat.gui.ClientGUI;
import chat.object.Chat;
import chat.object.Object;
import chat.object.Person;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private String userName;
    private ObjectOutputStream ous;
    private static ArrayList<Person> clients = new ArrayList<>();
    private final ClientGUI clientGUI;
    private String targetSend = "global";
    private String targetProfile = "initials";

    public Client() {
         clientGUI = new ClientGUI(this);

        try {
            Socket socket = new Socket("127.0.0.1", 9000);
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
            msgObj.setSender(userName);
            msgObj.setReceiver(targetSend);
            msgObj.setType("Message");
            msgObj.setText(text);

            this.ous.writeObject(msgObj);
            this.ous.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestClientList(){
        try {
            Object reqObj = new Object();
            reqObj.setType("Client");
            reqObj.setAction(0);

            this.ous.writeObject(reqObj);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerClient(String userName, String profileType){
        try {
            requestClientList();

//            clients.add(userName);
            // RAWAN TELATTT
            Person client = new Person();
            client.setUserName(userName);
            client.setProfileType(profileType);
            Thread.sleep(2000);
            clients.add(client);

            Object personObj = new Object();
            personObj.setSender(userName);
            personObj.setClients(clients);
            personObj.setType("Client");
            personObj.setAction(1);

            this.ous.writeObject(personObj);
            this.ous.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void disconnectClient(String userName){
        try {
            for(int i=0; i<clients.size(); i++){
                if(clients.get(i).getUserName().equals(userName)){
                    clients.remove(i);
                    break;
                }
            }

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

    public ArrayList<Person> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Person> clients) {
        Client.clients = clients;
    }

    public String getTargetSend() {
        return targetSend;
    }

    public void setTargetSend(String targetSend) {
        this.targetSend = targetSend;
    }

    public String getTargetProfile() {
        return targetProfile;
    }

    public void setTargetProfile(String targetProfile) {
        this.targetProfile = targetProfile;
    }

    public static void main(String[] args) {
        Client clientRunner = new Client();
    }

    public void updateClientListUI() {
        clientGUI.updateClientList();
    }

    public ArrayList<String> getClientsList(){
        ArrayList<String> clientList = new ArrayList<>();
        for (Person client : clients) {
            clientList.add(client.getUserName());
        }
        return clientList;
    }

    public ArrayList<String> getClientsProfile(){
        ArrayList<String> clientList = new ArrayList<>();
        for (Person client : clients) {
            clientList.add(client.getProfileType());
        }
        return clientList;
    }

    public void incomingChat(Chat chat) {
        clientGUI.addChat(chat);
    }
}
