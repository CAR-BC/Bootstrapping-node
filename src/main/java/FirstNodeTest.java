import DAOLayer.DAO;
import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import network.Neighbour;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.sql.SQLException;


public class FirstNodeTest {
    public static void main(String[] args) throws FileUtilityException, IOException, SQLException {

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
        /*
         * Set the main directory as home
         * */
        System.setProperty(Constants.REGISTRY_HOME, System.getProperty("user.dir"));

        /*
         * At the very beginning
         * A Config common to all: network, blockchain, etc.
         * */
        CommonConfigHolder commonConfigHolder = CommonConfigHolder.getInstance();
        commonConfigHolder.setConfigUsingResource("peer1");
        /*
         * when initializing the network
         * */
        Node node = Node.getInstance();
        node.initTest();

        /*
         * when we want our node to start listening
         * */
        node.startListeningTest();

        Node.getInstance().addPeerToList("abcd1234","192.168.8.10", 49222);
        Node.getInstance().addPeerToList("pqrs5673","192.168.8.11", 49222);

//        for(Neighbour neighbour: Node.getInstance().getNodeConfig().getNeighbours()) {
//            JSONObject jsonObject = new JSONObject(neighbour);
//            jsonObject.put("signature", "$$signature$$");
//            System.out.println(jsonObject);
//        }

        DAO dao = new DAO();

        for(Neighbour neighbour: dao.getPeers()) {
            JSONObject jsonObject = new JSONObject(neighbour);
            jsonObject.put("signature", "$$signature$$");
            System.out.println(jsonObject);
        }

    }
}
