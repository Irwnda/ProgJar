import java.util.*;

public class Response {

    private int code;
    private String newLocation;
    private String textResponse;
    private ArrayList<String> linkURLs;
    private ArrayList<String> linkTexts;

    public Response(String response) {
        Scanner resp = new Scanner(response);
        boolean isClosed = true;

        this.code = getCode(response);
        this.newLocation = "";
        this.textResponse = response;
        this.linkURLs = new ArrayList<String>();
        this.linkTexts = new ArrayList<String>();

        if (this.code == 301) {
            this.setNewLocation();
        }

        while (resp.hasNextLine()) {
            String line = resp.nextLine();
            int anchorIndex = line.indexOf("<a ");

            if (anchorIndex != -1) {
                while (isClosed && anchorIndex != -1) {
                    line = line.substring(anchorIndex);
                    ArrayList<Object> anchor = getLink(line);

                    if (validateLink(anchor)) {
                        this.linkURLs.add(anchor.get(0).toString());
                        this.linkTexts.add(anchor.get(1).toString());
                    }

                    isClosed = (boolean) anchor.get(2);
                    anchorIndex = line.indexOf("<a ", (int) anchor.get(3));
                }
            }
        }
        resp.close();
    }

    public int getCode() {
        return this.code;
    }

    public String getNewLocation() {
        return this.newLocation;
    }

    public String getTextResponse() {
        return this.textResponse;
    }

    public ArrayList<String> getLinkURLs() {
        return this.linkURLs;
    }

    public ArrayList<String> getLinkTexts() {
        return this.linkTexts;
    }

    private void setNewLocation() {
        Scanner sc = new Scanner(this.textResponse);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.indexOf("Location") != -1) {
                String[] locationInfo = line.split(" ");
                this.newLocation = locationInfo[1];
            }
        }
        sc.close();
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

    private static int getCode(String response) {
        Scanner resp = new Scanner(response);
        String[] respCode = resp.nextLine().split(" ");
        resp.close();
        return Integer.parseInt(respCode[1]);
    }

    public void showLinks() {
        System.out.println("\nClickable links:");
        if (this.linkURLs.size() == 0)
            System.out.println("1.\t-");
        else
            for (int i = 0; i < this.linkURLs.size(); i++) {
                System.out.println((i + 1) + ".\t" + linkURLs.get(i));
                System.out.println("\t" + linkTexts.get(i) + "\n");
            }
    }
}
