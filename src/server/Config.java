package server;

import utils.CColors;

import java.io.*;

public class Config {
    private String port;
    private String ip;
    private String docRoot;

    public String getConfigPath() {
        return configPath;
    }

    private String configPath;

    public Config(String configPath) {
        this.configPath = configPath;

        try {
            FileReader fReader = new FileReader(configPath);
            BufferedReader bufReader = new BufferedReader(fReader);

            String line;
            line = bufReader.readLine();
            while (line != null) {
                if(line.contains("Listen")) {
                    String port = line.split(" ")[1];
                    this.setPort(port);
                }

                if(line.contains("ServerName")) {
                    String ip = line.split(" ")[1];
                    this.setIp(ip);
                }

                if(line.contains("DocumentRoot")) {
                    String[] conf = line.split(" ");
                    this.setDocRoot(conf[1].substring(1,conf[1].length()-1));
                }

                line = bufReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println(CColors.RED + "File Not Found. Terminating..." + CColors.RESET);
        } catch (IOException e) {
            System.out.println(CColors.RED + "IOException. Terminating..." + CColors.RESET);
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

    public void setDocRootByHost(String configPath, String Host) throws IOException {
        FileReader fReader = new FileReader(configPath);
        BufferedReader bufReader = new BufferedReader(fReader);

        String line;
        line = bufReader.readLine();
        while (line != null) {
            if(line.contains(Host) || line.contains("DocumentRoot")){
                String[] conf = line.split(" ");
                setDocRoot(conf[1].substring(1,conf[1].length()-1));
            }
            line = bufReader.readLine();
        }
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
