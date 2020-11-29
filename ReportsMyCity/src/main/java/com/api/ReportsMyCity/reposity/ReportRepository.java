package com.api.ReportsMyCity.reposity;

import com.api.ReportsMyCity.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}
