package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.City;
import com.api.ReportsMyCity.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

    @Transactional(readOnly = true)
    City findByName(String name);

}
