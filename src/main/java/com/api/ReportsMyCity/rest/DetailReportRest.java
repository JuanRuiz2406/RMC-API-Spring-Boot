package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.DetailReport;
import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.exceptions.ResourceNotFoundException;
import com.api.ReportsMyCity.repository.DetailReportRepository;
import com.api.ReportsMyCity.repository.ReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("detailReport")
public class DetailReportRest {

    private final DetailReportRepository detailReportRepository;
    private ReportRest reportRest;

    public DetailReportRest(DetailReportRepository detailReportRepository, ReportRest reportRest) {
        this.detailReportRepository = detailReportRepository;
        this.reportRest = reportRest;
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

    @GetMapping(value = "/byReport/{reportId}")
    public ResponseEntity<List<DetailReport>> getByReportId(@PathVariable("reportId") int reportId){

        Report report = reportRest.getById(reportId);
        List<DetailReport> detailsByReport = detailReportRepository.findByReport(report);
        if(detailsByReport.isEmpty()){
            return ResponseEntity.ok(detailsByReport);
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
