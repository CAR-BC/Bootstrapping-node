import DAOLayer.DAO;
import network.Neighbour;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class PeerTest {
    public static void main(String[] args) throws SQLException {
        DAO dao = new DAO();
        ArrayList<Neighbour> list = new ArrayList<>();

        list = dao.getPeers();

        for(Neighbour neighbour: list) {
            System.out.println(new JSONObject(neighbour));
        }

    }
}
