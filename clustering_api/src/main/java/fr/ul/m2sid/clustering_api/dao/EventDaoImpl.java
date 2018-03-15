package fr.ul.m2sid.clustering_api.dao;

import fr.ul.m2sid.clustering_api.entity.Event;
import fr.ul.m2sid.clustering_api.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class EventDaoImpl implements EventDao {

    @Autowired
    Converter converter;

    @Override
    public void saveEvent(Event event) {
        try {
            PreparedStatement pstmt = PostgresConnection.getDbCon().conn.prepareStatement(
                    "INSERT INTO event_assistance (" +
                            "heure_enregistrement, " +
                            "emplacement, " +
                            "idclient, " +
                            "image_name) " +
                            "VALUES (?, ?, ?, ?) ");


            pstmt.setDate(1, converter.convertDateToSQLFormat(event.getHeure_enregistrement()));
            pstmt.setString(2, event.getEmplacement());
            pstmt.setInt(3, event.getIdclient());
            pstmt.setString(4, event.getImage_name());
            pstmt.execute();

        }
        catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement de l'evenement ! ");
            e.printStackTrace();
        }
    }
}
