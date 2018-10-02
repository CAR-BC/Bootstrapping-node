package network.communicationHandler;

import network.Client.RequestMessage;
import network.Node;
import network.Protocol.BlockMessageCreator;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MessageSender {

    private static MessageSender messageSender;

    private MessageSender() {};

    public static MessageSender getInstance() {
        if(messageSender == null) {
            messageSender = new MessageSender();
        }
        return messageSender;
    }

    public void requestIP(int ListeningPort) throws IOException {

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine(); //you get the IP as a String

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip",ip);
        jsonObject.put("ListeningPort",ListeningPort);

        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "RequestIP");
//        Node.getInstance().sendMessageToNeighbour(1, blockMessage);
        Node.getInstance().sendMessageToPeer("127.0.0.1", 49211,blockMessage);
    }

    public void sendRequestedPeerDetails(String peerDetails, String peerId) {

    }


}
