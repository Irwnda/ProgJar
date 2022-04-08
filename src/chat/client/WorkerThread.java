package chat.client;

import chat.object.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class WorkerThread extends Thread {
    private final ObjectInputStream ois;

    public WorkerThread(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void run() {
        while(true) {
            try {
                Message message = (Message) ois.readObject();

                System.out.println(message.getSender() + ": " + message.getText());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}