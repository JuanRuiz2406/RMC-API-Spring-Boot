package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
