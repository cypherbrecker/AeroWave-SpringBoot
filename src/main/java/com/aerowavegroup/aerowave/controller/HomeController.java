package com.aerowavegroup.aerowave.controller;

import com.aerowavegroup.aerowave.model.*;
import com.aerowavegroup.aerowave.repository.RouteRepository;
import com.aerowavegroup.aerowave.repository.TicketRepository;
import com.aerowavegroup.aerowave.service.RouteService;
import com.aerowavegroup.aerowave.service.RouteServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import com.aerowavegroup.aerowave.repository.UserRepository;
import com.aerowavegroup.aerowave.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RouteRepository routeRepo;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TicketRepository ticketRepo;


    @GetMapping("/")
    public String index(Model model, Principal principal) {
       /* List<Route> teszt = routeRepo.findAll();
        model.put("teszt", teszt);*/


        List<String> fromCities = routeRepo.findAllFromCities();
        List<String> toCities = routeRepo.findAllToCities();

        model.addAttribute("fromCities", fromCities);
        model.addAttribute("toCities", toCities);

        List<PromoRoute> promos = PromoRoute.convertRouteListToPromoList(routeRepo.findFiveBestDiscounted());

        model.addAttribute("promoRoutes", promos);
        getPromoDetails(principal,0, model);

       /*if (principal != null) {
           Authentication auth = (Authentication) principal;
           if (auth.isAuthenticated()) {
               System.out.println("Bejelentkezett user van az indexen!");
           } else {
               System.out.println("Not Bejelentkezett user van az indexen!");
           }
       } else {
           System.out.println("Nincs bejelentkezve");
       }*/

        return "index";
    }

    @GetMapping("/promo={promoId}")
    public String getPromoDetails(Principal p, @PathVariable int promoId, Model model) {
        PromoRoute pr = routeService.getPromoRouteById(promoId);
        model.addAttribute("curPromo", pr);

        if(p != null){
            Authentication auth = (Authentication) p;
            if (auth.isAuthenticated()) {
                String email = p.getName();
                Integer idkeres = userRepo.findIDByEmail(email);
                model.addAttribute("idkeres", idkeres);
            }
        }
        return "index";
    }

    @PostMapping("/")
    public String searchRoutes(Principal p,@RequestParam("from_place") String from,
                               @RequestParam("to_place") String to,
                               @RequestParam("date_of_departure") String date_of_departure,
                               @RequestParam("time_of_departure") String time_of_departure,
                               @RequestParam("teszt") int teszt,
                               Model model) {

        model.addAttribute("teszt", teszt);

        List<Route> matchingRoutes = routeRepo.findByFromPlaceAndToPlaceInTime(
                from, to, date_of_departure, time_of_departure);

        boolean noResults = matchingRoutes.isEmpty();
        boolean hasResults = !noResults;

        if (p != null) { //ha be van jelentkezve
            Authentication auth = (Authentication) p;
            if (auth.isAuthenticated()) {
                String email = p.getName();
                Integer idkeres = userRepo.findIDByEmail(email);
                model.addAttribute("idkeres", idkeres);
                System.out.println("Bejelentkezett user keresett!");
            } else {
                System.out.println("Nem bejelentkezett user keresett");
            }
        } else {
            System.out.println("Nem bejelentkezett user keresett");
        }



       /* String email = p.getName();
        Integer idkeres = userRepo.findIDByEmail(email);
        model.addAttribute("idkeres", idkeres);*/



        if (noResults) {
            model.addAttribute("noResults", noResults);
        } else {
            model.addAttribute("matchingRoutes", matchingRoutes);
            model.addAttribute("noResults", null);
            model.addAttribute("hasResults", hasResults);
        }

        System.out.println("Honnan:" + from);
        System.out.println("Hova:" + to);
        System.out.println("Mikor: " + date_of_departure);
        System.out.println("Hanytol: " + time_of_departure);
        System.out.println("Utasok szama: " + teszt);

        return "search_result";
    }



    @GetMapping("/informations")
    public String informations() {
        return "information";
    }

    @GetMapping("/regulation")
    public String regulation() {
        return "regulation";
    }

    @GetMapping("/availability")
    public String availability() {
        return "availability";
    }

    @GetMapping("/search-result")
    public String searchResult(Model model, Principal p){



        return "search_result";
    }

    @GetMapping("/signin")
    public String login(Principal p) {
        if (p == null) { //nincs bejelentkezve
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(Principal p) {
        if (p == null) { //nincs bejelentkezve
            return "registration";
        }
        return "redirect:/";
    }

    @PostMapping("/createUser")
    public String createuser(@ModelAttribute User user, HttpSession session) {

        boolean exist = userService.checkEmail(user.getEmail());

        if (exist) {
            System.out.println("Ez az email cim már használatban van! (Hiba)");
            session.setAttribute("msg","Ez az email cim már használatban van!");
        } else {
            User success = userService.createUser(user);
            if (success != null) {
                System.out.println("Sikeres regisztracio!");
                session.setAttribute("msg","Sikeres regisztracio!");
                System.out.println("letrehozva: "+ user);
                return "redirect:/signin";
            } else {
                System.out.println("Hiba, a regisztralas soran! (Hiba)");
                session.setAttribute("msg","Hiba, a regisztralas soran!");
            }
        }

        return "redirect:/register"; //registration
    }


    @PostMapping("/pay")
    public String payment(@ModelAttribute Ticket ticket, Model model, Principal p,
                          @RequestParam("routeID") int routeID,
                          @RequestParam("price") int price ,
                          @RequestParam("number_of_passengers") int utasok,
                          @RequestParam("userID") int valamiSzama) {

        String email = p.getName();


        // String email = p.getName();

        System.out.println("POSTED");


        User loginUser = userRepo.findByEmail(email);


        System.out.println("---------------------------");
        System.out.println("User credits: " + loginUser.getCredits());
        System.out.println("jegy ára: " + price*utasok);



        if (loginUser.getCredits() >= price) {
            System.out.println("Megtudod venni");
            // loginUser.setCredits(loginUser.getCredits()-price);
            userService.saveCredits(loginUser,price*utasok); //itt vonja le
            ticketRepo.save(ticket);
            routeRepo.updateljuk(utasok, routeID);


            System.out.println(loginUser.getCredits());

        } else {
            System.out.println("Erre nincs pénzed");
        }

        System.out.println("Pénzösszeg (új): " + loginUser.getCredits());
        /*System.out.println("Route id: " + id);
        System.out.println("Vásárló id: " + valamiSzama);*/


        return "redirect:/user/";
        //redirect:/user/
    }

}
