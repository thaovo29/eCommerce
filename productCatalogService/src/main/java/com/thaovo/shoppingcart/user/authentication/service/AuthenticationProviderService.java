package com.thaovo.shoppingcart.user.authentication.service;

import com.thaovo.shoppingcart.user.authentication.exceptions.AccountDisabledException;
import com.thaovo.shoppingcart.user.authentication.exceptions.AccountLockedNotTimeout;
import com.thaovo.shoppingcart.user.authentication.model.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationProviderService implements AuthenticationProvider {
    private final JpaUserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationProviderService(JpaUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!userDetails.getUser().isEnabled()) {
            var disabledUntil = Optional.ofNullable(userDetails.getUser().getDisabledUntil());
            if (disabledUntil.isEmpty()) {
                throw new AccountDisabledException("Account is disabled, please contact admin for more information");
            }
            if (disabledUntil.get().isAfter(LocalDateTime.now())) {
                throw new AccountLockedNotTimeout("Account is disabled, please try again later until "
                        + userDetails.getUser().getDisabledUntil());
            }
        }
        if (bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
