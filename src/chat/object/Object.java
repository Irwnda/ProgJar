package chat.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Object implements Serializable {
    enum objectType{
        CLIENT, CLIENTLIST, MESSAGE
    }
    private String sender;
    private String text;
    private int action;
    private ArrayList<String> clients;
    private String type;

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

    public ArrayList<String> getClients(){ return clients; }

    public void setClients(ArrayList<String> clients) { this.clients = clients; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}