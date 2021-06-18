package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.DetailReport;
import com.api.ReportsMyCity.entity.Photography;
import com.api.ReportsMyCity.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhotographyRepository extends JpaRepository<Photography, Integer> {
    @Transactional(readOnly = true)
    List<Photography> findByReports(Report report);
}
