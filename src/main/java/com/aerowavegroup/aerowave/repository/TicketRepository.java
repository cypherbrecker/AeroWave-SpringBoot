package com.aerowavegroup.aerowave.repository;


import com.aerowavegroup.aerowave.model.Ticket;
import com.aerowavegroup.aerowave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT t FROM Ticket t WHERE t.userID.id = :userID AND t.is_payed = true")
    List<Ticket> findAllByUserID(@Param("userID") int userID);

    @Query("SELECT t FROM Ticket t WHERE t.ticket_id = :ticket_id")
    Ticket findById(@Param("ticket_id") int ticket_id);
}
