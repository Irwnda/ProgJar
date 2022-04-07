package server;

import utils.Dbg;

import java.util.Locale;

public class Request {
    private String fullReq;
    private String urn;
    private String conn;
    private String range = "";

    public Request(String fullReq, Config cfg) {
        this.fullReq = fullReq;

        // Dbg.debugKu(fullReq);

        String[] res = fullReq.split(" ");
        setUrn(res[1].substring(1));

        int idxOfConn = fullReq.indexOf("Connection: ")+12;
        int idxOfEndConn = fullReq.indexOf("\n", idxOfConn);

        this.conn = fullReq.substring(idxOfConn, idxOfEndConn);
        this.conn = this.conn.toLowerCase();

        int idxOfRange = fullReq.indexOf("Range: ")+7;
        int idxOfEndRange = fullReq.indexOf("\n", idxOfConn);

        this.range = fullReq.substring(idxOfRange, idxOfEndRange);
    }

    public String getFullReq() {
        return fullReq;
    }

    public void setFullReq(String fullReq) {
        this.fullReq = fullReq;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getConn() {
        return conn;
    }

    public String getRange(){ return range; }
}
