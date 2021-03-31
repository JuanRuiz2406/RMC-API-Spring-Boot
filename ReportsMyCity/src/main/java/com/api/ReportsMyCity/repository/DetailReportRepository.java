package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.DetailReport;
import com.api.ReportsMyCity.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetailReportRepository  extends JpaRepository<DetailReport, Integer> {

    @Transactional(readOnly = true)
    List<DetailReport> findByReport(Report report);
}
