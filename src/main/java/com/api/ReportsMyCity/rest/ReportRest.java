package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.*;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.ReportRepository;
import com.api.ReportsMyCity.rest.UserRest;
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
@RequestMapping("report")
public class ReportRest {

    private final ReportRepository reportRepository;
    private CoordenatesRest coordenatesRest;
    private CityRest cityRest;
    private PhotographyRest photographyRest;
    private UserRest userRest;

    public ReportRest(ReportRepository reportRepository, CoordenatesRest coordenatesRest, CityRest cityRest, UserRest userRest) {
        this.reportRepository = reportRepository;
        this.coordenatesRest = coordenatesRest;
        this.cityRest = cityRest;
        this.userRest = userRest;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<Report>> getAll() {
        List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports);
    }

    @CrossOrigin
    @GetMapping(value = "/byPublicPrivacyAndVisibleState")
    public ResponseEntity<List<Report>> getByPublicPrivacyAndVisibleState() {
        List<Report> reportsToShow = reportRepository.findByStateAndPrivacy("Aceptado", "Público");
        return ResponseEntity.ok(reportsToShow);
    }

    @GetMapping(value = "{reportId}")
    public Report getById(@PathVariable("reportId") int reportId){
        return this.reportRepository.findById(reportId).orElseThrow(()->new ResourceNotFoundException("Error, el reporte no existe."));
    }

    @GetMapping(value = "/byUserEmail/{email}")
    public ResponseEntity<List<Report>> getByUserEmail(@PathVariable("email") String email) throws Exception {
        User userFound = userRest.getByEmail(email).getBody();
        List<Report> userReports = reportRepository.findByUser(userFound);
        if(userReports.isEmpty()){
            return new ResponseEntity(new Message("Este Usuario no ha Realizado Reportes", HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userReports);
    }

    @GetMapping(value = "/byMunicipality/{muni}")
    public ResponseEntity<List<Report>> getByMunicipality(@PathVariable("muni")Municipality municipality){
        List<Report> reportsMunicipality = reportRepository.findByMunicipality(municipality);
        if(reportsMunicipality.isEmpty()){
            return new ResponseEntity(new Message("Municipalidad no tiene reportes",HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(reportsMunicipality);
    }

    @GetMapping(value = "/byState/{state}")
    public ResponseEntity<List<Report>> getByState(@PathVariable("state") String state){
        List<Report> reportsByState = reportRepository.findByState(state);
        if(reportsByState.isEmpty()){
            return new ResponseEntity(new Message("No Existen reportes con el estado: " + state, HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(reportsByState);
    }

    @CrossOrigin
    @PostMapping(value = "/city/{cityName}")
    public ResponseEntity create(@RequestBody Report report, @PathVariable("cityName") String cityName) throws  Exception{

        Coordenates newCoordenates = report.getCoordenates();
        Coordenates savedCoordenates = coordenatesRest.create(newCoordenates).getBody();
        report.setCoordenates(savedCoordenates);
        System.out.println(report.getImgURL());
        Municipality municipalityFound = cityRest.getMunicipalityByCityName(cityName).getBody();
        report.setMunicipality(municipalityFound);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Report>> violations = validator.validate(report);
        for(ConstraintViolation<Report> violation : violations) {
            return new ResponseEntity(new Message(violation.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY.value()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Report reportSaved = reportRepository.save(report);
        if( reportSaved!= null) {
            return new ResponseEntity(new Message("Reporte guardado exitosamente con ID:" + reportSaved.getId(),HttpStatus.OK.value()), HttpStatus.OK);
        }else {
            return new ResponseEntity(new Message("Error al crear reporte",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity update(@RequestBody Report reportChanges){

        Optional<Report> existingReport = reportRepository.findById(reportChanges.getId());

        if(existingReport.isPresent()) {
            Report updateReport = existingReport.get();
            updateReport.setDescription(reportChanges.getDescription());
            updateReport.setPrivacy(reportChanges.getPrivacy());
            updateReport.setState(reportChanges.getState());

            if(reportRepository.save(updateReport) != null) {
                return new ResponseEntity(new Message("Reporte actualizado exitosamente",HttpStatus.OK.value()), HttpStatus.OK);
            }else {
                return new ResponseEntity(new Message("Error al actualizar reporte",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity(new Message("El reporte no existe",HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "{reportId}")
    public ResponseEntity<Report> delete(@PathVariable("reportId") int reportId) {

        Optional<Report> existingReport = reportRepository.findById(reportId);

        if (existingReport.isPresent()) {
            Report deleteReport = existingReport.get();
            deleteReport.setState("Eliminado");

            if (reportRepository.save(deleteReport) != null) {
                return new ResponseEntity(new Message("Reporte eliminado exitosamente", HttpStatus.OK.value()), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Message("Error al eliminar el reporte", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(new Message("El reporte no existe", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

    }

}
