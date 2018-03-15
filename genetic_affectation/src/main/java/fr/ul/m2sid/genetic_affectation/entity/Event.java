package fr.ul.m2sid.genetic_affectation.entity;

public class Event implements Comparable<Event>{

    private Integer identifiant;
    private Integer idclient;
    private String emplacement;
    private String heure_enregistrement;
    private String image_name;
    private Client client;

    public Event() {
    }

    public Event(Integer identifiant, Integer idclient, String emplacement, String heure_enregistrement, String image_name, Client client) {
        this.identifiant = identifiant;
        this.idclient = idclient;
        this.emplacement = emplacement;
        this.heure_enregistrement = heure_enregistrement;
        this.image_name = image_name;
        this.client = client;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public Integer getIdclient() {
        return idclient;
    }

    public void setIdclient(Integer idclient) {
        this.idclient = idclient;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getHeure_enregistrement() {
        return heure_enregistrement;
    }

    public void setHeure_enregistrement(String heure_enregistrement) {
        this.heure_enregistrement = heure_enregistrement;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Event{" +
                "identifiant=" + identifiant +
                ", idclient=" + idclient +
                ", emplacement='" + emplacement + '\'' +
                ", heure_enregistrement='" + heure_enregistrement + '\'' +
                ", image_name='" + image_name + '\'' +
                ", client=" + client +
                '}';
    }

    @Override
    public int compareTo(Event event) {
        if (this.client.getCluster() < event.client.getCluster())
            return 1;
        else if (this.client.getCluster() > event.client.getCluster())
            return -1;
        else
            return -1;
    }
}
