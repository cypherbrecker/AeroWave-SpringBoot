package com.aerowavegroup.aerowave.service;

import com.aerowavegroup.aerowave.model.PromoRoute;
import com.aerowavegroup.aerowave.model.Route;

public interface RouteService {
    public Route createRoute(Route route);
    public void updateHeadcount(int utasok, int routeID);
    PromoRoute getPromoRouteById(int promoId);
}