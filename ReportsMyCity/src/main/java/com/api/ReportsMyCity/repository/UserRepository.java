package com.api.ReportsMyCity.repository;

import com.api.ReportsMyCity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("managerUser")
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User findByIdCard(String idCard);
    boolean existsByEmail(String email);
    boolean existsByIdCard(String idCard);

}
