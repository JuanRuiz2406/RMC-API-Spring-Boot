package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.DepartamentMunicipality;
import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.repository.DepartamentRepository;
import com.api.ReportsMyCity.security.dto.Message;
import org.springframework.http.HttpStatus;
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
@RequestMapping("departament")
public class DepartamentRest {

    private final DepartamentRepository departamentRepository;
    private MunicipalityRest municipalityRest;
    private UserRest userRest;

    public DepartamentRest(DepartamentRepository departamentRepository, MunicipalityRest municipalityRest, UserRest userRest) {
        this.departamentRepository = departamentRepository;
        this.municipalityRest = municipalityRest;
        this.userRest = userRest;
    }

    // Faltan Msjs

    @GetMapping
    public ResponseEntity<List<DepartamentMunicipality>> getAll() {

        List<DepartamentMunicipality> departaments = departamentRepository.findAll();
        return ResponseEntity.ok(departaments);
    }

    @GetMapping(value = "/byId/{departamentId}")
    public ResponseEntity<DepartamentMunicipality> getById(@PathVariable("departamentId") int departamentId) {

        Optional<DepartamentMunicipality> departamentById = departamentRepository.findById(departamentId);

        if (departamentById.isPresent()) {
            return ResponseEntity.ok(departamentById.get());
        } else {
            return new ResponseEntity(new Message("No existe departamento con ese id", HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byMunicipality/{muniId}")
    public ResponseEntity<List<DepartamentMunicipality>> getByMunicipalityId(@PathVariable("muniId") int municipalityId) {
        Municipality municipalityFound = municipalityRest.getById(municipalityId);

        List<DepartamentMunicipality> departaments = departamentRepository.findByMunicipality(municipalityFound);

        return ResponseEntity.ok(departaments);
    }

    @CrossOrigin
    @GetMapping(value = "/byUser/{userId}")
    public ResponseEntity<List<DepartamentMunicipality>> getByUserId(@PathVariable("userId") int userId) {
        User userFound = userRest.getById(userId);

        List<DepartamentMunicipality> departament = departamentRepository.findByManager(userFound);

        return ResponseEntity.ok(departament);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody DepartamentMunicipality departament) throws Exception {

        User newManager = departament.getManager();
        User savedManager = userRest.createMunicipalityManager(newManager).getBody();
        departament.setManager(savedManager);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<DepartamentMunicipality>> violations = validator.validate(departament);
        for (ConstraintViolation<DepartamentMunicipality> violation : violations) {
            return new ResponseEntity(new Message(violation.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        DepartamentMunicipality existingEmail = departamentRepository.findByEmail(departament.getEmail());
        DepartamentMunicipality existingName = departamentRepository.findByName(departament.getName());

        if (existingEmail != null) {
            return new ResponseEntity(new Message("Error, ya existe un departamento con este correo", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } else if (existingName != null) {
            return new ResponseEntity(new Message("Error, otro departamento posee este nombre.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } else {
            departamentRepository.save(departament);
            return new ResponseEntity(new Message("Departamento guardado exitosamente", HttpStatus.OK.value()), HttpStatus.OK);
        }

    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity update(@RequestBody DepartamentMunicipality departamentChanges) {

        Optional<DepartamentMunicipality> existingDepartament = departamentRepository.findById(departamentChanges.getId());

        if (existingDepartament.isPresent()) {
            if (!departamentChanges.getName().isEmpty()) {
                if (!departamentChanges.getEmail().isEmpty()) {

                    DepartamentMunicipality updateDepartament = existingDepartament.get();
                    updateDepartament.setDescription(departamentChanges.getDescription());
                    updateDepartament.setEmail(departamentChanges.getEmail());
                    updateDepartament.setName(departamentChanges.getName());
                    updateDepartament.setState(departamentChanges.getState());
                    updateDepartament.setTelephone(departamentChanges.getTelephone());
                    updateDepartament.setManager(departamentChanges.getManager());

                    if (departamentRepository.save(updateDepartament) != null) {
                        return new ResponseEntity(new Message("Departamento actualizado correctamente", HttpStatus.OK.value()), HttpStatus.OK);
                    } else {
                        return new ResponseEntity(new Message("Error al actualizar el departamento.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity(new Message("Introduzca un correo electronico correcto.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(new Message("Introduzca un nombre de departamento correcto.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity(new Message("Error, el departamento no existe.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @DeleteMapping(value = "{departamentId}")
    public ResponseEntity delete(@PathVariable("departamentId") int departamentId) {

        Optional<DepartamentMunicipality> existingDepartament = departamentRepository.findById(departamentId);

        if (existingDepartament.isPresent()) {

            DepartamentMunicipality deleteMunicipality = existingDepartament.get();
            deleteMunicipality.setState("Inactivo");

            if (departamentRepository.save(deleteMunicipality) != null) {
                return new ResponseEntity(new Message("El departamento se eliminó exitosamente", HttpStatus.OK.value()), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Message("Error al eliminar el departamento.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity(new Message("Error, el departamento no existe.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

}
