package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.reposity.ReportRepository;
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
@RequestMapping("report")
public class ReportRest {

    @Autowired
    private ReportRepository reportRepo;

    @GetMapping // report/
    public ResponseEntity<List<Report>> getReport() throws ResourceNotFoundException {

        List<Report> reports = reportRepo.findAll();
        if(reports.isEmpty()) {
            throw new ResourceNotFoundException("No hay reportes");
        }
        return ResponseEntity.ok(reports);
    }

    @RequestMapping(value = "{reportId}") // reports/{reportID}
    public Report getReportById(@PathVariable("reportId") int reportId){

        return this.reportRepo.findById(reportId).orElseThrow(()->new ResourceNotFoundException("Reporte no existe o no fue encontrado"));

    }

    @PostMapping
    public void createReport(@RequestBody Report report) throws ResourceNotFoundException, ApiOkException, Exception{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Report>> violation = validator.validate(report);
        for(ConstraintViolation<Report> violation2 : violation) {
            throw new ApiUnproccessableEntityException(violation2.getMessage());
        }

        if(reportRepo.save(report) != null) {
            throw new ApiOkException("Se guardo el reporte");
        }else {
            throw new Exception("Error al guardar reporte, problemos internos");
        }


    }

    @DeleteMapping(value = "{reportId}")
    public ResponseEntity<Report> deleteReport(@PathVariable("reportId") int reportId) {

        Optional<Report> report = reportRepo.findById(reportId);

        if(!report.isPresent()) {
            throw new ResourceNotFoundException("Digite un reporte correcto");
        }else {
            reportRepo.deleteById(reportId);
            throw new ApiOkException("Se elimino exitosamente");
        }

    }

    @PutMapping
    public void updateReport(@RequestBody Report report) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Report> optionalReport = reportRepo.findById(report.getId());

        if(optionalReport.isPresent()) {
            Report updateReport = optionalReport.get();
            updateReport.setDescription(report.getDescription());
            updateReport.setPrivacy(report.getPrivacy());
            updateReport.setState(report.getState());

            if(reportRepo.save(updateReport) != null) {
                throw new ApiOkException("El reporte se actualizo exitosamente");
            }else {
                throw new Exception("Error al actualizar el reporte");
            }
        }else {
            throw new ResourceNotFoundException("El reporte no existe");
        }
    }
}
