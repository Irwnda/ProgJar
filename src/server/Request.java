package server;

import utils.Dbg;

public class Request {
    private String fullReq;
    private String urn;
    private String conn;

    public Request(String fullReq, Config cfg) {
        this.fullReq = fullReq;

        // Dbg.debugKu(fullReq);

        String res[] = fullReq.split(" ");
        setUrn(res[1].substring(1));

        int idxOfConn = fullReq.indexOf("Connection: ")+12;
        int idxOfEndConn = fullReq.indexOf("\n", idxOfConn);

        String conn = fullReq.substring(idxOfConn, idxOfEndConn);
        this.conn = conn;
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
}
