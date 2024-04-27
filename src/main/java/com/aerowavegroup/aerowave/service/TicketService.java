package com.aerowavegroup.aerowave.service;

import com.aerowavegroup.aerowave.model.Ticket;

public interface TicketService {
    public Ticket createTicket(Ticket ticket);

    void deleteTicket(Ticket ticket);
}