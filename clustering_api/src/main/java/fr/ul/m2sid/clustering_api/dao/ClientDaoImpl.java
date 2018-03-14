package fr.ul.m2sid.clustering_api.dao;

import fr.ul.m2sid.clustering_api.entity.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDaoImpl implements ClientDao {

    @Override
    public int getCluster(Client client) {
        System.out.println(client.toString());
        int cluster = -1;
        try {
            PreparedStatement pstmt = PostgresConnection.getDbCon().conn.prepareStatement(
                    "SELECT \"classement.cluster\" FROM chutes WHERE idunique LIKE ?"
            );
            pstmt.setString(1, client.getIdUnique());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                cluster = rs.getInt("classement.cluster");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cluster;
    }
}
