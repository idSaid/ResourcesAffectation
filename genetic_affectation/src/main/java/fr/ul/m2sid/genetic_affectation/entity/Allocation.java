package fr.ul.m2sid.genetic_affectation.entity;

import java.io.Serializable;

public class Allocation implements Serializable {

    private Integer idEvent;
    private Integer idAgent;

    public Allocation(Integer idEvent, Integer idAgent) {
        this.idAgent = idAgent;
        this.idEvent = idEvent;
    }

    public Allocation() {
    }

    public Integer getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(Integer idAgent) {
        this.idAgent = idAgent;
    }

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }
}
