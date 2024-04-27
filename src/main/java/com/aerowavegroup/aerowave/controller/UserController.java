package com.aerowavegroup.aerowave.controller;


import com.aerowavegroup.aerowave.config.UserDetailsServiceImpl;
import com.aerowavegroup.aerowave.model.Airplane;
import com.aerowavegroup.aerowave.model.Route;
import com.aerowavegroup.aerowave.model.Ticket;
import com.aerowavegroup.aerowave.model.User;
import com.aerowavegroup.aerowave.repository.AirplaneRepository;
import com.aerowavegroup.aerowave.repository.RouteRepository;
import com.aerowavegroup.aerowave.repository.TicketRepository;
import com.aerowavegroup.aerowave.repository.UserRepository;
import com.aerowavegroup.aerowave.service.RouteService;
import com.aerowavegroup.aerowave.service.TicketService;
import com.aerowavegroup.aerowave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RouteRepository routeRepo;

    @Autowired
    private AirplaneRepository airplaneRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;


    @ModelAttribute
    private void userDetails(Model m, Principal p) {

        if (p != null) {
            String nev = p.getName();
            User user = userRepo.findByEmail(nev);
           // m.addAttribute("user", user);

            if (user != null) {
                m.addAttribute("user", user);
            } else {
                m.addAttribute("user", "nem talalhato");
            }




            //  System.out.println(user);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            System.out.println(authentication.getDetails());
            System.out.println(authentication.isAuthenticated());
            System.out.println(authentication.getPrincipal());
            System.out.println(authentication.getAuthorities());


            if (authentication.isAuthenticated()) {
                System.out.println("Bejelentkezett: " + user);
            }


        } else {
            m.addAttribute("user", "NULL");
        }

    }

    @GetMapping({"","/"})
    public String home(Model model, Principal p) {
        String email = p.getName();
        User loginUser = userRepo.findByEmail(email);
        Integer userCredits = userRepo.findCreditByEmail(email);
        model.addAttribute("userCredits", userCredits);

        model.addAttribute("tickets", ticketRepo.findAllByUserID(loginUser.getId()));

        return "user/profile";
    }

    @GetMapping("/admin")
    public String adminPanel(Principal p, Model model, Map<String,Object> routesm) {
        String nev = p.getName();
        User user = userRepo.findByEmail(nev);
        if (user.isAdmin()) {
            List<Airplane> airplaneValues = airplaneRepo.findDistinctAirplaneID();
            model.addAttribute("airplaneValues", airplaneValues);
            model.addAttribute("selectedairplaneValues", "");
            List<Route> routes = routeRepo.routePrint();
            routesm.put("routes",routes);

            return "user/admin";
        }
        return "redirect:/";
    }


    @PostMapping("/createRoute")
    public String createroute(@ModelAttribute Route route, @RequestParam("airplaneID") int x){
        routeService.createRoute(route);
        System.out.println("Sikeres!");
        System.out.println(x);
        return "redirect:/user/admin";
    }



    @PostMapping("/deleteTicket")
    public String deleteTicket(Principal p, @RequestParam("ticket_id") int ticket_id){
        User loginUser = userRepo.findByEmail(p.getName());
        Ticket ticket = ticketRepo.findById(ticket_id);

        userService.refundCredits(loginUser, ticket.getRouteID().getPrice() * ticket.getNumber_of_passengers());
        ticket.getRouteID().setCurrent_headcount(ticket.getRouteID().getCurrent_headcount() - ticket.getNumber_of_passengers());
        ticketService.deleteTicket(ticket);

        return "redirect:/user/";
    }


    @PostMapping("/updatePassword")
    public String changePassword(Principal p, @RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass) {
        String email = p.getName();
        User loginUser = userRepo.findByEmail(email);

        boolean isCorrect = passwordEncoder.matches(oldPass, loginUser.getPassword());

        if(isCorrect) {
            System.out.println("Helyes régi jelszó!");

            if (passwordEncoder.matches(newPass, loginUser.getPassword())) {
                System.out.println("ugyanarra akarod valtoztatni!"); //ne változtassa meg
            } else {
                loginUser.setPassword(passwordEncoder.encode(newPass));
                User updatedPass = userRepo.save(loginUser);

                if (updatedPass != null) {
                    System.out.println("Sikeres jelszovaltoztatas!");
                } else {
                    System.out.println("Hoppá, valami hibatörtént a jelszóváltoztatás során!");
                }
            }

        } else {
            System.out.println("Hibás régi jelszó!");
        }

        return "redirect:/user";
    }
    @PostMapping("/updateRoute")
    public String updateRoute(@RequestParam("route_id") int routeId,
                              @RequestParam("from_place") String from_place,
                              @RequestParam("to_place") String to_place,
                              @RequestParam("date_of_departure") String date_of_departure,
                              @RequestParam("time_of_departure") String time_of_departure,
                              @RequestParam("date_of_arrival") String date_of_arrival,
                              @RequestParam("time_of_arrival") String time_of_arrival,
                              @RequestParam(value = "traffic_jam", defaultValue = "false") boolean traffic_jam,
                              @RequestParam(value = "discount", defaultValue = "false") boolean discount,
                              @RequestParam("price") int price,
                              @RequestParam("airplaneID") Airplane airplaneid
                              ){
        System.out.println(routeId);
        Route modositas = routeRepo.findById(routeId).get();
        modositas.setFrom_place(from_place);
        modositas.setTo_place(to_place);
        modositas.setDate_of_departure(date_of_departure);
        modositas.setTime_of_departure(time_of_departure);
        modositas.setDate_of_arrival(date_of_arrival);
        modositas.setTime_of_arrival(time_of_arrival);
        modositas.setTraffic_jam(traffic_jam);
        modositas.setDiscount(discount);
        modositas.setPrice(price);
        modositas.setAirplaneID(airplaneid);
        routeRepo.save(modositas);
        return "redirect:/user/admin";
    }

    @PostMapping("/delete")
    public String deleteRoute(@RequestParam("id") int routeId){
        routeRepo.deleteById(routeId);
        return "redirect:/user/admin";
    }
}
