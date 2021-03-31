package com.api.ReportsMyCity.entity;

import javax.persistence.*;

@Entity
@Table(name = "photography")
public class Photography {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 200, unique = true)
    private String imagePath;

    @Column(length = 50)
    private String category;

    @ManyToOne
    private Report reports;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Report getReports() {
        return reports;
    }

    public void setReports(Report reports) {
        this.reports = reports;
    }
}
