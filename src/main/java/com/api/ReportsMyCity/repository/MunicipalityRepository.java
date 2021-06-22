package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.Municipality;
import com.api.ReportsMyCity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {

    Municipality findByEmail(String email);
    Municipality findByName(String name);
    @Transactional(readOnly = true)
    List<Municipality> findByManager(User manager);

}
