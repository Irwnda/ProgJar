package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Response {
    private String contentType = "";
    private String body = "";
    private String fullRes = "";
    private String statusCode = "";
    private String urn = "";

    public Response(String urn, Config cfg) {
        this.urn = urn;
        computeRes(cfg);

    }

    public void computeRes(Config cfg){
        // Set content body with file content by the requested urn
        try {
            FileInputStream fipt = new FileInputStream(cfg.getDocRoot() + urn);
            body = new String(fipt.readAllBytes());
            statusCode = "200 OK";
        }
        catch (FileNotFoundException e){
            body = "404 File Not Found";
            statusCode = "404 Not Found";

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Determine the content type of requested urn
        String text[] = {"html", "php", "txt"};
        String ext = urn.split("\\.")[1];

        if( Arrays.stream(text).anyMatch( ext::equals ) ){
            contentType = ("text/html");
        }
        else{
            contentType = ("application/pdf");
        }

        setFullRes();
    }

    public void setFullRes() {
        fullRes = fullRes.concat("HTTP/1.0 " + getStatusCode() + "\r\n");
        fullRes = fullRes.concat("Content-Type: " + getContentType() + "\r\n");
        fullRes = fullRes.concat("Content-length: " + getBody().length() + "\r\n");
        fullRes = fullRes.concat("\r\n\r\n");
        fullRes = fullRes.concat(getBody());

    }

    public String getFullRes(){
        return fullRes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
