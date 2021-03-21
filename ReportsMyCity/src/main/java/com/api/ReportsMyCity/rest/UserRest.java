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

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserRest(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            return new ResponseEntity(new Message("No se encontro el usuario"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> getByEmail(String userEmail){
        User user = userRepository.findByEmail(userEmail);
        if (user != null)
            return new ResponseEntity(user,HttpStatus.OK);
        else
            return new ResponseEntity(new Message("El usuario no existe"), HttpStatus.NO_CONTENT);
    }

    public boolean getExistsByEmail(String userEmail){
        return userRepository.existsByEmail(userEmail);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) throws Exception{

        user.setRole("user");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for(ConstraintViolation<User> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        User UserWhitExistingEmail = userRepository.findByEmail(user.getEmail());

        if (UserWhitExistingEmail != null) { // Si != null entra para volver a validar... ?
            String existingEmail = UserWhitExistingEmail.getEmail();
            if (!existingEmail.equals(user.getEmail())) { // Creo que esta validación está de más. * ver como esta en municipality
                userRepository.save(user);
                return new ResponseEntity(new Message("Usuario guardado"), HttpStatus.CREATED);
            }else {
                return new ResponseEntity(new Message("Error al guardar, ya existe el usuario"), HttpStatus.BAD_REQUEST);
            }
        }else { // Si == nulo guardar, una de las 2 esta sobrando
            userRepository.save(user);
            return new ResponseEntity(new Message("Usuario guardado"), HttpStatus.CREATED);
        }
    }

    @PostMapping(value = "/admin")
    public ResponseEntity createAdmin(@RequestBody User user) throws Exception{
        user.setRole("admin");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for(ConstraintViolation<User> violation2 : violations) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }

        User optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalUser != null) { //mismo caso de antes
            String mailString = optionalUser.getEmail();
            if (!mailString.equals(user.getEmail())) {
                userRepository.save(user);
                return new ResponseEntity(new Message("Usuario guardado"), HttpStatus.CREATED);
            }else {
                return new ResponseEntity(new Message("Error al guardar, ya existe el usuario"), HttpStatus.BAD_REQUEST);
            }
        }else {
            userRepository.save(user);
            return new ResponseEntity(new Message("Usuario guardado"), HttpStatus.CREATED);
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
                return new ResponseEntity(new Message("Usuario actualizado"), HttpStatus.CREATED);
            }else {
                return new ResponseEntity(new Message("Error aL actualizar el usuario"), HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity(new Message("Usuario No existe"), HttpStatus.NO_CONTENT);
        }

    }

    @DeleteMapping(value = "{userId}") // users/userId {DELETE}
    public ResponseEntity delete(@PathVariable("userId") int userId){

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            return new ResponseEntity(new Message("Error al eliminar usuario"), HttpStatus.BAD_REQUEST);
        }else {
            userRepository.deleteById(userId);
            return new ResponseEntity(new Message("Usuario Eliminado Exitosamente"), HttpStatus.OK);
        }
    }

    @PutMapping(value = "state/") //???
    public ResponseEntity deleteUserUpdate(@RequestBody User user) throws Exception{

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            User updateUser = optionalUser.get();
            updateUser.setRole("Inactivo");

            if(userRepository.save(updateUser)!= null){
                return new ResponseEntity(new Message("Usuario Eliminado Exitosamente"), HttpStatus.OK);
            }else {
                return new ResponseEntity(new Message("Error al eliminar usuario"), HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity(new Message("Error al eliminar usuario, usuario no existe"), HttpStatus.BAD_REQUEST);
        }
    }

}
