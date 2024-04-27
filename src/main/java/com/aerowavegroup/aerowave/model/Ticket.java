package com.aerowavegroup.aerowave.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticket_id;
    private boolean is_payed = true;
    private int number_of_passengers;


    @ManyToOne
    @JoinColumn(name= "userID", referencedColumnName = "id")
    private User userID;

    @ManyToOne
    @JoinColumn(name= "routeID", referencedColumnName = "route_id")
    private Route routeID;
}