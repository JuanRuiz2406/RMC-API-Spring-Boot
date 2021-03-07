package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.Coordenates;
import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.entity.User;
import com.api.ReportsMyCity.exceptions.ApiOkException;
import com.api.ReportsMyCity.exceptions.ApiUnproccessableEntityException;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.ReportRepository;
import com.api.ReportsMyCity.rest.UserRest;
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

    @GetMapping
    public ResponseEntity<List<Report>> getAll() throws ResourceNotFoundException {

        List<Report> reports = reportRepository.findAll();
        if(reports.isEmpty()) {
            throw new ResourceNotFoundException("No hay reportes registrados.");
        }
        return ResponseEntity.ok(reports);
    }

    @GetMapping(value = "{reportId}")
    public Report getById(@PathVariable("reportId") int reportId){

        return this.reportRepository.findById(reportId).orElseThrow(()->new ResourceNotFoundException("Error, el reporte no existe."));

    }

    @GetMapping(value = "/byUserIdCard/{userIdCard}")
    public ResponseEntity<List<Report>> getByUserIdCard(@PathVariable("userIdCard") String userIdCard) throws Exception {
        User userFound = userRest.getByIdCard(userIdCard);
        List<Report> userReports = reportRepository.findByUser(userFound);
        if(userReports.isEmpty()){
            throw new ResourceNotFoundException("Este usuario no posee reportes.");
        }
        return ResponseEntity.ok(userReports);
    }

    @GetMapping(value = "/byMunicipality/{muni}")
    public ResponseEntity<List<Report>> getByMunicipality(@PathVariable("muni")Municipality municipality){
        List<Report> reportsMunicipality = reportRepository.findByMunicipality(municipality);
        if(reportsMunicipality.isEmpty()){
            throw new ResourceNotFoundException("Municipalidad no posee reportes");
        }
        return ResponseEntity.ok(reportsMunicipality);
    }

    @GetMapping(value = "/byState/{state}")
    public ResponseEntity<List<Report>> getByState(@PathVariable("state") String state){
        List<Report> reportsByState = reportRepository.findByState(state);
        if(reportsByState.isEmpty()){
            throw new ResourceNotFoundException("No hay reportes que posean el estado: " + state);
        }
        return ResponseEntity.ok(reportsByState);
    }

    @PostMapping(value = "/city/{cityName}")
    public void create(@RequestBody Report report, @PathVariable("cityName") String cityName) throws ResourceNotFoundException, ApiOkException, Exception{

        Coordenates newCoordenates = report.getCoordenates();
        Coordenates savedCoordenates = coordenatesRest.create(newCoordenates);
        report.setCoordenates(savedCoordenates);

        Municipality municipalityFound = cityRest.getMunicipalityByCityName(cityName);
        report.setMunicipality(municipalityFound);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Report>> violations = validator.validate(report);
        for(ConstraintViolation<Report> violation : violations) {
            throw new ApiUnproccessableEntityException(violation.getMessage());
        }

        if(reportRepository.save(report) != null) {
            throw new ApiOkException("Reporte guardado exitosamente.");
        }else {
            throw new Exception("Error al guardar el reporte.");
        }

    }

    @PutMapping
    public void update(@RequestBody Report reportChanges) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Report> existingReport = reportRepository.findById(reportChanges.getId());

        if(existingReport.isPresent()) {
            Report updateReport = existingReport.get();
            updateReport.setDescription(reportChanges.getDescription());
            updateReport.setPrivacy(reportChanges.getPrivacy());
            updateReport.setState(reportChanges.getState());

            if(reportRepository.save(updateReport) != null) {
                throw new ApiOkException("Reporte actualizado exitosamente.");
            }else {
                throw new Exception("Error al actualizar el reporte.");
            }
        }else {
            throw new ResourceNotFoundException("Error, el reporte no existe.");
        }
    }

    @DeleteMapping(value = "{reportId}")
    public ResponseEntity<Report> delete(@PathVariable("reportId") int reportId) {

        Optional<Report> deleteReport = reportRepository.findById(reportId);

        if(!deleteReport.isPresent()) {
            throw new ResourceNotFoundException("Digite un reporte correcto");
        }else {
            reportRepository.deleteById(reportId);
            throw new ApiOkException("Reporte eliminado exitosamente.");
        }

    }

    @PutMapping(value = "state/{report}") //???
    public void deleteReportUpdate(@PathVariable("report") Report report) throws ResourceNotFoundException, ApiOkException, Exception{

        Optional<Report> optionalReport = reportRepository.findById(report.getId());

        if(optionalReport.isPresent()) {
            Report updateReport = optionalReport.get();
            updateReport.setDescription(report.getDescription());
            updateReport.setPrivacy(report.getPrivacy());
            updateReport.setState("Eliminado");

            if(reportRepository.save(updateReport) != null) {
                throw new ApiOkException("Reporte eliminado exitosamente.");
            }else {
                throw new Exception("Error al eliminar el reporte.");
            }
        }else {
            throw new ResourceNotFoundException("Error, el reporte no existe.");
        }
    }

}
