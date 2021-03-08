package com.api.ReportsMyCity.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static java.util.Collections.emptyList;

public class JwtUtil {

    //Metodo para crear el JWT y enviarlo al cliente en el header de la respuesta
    public static void addAuthentication(HttpServletResponse request, String name) {
        String token = Jwts.builder()
                .setSubject(name)//Asiganamos un tiempo de expiracion
                .signWith(SignatureAlgorithm.HS512, "P@tit0")//Hash con el que firmaremos la clave
                .compact();

        request.addHeader("Authorization", "Bearer " + token);//Agregamos encabezado del token
    }

    static Authentication getAuthentication(HttpServletRequest request){
        // Obtenemos el token que viene en el encabezado de la peticion
        String token = request.getHeader("Authorization");

        // si hay un token presente, entonces lo validamos
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey("P@tit0")
                    .parseClaimsJws(token.replace("Bearer", "")) //este metodo es el que valida
                    .getBody()
                    .getSubject();

            // Recordamos que para las demás peticiones que no sean /login
            // no requerimos una autenticacion por username/password
            // por este motivo podemos devolver un UsernamePasswordAuthenticationToken sin password
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                    null;
        }
        return null;
    }
}
