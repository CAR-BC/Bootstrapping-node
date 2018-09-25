package network.Protocol;

import network.Client.RequestMessage;
import java.sql.Timestamp;

public class PeerDetailsMessageCreator {

    public static RequestMessage createPeerDetailsMessage(String details){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String sender = "myPublicKey";
        String receiver = "yourPublicKey";
        String messageType = "IPResponse";

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.addHeader("timestamp", timestamp.toString());
        requestMessage.addHeader("sender", sender);
//        requestMessage.addHeader("receiver", receiver);
        requestMessage.addHeader("messageType", messageType);
        requestMessage.addTheData(details);
        return requestMessage;
    }
}
