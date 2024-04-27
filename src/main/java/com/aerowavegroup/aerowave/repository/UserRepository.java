package com.aerowavegroup.aerowave.repository;

import com.aerowavegroup.aerowave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByEmail(String email);
    public User findByEmail(String email);

    @Query("SELECT u.credits FROM User u WHERE u.email = :email")
    Integer findCreditByEmail(@Param("email") String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Integer findIDByEmail(@Param("email") String email);

}
