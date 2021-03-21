package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Coordenates;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.CoordenatesRepository;
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
@RequestMapping("coordenates")
public class CoordenatesRest {

    private final CoordenatesRepository coordenatesRepository;

    public CoordenatesRest(CoordenatesRepository coordenatesRepository) {
        this.coordenatesRepository = coordenatesRepository;
    }

    @GetMapping
    public ResponseEntity<List<Coordenates>> getAll() {

        List<Coordenates> coordenates = coordenatesRepository.findAll();
        return ResponseEntity.ok(coordenates);

    }

    @GetMapping(value = "{coordenadesId}")
    public Coordenates getById(@PathVariable("coordenadesId") int coordenadesId) {

        return this.coordenatesRepository.findById(coordenadesId).orElseThrow(()->new ResourceNotFoundException("Error, esta ubicación no existe."));

    }

    @PostMapping
    public ResponseEntity<Coordenates> create(@RequestBody Coordenates coordenates) throws Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Coordenates>> violations = validator.validate(coordenates);
        for(ConstraintViolation<Coordenates> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        Coordenates savedCoordenates = coordenatesRepository.save(coordenates);

        if(savedCoordenates != null) {
            return ResponseEntity.ok(savedCoordenates);
        }else {
            return new ResponseEntity(new Message("Error al guardar las coordenadas"), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping
    public ResponseEntity update(@RequestBody Coordenates coordenatesChanges){

        Optional<Coordenates> existingCoordenates = coordenatesRepository.findById(coordenatesChanges.getId());
        if (existingCoordenates.isPresent()) {
            Coordenates updateCoordenates = existingCoordenates.get();
            updateCoordenates.setLatitude(coordenatesChanges.getLatitude());
            updateCoordenates.setLongitude(coordenatesChanges.getLongitude());

            if(updateCoordenates.getLatitude().isEmpty()){
                return new ResponseEntity(new Message("Error, introduzca la latitud."), HttpStatus.BAD_REQUEST);
            }else if(updateCoordenates.getLongitude().isEmpty()) {
                return new ResponseEntity(new Message("Error, introduzca la longitud."), HttpStatus.BAD_REQUEST);
            }else if(coordenatesRepository.save(updateCoordenates)!=null) {
                return new ResponseEntity(new Message("Ubicacion actualizada correctamente."), HttpStatus.OK);
            }else {
                return new ResponseEntity(new Message("Error al actualizar la ubicacion."), HttpStatus.BAD_REQUEST);
            }

        }else {
            return new ResponseEntity(new Message("Error, esta ubicación no existe."), HttpStatus.BAD_REQUEST);
        }

    }

}
