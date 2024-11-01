package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.entity.User.User;
import com.project.shopiibackend.repository.IUserRepository;
import com.project.shopiibackend.security.UserPrincipal;
import com.project.shopiibackend.service.IUserPrincipalService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPrincipalService implements IUserPrincipalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPrincipalService.class);

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.info("UserPrincipalService -> loadUserByUsername invoked");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username or email: " + email));
        return UserPrincipal.create(user);
    }


}
