package server;

import utils.ConsoleColors;
import utils.Debug;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {
        // Read config
        String configPath = new File("").getAbsolutePath().concat("\\src\\server\\httpd.conf");
        Config config = new Config(configPath);
        Debug.debugKu(config.toString());

        // Create the Server
        ServerSocket server = new ServerSocket(Integer.parseInt(config.getPort()), 5);
        Debug.debugKu("0");

        // Listen to any client request
        while (true){
            Socket client = server.accept();
            Debug.debugKu("1");

            // Obtain BufferReader and BufferWriter
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            // Read the msg from client
            String msg = br.readLine();
            String urn = msg.split(" ")[1];
            String urnWithoutSlash = urn.substring(1);

            String fileContent = "";
            String statusCode;
            try {
                FileInputStream fipt = new FileInputStream(config.getDocRoot() + urnWithoutSlash);
                fileContent = new String(fipt.readAllBytes());
                statusCode = "200 OK";
            }
            catch (FileNotFoundException e){
                fileContent = "404 File Not Found";
                statusCode = "404 Not Found";

            }

            while( !msg.isEmpty() ){
                System.out.println(msg);
                msg = br .readLine();
            }
            Debug.debugKu("2");

            // Write the reply msg to server
            bw.write("HTTP/1.0 "+statusCode+"\r\nContent-Type: text/html\r\nContent-length: "+fileContent.length()+"\r\n\r\n"+fileContent);
            bw.flush();
            Debug.debugKu("3");

            // Close the connection
            client.close();
        }

        // server.close();
        // debugKu("4");

    }

}
