package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.reposity.PhotographyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("photography")
public class photographyRest {

    @Autowired
    private PhotographyRepository photoRepo;

    @GetMapping
    public ResponseEntity<List<Photography>> getPhotographys() {
        List<Photography> photos = photoRepo.findAll();
        if(photos.isEmpty()) {
            throw new ResourceNotFoundException("No hay imagenes");
        }
        return ResponseEntity.ok(photos);
    }

    public void createPhoto(@RequestBody Photography photo) throws ResourceNotFoundException, ApiOkException, Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Photography>> violation = validator.validate(photo);
        for(ConstraintViolation<Photography> violation2 : violation) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }

        if(photoRepo.save(photo) != null) {
            throw new ApiOkException("Se guardo la Foto");
        }else {
            throw new Exception("Error al guardar la foto, problemos internos");
        }
    }
}
