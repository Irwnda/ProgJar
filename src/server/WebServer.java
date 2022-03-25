package server;

import utils.Dbg;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * DESCRIPTION :
 *
 * 1. Serves any file under the website root directory to the client. If the requested file is a text or HTML file, the browser will show the content. If it is a binary file (e.g. PDF, images, documents, etc.) the browser will download it. (Hint: use the correct content-type for each file served)
 *
 * 2. Shows a list of files and folders when the client requests a directory that does not have index.html inside it. The list must show the file/folder name, last modified date, and size. The name must also be clickable and make the user requests the file/folder when they click it. (Hint: send a temporary HTML string containing the necessary contents)
 *
 * 3. Serves multiple websites like Nginx/Apache VirtualHost. The client must be able to access various domains handled by your web server. (Hint: modify /etc/hosts in Linux or C:\Windows\System32\drivers\etc.\hosts in Windows to add your own domain names such that your browser can recognise them)
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
        Dbg.debugKu(config.toString());

        // Create the Server
        ServerSocket server = new ServerSocket(Integer.parseInt(config.getPort()), 5, InetAddress.getByName(config.getIp()));
        Dbg.debugKu("0 : Server Ready");

        // Listen to any client request
        while (!server.isClosed()){
            Socket client = server.accept();
            Dbg.debugKu("1 : New Client Connected");

            ClientThread ct = new ClientThread(client, config);
            ct.start();

            Dbg.debugKu("5 : Thread Started");

        }

         server.close();
         Dbg.debugKu("6 : Server CLOSED");

    }

}
