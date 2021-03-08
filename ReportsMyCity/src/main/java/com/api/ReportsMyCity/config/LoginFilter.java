package com.api.ReportsMyCity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private JwtUtil tokenAuthentication;

    public LoginFilter(String url, AuthenticationManager authManager){
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        tokenAuthentication = new JwtUtil();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        InputStream body = request.getInputStream();
        UserConfig user = new ObjectMapper().readValue(body, UserConfig.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsuario(),
                        user.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtUtil.addAuthentication(response, authResult.getName());
    }
}
