package chat.client;

import chat.gui.ClientGUI;

public class Client {
    private String userName;

    public Client(String userName) {
        this.userName = userName;
        ClientGUI clientGUI = new ClientGUI(this);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static void main(String[] args) {
        Client client1 = new Client("");

        Client client2 = new Client("Erik");
    }
}
