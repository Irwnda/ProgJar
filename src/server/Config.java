package server;

import utils.ConsoleColors;
import utils.Debug;

import java.io.*;

public class Config {
    private String port;
    private String ip;
    private String docRoot;

    public Config(String configPath) throws FileNotFoundException {
        try {
            FileReader fReader = new FileReader(new File(configPath));
            BufferedReader bufReader = new BufferedReader(fReader);

            String line;
            line = bufReader.readLine();
            while (line != null) {
                if(line.indexOf("Listen") != -1) {
                    int idxOfPort = ("Listen ").length();
                    String port = line.substring(idxOfPort);
                    this.setPort(port);
                }

                if(line.indexOf("ServerName") != -1) {
                    int idxOfIp = ("ServerName ").length();
                    String ip = line.substring(idxOfIp);
                    this.setIp(ip);
                }

                if(line.indexOf("DocumentRoot") != -1) {
                    int idxOfDocRoot = ("DocumentRoot ").length();
                    String docRoot = line.substring(idxOfDocRoot);

                    docRoot = docRoot.substring(docRoot.indexOf("\"")+1);
                    docRoot = docRoot.substring(0, docRoot.indexOf("\""));

                    this.setDocRoot(docRoot);
                }

                line = bufReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println(ConsoleColors.RED + "File Not Found. Terminating..." + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "IOException. Terminating..." + ConsoleColors.RESET);
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDocRoot() {
        return docRoot;
    }

    public void setDocRoot(String docRoot) {
        this.docRoot = docRoot;
    }

    @Override
    public String toString() {
        return "Config{" +
                "port='" + port + '\'' +
                ", ip='" + ip + '\'' +
                ", docRoot='" + docRoot + '\'' +
                '}';
    }
}
