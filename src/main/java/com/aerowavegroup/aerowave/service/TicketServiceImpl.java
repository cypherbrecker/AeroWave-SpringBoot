package com.aerowavegroup.aerowave.service;

import com.aerowavegroup.aerowave.model.Ticket;
import com.aerowavegroup.aerowave.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepo;
    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        ticketRepo.delete(ticket);
    }
}
