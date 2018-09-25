package config;

import network.Neighbour;

import java.util.ArrayList;
import java.util.List;

public class NodeConfig {
    private final Long peerID;
    private int ListenerPort;
    private List<Neighbour> neighbours;


    public NodeConfig(Long peerID) {
        this.peerID = peerID;
        this.neighbours = new ArrayList<>();
    }

    public Long getPeerID() {
        return peerID;
    }

    public final void setListenerPort(int ListenerPort) {
        this.ListenerPort = ListenerPort;
    }

    public int getListenerPort() {
        return ListenerPort;
    }

    public void addNeighbour(Neighbour neighbour) {
        if(!isNeighbourExist(neighbour.getIp(),neighbour.getPort())) {
            this.neighbours.add(neighbour);
        }
    }

    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    public Neighbour getNeighbourByPublicKey(String publicKey) {
        Neighbour neighbour = null;

        for(Neighbour peer: neighbours) {
            if(publicKey.equals(peer.getPublicKey())) {
                neighbour = peer;
            }
        }
        return neighbour;
    }

    public boolean isNeighbourExist(String ip, int port) {
        for(Neighbour neighbour: neighbours) {
            if(neighbour.getIp().equals(ip) && neighbour.getPort() == port){
                return true;
            }
        }
        return false;
    }
}
