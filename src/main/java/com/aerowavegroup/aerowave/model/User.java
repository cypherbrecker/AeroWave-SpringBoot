package com.aerowavegroup.aerowave.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String full_name;
    private String password;
    private String email;
    private boolean admin = false;
    private int credits = 650000;
}
