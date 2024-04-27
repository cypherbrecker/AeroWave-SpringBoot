package com.aerowavegroup.aerowave.repository;

import com.aerowavegroup.aerowave.model.PromoRoute;
import com.aerowavegroup.aerowave.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RouteRepository  extends JpaRepository<Route, Integer> {
    @Query("SELECT DISTINCT r.from_place FROM Route r")
    List<String> findAllFromCities();

    @Query("SELECT DISTINCT r.to_place FROM Route r")
    List<String> findAllToCities();

    @Modifying
    @Query("UPDATE Route r SET r.current_headcount = :utasok + r.current_headcount WHERE r.route_id = :routeID")
    @Transactional
    public void updateljuk(@Param("utasok") int utasok,
                           @Param("routeID") int routeID);
    @Query("select r FROM Route r WHERE r.current_headcount = 0 and r.discount = false ")
    List<Route> routePrint();

    @Query("SELECT r FROM Route r WHERE r.discount = true AND CAST(r.date_of_departure AS DATE) >= CURRENT_DATE() ORDER BY r.date_of_departure ASC, r.price ASC LIMIT 5")
    List<Route> findFiveBestDiscounted();

    @Query("SELECT r FROM Route r WHERE r.from_place = :from AND r.to_place = :to AND r.date_of_departure >= :date_of_departure AND r.time_of_departure >= :time_of_departure AND r.discount = false") //best
    List<Route> findByFromPlaceAndToPlaceInTime(
            @Param("from") String from,
            @Param("to") String to,
            @Param("date_of_departure") String date_of_departure,
            @Param("time_of_departure") String time_of_departure);


}