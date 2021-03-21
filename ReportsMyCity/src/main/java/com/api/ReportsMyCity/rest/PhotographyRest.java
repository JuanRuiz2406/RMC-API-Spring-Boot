package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.PhotographyRepository;
import com.api.ReportsMyCity.security.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("photography")
public class PhotographyRest {

    private final PhotographyRepository photographyRepository;

    public PhotographyRest(PhotographyRepository photographyRepository) {
        this.photographyRepository = photographyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Photography>> getAll() {
        List<Photography> photographs = photographyRepository.findAll();
        return ResponseEntity.ok(photographs);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Photography photo, MultipartFile File) throws ResourceNotFoundException, ApiOkException, Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Photography>> violation = validator.validate(photo);
        for(ConstraintViolation<Photography> violation2 : violation) {
            return new ResponseEntity(new Message(violation2.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(photographyRepository.save(photo) != null) {
            return new ResponseEntity(new Message("Imagen guardada exitosamente."), HttpStatus.OK);
        }else {
            return new ResponseEntity(new Message("Error al guardar la imagen."), HttpStatus.BAD_REQUEST);
        }
    }
}
