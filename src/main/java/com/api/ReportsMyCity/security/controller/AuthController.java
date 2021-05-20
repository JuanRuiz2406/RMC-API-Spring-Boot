package com.api.ReportsMyCity.security.controller;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.repository.UserRepository;
import com.api.ReportsMyCity.rest.UserRest;
import com.api.ReportsMyCity.security.dto.JwtDto;
import com.api.ReportsMyCity.security.dto.LoginUsuario;
import com.api.ReportsMyCity.security.dto.Message;
import com.api.ReportsMyCity.security.dto.NewUser;
import com.api.ReportsMyCity.security.jwt.JwtProvider;
import com.api.ReportsMyCity.service.UserDetailsServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRest userRest;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                          UserRest userRest, UserRepository userRepository, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRest = userRest;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NewUser newUser, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("Campos mal rellenado binding",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        if(userRest.getExistsByEmail(newUser.getEmail()))
            return new ResponseEntity(new Message("Ese correo ya existe",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        User user =
                new User(0,newUser.getIdCard(),newUser.getName(),newUser.getLastName(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()), newUser.getRole(), newUser.getDirection(), "activo");
        userRepository.save(user);
        return new ResponseEntity(new Message("Usuario guardado", HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("Campos mal puestos", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        JwtDto jwtDto =  new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities(), user);
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

    @PostMapping("/loginWithAPI")
    public JwtDto loginWithAPI(LoginUsuario loginUsuario){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        JwtDto jwtDto =  new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities(), user);
        return jwtDto;
    }

    /*@PostMapping("/loginWithGoogle")
    public JwtDto loginWithGoogle(LoginUsuario loginUsuario){
        User user = userRepository.findByEmail(loginUsuario.getEmail());//busco al usuario
        if(user != null){

        }
    }*/

    @PostMapping("/loginproviders")
    public ResponseEntity<?> loginProviders(@Valid @RequestBody LoginUsuario loginUsuario){
        if(loginUsuario == null){
            return new ResponseEntity(new Message("Campos mal rellenados", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        if(loginUsuario.getProvider() == "google"){

        }
        else if(loginUsuario.getProvider() == "facebook"){

        }

        JwtDto jwt = loginWithAPI(loginUsuario);
        return new ResponseEntity(jwt, HttpStatus.OK);
    }

    //añadir la parte de olvide mi contraseña.
}
