package com.api.ReportsMyCity.config;

import com.api.ReportsMyCity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("userService")
    private UserService userdetailservice;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userdetailservice);
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs","s/configuration/ui",
                                                "/swagger-resources/**",
                                                    "/configuration/security",
                                                        "/swagger-ui.html",
                                                            "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/user","/user/byEmail/{userEmail}").permitAll()//permitimos el acceso a /login.
                .anyRequest().authenticated()//cualquier otra peticion requiere autenticacion
                .and()//Las peticiones /login pasaran previamente por este filtro
                .addFilterBefore(new LoginFilter("/user/byEmail/{userEmail}", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);//las demas peticiones pasaran por este otro filtro
    }
}
