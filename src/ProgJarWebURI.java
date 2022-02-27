import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ProgJarWebURI {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter valid URL\t: ");
        String[] inputURL = sc.nextLine().split("/", 2);
        sc.close();

        String server = inputURL[0];
        String path = ("/" + inputURL[1]);

        Socket socket = new Socket(server, 80);

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

        bos.write(("GET " + path + " HTTP/1.1\r\nHost: " + server + "\r\n\r\n").getBytes());
        bos.flush();

        byte[] c = bis.readAllBytes();

        showLinks(new String(c));

        socket.close();
    }

    private static void showLinks(String response) {
        ArrayList < ArrayList < Object >> anchors = new ArrayList < ArrayList < Object >> ();
        Scanner resp = new Scanner(response);
        boolean isClosed = true;

        while (resp.hasNextLine()) {
            String line = resp.nextLine();
            int anchorIndex = line.indexOf("<a ");

            if (anchorIndex != -1) {
                while (isClosed && anchorIndex != -1) {
                    line = line.substring(anchorIndex);
                    ArrayList < Object > anchor = getLink(line);
                    anchors.add(anchor);

                    isClosed = (boolean) anchor.get(2);

                    anchorIndex = line.indexOf("<a ", (int) anchor.get(3));
                }
            }

        }
        System.out.println("\nClickable links:");
        for (int i = 0; i < anchors.size(); i++) {
            System.out.println((i + 1) + ".\t" + anchors.get(i).get(0));
            System.out.println("\t" + anchors.get(i).get(1) + "\n");
        }
        resp.close();
    }

    private static ArrayList < Object > getLink(String line) {
        ArrayList < Object > anchor = new ArrayList < Object > ();
        int indexOfHref = line.indexOf("href=");
        int anchorURLFirst = line.indexOf("\"", indexOfHref);
        int anchorURLLast = line.indexOf("\"", anchorURLFirst + 1);

        anchor.add(line.substring(anchorURLFirst + 1, anchorURLLast));

        int anchorTextFirst = line.indexOf(">", anchorURLLast);
        int anchorTextLast = line.indexOf("</a>", anchorTextFirst + 1);

        boolean closed = anchorTextLast != -1;
        if (closed) {
            anchor.add(line.substring(anchorTextFirst + 1, anchorTextLast));
        } else {
            anchor.add(line.substring(anchorTextFirst + 1));
        }
        anchor.add(closed);
        anchor.add(anchorTextLast);

        return anchor;
    }

}