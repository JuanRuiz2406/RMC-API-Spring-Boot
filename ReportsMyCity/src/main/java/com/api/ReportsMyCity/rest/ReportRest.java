package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.entity.User;
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
            throw new ResourceNotFoundException("No hay reportes registrados.");
        }
        return ResponseEntity.ok(reports);
    }

    @RequestMapping(value = "{reportId}", method = RequestMethod.GET) // reports/{reportId}
    public Report getReportById(@PathVariable("reportId") int reportId){

        return this.reportRepo.findById(reportId).orElseThrow(()->new ResourceNotFoundException("Error, el reporte no existe."));

    }

    @RequestMapping(value = "/by/{user}", method = RequestMethod.GET) // by/{user}
    public Report getReportByUser(@PathVariable("user") User user){

        return this.reportRepo.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Este usuario no posee reportes."));

    }

    @RequestMapping(value = "/contains/{title}", method = RequestMethod.GET) // contains/{title}
    public Report getReportByTitle(@PathVariable("title") String title){

        return this.reportRepo.findByTitle(title).orElseThrow(()->new ResourceNotFoundException("No hay reportes con título similar."));

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
            throw new ApiOkException("Reporte guardado exitosamente.");
        }else {
            throw new Exception("Error al guardar el reporte.");
        }


    }

    @DeleteMapping(value = "{reportId}")
    public ResponseEntity<Report> deleteReport(@PathVariable("reportId") int reportId) {

        Optional<Report> report = reportRepo.findById(reportId);

        if(!report.isPresent()) {
            throw new ResourceNotFoundException("Digite un reporte correcto");
        }else {
            reportRepo.deleteById(reportId);
            throw new ApiOkException("Reporte eliminado exitosamente.");
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
                throw new ApiOkException("Reporte actualizado exitosamente.");
            }else {
                throw new Exception("Error al actualizar el reporte.");
            }
        }else {
            throw new ResourceNotFoundException("Error, el reporte no existe.");
        }
    }

    @RequestMapping(value = "state/{report}",method = RequestMethod.PUT)
    public void deleteReportUpdate(@PathVariable("report") Report report) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Report> optionalReport = reportRepo.findById(report.getId());

        if(optionalReport.isPresent()) {
            Report updateReport = optionalReport.get();
            updateReport.setDescription(report.getDescription());
            updateReport.setPrivacy(report.getPrivacy());
            updateReport.setState("Eliminado");

            if(reportRepo.save(updateReport) != null) {
                throw new ApiOkException("Reporte eliminado exitosamente.");
            }else {
                throw new Exception("Error al eliminar el reporte.");
            }
        }else {
            throw new ResourceNotFoundException("Error, el reporte no existe.");
        }
    }


}
