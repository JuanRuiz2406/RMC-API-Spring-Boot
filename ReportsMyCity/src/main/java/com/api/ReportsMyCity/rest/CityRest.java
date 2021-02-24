package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.*;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.CityRepository;
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

    public CityRest(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAll() throws ResourceNotFoundException {

        List<City> cities = cityRepository.findAll();
        if(cities.isEmpty()) {
            throw new ResourceNotFoundException("No hay ciudades registradas.");
        }
        return ResponseEntity.ok(cities);

    }

    @GetMapping(value = "/byName/{cityName}")
    public Municipality getMunicipalityByCityName(@PathVariable("cityName") String cityName) throws Exception{

        City city = cityRepository.findByName(cityName);
        if(city != null){
            return city.getMunicipality();
        }else{
            throw new Exception("Esta ciudad no existe en el sistema");
        }

    }

    @PostMapping
    public void create(@RequestBody City city) throws ResourceNotFoundException, ApiOkException, Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<City>> violations = validator.validate(city);
        for(ConstraintViolation<City> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        City existingName = cityRepository.findByName(city.getName());

        if(existingName !=  null) {
            throw new Exception("Error, otra ciudad posee este nombre.");
        }else {
            cityRepository.save(city);
            throw new ApiOkException("Ciudad guardada exitosamente.");
        }

    }

    @PutMapping
    public void update(@RequestBody City cityChanges) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<City> existingCity = cityRepository.findById(cityChanges.getId());
        if (existingCity.isPresent()) {
            City updateCity = existingCity.get();
            updateCity.setName(cityChanges.getName());
            updateCity.setMunicipality(cityChanges.getMunicipality());

            if(updateCity.getName().isEmpty()){
                throw new ResourceNotFoundException("Error, ingrese el nombre.");
            }else if(cityRepository.save(updateCity)!=null) {
                throw new ApiOkException("Ciudad actualizada correctamente.");
            }else {
                throw new Exception("Error al actualizar la ciudad.");
            }

        }else {
            throw new ResourceNotFoundException("Error, esta ciudad no se encuentra en el sistema.");
        }

    }

    @DeleteMapping(value = "{cityId}")
    public ResponseEntity<City> delete(@PathVariable("cityId") int cityId) {

        Optional<City> deleteCity = cityRepository.findById(cityId);

        if(!deleteCity.isPresent()) {
            throw new ResourceNotFoundException("Ingrese una ciudad valida");
        }else {
            cityRepository.deleteById(cityId);
            throw new ApiOkException("Ciudad eliminada exitosamente.");
        }

    }

}
