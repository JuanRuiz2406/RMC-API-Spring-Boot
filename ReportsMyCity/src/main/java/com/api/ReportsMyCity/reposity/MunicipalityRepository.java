package com.api.ReportsMyCity.reposity;

import com.api.ReportsMyCity.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {

    Municipality findByEmail(String email);
    Municipality findByName(String name);
}
