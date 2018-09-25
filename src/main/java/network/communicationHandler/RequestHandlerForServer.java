package network.communicationHandler;

import network.Client.RequestMessage;
import network.Node;
import network.Protocol.PeerDetailsMessageCreator;
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
        switch (messageType) {
            case "RequestIP":
                System.out.println("RequestIP");
                handleRequestIP(data);
                break;

            case "TransactionValidation":
                System.out.println("TransactionProposalResponse");
//                handleTransactionProposalResponse(data);
                break;
        }
    }

    public void handleRequestIP(JSONObject data) {
        String IP = data.getString("ip");
        int port = data.getInt("ListeningPort");

//        JSONObject peers = new JSONObject();
//        JSONArray IPList = new JSONArray();
//
//        for(int i = 1; i< 5; i++) {
//            JSONObject temp = new JSONObject();
//            temp.put("publicKey",String.valueOf(i));
//            temp.put("ip",String.valueOf(i)+".10.13.245");
//            temp.put("ListeningPort",i*100);
//            IPList.put(temp);
//        }
//        peers.put("peers", IPList);

        String peersDetailsAsJSONString = Node.getInstance().getPeersAsJSONString();
        //save ip and port
        Node.getInstance().addPeerToList("PK",IP,port);
        RequestMessage peersDetails = PeerDetailsMessageCreator.createPeerDetailsMessage(peersDetailsAsJSONString);
        peersDetails.addHeader("keepActive", "false");
        Node.getInstance().sendMessageToPeer(IP, port,peersDetails);
    }
}
