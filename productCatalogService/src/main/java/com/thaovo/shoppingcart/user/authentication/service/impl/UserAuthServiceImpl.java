package com.thaovo.shoppingcart.user.authentication.service.impl;

import com.thaovo.shoppingcart.user.authentication.dto.ChangePasswordDto;
import com.thaovo.shoppingcart.user.authentication.dto.UserRegisterDTO;
import com.thaovo.shoppingcart.user.authentication.entity.Authority;
import com.thaovo.shoppingcart.user.authentication.entity.UserAuthEntity;
import com.thaovo.shoppingcart.user.authentication.exceptions.*;
import com.thaovo.shoppingcart.user.authentication.mapping.UserMapper;
import com.thaovo.shoppingcart.user.authentication.repository.UserRepository;
import com.thaovo.shoppingcart.user.authentication.service.UserAuthService;
import com.thaovo.shoppingcart.user.authentication.validator.UserValidator;
import com.thaovo.shoppingcart.user.otp.entities.OtpVerificationEntity;
import com.thaovo.shoppingcart.user.otp.exceptions.*;
import com.thaovo.shoppingcart.user.otp.repo.VerificationRepo;
import com.thaovo.shoppingcart.user.otp.service.MailOtpService;
import com.thaovo.shoppingcart.user.otp.service.PhoneOtpService;
import com.thaovo.shoppingcart.user.otp.utils.Generator;
import com.thaovo.shoppingcart.user.profile.entity.ProfileEntity;
import com.thaovo.shoppingcart.user.profile.exceptions.AuthorizationException;
import com.thaovo.shoppingcart.user.profile.repository.ProfileRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    final
    UserValidator validator;
    private final AuthenticationProvider authenticationProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final MailOtpService mailOtpService;

    private final PhoneOtpService phoneOtpService;

    private final VerificationRepo verificationRepo;

    public UserAuthServiceImpl(UserValidator validator,
                               AuthenticationProvider authenticationProvider,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               UserMapper userMapper,
                               UserRepository userRepository,
                               ProfileRepository profileRepository,
                               MailOtpService mailOtpService,
                               PhoneOtpService phoneOtpService,
                               VerificationRepo verificationRepo) {
        this.validator = validator;
        this.authenticationProvider = authenticationProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.mailOtpService = mailOtpService;
        this.phoneOtpService = phoneOtpService;
        this.verificationRepo = verificationRepo;
    }

    @Override
    public String register(UserRegisterDTO userRegisterDTO)
            throws DataValidationException, UsernameAlreadyExists, EmailAlreadyLinked {

        validator.validateRegisterDto(userRegisterDTO);

        UserAuthEntity register = userMapper.toEntity(userRegisterDTO);
        register.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));
        Authority authority = new Authority("ROLE_USER");
        register.setAuthorities(Set.of(authority));

        UserAuthEntity saved;
        try {
            saved = userRepository.save(register);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Username already exists");
        } // created and saved user

        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(userRegisterDTO.getUsername(), userRegisterDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Setup user profile with email
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(userRegisterDTO.getEmail());
        profileEntity.setUser(saved);
        try {
            profileRepository.save(profileEntity);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Email already exists");
        }

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public void resetPassword(String newPassword, String token) throws DataValidationException {
        validator.checkPassword(newPassword);
        OtpVerificationEntity otpVerification = verificationRepo.findByVerificationToken(token).orElseThrow(
                () -> new IllegalArgumentException("Bad credentials")
        );
        String username = otpVerification.getUserAuthEntity().getUsername();
        userRepository.findUserByUsername(username).ifPresent(userAuthEntity -> {
            userAuthEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(userAuthEntity);
        });
        verificationRepo.delete(otpVerification);
    }

    @Override
    @Transactional
    public void verifyPhone(String phone, String code) throws TwilioServiceException, DataValidationException,
            OtpIncorrectException, OtpSentException {
        validator.checkPhoneNumberE164(phone);

        Optional<ProfileEntity> byPhone = profileRepository.findByPhone(phone);

        if (byPhone.isPresent() && !byPhone.get().getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AuthorizationException("You just can verify your own phone");
        }

        if (byPhone.isPresent() && byPhone.get().isPhoneVerified()) {
            if (byPhone.get().getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new PhoneAlreadyLinked("Phone already linked to your account");
            }
            throw new PhoneAlreadyLinked("Phone already linked to another account");
        }

        if (code == null) {
            phoneOtpService.sendVerification(phone);
            throw new OtpSentException("OTP sent, please check your phone");
        } else {
            phoneOtpService.checkVerification(phone, code);
            profileRepository.findByUserUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                    .ifPresent(profileEntity -> {
                        profileEntity.setPhone(phone);
                        profileEntity.setPhoneVerified(true);
                        profileRepository.save(profileEntity);
                    });
        }


    }

    @Override
    public String forgotPasswordByPhone(String phone, String code)
            throws DataValidationException, TwilioServiceException, OtpSentException, OtpIncorrectException, PhoneNotFound {

        validator.checkPhoneNumberE164(phone);

        Optional<ProfileEntity> isVerified = profileRepository.findByPhone(phone);
        if (isVerified.isPresent() && !isVerified.get().isPhoneVerified()) {
            throw new PhoneNotVerifiedException("Phone not verified, just verified phone can reset password");
        }

        if (isVerified.isEmpty()) {
            throw new PhoneNotFound("Phone not found");
        }

        if (code == null) {
            phoneOtpService.sendVerification(phone);
            throw new OtpSentException("OTP sent, please check your phone");
        } else {
            phoneOtpService.checkVerification(phone, code);
            OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
            otpVerificationEntity.setVerificationToken(Generator.randomString64());
            otpVerificationEntity.setUserAuthEntity(userRepository.findByProfilePhone(phone).orElseThrow());
            verificationRepo.save(otpVerificationEntity);
            return otpVerificationEntity.getVerificationToken();
        }

    }

    @Override
    public void changePassword(ChangePasswordDto dto)
            throws DataValidationException, PasswordIncorrectException {
        if (dto.getNewPassword().equals(dto.getOldPassword()))
            throw new DataValidationException("New password must be different from old password");
        validator.checkPassword(dto.getNewPassword());
        userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .ifPresent(userAuthEntity -> {
                            if (bCryptPasswordEncoder.matches(dto.getOldPassword(), userAuthEntity.getPassword())) {
                                userAuthEntity.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
                                userRepository.save(userAuthEntity);
                            } else {
                                throw new PasswordIncorrectException("Old password incorrect");
                            }
                        }
                );
    }

    @Override
    public String forgotPasswordByMail(String email, String code) throws OtpIncorrectException, OtpExpiredException,
            OtpMaxAttemptsExceededException, OtpStillActiveException, OtpSentException, DataValidationException, EmailNotFound {
        validator.checkMail(email);

        // just verified email can reset password
        Optional<ProfileEntity> isVerified = profileRepository.findByEmail(email);
        if (isVerified.isPresent() && !isVerified.get().isEmailVerified()) {
            throw new EmailNotVerifiedException("Email not verified, just verified email can reset password");
        }

        if (isVerified.isEmpty()) {
            throw new EmailNotFound("Email not found");
        }

        if (!mailOtpService.isOtpSent(email)) {
            mailOtpService.sendVerification(email);
            throw new OtpSentException("OTP sent, please check your email");
        } else {
            mailOtpService.checkVerification(email, code);
            OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
            otpVerificationEntity.setVerificationToken(Generator.randomString64());
            otpVerificationEntity.setUserAuthEntity(userRepository.findByProfileEmail(email).orElseThrow());
            verificationRepo.save(otpVerificationEntity);
            return otpVerificationEntity.getVerificationToken();
        }
    }

    @Override
    @Transactional
    public void verifyEmail(String email, String code) // authenticated user can verify email
            throws OtpStillActiveException, OtpMaxAttemptsExceededException, OtpIncorrectException, OtpExpiredException,
            DataValidationException, OtpSentException, OtpNotFoundException {

        validator.checkMail(email);

        Optional<ProfileEntity> checkLinked = profileRepository.findByEmail(email);
        if (checkLinked.isPresent() &&
                !checkLinked.get().getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AuthorizationException("You just can verify your own email");
        }

        if (checkLinked.isPresent() && checkLinked.get().isEmailVerified()) {
            if (checkLinked.get().getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new EmailAlreadyLinked("Email already linked to your account");
            }
            throw new EmailAlreadyLinked("Email already linked to another account");
        }

        if (!mailOtpService.isOtpSent(email)) {
            mailOtpService.sendVerification(email);
            throw new OtpSentException("OTP sent, please check your email");
        } else {
            if (code == null) throw new OtpNotFoundException("OTP not found");
            mailOtpService.checkVerification(email, code);
            profileRepository.findByUserUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                    .ifPresent(profileEntity -> {
                        profileEntity.setEmail(email);
                        profileEntity.setEmailVerified(true);
                        profileRepository.save(profileEntity);
                    });
        }
    }
}
