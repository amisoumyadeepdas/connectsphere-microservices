package com.example.services;

import com.example.dto.UserRequest;
import com.example.entity.User;
import java.util.List;

public interface UserService {

    User createUser(UserRequest request);

    List<User> getAllUsers();

    User updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    User getUserById(Long id);

    User getUserByEmail(String email);
}

