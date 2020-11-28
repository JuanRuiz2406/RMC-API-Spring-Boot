package com.api.ReportsMyCity.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(length = 45)
    private String title;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(length = 100, nullable = false)
    private String description;

    @Column(length = 45)
    private String state;

    @NotBlank(message = "La privacidad es obligatoria")
    @Column(length = 45, nullable = false)
    private String privacy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
