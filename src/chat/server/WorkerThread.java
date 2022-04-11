package chat.server;

import chat.object.Object;
import utils.CColors;
import utils.Dbg;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private Server server;
    private Socket socket;

    public WorkerThread(Socket socket, Server server) {
        try {
            this.ous = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.server = server;
            this.socket = socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Object obj = (Object) this.ois.readObject();

                if(obj.getType().equals("Message")){
                    if(obj.getReceiver().equals("global"))
                        server.sendToAll(obj);
                    else {
                        String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                        server.sendToAClient(obj, clientId);
                    }
                }
                else if(obj.getType().equals("Client")){
                    if(obj.getAction()==1){
                        // add Client
                        server.setClients(obj.getClients());
                    }
                    else if (obj.getAction()==0){
                        // Request list client
                        Object objClientList = new Object();
                        objClientList.setType("Client");
                        objClientList.setAction(0);
                        objClientList.setClients(server.getClients());
                        send(objClientList);
                    }
                    else if (obj.getAction()==-1){
                        // Remove client dengan cara atur server client dg kiriman dari client yg disconect
                        server.setClients(obj.getClients());

                        Object objClientList = new Object();
                        objClientList.setType("Client");
                        objClientList.setAction(-1);
                        objClientList.setClients(server.getClients());
                        send(objClientList);
                    }
                    server.updateConnectedClient();
                }
            } catch (IOException | ClassNotFoundException e) {
                Dbg.debugKu(CColors.RED + "Server Terminated" + CColors.RESET);
                System.exit(0);
                // e.printStackTrace();
            }
        }
    }

    public void send(Object obj) {
        try {
            this.ous.writeObject(obj);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}