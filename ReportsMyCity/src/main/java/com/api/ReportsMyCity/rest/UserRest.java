package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.UserRepository;
import com.api.ReportsMyCity.security.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("user")
public class UserRest {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRest(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        checkRMCUsers();
    }

    private void checkRMCUsers() {
        if (userRepository.count() == 0) {
            createRMCUsers();
        }
    }

    private void createRMCUsers() {
        User userJuan = new User(0,"117990636","Juan","Ruiz",
                "juan@rmc.com",passwordEncoder.encode("123456789"),"RMCTeam","Casa", "", true);
        User userMarco = new User(0,"123","Marco","Alvarado",
                "marco@rmc.com",passwordEncoder.encode("123456789"),"RMCTeam","Casa","", true);
        User userDiego = new User(0,"123","Diego","Villareal",
                "diego@rmc.com",passwordEncoder.encode("123456789"),"RMCTeam","Casa", "", true);
        userRepository.save(userJuan);
        userRepository.save(userMarco);
        userRepository.save(userDiego);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "{userId}")
    public User getById(@PathVariable("userId") int userId){
        return this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("No existe ningun usuario con ese numero de indentificacion"));
    }

    @GetMapping(value = "/byIdCard/{userIdCard}")
    public ResponseEntity<User> getByIdCard(@PathVariable("userIdCard") String userIdCard) throws Exception{
        User userFound = userRepository.findByIdCard(userIdCard);
        if(userFound != null){
            return new ResponseEntity(userFound, HttpStatus.OK);
        }else{
            return new ResponseEntity(new Message("No se encontro el usuario",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byEmail/{userEmail}")
    public ResponseEntity<User> getByEmail(@PathVariable("userEmail") String userEmail){
        User user = userRepository.findByEmail(userEmail);
        if (user != null)
            return new ResponseEntity(user,HttpStatus.OK);
        else
            return new ResponseEntity(new Message("El usuario no existe",HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
    }

    public boolean getExistsByEmail(String userEmail){
        return userRepository.existsByEmail(userEmail);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) throws Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for(ConstraintViolation<User> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        User UserWhitExistingEmail = userRepository.findByEmail(user.getEmail());

        if (UserWhitExistingEmail != null) {
            return new ResponseEntity(new Message("Error al guardar, ya existe el usuario",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else {
            userRepository.save(user);
            return new ResponseEntity(new Message("Usuario guardado",HttpStatus.CREATED.value()), HttpStatus.CREATED);
        }
    }


    @PutMapping
    public ResponseEntity update(@RequestBody User user){

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            User updateUser = optionalUser.get();
            updateUser.setName(user.getName());
            updateUser.setLastname(user.getLastname());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
            updateUser.setRole("user");
            updateUser.setDirection(user.getDirection());

            if(userRepository.save(updateUser)!= null){
                return new ResponseEntity(new Message("Usuario actualizado",HttpStatus.CREATED.value()), HttpStatus.CREATED);
            }else {
                return new ResponseEntity(new Message("Error aL actualizar el usuario",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity(new Message("Usuario No existe",HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }

    }

    @DeleteMapping(value = "{userId}") // users/userId {DELETE}
    public ResponseEntity delete(@PathVariable("userId") int userId){

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            return new ResponseEntity(new Message("Error al eliminar usuario",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else {
            userRepository.deleteById(userId);
            return new ResponseEntity(new Message("Usuario Eliminado Exitosamente",HttpStatus.OK.value()), HttpStatus.OK);
        }
    }

    @PutMapping(value = "state/") //???
    public ResponseEntity deleteUserUpdate(@RequestBody User user) throws Exception{

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            User updateUser = optionalUser.get();
            updateUser.setRole("Inactivo");

            if(userRepository.save(updateUser)!= null){
                return new ResponseEntity(new Message("Usuario Eliminado Exitosamente",HttpStatus.OK.value()), HttpStatus.OK);
            }else {
                return new ResponseEntity(new Message("Error al eliminar usuario",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity(new Message("Error al eliminar usuario, usuario no existe",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

}
