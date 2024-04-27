package com.aerowavegroup.aerowave.service;

import com.aerowavegroup.aerowave.model.User;

public interface UserService {
    public User createUser(User user);
    public boolean checkEmail(String email);

    public User saveCredits(User user, int count);

    User refundCredits(User user, int count);
}
