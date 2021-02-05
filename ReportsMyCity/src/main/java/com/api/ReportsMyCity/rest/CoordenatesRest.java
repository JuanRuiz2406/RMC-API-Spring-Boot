package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Coordenates;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.CoordenatesRepository;
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
    public ResponseEntity<List<Coordenates>> getAll() throws ResourceNotFoundException {

        List<Coordenates> coordenates = coordenatesRepository.findAll();
        if(coordenates.isEmpty()) {
            throw new ResourceNotFoundException("No hay ubicaciones registradas.");
        }
        return ResponseEntity.ok(coordenates);

    }

    @GetMapping(value = "{coordenadesId}")
    public Coordenates getById(@PathVariable("coordenadesId") int coordenadesId) {

        return this.coordenatesRepository.findById(coordenadesId).orElseThrow(()->new ResourceNotFoundException("Error, esta ubicación no existe."));

    }

    @PostMapping
    public void create(@RequestBody Coordenates coordenates) throws ResourceNotFoundException, ApiOkException, Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Coordenates>> violations = validator.validate(coordenates);
        for(ConstraintViolation<Coordenates> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        if(coordenatesRepository.save(coordenates) != null) {
            throw new ApiOkException("Ubicación guardada exitosamente.");
        }else {
            throw new ResourceNotFoundException("Error al guardar la ubicación.");
        }

    }

    @PutMapping
    public void update(@RequestBody Coordenates coordenatesChanges) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Coordenates> existingCoordenates = coordenatesRepository.findById(coordenatesChanges.getId());
        if (existingCoordenates.isPresent()) {
            Coordenates updateCoordenates = existingCoordenates.get();
            updateCoordenates.setLatitude(coordenatesChanges.getLatitude());
            updateCoordenates.setLongitude(coordenatesChanges.getLongitude());

            if(updateCoordenates.getLatitude().isEmpty()){
                throw new ResourceNotFoundException("Error, introduzca la latitud.");
            }else if(updateCoordenates.getLongitude().isEmpty()) {
                throw new ResourceNotFoundException("Error, introduzca la longitud.");
            }else if(coordenatesRepository.save(updateCoordenates)!=null) {
                throw new ApiOkException("Ubicación actualizada correctamente.");
            }else {
                throw new Exception("Error al actualizar la ubicación.");
            }

        }else {
            throw new ResourceNotFoundException("Error, esta ubicación no existe.");
        }

    }

}
