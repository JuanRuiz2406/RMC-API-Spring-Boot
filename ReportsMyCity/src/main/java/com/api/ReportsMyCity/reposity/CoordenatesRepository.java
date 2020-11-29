package com.api.ReportsMyCity.reposity;

import com.api.ReportsMyCity.entity.Coordenates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordenatesRepository extends JpaRepository<Coordenates, Integer> {
}
