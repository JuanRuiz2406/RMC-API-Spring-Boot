package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
}
