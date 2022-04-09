package chat.client;

import chat.object.Object;
import utils.Dbg;

import java.io.IOException;
import java.io.ObjectInputStream;

public class WorkerThread extends Thread {
    private final ObjectInputStream ois;
    private Client client;

    public WorkerThread(ObjectInputStream ois, Client client) {
        this.ois = ois;
        this.client = client;
    }

    public void run() {
        while(true) {
            try {
                Object obj = (Object) ois.readObject();
                if(obj.getType().equals("Message")){
                    System.out.println(obj.getSender() + ": " + obj.getText());
                }
                else if(obj.getType().equals("ClientList")){
                    System.out.println(obj.getClients());
                    this.client.setClients(obj.getClients());
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}