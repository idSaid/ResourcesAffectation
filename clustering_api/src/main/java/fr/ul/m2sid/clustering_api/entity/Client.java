package fr.ul.m2sid.clustering_api.entity;

import fr.ul.m2sid.clustering_api.dao.ClientDao;
import fr.ul.m2sid.clustering_api.dao.ClientDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class Client {

    private String idUnique;
    private int cluster;

    ClientDao clientDao = new ClientDaoImpl();

    public Client() {
    }

    public Client(String idUnique) {
        this.idUnique = idUnique;
        this.cluster = clientDao.getCluster(this);
    }

    public String getIdUnique() {
        return idUnique;
    }

    public void setIdUnique(String idUnique) {
        this.idUnique = idUnique;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idUnique='" + idUnique + '\'' +
                ", cluster=" + cluster +
                '}';
    }
}
