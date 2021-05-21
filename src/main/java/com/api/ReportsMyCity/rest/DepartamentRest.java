package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.City;
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
    public ResponseEntity<List<DepartamentMunicipality>> getAll(){

        List<DepartamentMunicipality> departaments = departamentRepository.findAll();
        return ResponseEntity.ok(departaments);
    }

    @GetMapping(value = "/byId/{departamentId}")
    public ResponseEntity<DepartamentMunicipality> getById(@PathVariable("departamentId") int departamentId){

        Optional<DepartamentMunicipality> departamentById = departamentRepository.findById(departamentId);

        if(departamentById.isPresent()) {
            return ResponseEntity.ok(departamentById.get());
        }else {
            return new ResponseEntity(new Message("No existe departamento con ese id", HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byMunicipality/{muniId}")
    public ResponseEntity<List<DepartamentMunicipality>> getByMunicipalityId(@PathVariable("muniId")int municipalityId){
        Municipality municipalityFound = municipalityRest.getById(municipalityId);

        List<DepartamentMunicipality> departaments = departamentRepository.findByMunicipality(municipalityFound);

        return ResponseEntity.ok(departaments);
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
        for(ConstraintViolation<DepartamentMunicipality> violation : violations) {
            return new ResponseEntity(new Message(violation.getMessage(),HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        DepartamentMunicipality existingEmail = departamentRepository.findByEmail(departament.getEmail());
        DepartamentMunicipality existingName = departamentRepository.findByName(departament.getName());

        if(existingEmail !=  null) {
            return new ResponseEntity(new Message("Error, ya existe un departamento con este correo",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else if(existingName != null){
            return new ResponseEntity(new Message("Error, otro departamento posee este nombre.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else {
            departamentRepository.save(departament);
            return new ResponseEntity(new Message("Departamento guardado exitosamente",HttpStatus.OK.value()), HttpStatus.OK);
        }

    }

    @PutMapping
    public ResponseEntity<DepartamentMunicipality> update(@RequestBody DepartamentMunicipality departamentChanges){

        Optional<DepartamentMunicipality> existingDepartament = departamentRepository.findById(departamentChanges.getId());

        if(existingDepartament.isPresent()) {
            DepartamentMunicipality updateDepartament = existingDepartament.get();
            updateDepartament.setDescription(departamentChanges.getDescription());
            updateDepartament.setEmail(departamentChanges.getEmail());
            updateDepartament.setName(departamentChanges.getName());
            updateDepartament.setState(departamentChanges.getState());
            updateDepartament.setTelephone(departamentChanges.getTelephone());

            departamentRepository.save(updateDepartament);
            return ResponseEntity.ok(updateDepartament);
        }else {
            return ResponseEntity.notFound().build();

        }
    }

    @DeleteMapping(value = "{departamentId}")
    public ResponseEntity<DepartamentMunicipality> delete(@PathVariable("departamentId") int departamentId){

        departamentRepository.deleteById(departamentId);
        return ResponseEntity.ok(null);
    }

}
