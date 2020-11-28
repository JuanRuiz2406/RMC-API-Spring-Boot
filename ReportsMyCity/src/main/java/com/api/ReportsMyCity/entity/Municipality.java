package com.api.ReportsMyCity.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "municipality")
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @NotBlank(message = "La direccion de linea es obligatorio")
    @Column(length = 200)
    private String adress;

    @Email(message = "El correo tiene formato incorrecto")
    @NotBlank(message = "El Correo es obligatorio")
    @Column(length = 100)
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    @Column(length = 10)
    private String telephone;

    @NotBlank(message = "El horario es obligatorio")
    @Column(length = 45)
    private String schudule;

    @Column(length = 100)
    private String webSite;

    @OneToOne
    private Coordenates coordenates;

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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSchudule() {
        return schudule;
    }

    public void setSchudule(String schudule) {
        this.schudule = schudule;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Coordenates getCoordenates() {
        return coordenates;
    }

    public void setCoordenates(Coordenates coordenates) {
        this.coordenates = coordenates;
    }
}
