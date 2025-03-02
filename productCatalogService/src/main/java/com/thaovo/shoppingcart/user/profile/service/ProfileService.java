package com.thaovo.shoppingcart.user.profile.service;

import com.thaovo.shoppingcart.product.exceptions.ProductImageNotFoundException;
import com.thaovo.shoppingcart.product.services.ImageService;
import com.thaovo.shoppingcart.user.authentication.exceptions.DataValidationException;
import com.thaovo.shoppingcart.user.profile.dto.ProfileDTO;
import com.thaovo.shoppingcart.user.profile.entity.ProfileEntity;
import com.thaovo.shoppingcart.user.profile.exceptions.AuthorizationException;
import com.thaovo.shoppingcart.user.profile.exceptions.ConcurrentUpdateException;
import com.thaovo.shoppingcart.user.profile.exceptions.ProfileNotFoundException;
import com.thaovo.shoppingcart.user.profile.mapping.ProfileMapper;
import com.thaovo.shoppingcart.user.profile.repository.ProfileRepository;
import com.thaovo.shoppingcart.user.profile.validator.ProfileValidator;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final ProfileValidator validator;

    private final ProfileMapper mapper;

    private final ImageService imageService;

    @PersistenceContext
    private EntityManager em;

    public ProfileService(ProfileRepository profileRepository,
                          ProfileValidator validator,
                          ProfileMapper mapper,
                          ImageService imageService) {
        this.profileRepository = profileRepository;
        this.validator = validator;
        this.mapper = mapper;
        this.imageService = imageService;
    }

    public ProfileDTO getProfile() {
        // Get the currently authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileDTO retrievedProfileDto = mapper.toDTO(profileRepository.findByUserUsername(username).orElseThrow());
        retrievedProfileDto.setUsername(username);
        retrievedProfileDto.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        return retrievedProfileDto;
    }

    @Transactional(rollbackFor = {ObjectOptimisticLockingFailureException.class, Exception.class})
    public ProfileDTO updateBasic(ProfileDTO profileDTO) throws DataValidationException {
        // basic fields are: fullName, gender, birthday, weight, height
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals(profileDTO.getUsername())) {
            throw new AuthorizationException("You just can update your own profile");
        }

        validator.validateBasicProfileInfo(profileDTO);

        ProfileEntity existsProfile = profileRepository.findByUserUsername(authenticatedUsername).orElseThrow();

        em.detach(existsProfile);

        ProfileEntity updated = mapper.toEntity(profileDTO);

        // set advanced fields
        existsProfile.setGender(updated.getGender());
        existsProfile.setHeight(updated.getHeight());
        existsProfile.setWeight(updated.getWeight());
        existsProfile.setBirthday(updated.getBirthday());
        existsProfile.setFullName(updated.getFullName());

        try {
            ProfileEntity result = profileRepository.save(existsProfile);
            ProfileDTO resultDto = mapper.toDTO(result);
            resultDto.setUsername(profileDTO.getUsername());
            return resultDto;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrentUpdateException(
                    String.format("User profile with username %s has been updated by another user",
                            profileDTO.getUsername()));
        }
    }

    public ProfileDTO updateAvatar(String imageUrl) throws ProfileNotFoundException {
        var found = profileRepository.findByUserUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (found.isEmpty()) {
            throw new ProfileNotFoundException("User profile not found");
        }
        ProfileEntity profile = found.get();
        if (!imageService.isImageExist(imageUrl)) {
            throw new ProductImageNotFoundException(String.format("Image %s not found in the system. Please upload it first",
                    imageUrl));
        }
        profile.setAvatar(imageUrl);
        return mapper.toDTO(profileRepository.save(profile));
    }
}
