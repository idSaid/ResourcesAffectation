package fr.ul.m2sid.genetic_affectation.dao;

import fr.ul.m2sid.genetic_affectation.entity.Agent;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

@Service
public class AgentDaoImpl implements AgentDao {

    @Override
    public TreeSet<Agent> getFreeAgents() {
        TreeSet<Agent> agents= new TreeSet<>();
        try {
            PreparedStatement pstmt = PostgresConnection.getDbCon().conn.prepareStatement(
                    "SELECT id_resource , capacite_events, score FROM teleoperateurs_informations2 " +
                            "WHERE capacite_events > 0 and is_agent = 1;"
            );
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Agent agent = new Agent(rs.getInt("id_resource"));
                agent.setScore(rs.getDouble("score"));
                agent.setAgent(true);
                agent.setCapacite_events(rs.getInt("capacite_events"));
                agents.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public Set<Agent> getCallCenters() {
        Set<Agent> agents= new TreeSet<>();
        try {
            PreparedStatement pstmt = PostgresConnection.getDbCon().conn.prepareStatement(
                    "SELECT id_resource, availability ,score, is_agent FROM teleoperateurs_informations2 WHERE is_agent = 0 ;"
            );
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Agent agent = new Agent(rs.getInt("id_resource"));
                agent.setAgent(false);
                agents.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public ArrayList<Integer> getIdOTeleoperateurs() {
        ArrayList<Integer> idTeleoperateurs = new ArrayList<Integer>();
        try {
            Statement stmt = PostgresConnection.getDbCon().conn.createStatement();
            //stmt.execute("SELECT idconseiller FROM teleoperateurbonita;");
            stmt.execute("SELECT id_resource FROM teleoperateurs_informations2 WHERE capacite_events > 0 and is_agent = 1;");
            ResultSet resultSet = stmt.getResultSet();

            while (resultSet.next()) {
                idTeleoperateurs.add(Integer.parseInt(resultSet.getString("id_resource")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idTeleoperateurs;
    }

    @Override
    public Agent getAgentById(Integer id) {
        Agent agent = new Agent();
        try {
            PreparedStatement pstmt = PostgresConnection.getDbCon().conn.prepareStatement("SELECT * FROM teleoperateurs_informations2 WHERE id_resource=?;");
            pstmt.setString(1, id.toString());
            ResultSet resultSet = pstmt.executeQuery();
            double score;
            while (resultSet.next()) {
                id = resultSet.getInt("id_resource");
                score = resultSet.getDouble("score");
                agent = new Agent(id,score,true,0); // TODO : boolean + capacite_events
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent;
    }

}
