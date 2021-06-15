package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.*;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.CityRepository;
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
@RequestMapping("city")
public class CityRest {

    private final CityRepository cityRepository;
    private MunicipalityRest municipalityRest;

    public CityRest(CityRepository cityRepository, MunicipalityRest municipalityRest) {
        this.cityRepository = cityRepository;
        this.municipalityRest = municipalityRest;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAll() throws ResourceNotFoundException {
        List<City> cities = cityRepository.findAll();
        return ResponseEntity.ok(cities);
    }

    @GetMapping(value = "/byName/{cityName}")
    public ResponseEntity<Municipality> getMunicipalityByCityName(@PathVariable("cityName") String cityName) throws Exception{

        City city = cityRepository.findByName(cityName);
        if(city != null){
            return ResponseEntity.ok(city.getMunicipality());
        }else{
            return new ResponseEntity(new Message("Esta ciudad no existe en el sistema",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @GetMapping(value = "/byMunicipality/{muniId}")
    public ResponseEntity<List<City>> getByMunicipalityId(@PathVariable("muniId")int municipalityId){
        Municipality municipalityFound = municipalityRest.getById(municipalityId);

        List<City> citiesMunicipality = cityRepository.findByMunicipality(municipalityFound);

        return ResponseEntity.ok(citiesMunicipality);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody City city) throws ResourceNotFoundException, ApiOkException, Exception{

        Municipality municipalityFound = municipalityRest.getById(city.getMunicipality().getId());
        city.setMunicipality(municipalityFound);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<City>> violations = validator.validate(city);
        for(ConstraintViolation<City> violation : violations) {
            return new ResponseEntity(new Message(violation.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        City existingName = cityRepository.findByName(city.getName());

        if(existingName !=  null) {
            return new ResponseEntity(new Message("Error, otra ciudad posee este nombre.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else {
            cityRepository.save(city);
            return new ResponseEntity(new Message("Ciudad guardada exitosamente.", HttpStatus.OK.value()), HttpStatus.OK);
        }

    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity update(@RequestBody City cityChanges){

        Optional<City> existingCity = cityRepository.findById(cityChanges.getId());
        if (existingCity.isPresent()) {
            City updateCity = existingCity.get();
            updateCity.setName(cityChanges.getName());
            updateCity.setMunicipality(cityChanges.getMunicipality());

            if(updateCity.getName().isEmpty()){
                return new ResponseEntity(new Message("Error, ingrese el nombre.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }else if(cityRepository.save(updateCity)!=null) {
                return new ResponseEntity(new Message("Ciudad actualizada correctamente.", HttpStatus.OK.value()), HttpStatus.OK);
            }else {
                return new ResponseEntity(new Message("Error al actualizar la ciudad.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }

        }else {
            return new ResponseEntity(new Message("Error, esta ciudad no se encuentra en el sistema.",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @DeleteMapping(value = "{cityId}")
    public ResponseEntity<City> delete(@PathVariable("cityId") int cityId) {

        Optional<City> deleteCity = cityRepository.findById(cityId);

        if(!deleteCity.isPresent()) {
            return new ResponseEntity(new Message("Ingrese una ciudad valid", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }else {
            cityRepository.deleteById(cityId);
            return new ResponseEntity(new Message("Ciudad eliminada exitosamente", HttpStatus.OK.value()), HttpStatus.OK);
        }

    }

}
