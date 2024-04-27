package com.aerowavegroup.aerowave.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PromoRoute extends Route{
    private int display_id;

    public PromoRoute(Route route, int display_id) {
        setRoute_id(route.getRoute_id());
        setFrom_place(route.getFrom_place());
        setTo_place(route.getTo_place());
        setDate_of_departure(route.getDate_of_departure());
        setTime_of_departure(route.getTime_of_departure());
        setDate_of_arrival(route.getDate_of_arrival());
        setTime_of_arrival(route.getTime_of_arrival());
        setCurrent_headcount(route.getCurrent_headcount());
        setTraffic_jam(route.isTraffic_jam());
        setDiscount(route.isDiscount());
        setPrice(route.getPrice());
        setAirplaneID(route.getAirplaneID());
        this.display_id = display_id;
    }

    public static List<PromoRoute> convertRouteListToPromoList(List<Route> routes){
        List<PromoRoute> promos = new ArrayList<>();
        int i = 0;
        for (var route: routes) {
            promos.add(new PromoRoute(route, i++));
        }
        return promos;
    }
}
