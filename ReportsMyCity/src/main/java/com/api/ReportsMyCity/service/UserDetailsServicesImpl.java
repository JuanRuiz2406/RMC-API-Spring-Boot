package com.api.ReportsMyCity.service;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.rest.UserRest;
import com.api.ReportsMyCity.security.entity.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired
    UserRest userRest;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        try {
            User user = userRest.getByEmail(userEmail);
            return UsuarioPrincipal.build(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
