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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DepartamentMunicipality departament;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateDetail() {
        return updateDetail;
    }

    public void setUpdateDetail(String updateDetail) {
        this.updateDetail = updateDetail;
    }

    public Report getReport() {
        return report;
    }

    public DepartamentMunicipality getDepartament() {
        return departament;
    }

    public void setDepartament(DepartamentMunicipality departament) {
        this.departament = departament;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
