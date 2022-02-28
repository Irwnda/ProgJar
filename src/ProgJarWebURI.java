import java.io.*;
import java.net.*;
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
        while (response.getCode().charAt(0) == '3') {
            System.out.println("Redirecting to: " + response.getNewLocation());
            System.out.println("==============================");
            response = new Response(openURL(response.getNewLocation()));
        }

        System.out.println(response.getTextResponse());
        System.out.println("==============================");
        response.showLinks();
        System.out.println("==============================");
        downloadFile("http://www.africau.edu/images/default/sample.pdf");
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

    public static void downloadFile(String link) throws UnknownHostException, IOException {
        try {
            URL url = new URL(link);
            File out = new File("file.pdf");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            double fileSize = (double) http.getContentLengthLong();
            BufferedInputStream bis = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(out);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0.00;

            while ((read = bis.read(buffer, 0, 1024)) >= 0) {
                bos.write(buffer, 0, read);
                downloaded += (read);
                percentDownloaded = (downloaded * 100 / fileSize);
                String percent = String.format("%.4f", percentDownloaded);
                System.out.println("Downloaded " + percent + "% of file.");
            }

            bos.close();
            bis.close();
            System.out.println("Download completed");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}