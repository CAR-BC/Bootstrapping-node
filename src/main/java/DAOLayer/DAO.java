package DAOLayer;

import config.ConnectionFactory;
import network.Neighbour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAO {

    Connection connection;
    PreparedStatement ptmt;
    private final Logger log = LoggerFactory.getLogger(DAO.class);


    public DAO() {
        try {
            connection = ConnectionFactory.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void AddPeerToDatabase(String nodeID, String ip, int port ) {
        String queryString = "INSERT INTO `PeerDetails`(`node_id`,`ip`,`port`) VALUES(?,?,?)";
        try {

            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1,nodeID);
            ptmt.setString(2, ip);
            ptmt.setInt(3, port);
            ptmt.executeUpdate();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            log.info("Peer added to database successfully: {}", nodeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePeer(String nodeID, String ip, int port ) {
        String queryString = "UPDATE `PeerDetails` SET `ip` = ?, `port` = ? WHERE  `node_id` = ?";
        try {

            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, ip);
            ptmt.setInt(2, port);
            ptmt.setString(3, nodeID);
            ptmt.executeUpdate();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            log.info("Peer details updated successfully: {}", nodeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Neighbour> getPeers() throws SQLException {

        String queryString = "SELECT * FROM `PeerDetails`";
        ResultSet resultSet = null;
//        ArrayList<JSONObject> peerList = new ArrayList<>();
        ArrayList<Neighbour> peerList = new ArrayList<>();
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
//                JSONObject peer = new JSONObject();
//                peer.put("nodeID", resultSet.getString("node_id"));
//                peer.put("ip", resultSet.getString("ip"));
//                peer.put("listeningPort", resultSet.getString("port"));
//                peer.put("publicKey", resultSet.getString("public_key"));
//                peerList.add(peer);

                Neighbour neighbour = new Neighbour(resultSet.getString("node_id"),resultSet.getString("ip"),
                        resultSet.getInt("port"));
                peerList.add(neighbour);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            return peerList;
        }
    }


}
