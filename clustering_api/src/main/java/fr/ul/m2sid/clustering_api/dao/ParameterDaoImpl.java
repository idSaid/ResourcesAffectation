package fr.ul.m2sid.clustering_api.dao;

import fr.ul.m2sid.clustering_api.entity.Parametre;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class ParameterDaoImpl implements ParameterDao {

    @Override
    public void updateParametre(Parametre parametre) {
        try {
            PreparedStatement pstmt = PostgresConnection.getDbCon().conn.prepareStatement("UPDATE parametres SET value = ? WHERE name = 'nbclusters';");
            pstmt.setString(1, parametre.getValue());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
