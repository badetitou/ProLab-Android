package com.tbe.prolab.Fonctionnalities;

import java.sql.Date;

/**
 * Created by badetitou on 3/20/15.
 */


public class Fonctionnality {

    private int id;
    private String name;
    private String description;
    private int avancement;
    private Date deadLine;

    public Fonctionnality(String name, String description, Date deadLine){
        this.name = name;
        this.description = description;
        this.deadLine = deadLine;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvancement() {
        return avancement;
    }

    public void setAvancement(int avancement) {
        this.avancement = avancement;
    }

    public Fonctionnality(){}

}