package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Coordenates;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.reposity.CoordenatesRepository;
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
@RequestMapping("coordenates")
public class CoordenatesRest {

    @Autowired
    private CoordenatesRepository coordenatesRepo;

    @GetMapping // users/
    public ResponseEntity<List<Coordenates>> getDirections() throws ResourceNotFoundException {

        List<Coordenates> coordenates = coordenatesRepo.findAll();
        if(coordenates.isEmpty()) {
            throw new ResourceNotFoundException("No hay coordenadas guardadas");
        }
        return ResponseEntity.ok(coordenates);

    }

    @RequestMapping(value = "{coordenadesId}") // directions/{directionId}
    public Coordenates getCoordenatesById(@PathVariable("coordenadesId") int coordenadesId) {

        return this.coordenatesRepo.findById(coordenadesId).orElseThrow(()->new ResourceNotFoundException("No existe una coordenada con ese id"));

    }

    @PostMapping // directions {POST}
    public void createCoordenates(@RequestBody Coordenates coordenates) throws ResourceNotFoundException, ApiOkException, Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Coordenates>> violation = validator.validate(coordenates);
        for(ConstraintViolation<Coordenates> violation2 : violation) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }

        if(coordenatesRepo.save(coordenates) != null) {
            throw new ApiOkException("Coordenada guardada exitosamente");
        }else {
            throw new ResourceNotFoundException("Problemas al guardar la coordenada");
        }

    }

    @DeleteMapping(value = "{coordenatesId}") // directions/directionId {DELETE}
    public void deleteCoordenates(@PathVariable("coordenatesId") int coordenatesId) throws ResourceNotFoundException, ApiOkException{

        Optional<Coordenates> dire = coordenatesRepo.findById(coordenatesId);

        if(dire.isPresent()) {
            coordenatesRepo.deleteById(coordenatesId);
            throw new ApiOkException("Se elimino exitosamente");
        }else {
            throw new ResourceNotFoundException("No existe una coordenada con ese id");
        }

    }

    @PutMapping
    public void updateDirection(@RequestBody Coordenates direction) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Coordenates> optionalDirection = coordenatesRepo.findById(direction.getId());
        if (optionalDirection.isPresent()) {
            Coordenates updateDirection = optionalDirection.get();
            updateDirection.setLatitude(direction.getLatitude());
            updateDirection.setLongitude(direction.getLongitude());

            if(updateDirection.getLatitude().isEmpty()){
                throw new ResourceNotFoundException("Error introduzca una latitud");
            }else if(updateDirection.getLongitude().isEmpty()) {
                throw new ResourceNotFoundException("Error introduzca una longitud");
            }else if(coordenatesRepo.save(updateDirection)!=null) {
                throw new ApiOkException("Se actualizo correctamente");
            }else {
                throw new Exception("Error al actualizar, error en el servidor");
            }

        }else {
            throw new ResourceNotFoundException("No existe la coordenada");
        }

    }
}
