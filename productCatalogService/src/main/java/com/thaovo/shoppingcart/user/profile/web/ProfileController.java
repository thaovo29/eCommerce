package com.thaovo.shoppingcart.user.profile.web;

import com.thaovo.shoppingcart.AbstractApplicationController;
import com.thaovo.shoppingcart.product.services.ImageService;
import com.thaovo.shoppingcart.user.authentication.dto.ApiResponse;
import com.thaovo.shoppingcart.user.authentication.exceptions.DataValidationException;
import com.thaovo.shoppingcart.user.profile.dto.ProfileDTO;
import com.thaovo.shoppingcart.user.profile.exceptions.AuthorizationException;
import com.thaovo.shoppingcart.user.profile.exceptions.ConcurrentUpdateException;
import com.thaovo.shoppingcart.user.profile.exceptions.ProfileNotFoundException;
import com.thaovo.shoppingcart.user.profile.service.ProfileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

@RestController
@RequestMapping("/user/profile")
public class ProfileController extends AbstractApplicationController {

    private final ProfileService service;

    private final ImageService imageService;

    public ProfileController(ProfileService service, ImageService imageService) {
        this.service = service;
        this.imageService = imageService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile() {
        ApiResponse<ProfileDTO> response = new ApiResponse<>();
        ProfileDTO profile = service.getProfile();
        response.setData(profile);
        response.setSuccess(true);
        response.setMessage("Profile retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/basic")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateProfile(@RequestBody ProfileDTO profile) {
        ApiResponse<ProfileDTO> response = new ApiResponse<>();
        try {
            ProfileDTO updatedProfile = service.updateBasic(profile);
            response.setData(updatedProfile);
            response.setSuccess(true);
            response.setMessage("Profile updated successfully");
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AuthorizationException ae) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ae.getMessage());
        } catch (ConcurrentUpdateException cue) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, cue.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/avatar")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateAvatar(@RequestParam String avatarUrl) {
        ApiResponse<ProfileDTO> response = new ApiResponse<>();
        try {
            response.setMessage("Profile updated successfully");
            response.setSuccess(true);
            response.setData(service.updateAvatar(avatarUrl));
        } catch (ProfileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) throws IOException {
        Path filePath = imageService.getImagePath(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/*")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/image/upload")
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String fileName = imageService.uploadImage(file);
            String imageUrl = "/user/profile/image/" + fileName;
            return new ApiResponse<>("Image uploaded successfully", true, imageUrl);
        } catch (FileAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
