package network.Protocol;

import network.Client.RequestMessage;

import java.sql.Timestamp;

public class AckMessageCreator {
    public static RequestMessage createAckMessage(String ackFor){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String sender = "myPublicKey";
        String receiver = "yourPublicKey";
        String messageType = "Ack";

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.addHeader("timestamp", timestamp.toString());
        requestMessage.addHeader("sender", sender);
        requestMessage.addHeader("receiver", receiver);
        requestMessage.addHeader("messageType", messageType);
        requestMessage.addHeader("ackFor", ackFor);
        return requestMessage;
    }
}
