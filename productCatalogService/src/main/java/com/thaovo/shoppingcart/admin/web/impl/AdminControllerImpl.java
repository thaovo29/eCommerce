package com.thaovo.shoppingcart.admin.web.impl;

import com.thaovo.shoppingcart.AbstractApplicationController;
import com.thaovo.shoppingcart.admin.model.dto.UserReqDto;
import com.thaovo.shoppingcart.admin.model.dto.UserResponseDto;
import com.thaovo.shoppingcart.admin.model.exception.UserUsernameAlreadyExist;
import com.thaovo.shoppingcart.admin.model.exception.UserUsernameNotFoundException;
import com.thaovo.shoppingcart.admin.service.AdminService;
import com.thaovo.shoppingcart.admin.web.AdminController;
import com.thaovo.shoppingcart.user.authentication.exceptions.DataValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminControllerImpl extends AbstractApplicationController implements AdminController {

    private final AdminService adminService;

    public AdminControllerImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserReqDto insert) {
        try {
            return ResponseEntity.ok(adminService.insert(insert));
        } catch (UserUsernameAlreadyExist e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserReqDto modifier) {
        try {
            return ResponseEntity.ok(adminService.modify(modifier));
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UserUsernameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(adminService.getUserByUsername(username));
        } catch (UserUsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @DeleteMapping("/{username}")
    public ResponseEntity<UserResponseDto> deleteUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(adminService.destroy(username));
        } catch (UserUsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
