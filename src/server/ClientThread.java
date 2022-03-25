package server;

import utils.Dbg;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientThread extends Thread{
    private Socket client;
    private Config config;

    public ClientThread(Socket client, Config config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public void run() {

        try {
            // Obtain BufferReader and BufferWriter
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            // Read the msg from client
            String fullMsg = "";
            String msg = br.readLine();

            // Handling putus2 dewe
            if (msg == null) {
                System.out.println("Reconnecting...");
                Thread.yield();
            }

            fullMsg = fullMsg.concat(msg);

            while (!msg.isEmpty()) {
                msg = br.readLine();
                fullMsg = fullMsg.concat(msg + "\n");

                if (msg.contains("Host")) {
                    String host = msg.split(" ")[1];
                    config.setDocRootByHost(config.getConfigPath(), host);
                }
            }

            // Save request from client
            Request req = new Request(fullMsg, config);
            // System.out.println(req.getFullReq());
            Dbg.debugKu("2");

            // Create response for client
            Response res = new Response(req.getUrn(), config);
            Dbg.debugKu("3");

            // Write the reply msg to server
            bw.write(res.getFullRes());
            bw.flush();
            Dbg.debugKu("4");

            // Close the connection
            if (!Objects.equals(req.getConn(), "keep-alive")) {
                client.close();
                Dbg.debugKu("Closing connection ...");
            } else {
                // Dbg.debugKu("Using keep-alive ...");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
