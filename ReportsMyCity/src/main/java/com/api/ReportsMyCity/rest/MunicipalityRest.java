package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.MunicipalityRepository;
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

    public MunicipalityRest(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    @GetMapping
    public ResponseEntity<List<Municipality>> getAll() throws ResourceNotFoundException {
        List<Municipality> municipalities = municipalityRepository.findAll();
        if(municipalities.isEmpty()) {
            throw new ResourceNotFoundException("No hay municipalidades registradas.");
        }else {
            return ResponseEntity.ok(municipalities);
        }
    }

    @GetMapping(value = "{municipalityId}")
    public Municipality getById(@PathVariable("municipalityId") int municipalityId){
        return this.municipalityRepository.findById(municipalityId).orElseThrow(()->new ResourceNotFoundException("Error, la municipalidad no existe."));
    }

    @PostMapping
    public void create(@RequestBody Municipality municipality) throws ResourceNotFoundException, ApiOkException, Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Municipality>> violations = validator.validate(municipality);
        for(ConstraintViolation<Municipality> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        Municipality existingEmail = municipalityRepository.findByEmail(municipality.getEmail());
        Municipality existingName = municipalityRepository.findByName(municipality.getName());

        if(existingEmail !=  null) {
            throw new Exception("Error, otra municipalidad posee este correo electrónico.");
        }else if(existingName != null){
            throw new Exception("Error, otra municipalidad posee este nombre.");
        }else {
            municipalityRepository.save(municipality);
            throw new ApiOkException("Municipalidad guardada exitosamente.");
        }

    }

    @PutMapping
    public void update(@RequestBody Municipality municipalityChanges) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Municipality> existingMunicipality = municipalityRepository.findById(municipalityChanges.getId());

        if(existingMunicipality.isPresent()) {
            if(!municipalityChanges.getName().isEmpty()) {
                if(!municipalityChanges.getEmail().isEmpty()) {

                    Municipality updateMunicipality = existingMunicipality.get();
                    updateMunicipality.setName(municipalityChanges.getName());
                    updateMunicipality.setEmail(municipalityChanges.getEmail());
                    updateMunicipality.setAdress(municipalityChanges.getAdress());
                    updateMunicipality.setSchedule(municipalityChanges.getSchedule());
                    updateMunicipality.setTelephone(municipalityChanges.getTelephone());
                    updateMunicipality.setWebSite(municipalityChanges.getWebSite());

                    if(municipalityRepository.save(updateMunicipality) != null) {
                        throw new ApiOkException("Municipalidad actualizada exitosamente.");
                    }else {
                        throw new Exception("Error al actualizar la municipalidad.");
                    }
                }else {
                    throw new ResourceNotFoundException("Introduzca un correo electrónico.");
                }
            }else {
                throw new ResourceNotFoundException("Introduzca un nombre válido.");
            }

        }else {
            throw new ResourceNotFoundException("Error, esta municipalidad no existe.");
        }
    }

    @DeleteMapping(value = "{municipalityId}")
    public void delete(@PathVariable("municipalityId") int municipalityId) throws ResourceNotFoundException, ApiOkException{

        Optional<Municipality> deleteMunicipality = municipalityRepository.findById(municipalityId);

        if(deleteMunicipality.isPresent()) {
            municipalityRepository.deleteById(municipalityId);
            throw new ApiOkException("Municipalidad eliminada exitosamente.");
        }else {
            throw new ResourceNotFoundException("Error, esta municipalidad no existe.");
        }
    }

}
