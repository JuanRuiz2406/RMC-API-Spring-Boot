package com.api.ReportsMyCity.service;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserService implements UserDetailsService {

    @Autowired
    @Qualifier("managerUser")
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),buildgrante(user.getRole()));
    }

    public List<GrantedAuthority> buildgrante(String rol){
        String[] roles = {};
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(rol));

        return auths;
    }
}
