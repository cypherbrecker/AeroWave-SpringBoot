package com.aerowavegroup.aerowave.service;

import com.aerowavegroup.aerowave.model.PromoRoute;
import com.aerowavegroup.aerowave.model.Route;
import com.aerowavegroup.aerowave.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteRepository routeRepo;
    @Override
    public Route createRoute(Route route) {
        System.out.println(route);
        return routeRepo.save(route);
    }

    @Transactional
    @Override
    public void updateHeadcount(int utasok, int routeID) {
        routeRepo.updateljuk(utasok, routeID);
    }

    @Override
    public PromoRoute getPromoRouteById(int promoId) {
        List<PromoRoute> promos = PromoRoute.convertRouteListToPromoList(routeRepo.findFiveBestDiscounted());

        // biztosítja, hogy mind negatív, mind pozitív túlcsordulás esetén jót ad, körbe megy
        return promos.get((promoId % promos.size() + promos.size()) % promos.size());
    }
}