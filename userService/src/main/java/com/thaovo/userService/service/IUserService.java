package com.thaovo.userService.service;

import com.thaovo.userService.data.Role;
import com.thaovo.userService.data.User;
import com.thaovo.userService.model.UserDTO;

import java.util.List;

public interface IUserService {
    List<User> getAllUser();
    List<Role> getAllRole();
    User saveUser(User user);
    Role saveRole(Role role);
    void addRole(String username, String roleName);
    UserDTO login(String username, String password);
    UserDTO validateToken(String token);
}
