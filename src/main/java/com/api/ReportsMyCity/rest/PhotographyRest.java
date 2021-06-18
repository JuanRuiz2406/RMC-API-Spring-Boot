package com.api.ReportsMyCity.rest;


import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.exceptions.ApiOkException;

import com.api.ReportsMyCity.repository.PhotographyRepository;
import com.api.ReportsMyCity.security.dto.Message;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("photography")
public class PhotographyRest {

    private final PhotographyRepository photographyRepository;
    private ReportRest reportRest;

    public PhotographyRest(PhotographyRepository photographyRepository, ReportRest reportRest) {
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
        List<Photography> photoByReport = photographyRepository.findByReport(report);
        if(photoByReport.isEmpty()){
            return ResponseEntity.ok(photoByReport);
        }
        return ResponseEntity.ok(photoByReport);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Photography photography) throws Exception {
        Photography savedPhotography =  photographyRepository.save(photography);
        if(savedPhotography != null) {
            throw new ApiOkException("Imagen guardada exitosamente con ID:" + savedPhotography.getId());
        }else {
            return new ResponseEntity(new Message("Error al guardar la imagen.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

}
