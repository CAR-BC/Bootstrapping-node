package network;

import DAOLayer.DAO;
import config.CommonConfigHolder;
import config.NodeConfig;
import network.Client.Client;
import network.Client.RequestMessage;
import network.Listener.FirstNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public final class Node {
    private final Logger log = LoggerFactory.getLogger(Node.class);
    private static final Node instance = new Node();

    Client client;

    private NodeConfig nodeConfig;

    private Node() {}

    public static Node getInstance() {
        return instance;
    }

    public void init() {

        /* Set config and its parameters */
        Random random = new Random();
        long peerID = random.nextLong();

        //Create config
        this.nodeConfig = new NodeConfig(peerID);

        //Set port to listen on
        JSONObject commonConfig = CommonConfigHolder.getInstance().getConfigJson();
        nodeConfig.setListenerPort(commonConfig.getInt("listener_port"));

        //Add neighbours list
        JSONArray neighbours = commonConfig.getJSONArray("neighbours");
        for (int i = 0; i < neighbours.length(); i++) {
            JSONObject neighbourJson = neighbours.getJSONObject(i);
            String neightbourIP = neighbourJson.getString("ip");
            int neightbourPort = neighbourJson.getInt("port");
            Neighbour neighbour = new Neighbour(neightbourIP, neightbourPort);
            nodeConfig.addNeighbour(neighbour);
        }

        log.info("Initializing Node:{}", peerID);

    }

    //revert later

    public void initTest() {

        /* Set config and its parameters */
        Random random = new Random();
        long peerID = 0;

        //Create config
        this.nodeConfig = new NodeConfig(peerID);

        //Set port to listen on
        JSONObject commonConfig = CommonConfigHolder.getInstance().getConfigJson();
        nodeConfig.setListenerPort(commonConfig.getInt("listener_port"));
        System.out.println(commonConfig.toString());

        //Add neighbours list
        JSONArray neighbours = commonConfig.getJSONArray("neighbours");
//        for (int i = 0; i < neighbours.length(); i++) {
//            JSONObject neighbourJson = neighbours.getJSONObject(i);
//            String neightbourIP = neighbourJson.getString("ip");
//            int neightbourPort = neighbourJson.getInt("port");
//            Neighbour neighbour = new Neighbour(neightbourIP, neightbourPort);
//            nodeConfig.addNeighbour(neighbour);
//        }

        log.info("Initializing Node:{}", peerID);

    }

    //revert later

    FirstNode firstNode;
    public void startListeningTest() {
        this.firstNode = new FirstNode();
        this.firstNode.init(nodeConfig.getListenerPort());
        this.firstNode.start();
        log.info("Initialized listener");
    }

    //move later to server node

    public void addPeerToList(String nodeID, String peerIP, int peerPort ) {
        DAO dao = new DAO();
        JSONObject peer = findPeerDetails(nodeID);

        if( peer == null) {
            Neighbour neighbour = new Neighbour(nodeID, peerIP,peerPort);
            nodeConfig.addNeighbour(neighbour);
            dao.AddPeerToDatabase(nodeID,peerIP,peerPort);
            Node.getInstance().showNeighbours();
            log.info("peer registered successfully: ", nodeID);
        }else {
            String ip = peer.getString("ip");
            int port = peer.getInt("port");

            if(!ip.equals(peerIP) || port != peerPort) {
                Node.getInstance().getNodeConfig().updateNeighbourDetails(nodeID, peerIP, peerPort);
                dao.updatePeer(nodeID,peerIP,peerPort);
                Node.getInstance().showNeighbours();
                log.info("peer data updated successfully: ", nodeID);
            }
        }
    }

    public NodeConfig getNodeConfig() {
        return nodeConfig;
    }

    public void sendMessageToNeighbour(int neighnourIndex, RequestMessage requestMessage) {
        Client client = new Client();
        Neighbour neighbour1 = nodeConfig.getNeighbours().get(neighnourIndex);
        client.init(neighbour1, requestMessage);
        client.start();
        log.info("Initialized client");
    }

    public void sendMessageToPeer(String IP, int port, RequestMessage requestMessage) {
        Client client = new Client();
        client.initTest(IP,port,requestMessage);
        client.start();
        log.info("Initialized client");
    }

    public JSONObject getPeersAsJSONString() {
        JSONObject peers = new JSONObject();
        JSONArray IPList = new JSONArray();
        for(Neighbour neighbour: nodeConfig.getNeighbours()) {
            JSONObject temp = new JSONObject();
            temp.put("ip", neighbour.getIp());
            temp.put("peerID", neighbour.getNodeID());
            temp.put("ListeningPort", neighbour.getPort());
            IPList.put(temp);
        }
        peers.put("peers",IPList);
        return peers;
    }

    public JSONObject findPeerDetails(String peerID) {
        for(Neighbour neighbour: nodeConfig.getNeighbours()) {
            if(peerID.equals(neighbour.getNodeID())) {
                return new JSONObject(neighbour);
            }
        }
        return null;
    }

    //test method

    public void showNeighbours() {
        for(Neighbour neighbour: nodeConfig.getNeighbours()) {
            System.out.println(new JSONObject(neighbour));
        }
    }

}
