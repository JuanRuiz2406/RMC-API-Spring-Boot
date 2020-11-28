package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.DetailReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsReportRepository extends JpaRepository<DetailReport, Integer> {
}
