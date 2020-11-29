package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepo;

    @GetMapping // user/
    public ResponseEntity<List<User>> getUsers() throws ResourceNotFoundException{
        List<User> users = userRepo.findAll();
        if(users.isEmpty()){
            throw new ResourceNotFoundException("No hay usuarios registrados");
        }
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "{userId}") //user/{userId}
    public User getUserById(@PathVariable("userId") int userId){
        return this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("No existe un usuario registrado con ese id"));
    }

    @PostMapping // user
    public void createUser(@RequestBody User user) throws ApiOkException, Exception, ApiUnproccessableEntityException{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for(ConstraintViolation<User> violation2 : violations) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }

        User optionalUser = userRepo.findByEmail(user.getEmail());

        if (optionalUser != null) {
            String mailString = optionalUser.getEmail();
            if (!mailString.equals(user.getEmail())) {
                userRepo.save(user);
                throw new ApiOkException("El usuario se guardo exitosamente");
            }else {
                throw new Exception("Ya existe un usuario con ese correo");
            }
        }else {
            userRepo.save(user);
            throw new ApiOkException("El usuario se guardo exitosamente");
        }
    }

    @DeleteMapping(value = "{userId}") // users/userId {DELETE}
    public void deleteUser(@PathVariable("userId") int userId)  throws ResourceNotFoundException, ApiOkException{

        Optional<User> user = userRepo.findById(userId);

        if(!user.isPresent()) {
            throw new ResourceNotFoundException("Digite un usuario correcto");
        }else {
            userRepo.deleteById(userId);
            throw new ApiOkException("Se elimino exitosamente");
        }
    }

    @PutMapping
    public void updateUser(@RequestBody User user) throws Exception{

        Optional<User> optionalUser = userRepo.findById(user.getId());
        if (optionalUser.isPresent()) {
            User updateUser = optionalUser.get();
            updateUser.setName(user.getName());
            updateUser.setLastname(user.getLastname());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(user.getPassword());
            updateUser.setRole("user");
            updateUser.setDirection(user.getDirection());

            if(userRepo.save(updateUser)!= null){
                throw new ApiOkException("El usuario se actualizo exitosamente");
            }else {
                throw new Exception("Error al actualizar el usuario");
            }
        }else {
            throw new ResourceNotFoundException("El usario no existe");
        }

    }


}
