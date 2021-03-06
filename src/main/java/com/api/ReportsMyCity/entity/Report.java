package com.api.ReportsMyCity.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(length = 45, nullable = false)
    private String title;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(length = 100, nullable = false)
    private String description;

    @NotBlank(message = "El estado es obligatorio")
    @Column(length = 45, nullable = false)
    private String state;

    @NotBlank(message = "La privacidad es obligatoria")
    @Column(length = 45, nullable = false)
    private String privacy;

    @JsonFormat(pattern="dd/MM/yyyy")
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Coordenates coordenates;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Municipality municipality;


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Coordenates getCoordenates() {
        return coordenates;
    }

    public void setCoordenates(Coordenates coordenates) {
        this.coordenates = coordenates;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
