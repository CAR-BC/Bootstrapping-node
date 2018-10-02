package network.communicationHandler;

import config.ChainUtil;
import config.KeyGenerator;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.MessageCreator;
import org.json.JSONObject;

import java.util.Map;

public class RequestHandlerForServer {

    private static RequestHandlerForServer requestHandlerForServer;

    private RequestHandlerForServer() {
    }

    public static RequestHandlerForServer getInstance() {
        if (requestHandlerForServer == null) {
            requestHandlerForServer = new RequestHandlerForServer();
        }
        return requestHandlerForServer;
    }

    public void handleRequest(Map headers, JSONObject data) {

        String messageType = (String) headers.get("messageType");
        String peerID = (String) headers.get("sender");
        switch (messageType) {
            case "RequestIP":
                System.out.println("RequestIP");
                handleRequestIP(data, peerID);
                break;

            case "RequestPeerDetails":
                System.out.println("RequestPeerDetails");
                handlePeerDetailsRequest(data);
                break;

        }
    }

    public void handleRequestIP(JSONObject data, String peerID) {
        String IP = data.getString("ip");
        int port = data.getInt("ListeningPort");
        JSONObject peersDetailsAsJSONString = Node.getInstance().getPeersAsJSONString();
        Node.getInstance().addPeerToList(peerID, IP,port);
        RequestMessage peersDetails = MessageCreator.createMessage(peersDetailsAsJSONString,"IPResponse");
        peersDetails.addHeader("keepActive", "false");
        Node.getInstance().sendMessageToPeer(IP, port,peersDetails);
    }

    public void handlePeerDetailsRequest(JSONObject data) {
        String ip = data.getString("ip");
        int listeningPort = data.getInt("listeningPort");
        String peerID = data.getString("peerID");
        JSONObject jsonObject = new JSONObject();
        JSONObject peerDetails = Node.getInstance().findPeerDetails(peerID);
        jsonObject.put("peerDetails", peerDetails);
        jsonObject.put("signature", ChainUtil.getInstance().digitalSignature(peerDetails.toString()));
        jsonObject.put("signedData", peerDetails.toString());
        jsonObject.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());
        RequestMessage peerDetailMessage = MessageCreator.createMessage(jsonObject,"RequestedPeerDetails");
        Node.getInstance().sendMessageToPeer(ip,listeningPort, peerDetailMessage);
    }


}
