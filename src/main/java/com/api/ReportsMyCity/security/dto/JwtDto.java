package com.api.ReportsMyCity.security.dto;

import com.api.ReportsMyCity.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDto {
    private String token;
    private String bearer = "Bearer";
    private String email;
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDto(String token, String email, Collection<? extends GrantedAuthority> authorities, User user) {
        this.token = token;
        this.email = email;
        this.authorities = authorities;
        this.user = user;
    }

    public JwtDto(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
