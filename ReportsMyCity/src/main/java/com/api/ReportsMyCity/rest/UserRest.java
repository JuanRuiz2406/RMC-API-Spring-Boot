package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.UserRepository;
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

    private final UserRepository userRepository;

    public UserRest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() throws ResourceNotFoundException{
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new ResourceNotFoundException("No hay usuarios registrados.");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "{userId}")
    public User getById(@PathVariable("userId") int userId){
        return this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Error, este usuario no existe."));
    }

    @GetMapping(value = "/byIdCard/{userIdCard}")
    public User getByIdCard(@PathVariable("userIdCard") String userIdCard) throws Exception{
        User userTemp = userRepository.findByIdCard(userIdCard);
        if(userTemp != null){
            throw new Exception("No existe el usuario");
        }else{
            return userTemp;
        }
    }

    @PostMapping
    public void createUser(@RequestBody User user) throws ApiOkException, Exception, ApiUnproccessableEntityException{

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
                throw new ApiOkException("Usuario guardado exitosamente.");
            }else {
                throw new Exception("Error, otro usuario posee este correo electrónico.");
            }
        }else { // Si == nulo guardar, una de las 2 esta sobrando
            userRepository.save(user);
            throw new ApiOkException("Usuario guardado exitosamente.");
        }
    }

    @PostMapping(value = "/admin")
    public void createAdmin(@RequestBody User user) throws ApiOkException, Exception, ResourceNotFoundException{
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
                throw new ApiOkException("Usuario guardado exitosamente.");
            }else {
                throw new Exception("Error, otro usuario posee este correo electrónico.");
            }
        }else {
            userRepository.save(user);
            throw new ApiOkException("Usuario guardado exitosamente.");
        }
    }

    @PutMapping
    public void update(@RequestBody User user) throws ApiOkException, ResourceNotFoundException, Exception{

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            User updateUser = optionalUser.get();
            updateUser.setName(user.getName());
            updateUser.setLastname(user.getLastname());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(user.getPassword());
            updateUser.setRole("user");
            updateUser.setDirection(user.getDirection());

            if(userRepository.save(updateUser)!= null){
                throw new ApiOkException("Usuario actualizado exitosamente.");
            }else {
                throw new Exception("Error al actualizar el usuario.");
            }
        }else {
            throw new ResourceNotFoundException("Error, este usuario no existe.");
        }

    }

    @DeleteMapping(value = "{userId}") // users/userId {DELETE}
    public void delete(@PathVariable("userId") int userId)  throws ResourceNotFoundException, ApiOkException{

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            throw new ResourceNotFoundException("Error, el usuario no ha sido encontrado.");
        }else {
            userRepository.deleteById(userId);
            throw new ApiOkException("Usuario eliminado exitosamente.");
        }
    }

    @PutMapping(value = "state/") //???
    public void deleteUserUpdate(@RequestBody User user) throws ApiOkException, ResourceNotFoundException, Exception{

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            User updateUser = optionalUser.get();
            updateUser.setRole("Inactivo");

            if(userRepository.save(updateUser)!= null){
                throw new ApiOkException("Eliminado correctamente");
            }else {
                throw new Exception("Error al eliminar el usuario");
            }
        }else{
            throw new ResourceNotFoundException("Error, este usuario no exite");
        }
    }

}
