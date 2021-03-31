package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.entity.Report;
import com.api.ReportsMyCity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Transactional (readOnly = true)
    List<Report> findByUser(User user);

    @Transactional (readOnly = true)
    List<Report> findByMunicipality(Municipality municipality);

    @Transactional (readOnly = true)
    List<Report> findByState(String state);

}
