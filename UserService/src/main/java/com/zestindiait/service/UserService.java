package com.zestindiait.service;

import com.zestindiait.dto.LoginDto;
import com.zestindiait.entities.User;

import java.util.List;

public interface UserService  {
   User saveUser(User user);

    List<User> getAllUser();

    User getUserById(String id);

    User login(LoginDto request);

    void deleteUser(String userId);
}
