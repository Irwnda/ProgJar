package server;

import utils.Debug;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * DESCRIPTION :
 *
 * 1. Serves any file under the website root directory to the client. If the requested file is a text or HTML file, the browser will show the content. If it is a binary file (e.g. PDF, images, documents, etc) the browser will download it. (Hint: use the correct content-type for each file served)
 *
 * 2. Shows a list of files and folders when the client requests a directory that does not have index.html inside it. The list must show the file/folder name, last modified date, and size. The name must also be clickable and make the user requests the file/folder when they click it. (Hint: send a temporary HTML string containing the necessary contents)
 *
 * 3. Serves multiple websites like Nginx/Apache VirtualHost. The client must be able to access various domains handled by your web server. (Hint: modify /etc/hosts in Linux or C:\Windows\System32\drivers\etc\hosts in Windows to add your own domain names such that your browser can recognise them)
 *
 * 4. Has a configuration file that allows us to configure the IP address and port bound by the webserver. The file must also include the root directory of each website handled by your webserver.
 *
 * 5. Keeps the connection open if the client requests it. (Hint: check the Connection HTTP header. Your webserver will not be able to accept another client once the connection is still open, but that is okay)
 *
 */

public class WebServer {
    public static void main(String[] args) throws IOException {
        // Read config
        String configPath = new File("").getAbsolutePath().concat("\\src\\server\\httpd.conf");
        Config config = new Config(configPath);
        Debug.debugKu(config.toString());

        // Create the Server
        ServerSocket server = new ServerSocket(Integer.parseInt(config.getPort()), 5, InetAddress.getByName(config.getIp()));
        Debug.debugKu("0");
        // Listen to any client request
        while (true){
            Socket client = server.accept();
            Debug.debugKu("1");

            // Obtain BufferReader and BufferWriter
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            // Read the msg from client
            String fullMsg = "";
            String msg = br.readLine();

            // Handling putus2 dewe
            if(msg == null){
                System.out.println("Reconnecting...");
                continue;
            }

            fullMsg = fullMsg.concat(msg);

            while( !msg.isEmpty() ){
                msg = br .readLine();
                fullMsg = fullMsg.concat(msg+"\n");

                if(msg.contains("Host")){
                    String host = msg.split(" ")[1];
                    config.setDocRootByHost(configPath, host);
                }
            }

            // Save request from client
            Request req = new Request(fullMsg, config);
            System.out.println(req.getFullReq());
            Debug.debugKu("2");

            // Create response for client
            Response res = new Response(req.getUrn(), config);
            Debug.debugKu("3");

            // Write the reply msg to server
            bw.write(res.getFullRes());
            bw.flush();
            Debug.debugKu("4");

            // Close the connection
            if(req.getConn() != "keep-alive")
                client.close();

            Debug.debugKu("5");

        }

        // server.close();
        // debugKu("4");

    }

}
