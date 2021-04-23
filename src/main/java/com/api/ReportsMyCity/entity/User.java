package com.api.ReportsMyCity.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 7022761777326374528L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "La tarjeta de identificacion es obligatoria")
    @Column(length = 20)
    private String idCard;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(length = 45, nullable = false)
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(length = 45, nullable = false)
    private String lastname;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo tiene un formato invalido")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    @Column(length = 45)
    private String role;

    @Column(length = 100)
    private String direction;

    @Column(length = 10)
    private String code;

    @Column(length = 100)
    private String codeDate;
    public User(int id, @NotBlank(message = "La tarjeta de identificacion es obligatoria") String idCard, @NotBlank(message = "El nombre es obligatorio") String name, @NotBlank(message = "El apellido es obligatorio") String lastname, @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo tiene un formato invalido") String email, @NotBlank(message = "La contraseña es obligatoria") String password, String role, String direction) {
        this.id = id;
        this.idCard = idCard;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.direction = direction;
    }

    public User() {

    public User() {
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCodeDate() {
        return codeDate;
    }

    public void setCodeDate(String codeDate) {
        this.codeDate = codeDate;
    }
}
