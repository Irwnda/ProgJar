import java.util.*;

public class Response {
    private String code;
    private String newLocation;
    private String textResponse;
    private ArrayList<String> linkURLs;
    private ArrayList<String> linkTexts;

    /**
     * Response Constructor
     * @param response
     */
    public Response(String response) {
        this.code = extractStatusCode(response);
        this.newLocation = "";
        this.textResponse = response;
        this.linkURLs = new ArrayList<String>();
        this.linkTexts = new ArrayList<String>();

    }

    /**
     * Check if response have anchor tag, extract the link and show it if so
     * @return hasValidAnchor
     */
    public boolean checkAnchor(){
        Scanner resp = new Scanner(this.textResponse);
        boolean isClosed = true;
        boolean hasValidAnchor = false;

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
                        hasValidAnchor = true;
                    }

                    isClosed = (boolean) anchor.get(2);
                    anchorIndex = line.indexOf("<a ", (int) anchor.get(3));
                }
            }
        }
        resp.close();
        return hasValidAnchor;
    }

    /**
     * Get response's status code
     * @return statusCode
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Get New Location as redirection needs
     * @return newLocation
     */
    public String getNewLocation() {
        return this.newLocation;
    }

    /**
     * Show text content of the response [ EXCLUDE HEADER ]
     */
    public void showTextResponse() {
        System.out.println(ConsoleColors.YELLOW + "\n####### Response Text :" + ConsoleColors.RESET);

        String content = this.textResponse;
        int idxOfContent = content.indexOf('<');
        content = this.textResponse.substring( idxOfContent );

//        int idxOfOpnBody = content.indexOf("<body>");
//        int idxOfClsBody = content.indexOf("</body>");
//
//        content = content.substring( idxOfOpnBody, idxOfClsBody+8 );

        System.out.println( content );

    }

    /**
     * Check if response define new location as redirection needs
     * @return hasRedirectLink
     */
    public boolean checkNewLocation() {
        Scanner sc = new Scanner(this.textResponse);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // 3xx redirect
            if (line.indexOf("Location") != -1) {
                String[] locationInfo = line.split(" ");
                this.newLocation = locationInfo[1];
                sc.close();
                return true;
            }
            // Refresh: x;url=new.url
            if (line.indexOf("Refresh:") != -1) {
                String[] refreshInfo = line.split("url=");
                this.newLocation = refreshInfo[1];
                sc.close();
                return true;
            }
        }
        sc.close();
        return false;
    }

    /**
     * Get link of the line of scanned response
     * @param line
     * @return listOfLinkComponent
     */
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

    /**
     * Exclude anchor tag that refer to same page (like '#')
     * @param linkInfo
     * @return isValidLink
     */
    private static boolean validateLink(ArrayList<Object> linkInfo) {
        return (linkInfo.get(0)).toString().indexOf(".") != -1;
    }

    /**
     * Delete html tag like header, paragraph, div, etc.
     * @param linkInfo
     * @return textOnlyLink
     */
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

    /**
     * Extract response's HTTP status code
     * @param response
     * @return statusCode
     */
    private static String extractStatusCode(String response) {
        Scanner resp = new Scanner(response);
        String[] respCode = resp.nextLine().split(" ");
        resp.close();
        return respCode[1];
    }

    /**
     * Show respective response's clickable link
     */
    public void showLinks() {
        System.out.println(ConsoleColors.YELLOW + "\n####### Clickable links :" + ConsoleColors.RESET);
        if (this.linkURLs.size() == 0)
            System.out.println("1.\t-");
        else
            for (int i = 0; i < this.linkURLs.size(); i++) {
                System.out.println((i + 1) + ".\t" + linkURLs.get(i));
                System.out.println("\t" + linkTexts.get(i) + "");
            }
    }

    /**
     * Check response's status code, and show appropriate error message
     * @return isError
     */
    public boolean checkErrorCode(){
        char firstCode = this.getCode().charAt(0);
        switch (firstCode){
            case '4':
                System.out.println(
                        ConsoleColors.RED
                                + "==|| Halaman Tidak dapat ditemukan :p - Status : "
                                + ConsoleColors.RED_BACKGROUND_BRIGHT+firstCode+"xx"+ConsoleColors.RED
                                + " ||=="
                                + ConsoleColors.RESET
                );
                return true;
            case '5':
                System.out.println(
                        ConsoleColors.RED
                                + "==|| Server meng-kacang-in kamu :( - Status : "
                                + ConsoleColors.RED_BACKGROUND_BRIGHT+firstCode+"xx"+ConsoleColors.RED
                                + " ||=="
                                + ConsoleColors.RESET
                );
                return true;
        }
        return false;
    }
}
