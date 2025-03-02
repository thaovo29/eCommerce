package com.thaovo.userService.controller;

import com.thaovo.userService.data.Role;
import com.thaovo.userService.data.User;
import com.thaovo.userService.model.AddRoleToUser;
import com.thaovo.userService.model.UserDTO;
import com.thaovo.userService.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/listUser")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/listRole")
    public List<Role> getAllRole() {
        return userService.getAllRole();
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody AddRoleToUser model) {
        userService.addRole(model.getUsername(), model.getRoleName());
    }

    @PostMapping("/addRole")
    public Role addRole(@RequestBody Role role) {
        return userService.saveRole(role);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/validateToken")
    public UserDTO validateToken(@RequestBody UserDTO user) {
        return userService.validateToken(user.getToken());
    }

    @GetMapping
    public String homePage() {
        return "Hello, there!";
    }
}
