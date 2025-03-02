package com.thaovovo.userService.service;

import com.thaovovo.userService.data.Role;
import com.thaovovo.userService.data.User;
import com.thaovovo.userService.model.UserDTO;

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
