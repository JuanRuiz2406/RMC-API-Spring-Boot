package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.PhotographyRepository;
import com.api.ReportsMyCity.security.dto.Message;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(photographs);
    }

    @PostMapping
    public ResponseEntity create(Photography photo, @RequestParam("Files") List<MultipartFile> files) throws ResourceNotFoundException, ApiOkException, Exception {
        boolean flag = true;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        for ( MultipartFile file : files){
            System.out.println(rootFolder);
            File myFile = new File(rootFolder+file.getOriginalFilename());
            myFile.createNewFile();
            FileOutputStream fos =new FileOutputStream(myFile);
            fos.write(file.getBytes());
            fos.close();
            photo.setImagePath(rootFolder+file.getOriginalFilename());
            photo.setId(photo.getId()+1);
            Set<ConstraintViolation<Photography>> violation = validator.validate(photo);
            for(ConstraintViolation<Photography> violation2 : violation) {
                throw new ApiUnproccessableEntityException(violation2.getMessage());
            }
            if(photographyRepository.save(photo) != null) {
                System.out.println("saved: " + photo.getImagePath());
                flag = true;
            }else {
                flag = false;
            }
        }

        if(flag) {
            throw new ApiOkException("Imagen guardada exitosamente.");
        }else {
            return new ResponseEntity(new Message("Error al guardar la imagen.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }


    }


}
