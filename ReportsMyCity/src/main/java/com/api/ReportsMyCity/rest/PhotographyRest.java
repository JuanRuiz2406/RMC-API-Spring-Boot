package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.PhotographyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("photography")
public class PhotographyRest {

    private final String rootFolder = System.getProperty("user.dir") + "\\uploads\\";
    private final PhotographyRepository photographyRepository;

    public PhotographyRest(PhotographyRepository photographyRepository) {
        this.photographyRepository = photographyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Photography>> getAll() {
        List<Photography> photographs = photographyRepository.findAll();
        if(photographs.isEmpty()) {
            throw new ResourceNotFoundException("No hay imágenes.");
        }
        return ResponseEntity.ok(photographs);
    }

    @PostMapping
    public void create( Photography photo, @RequestParam("File") MultipartFile file) throws ResourceNotFoundException, ApiOkException, Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        System.out.println(rootFolder);
        File myFile = new File(rootFolder+file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos =new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        Set<ConstraintViolation<Photography>> violation = validator.validate(photo);
        for(ConstraintViolation<Photography> violation2 : violation) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }
        photo.setImagePath(rootFolder+file.getOriginalFilename());
        if(photographyRepository.save(photo) != null) {
            throw new ApiOkException("Imagen guardada exitosamente.");
        }else {
            throw new Exception("Error al guardar la imagen.");
        }
    }


}
