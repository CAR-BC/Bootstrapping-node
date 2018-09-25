package DAOLayer;

import config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO {

    Connection connection;
    PreparedStatement ptmt;

    public DAO() {
        try {
            connection = ConnectionFactory.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void AddPeerToDatabase(String ip, int port ) {
        PreparedStatement ptmt;
        try {
            String queryString = "INSERT INTO ` PeerDetails`(`ip`,`port`) VALUES(?,?)";

            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, ip);
            ptmt.setInt(2, port);
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            System.out.println("Peer added to database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
