package server;

import utils.ConsoleColors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {

        // Untuk memesan addres tertentu saja yang boleh konek
        // Inet4Address addr = (Inet4Address) Inet4Address.getLoopbackAddress();

        String websiteRoot = new File("").getAbsolutePath().concat("\\src\\server\\");
        debugKu(websiteRoot);
        ServerSocket server = new ServerSocket(80, 5);
        debugKu("0");

        while (true){

            Socket client = server.accept();
            debugKu("1");

            // Obtain BufferReader and BufferWriter
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            // Read the msg from client
            String msg = br.readLine();
            String urn = msg.split(" ")[1];
            String urnWithoutSlash = urn.substring(1);

            debugKu(urnWithoutSlash);

            String fileContent = "";
            String statusCode;
            try {
                FileInputStream fipt = new FileInputStream(websiteRoot + urnWithoutSlash);
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
            debugKu("2");

            // Write the reply msg to server
            bw.write("HTTP/1.0 "+statusCode+"\r\nContent-Type: text/html\r\nContent-length: "+fileContent.length()+"\r\n\r\n"+fileContent);
            bw.flush();
            debugKu("3");

            // Close the connection
            client.close();
        }

        // server.close();
        // debugKu("4");

    }

    public static void debugKu(String s){
        System.out.println(ConsoleColors.YELLOW + " ===== DEBUG : ["+ s +"] :) ===== " + ConsoleColors.RESET);
    }
}
