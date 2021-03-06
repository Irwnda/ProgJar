package chat.client;

import chat.object.Chat;
import chat.object.Object;
import utils.CColors;
import utils.Dbg;

import java.io.IOException;
import java.io.ObjectInputStream;

public class WorkerThread extends Thread {
    private final ObjectInputStream ois;
    private final Client client;

    public WorkerThread(ObjectInputStream ois, Client client) {
        this.ois = ois;
        this.client = client;
    }

    public void run() {
        while(true) {
            try {
                Object obj = (Object) ois.readObject();

                Dbg.debugKu(obj.toString());

                if(obj.getType().equals("Message")){
                    System.out.println(obj.getSender() + ": " + obj.getText());
                    client.incomingChat(new Chat(obj.getSender(), obj.getText(), client.getUserName().equals(obj.getSender())));
                }
                else if(obj.getType().equals("Client")){
                    System.out.println(obj.getClientsList());
                    client.setClients(obj.getClients());

                    client.updateClientListUI();
                }

            } catch (IOException | ClassNotFoundException e) {
                Dbg.debugKu(CColors.RED + "Client Terminated" + CColors.RESET);
                System.exit(0);
                // e.printStackTrace();
            }

        }
    }
}