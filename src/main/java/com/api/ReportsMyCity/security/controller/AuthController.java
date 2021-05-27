package com.api.ReportsMyCity.security.controller;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.repository.UserRepository;
import com.api.ReportsMyCity.rest.UserRest;
import com.api.ReportsMyCity.security.dto.JwtDto;
import com.api.ReportsMyCity.security.dto.LoginUsuario;
import com.api.ReportsMyCity.security.dto.Message;
import com.api.ReportsMyCity.security.dto.NewUser;
import com.api.ReportsMyCity.security.jwt.JwtProvider;
import com.api.ReportsMyCity.security.jwt.JwtTokenFilter;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final AuthenticationManager authenticationManager;
    private final UserRest userRest;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRest userRest, UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRest = userRest;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NewUser newUser, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("Campos mal rellenado binding",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        if(userRest.getExistsByEmail(newUser.getEmail()))
            return new ResponseEntity(new Message("Ese correo ya existe",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);

        User user =
                new User(0,newUser.getIdCard(),newUser.getName(),newUser.getLastName(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()),newUser.getPassword(), newUser.getRole(), newUser.getDirection(), "activo");
        userRepository.save(user);
        return new ResponseEntity(new Message("Usuario guardado", HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        user.setPassdecode("secreto");
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

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithThird(@Valid @RequestBody NewUser newUser, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("Campos mal rellenado binding",HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);

        if (!userRest.getExistsByEmail(newUser.getEmail())){
            ResponseEntity res = nuevo(newUser, bindingResult);
            if(res.getStatusCode().value() != 201){
                return new ResponseEntity(new Message("Error al crear usuario", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
        }
        ResponseEntity<User> user = userRest.getByEmail(newUser.getEmail());//busco al usuario
        if(user != null){
            LoginUsuario loginUsuario = new LoginUsuario();
            loginUsuario.setEmail(user.getBody().getEmail());
            loginUsuario.setPassword(user.getBody().getPassdecode());
            loginUsuario.setProvider(newUser.getProvider());
            loginUsuario.setName(user.getBody().getName());
            loginUsuario.setLastname(user.getBody().getLastname());
            return login(loginUsuario);
        }
        return new ResponseEntity(new Message("Error en el servidor", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

}
