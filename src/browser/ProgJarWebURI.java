package browser;

import utils.CColors;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class ProgJarWebURI {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print(CColors.BLUE + "Enter valid URL\t: " + CColors.RESET);
        String input = sc.nextLine();
        sc.close();

        identifyURL(input);
    }

    public static void identifyURL(String URL) throws IOException {
        String[] inputURL;
        String protocol;
        String userURL = URL;

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

        try {
            byte[] headerByte = new byte[1];
            String headerStr = "";

            while( bis.read( headerByte ) != -1 ){
                headerStr += new String( headerByte );
                if( headerStr.indexOf("\r\n\r\n") != -1 ){
                    System.out.println(CColors.GREEN + "####### Header Scan COMPLETED" + CColors.RESET);
                    break;
                }

                headerByte = new byte[1];
            }

            Scanner sc = new Scanner(headerStr);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.indexOf("Content-Type:") != -1) {
                    if (line.indexOf("text") != -1) {
                        /**
                         * Make a request then save it as Response
                         */
                        Response response = new Response(openURL(userURL));
                        /**
                         * Check if response need redirect, make a new request to new url if so
                         */
                        while (response.checkNewLocation()) {
                            System.out.println(
                                    CColors.YELLOW + "####### Redirecting to : \n" + response.getNewLocation()
                                            + CColors.RESET);
                            response = new Response(openURL(response.getNewLocation()));
                        }

                        /**
                         * Show Error if response has error code, otherwise show response text
                         */
                        if (response.checkErrorCode())
                            System.exit(0);
                        else
                            response.showTextResponse();
                        /**
                         * Check if response have anchor tag, show the link if so
                         */
                        if (response.checkAnchor())
                            response.showLinks();
                        else
                            System.out.println(
                                    CColors.RED + "####### Anchor tag NOT Found !" + CColors.RESET);

                    } else {
                        downloadFile(userURL, headerStr);
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
//            downloadFile(userURL, new String(header));
        }
        socket.close();
    }

    /**
     * Open connection to specified URL
     * 
     * @param URL
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public static String openURL(String URL) throws UnknownHostException, IOException {
        System.out.println(CColors.GREEN + "####### Opening URL : " + URL + CColors.RESET);

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

    /**
     * Download file from specified link, save it in file
     * 
     * @param link
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void downloadFile(String link, String header) throws UnknownHostException, IOException {
        try {
            System.out.println(CColors.GREEN + "####### Downloading from URL : " + link + CColors.RESET);

            URL url = new URL(link);
            File out = new File("file2.pdf");
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