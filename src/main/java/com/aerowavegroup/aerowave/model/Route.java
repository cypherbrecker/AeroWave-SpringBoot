package com.aerowavegroup.aerowave.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int route_id;
    protected String from_place;
    protected String to_place;
    protected String date_of_departure;
    protected String time_of_departure;
    protected String date_of_arrival;
    protected String time_of_arrival;
    protected int current_headcount = 0;
    protected boolean traffic_jam;
    protected boolean discount;
    protected int price;

    @ManyToOne
    @JoinColumn(name = "airplaneID", referencedColumnName = "airplane_id")
    protected Airplane airplaneID;

    //  private int airplaneID;
}