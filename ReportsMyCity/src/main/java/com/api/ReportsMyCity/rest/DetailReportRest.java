package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.DetailReport;
import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.reposity.DetailReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("detailReport")
public class DetailReportRest {

    private final DetailReportRepository detailReportRepository;

    public DetailReportRest(DetailReportRepository detailReportRepository) {
        this.detailReportRepository = detailReportRepository;
    }

    @GetMapping
    public ResponseEntity<List<DetailReport>> getAll() {

        List<DetailReport> details = detailReportRepository.findAll();
        return ResponseEntity.ok(details);
    }

    @GetMapping(value = "{detailReportId}")
    public ResponseEntity<DetailReport> getById(@PathVariable("detailReportId") int detailReportId){

        Optional<DetailReport> DetailById = detailReportRepository.findById(detailReportId);

        if(DetailById.isPresent()) {
            return ResponseEntity.ok(DetailById.get());
        }else {
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping(value = "/byReport/{report}")
    public ResponseEntity<List<DetailReport>> getByReport(@PathVariable("report") Report report){

        List<DetailReport> detailsByReport = detailReportRepository.findByReport(report);
        if(detailsByReport.isEmpty()){
            throw new ResourceNotFoundException("No existen detalles");
        }
        return ResponseEntity.ok(detailsByReport);
    }

    @PostMapping // No tiene msjs ni validaciones
    public ResponseEntity<DetailReport> create(@RequestBody DetailReport detailReport){

        DetailReport newDetail = detailReportRepository.save(detailReport);
        return ResponseEntity.ok(newDetail);
    }

    @DeleteMapping(value = "{detailReportId}") // No tiene msjs ni validaciones
    public ResponseEntity<DetailReport> delete(@PathVariable("detailReportId") int detailReportId) {

        detailReportRepository.deleteById(detailReportId);
        return ResponseEntity.ok(null);
    }

}
