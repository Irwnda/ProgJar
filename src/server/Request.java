package server;

import utils.Debug;

public class Request {
    private String fullReq;
    private String urn;

    public Request(String fullReq, Config cfg) {
        this.fullReq = fullReq;

        String res[] = fullReq.split(" ");
        setUrn(res[1].substring(1));
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
}
