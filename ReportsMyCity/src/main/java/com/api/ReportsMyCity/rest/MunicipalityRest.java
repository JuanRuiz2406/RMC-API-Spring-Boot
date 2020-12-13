package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.reposity.MunicipalityRepository;
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
@RequestMapping("municipality")
public class MunicipalityRest {

    @Autowired
    private MunicipalityRepository municipalityRepo;

    @GetMapping //municipality
    public ResponseEntity<List<Municipality>> getMunicipalitys() throws ResourceNotFoundException {
        List<Municipality> municipalitys = municipalityRepo.findAll();
        if(municipalitys.isEmpty()) {
            throw new ResourceNotFoundException("No hay municipalidades registradas.");
        }else {
            return ResponseEntity.ok(municipalitys);
        }
    }

    @RequestMapping(value = "{muniId}", method = RequestMethod.GET) //municipalitys/{muniId}
    public Municipality getMuniById(@PathVariable("muniId") int muniId){
        return this.municipalityRepo.findById(muniId).orElseThrow(()->new ResourceNotFoundException("Error, la municipalidad no existe."));
    }

    @PostMapping
    public void createMunicipality(@RequestBody Municipality muni) throws ResourceNotFoundException, ApiOkException, Exception{


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Municipality>> violation = validator.validate(muni);
        for(ConstraintViolation<Municipality> violation2 : violation) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }

        Municipality muniTemp = municipalityRepo.findByEmail(muni.getEmail());
        Municipality muniTempName = municipalityRepo.findByName(muni.getName());

        //rules
        if(muniTemp !=  null) {
            throw new Exception("Error, otra municipalidad posee este correo electrónico.");
        }else if(muniTempName != null){
            throw new Exception("Error, otra municipalidad posee este nombre.");
        }else {
            municipalityRepo.save(muni);
            throw new ApiOkException("Municipalidad guardada exitosamente.");
        }

    }

    @DeleteMapping(value = "{muniId}")
    public void deleteMunicipality(@PathVariable("muniId") int muniId) throws ResourceNotFoundException, ApiOkException{

        Optional<Municipality> optionalMunicipality = municipalityRepo.findById(muniId);

        if(optionalMunicipality.isPresent()) {
            municipalityRepo.deleteById(muniId);
            throw new ApiOkException("Municipalidad eliminada exitosamente.");
        }else {
            throw new ResourceNotFoundException("Error, esta municipalidad no existe.");
        }
    }

    @PutMapping
    public void updateMunicipality(@RequestBody Municipality muni) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Municipality> optionalMunicipality = municipalityRepo.findById(muni.getId());

        if(optionalMunicipality.isPresent()) {
            Municipality updateMunicipality = optionalMunicipality.get();
            updateMunicipality.setName(muni.getName());
            updateMunicipality.setEmail(muni.getEmail());
            updateMunicipality.setAdress(muni.getAdress());
            updateMunicipality.setSchedule(muni.getSchedule());
            updateMunicipality.setTelephone(muni.getTelephone());
            updateMunicipality.setWebSite(muni.getWebSite());

            if(!updateMunicipality.getName().isEmpty()) {
                if(!updateMunicipality.getEmail().isEmpty()) {
                    if(municipalityRepo.save(updateMunicipality) != null) {
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
}
