package chat.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Object implements Serializable {
    enum objectType{
        CLIENT, CLIENTLIST, MESSAGE
    }
    private String sender;
    private String receiver;
    private String text;
    private int action;
    private ArrayList<Person> clients;
    private String type;    // 'Message' or 'Client'

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAction(){
        return action;
    }

    public void setAction(int action){ this.action = action; }

    public ArrayList<Person> getClients(){ return clients; }

    public void setClients(ArrayList<Person> clients) { this.clients = clients; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    @Override
    public String toString() {
        return "Object{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", action=" + action +
                ", type='" + type + '\'' +
                '}';
    }
}