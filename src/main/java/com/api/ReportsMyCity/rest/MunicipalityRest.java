package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.MunicipalityRepository;
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
@RequestMapping("municipality")
public class MunicipalityRest {

    private final MunicipalityRepository municipalityRepository;
    private UserRest userRest;

    public MunicipalityRest(MunicipalityRepository municipalityRepository, UserRest userRest) {
        this.municipalityRepository = municipalityRepository;
        this.userRest = userRest;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<Municipality>> getAll() throws ResourceNotFoundException {
        List<Municipality> municipalities = municipalityRepository.findAll();
        return ResponseEntity.ok(municipalities);
    }

    @GetMapping(value = "{municipalityId}")
    public Municipality getById(@PathVariable("municipalityId") int municipalityId){
        return this.municipalityRepository.findById(municipalityId).orElseThrow(()->new ResourceNotFoundException("Error, la municipalidad no existe."));
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody Municipality municipality) throws Exception {

        User newManager = municipality.getManager();
        User savedManager = userRest.createMunicipalityManager(newManager).getBody();
        municipality.setManager(savedManager);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Municipality>> violations = validator.validate(municipality);
        for(ConstraintViolation<Municipality> violation : violations) {
            return new ResponseEntity(new Message(violation.getMessage(),HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        Municipality existingEmail = municipalityRepository.findByEmail(municipality.getEmail());
        Municipality existingName = municipalityRepository.findByName(municipality.getName());

        if(existingEmail !=  null) {
            return new ResponseEntity(new Message("Error, ya existe una municipalidad con este correo",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else if(existingName != null){
            return new ResponseEntity(new Message("Error, otra municipalidad posee este nombre.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else {
            municipalityRepository.save(municipality);
            return new ResponseEntity(new Message("Municipalidad guardada exitosamente",HttpStatus.OK.value()), HttpStatus.OK);
        }

    }

    @PutMapping
    public ResponseEntity update(@RequestBody Municipality municipalityChanges) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Municipality> existingMunicipality = municipalityRepository.findById(municipalityChanges.getId());

        if(existingMunicipality.isPresent()) {
            if(!municipalityChanges.getName().isEmpty()) {
                if(!municipalityChanges.getEmail().isEmpty()) {

                    Municipality updateMunicipality = existingMunicipality.get();
                    updateMunicipality.setName(municipalityChanges.getName());
                    updateMunicipality.setEmail(municipalityChanges.getEmail());
                    updateMunicipality.setAdress(municipalityChanges.getAdress());
                    updateMunicipality.setState(municipalityChanges.getState());
                    updateMunicipality.setTelephone(municipalityChanges.getTelephone());
                    updateMunicipality.setWebSite(municipalityChanges.getWebSite());
                    updateMunicipality.setManager(municipalityChanges.getManager());

                    if(municipalityRepository.save(updateMunicipality) != null) {
                        return new ResponseEntity(new Message("Municipalidad actualizada correctamente",HttpStatus.OK.value()), HttpStatus.OK);
                    }else {
                        return new ResponseEntity(new Message("Error al actualizar la municipalidad.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                    }
                }else {
                    return new ResponseEntity(new Message("Introduzca un correo electronico correcto.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
                }
            }else {
                return new ResponseEntity(new Message("Introduzca un nombre de municipalidad correcto.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }

        }else {
            return new ResponseEntity(new Message("Error, la municipalidad no existe.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "{municipalityId}")
    public ResponseEntity delete(@PathVariable("municipalityId") int municipalityId) throws ResourceNotFoundException, ApiOkException{

        Optional<Municipality> deleteMunicipality = municipalityRepository.findById(municipalityId);

        if(deleteMunicipality.isPresent()) {
            municipalityRepository.deleteById(municipalityId);
            return new ResponseEntity(new Message("La municipalidad se elimino exitosamente", HttpStatus.OK.value()), HttpStatus.OK);
        }else {
            return new ResponseEntity(new Message("Error, Municipalidad no existe", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

}
