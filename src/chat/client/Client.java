package chat.client;

import chat.gui.ClientGUI;
import chat.object.Message;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private String userName;
    private ObjectOutputStream ous;

    public Client() {
        ClientGUI clientGUI = new ClientGUI(this);
        try {
            Socket socket = new Socket("127.0.0.1", 9000);
            ous = new ObjectOutputStream(socket.getOutputStream());
            WorkerThread wt = new WorkerThread(new ObjectInputStream(socket.getInputStream()));
            wt.start();
        } catch (IOException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void sendMessage(String text){
        try {
            Message message = new Message();
            message.setSender(this.userName);
            message.setText(text);

            this.ous.writeObject(message);
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

    public static void main(String[] args) {
        Client client = new Client();
    }
}
