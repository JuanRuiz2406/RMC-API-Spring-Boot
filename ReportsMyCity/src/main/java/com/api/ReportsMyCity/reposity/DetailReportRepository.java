package com.api.ReportsMyCity.reposity;

import com.api.ReportsMyCity.entity.DetailReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailReportRepository  extends JpaRepository<DetailReport, Integer> {
}
