import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ProgJarWebURI {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter valid URL\t: ");
        String input = sc.nextLine();
        String[] inputURL;
        String protocol;
        sc.close();

        if (input.indexOf("https://") != -1)
            protocol = "https";
        else
            protocol = "http";

        if (input.indexOf("://") != -1) {
            input = input.substring(input.indexOf("://") + 3);
        }

        inputURL = input.split("/", 2);
        String server = inputURL[0];
        String path = inputURL.length == 1 ? "/" : ("/" + inputURL[1]);

        Socket socket = new Socket(server, protocol == "http" ? 80 : 443);

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

        bos.write(("GET " + path + " " + protocol.toUpperCase() + "/1.1\r\nHost: " + server + "\r\n\r\n").getBytes());
        bos.flush();

        byte[] c = bis.readAllBytes();

        System.out.println(new String(c));
        System.out.println("==============================");
        showLinks(new String(c));
        System.out.println("==============================");

        socket.close();
    }

    private static void showLinks(String response) {
        ArrayList<ArrayList<Object>> anchors = new ArrayList<ArrayList<Object>>();
        Scanner resp = new Scanner(response);
        boolean isClosed = true;

        while (resp.hasNextLine()) {
            String line = resp.nextLine();
            int anchorIndex = line.indexOf("<a ");

            if (anchorIndex != -1) {
                while (isClosed && anchorIndex != -1) {
                    line = line.substring(anchorIndex);
                    ArrayList<Object> anchor = getLink(line);

                    if (validateLink(anchor))
                        anchors.add((anchor));

                    isClosed = (boolean) anchor.get(2);

                    anchorIndex = line.indexOf("<a ", (int) anchor.get(3));
                }
            }

        }

        System.out.println("\nClickable links:");
        if (anchors.size() == 0)
            System.out.println("1.\t-");
        else
            for (int i = 0; i < anchors.size(); i++) {
                System.out.println((i + 1) + ".\t" + anchors.get(i).get(0));
                System.out.println("\t" + anchors.get(i).get(1) + "\n");
            }
        resp.close();
    }

    private static ArrayList<Object> getLink(String line) {
        ArrayList<Object> anchor = new ArrayList<Object>();
        int indexOfHref = line.indexOf("href=");
        int anchorURLFirst = line.indexOf("\"", indexOfHref);
        int anchorURLLast = line.indexOf("\"", anchorURLFirst + 1);

        anchor.add(line.substring(anchorURLFirst + 1, anchorURLLast));

        int anchorTextFirst = line.indexOf(">", anchorURLLast);
        int anchorTextLast = line.indexOf("</a>", anchorTextFirst + 1);

        boolean closed = anchorTextLast != -1;
        if (closed) {
            anchor.add(getTextOnly(line.substring(anchorTextFirst + 1, anchorTextLast)));
        } else {
            anchor.add(getTextOnly(line.substring(anchorTextFirst + 1)));
        }
        anchor.add(closed);
        anchor.add(anchorTextLast);

        return anchor;
    }

    // This is to exclude anchor tag that refer to same page (like '#')
    private static boolean validateLink(ArrayList<Object> linkInfo) {
        return (linkInfo.get(0)).toString().indexOf(".") != -1;
    }

    // This is to delete html tag like header, paragraph, div, etc.
    private static String getTextOnly(String linkInfo) {
        String TextOnly = linkInfo;
        if (TextOnly.indexOf("<") != -1) {
            int gtSign = TextOnly.indexOf(">");
            int ClosingTag = TextOnly.indexOf("</");
            if (ClosingTag == -1)
                TextOnly = TextOnly.substring(gtSign + 1);
            else
                TextOnly = TextOnly.substring(gtSign + 1, ClosingTag);
        }

        return TextOnly.length() > 0 ? TextOnly.trim() : linkInfo;
        // This is to show the element inside anchor tag if there is only one element.
    }
}