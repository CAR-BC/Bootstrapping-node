package network.Listener.Handlers;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import network.Client.RequestMessage;
import network.Protocol.AckMessageCreator;
import network.communicationHandler.RequestHandlerForServer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

public class FirstNodeHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(FirstNodeHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException, ParseException, SQLException {
        if(msg instanceof RequestMessage){
            RequestMessage requestMessage = (RequestMessage) msg;
            Map<String, String> headers = requestMessage.readHeaders(); //TODO: Inspect headers
            String data = requestMessage.readData();

//            RequestHandler.getInstance().handleRequest(headers,data);
            //-------------------------------------------
            // call the workflow methods here after checking the headers
            // can use switch-case and call the methods
            //-----------------------------------------------

            System.out.println("=====================================");
            System.out.println("               IP Server             ");
            System.out.println("=====================================");
            System.out.println("----------headers----------------");
            System.out.println(headers.toString());
            System.out.println("----------data----------------");
            System.out.println(data);


            String messageType = (String)headers.get("messageType");
            RequestMessage ackMessage = AckMessageCreator.createAckMessage(messageType);
            ackMessage.addHeader("keepActive", "false");
            ackMessage.addTheData("hello from IP server");
            ChannelFuture f = ctx.writeAndFlush(ackMessage);
            //if the msg we received had the header "keepActive" set to false
            //then close the channel

            String clientIP = ((InetSocketAddress )ctx.channel().remoteAddress()).getAddress().getHostAddress();
            JSONObject receivedObject = new JSONObject(data);
            receivedObject.put("ip", clientIP);
            RequestHandlerForServer.getInstance().handleRequest(headers,receivedObject);

            System.out.println("++++++++++++IPAddress++++++++++++++++");
            System.out.println(clientIP);

            if("false".equals(headers.get("keepActive"))) {
                //finish the process
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
