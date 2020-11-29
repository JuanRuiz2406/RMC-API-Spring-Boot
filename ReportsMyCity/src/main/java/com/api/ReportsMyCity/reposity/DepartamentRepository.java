package com.api.ReportsMyCity.reposity;

import com.api.ReportsMyCity.entity.DepartamentMunicipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentRepository extends JpaRepository<DepartamentMunicipality, Integer> {
}
