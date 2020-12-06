package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.DetailReport;
import com.api.ReportsMyCity.reposity.DetailReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("detailReport")
public class DetailReportRest {

    @Autowired
    private DetailReportRepository detailRepo;

    @GetMapping // detailReport/
    public ResponseEntity<List<DetailReport>> getDetailReport() {

        List<DetailReport> detailReports = detailRepo.findAll();
        return ResponseEntity.ok(detailReports);
    }

    @RequestMapping(value = "{detailReportId}") // detailReports/{detailReportID}
    public ResponseEntity<DetailReport> getDetailReportById(@PathVariable("detailReportId") int detailReportId){

        Optional<DetailReport> optionalDetailReport = detailRepo.findById(detailReportId);

        if(optionalDetailReport.isPresent()) {
            return ResponseEntity.ok(optionalDetailReport.get());
        }else {
            return ResponseEntity.noContent().build();
        }

    }

    @PostMapping
    public ResponseEntity<DetailReport> createDetailReport(@RequestBody DetailReport detailReport){

        DetailReport newDetailReport = detailRepo.save(detailReport);
        return ResponseEntity.ok(newDetailReport);
    }

    @DeleteMapping(value = "{detailReportId}")
    public ResponseEntity<DetailReport> deleteDetailReport(@PathVariable("detailReportId") int detailReportId) {

        detailRepo.deleteById(detailReportId);
        return ResponseEntity.ok(null);
    }
}
