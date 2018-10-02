package network;

public class Neighbour {
    private String ip;
    private int port;
    private String nodeID;

    public Neighbour(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Neighbour(String nodeID, String ip, int port) {
        this.nodeID = nodeID;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }


}
