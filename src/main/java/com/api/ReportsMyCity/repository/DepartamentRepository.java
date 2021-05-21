package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.DepartamentMunicipality;
import com.api.ReportsMyCity.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartamentRepository extends JpaRepository<DepartamentMunicipality, Integer> {
    @Transactional(readOnly = true)
    List<DepartamentMunicipality> findByMunicipality(Municipality municipality);

    @Transactional(readOnly = true)
    DepartamentMunicipality findByEmail(String email);

    @Transactional(readOnly = true)
    DepartamentMunicipality findByName(String name);

}
