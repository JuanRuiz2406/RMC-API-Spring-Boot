package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Coordenates;
import com.api.ReportsMyCity.entity.DetailReport;
import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.entity.Report;
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
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("photography")
public class PhotographyRest {

    private final String rootFolder = System.getProperty("user.dir") + "\\uploads\\";
    private final PhotographyRepository photographyRepository;
    private ReportRest reportRest;

    public PhotographyRest(PhotographyRepository photographyRepository) {
        this.photographyRepository = photographyRepository;
        this.reportRest = reportRest;
    }

    @GetMapping
    public ResponseEntity<List<Photography>> getAll() {
        List<Photography> photographs = photographyRepository.findAll();
        return ResponseEntity.ok(photographs);
    }

    @CrossOrigin
    @GetMapping(value = "/byReport/{reportId}")
    public ResponseEntity<List<Photography>> getByReportId(@PathVariable("reportId") int reportId){

        Report report = reportRest.getById(reportId);
        List<Photography> photoByReport = photographyRepository.findByReports(report);
        if(photoByReport.isEmpty()){
            return ResponseEntity.ok(photoByReport);
        }
        return ResponseEntity.ok(photoByReport);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Photography photography) throws Exception {
        System.out.println("entró");
        System.out.println("--------------------------");
        Photography savedPhotography =  photographyRepository.save(photography);
        System.out.println("save: " + savedPhotography.getId());
        if(savedPhotography != null) {
            throw new ApiOkException("Imagen guardada exitosamente con ID:" + savedPhotography.getId());
        }else {
            return new ResponseEntity(new Message("Error al guardar la imagen.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

}
