package com.aerowavegroup.aerowave.service;

import com.aerowavegroup.aerowave.config.UserDetailsServiceImpl;
import com.aerowavegroup.aerowave.model.User;
import com.aerowavegroup.aerowave.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncode.encode(user.getPassword()));
        System.out.println(user);
        return userRepo.save(user);
    }

    @Override
    public User saveCredits(User user, int count) {
        user.setCredits(user.getCredits()-count);
        return userRepo.save(user);
    }

    @Override
    public User refundCredits(User user, int count) {
        user.setCredits(user.getCredits()+count);
        return userRepo.save(user);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}
