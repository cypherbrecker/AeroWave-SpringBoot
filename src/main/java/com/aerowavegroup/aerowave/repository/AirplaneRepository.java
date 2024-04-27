package com.aerowavegroup.aerowave.repository;
import com.aerowavegroup.aerowave.model.Airplane;
import com.aerowavegroup.aerowave.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirplaneRepository extends JpaRepository<Airplane, Integer>{


    @Query("SELECT DISTINCT r FROM Airplane r")
    List<Airplane> findDistinctAirplaneID();


}
