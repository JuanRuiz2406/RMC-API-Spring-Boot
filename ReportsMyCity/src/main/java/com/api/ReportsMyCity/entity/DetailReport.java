package com.api.ReportsMyCity.entity;

import javax.persistence.*;

@Entity
@Table(name = "detailReport")
public class DetailReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    private String updateDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdate() {
        return updateDetail;
    }

    public void setUpdate(String update) {
        this.updateDetail = update;
    }
}
