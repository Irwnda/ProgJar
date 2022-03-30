package server;

import utils.Dbg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;

public class Response {
    private String contentType = "";
    private String body = "";
    private String fullRes = "";
    private String statusCode = "";
    private final String urn;
    private String range = "";

    public Response(String urn, Config cfg, String range) {
        this.urn = urn;
        computeRes(cfg, range);

    }

    public void computeRes(Config cfg, String range){
        // Set content body with file content by the requested urn
        try {
            File file = new File(cfg.getDocRoot()+urn);
            FileInputStream fipt = new FileInputStream(file);

            if(!range.equals("")){
                int startRangeIdx = range.indexOf('=')+1;
                int endRangeIdx = range.indexOf('-');

                int startRange = Integer.parseInt(range.substring(startRangeIdx, endRangeIdx));
                int endRange = Integer.parseInt(range.substring(endRangeIdx+1));

                body = String.valueOf(fipt.read(new byte[1024], startRange, endRange-startRange));
                statusCode = "206 Partial Content";
                this.range = "Content-Range: bytes "+startRange+"-"+endRange+"/*";
            }
            else {
                body = new String(fipt.readAllBytes());
                statusCode = "200 OK";
            }

            // Determine the content type of requested urn
            String[] text = {"html", "php", "txt"};
            String[] image = {"jpeg", "jpg", "png"};
            String[] application = {"json", "pdf", "rtf", "zip"};
            String ext = urn.split("\\.")[1];

            if(Arrays.asList(text).contains(ext)){
                contentType = ("text/html");
            }
            else if(Arrays.asList(image).contains(ext)){
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String content = Base64.getEncoder().encodeToString(fileContent);
                body = String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <title>%s</title>
                        </head>
                        <body>
                            <img src='data:image/%s;base64,%s'>
                        </body>
                        </html>""", urn, ext, content);
                contentType = ("text/html");
            }
            else if(Arrays.asList(application).contains(ext)){
                contentType = ("application/"+ext);
            }
        }
        catch (FileNotFoundException e){
            File input = new File(cfg.getDocRoot() + urn);

            if(input.isDirectory()){
                try {
                    File file = new File(cfg.getDocRoot() + urn + "/index.html");
                    FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                    body = new String(fis.readAllBytes());
                    contentType = ("text/html");
                } catch (FileNotFoundException ex) {
                    try {
                        body = getFilesList(urn, cfg);
                        contentType = ("text/html");
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                try {
                    FileInputStream fis = new FileInputStream(cfg.getDocRoot()+"/404.html");
                    body = new String(fis.readAllBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            statusCode = "404 Not Found";

        } catch (IOException e) {
            e.printStackTrace();
        }

        setFullRes();
    }

    public void setFullRes() {
        fullRes = fullRes.concat("HTTP/1.0 " + getStatusCode() + "\r\n");
        fullRes = fullRes.concat("Content-Type: " + getContentType() + "\r\n");
        fullRes = fullRes.concat("Content-length: " + getBody().length() + "\r\n");
        if(!range.equals("")){
            fullRes = fullRes.concat(range);
        }
        fullRes = fullRes.concat("\r\n\r\n");
        fullRes = fullRes.concat(getBody());

    }

    private String getFilesList(String urn, Config cfg) throws IOException {
        File files = new File(cfg.getDocRoot() + urn);
        String[] fileList = files.list();
        StringBuilder response = new StringBuilder("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="icon" type="image/x-icon" href="https://pngimg.com/uploads/letter_p/letter_p_PNG46.png">
                    <title>FILE LIST</title>

                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
                </head>
                <body>
                <div class="container">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>File/Folder Name</th>
                        <th>Type</th>
                        <th>Last Modified</th>
                        <th>Size (Byte)</th>
                    </tr>
                    </thead>
                    <tbody>
                """);

        assert fileList != null;
        for(String fileName : fileList) {
            response.append("    <tr>\n");

            File file = new File(files+"/"+fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastModified = sdf.format(file.lastModified());

            if(urn.charAt(urn.length()-1)=='/')
                urn = urn.substring(0, urn.length()-1);

            response.append("        <td><a href=\"/").append(urn).append("/").append(fileName).append("\">").append(fileName).append("</a></td>\n");
            response.append(file.isFile() ? "        <td>File</td>\n" : "        <td>Folder</td>\n");
            response.append("        <td>").append(lastModified).append("</td>\n");
            response.append("        <td>").append(Files.size(file.toPath())).append("</td>\n");

            response.append("    </tr>\n");
        }

        response.append("""
                    </tbody>
                </table>
                </div>
                </body>
                </html>""");

        return response.toString();
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
