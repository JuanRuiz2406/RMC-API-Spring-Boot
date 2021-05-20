package com.api.ReportsMyCity.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "departamentMunicipality")
public class DepartamentMunicipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100)
    private String description;

    @NotBlank(message = "El telefono es obligatorio")
    @Column(length = 10, nullable = false)
    private String telephone;

    @Email(message = "El correo tiene formato incorrecto")
    @NotBlank(message = "El Correo es obligatorio")
    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Municipality municipality;

    @OneToOne
    @JoinColumn(nullable = false)
    private User manager;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}

