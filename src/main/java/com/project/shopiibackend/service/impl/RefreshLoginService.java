package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.constant.TokenConstant;
import com.project.shopiibackend.domain.dto.response.LoginResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.User.User;
import com.project.shopiibackend.domain.mapper.impl.LoginMapper;
import com.project.shopiibackend.domain.utility.TokenUtility;
import com.project.shopiibackend.repository.IUserRepository;
import com.project.shopiibackend.service.IRefreshLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class RefreshLoginService implements IRefreshLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshLoginService.class);

    private final IUserRepository userRepository;

    private final LoginMapper loginMapper;


    @Override
    public ResponsePage refreshLogin(HttpServletRequest request) {
        LOGGER.info("RefreshLoginServiceImpl -> refreshLogin invoked");
        String authToken = request.getHeader("Authorization");
        if (Optional.ofNullable(authToken).isEmpty()) {
            return ResponsePage.builder()
                    .message("Token is empty!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String token = authToken.substring(TokenConstant.STARTING_LETTER_IN_BEARER_TOKEN);
        String email = TokenUtility.getEmailFromJwt(token);
        User currentUser = userRepository.findByEmail(email).orElse(null);

        if (Optional.ofNullable(currentUser).isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String refreshToken = TokenUtility.generateToken(authentication);
            currentUser.setRememberToken(refreshToken);

            userRepository.save(currentUser);
            LoginResponse loginResponse = loginMapper.entityDto(currentUser, token);
            return ResponsePage.builder()
                    .message("Login successfully!")
                    .status(HttpStatus.OK)
                    .data(loginResponse)
                    .build();
        } else {
            return ResponsePage.builder()
                    .message("User not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
