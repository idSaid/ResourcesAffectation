package fr.ul.m2sid.genetic_affectation.entity;

public class Client {

    private String idUnique;
    private int cluster;

    public Client() {
    }

    public Client(String idUnique) {
        this.idUnique = idUnique;
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
