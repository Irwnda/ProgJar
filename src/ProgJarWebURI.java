import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class ProgJarWebURI {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter valid URL\t: ");
        String input = sc.nextLine();
        sc.close();

        Response response = new Response(openURL(input));
        if (response.getCode() == 301) {
            System.out.println("Redirecting to " + response.getNewLocation());
            System.out.println("==============================");
            response = new Response(openURL(response.getNewLocation()));
        }

        System.out.println(response.getTextResponse());
        System.out.println("==============================");
        response.showLinks();
        System.out.println("==============================");
    }

    public static String openURL(String URL) throws UnknownHostException, IOException {
        String[] inputURL;
        String protocol;

        if (URL.indexOf("https://") != -1)
            protocol = "https";
        else
            protocol = "http";

        if (URL.indexOf("://") != -1) {
            URL = URL.substring(URL.indexOf("://") + 3);
        }

        inputURL = URL.split("/", 2);
        String server = inputURL[0];
        String path = inputURL.length == 1 ? "/" : ("/" + inputURL[1]);

        final SocketFactory socketFactory = SSLSocketFactory.getDefault();
        final Socket socket;
        String request;
        if (protocol == "http") {
            socket = new Socket(server, 80);
            request = ("GET " + path + " HTTP/1.1\r\nHost: " + server + "\r\n\r\n");
        } else {
            socket = socketFactory.createSocket(server, 443);
            request = ("GET " + path + " HTTP/1.1\r\nConnection: close\r\nHost: " + server + "\r\n\r\n");
        }

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

        bos.write(request.getBytes());
        bos.flush();

        byte[] c = bis.readAllBytes();

        socket.close();

        return new String(c);
    }
}