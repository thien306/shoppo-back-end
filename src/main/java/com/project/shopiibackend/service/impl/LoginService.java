package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.constant.TokenConstant;
import com.project.shopiibackend.domain.dto.request.LoginRequest;
import com.project.shopiibackend.domain.dto.response.LoginResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.User.User;
import com.project.shopiibackend.domain.mapper.impl.LoginMapper;
import com.project.shopiibackend.domain.utility.TokenUtility;
import com.project.shopiibackend.exception.UserNotFoundException;
import com.project.shopiibackend.repository.IUserRepository;
import com.project.shopiibackend.security.TokenProvider;
import com.project.shopiibackend.service.ILoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LoginService implements ILoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private final AuthenticationManager authenticationManager;

    private final IUserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final LoginMapper loginMapper;

    @Override
    public ResponsePage login(LoginRequest loginRequest) {
        LOGGER.info("LoginService -> login invoked");
        try {

            // Xác thực thông tin đăng nhập
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Tạo mã thông báo
            String token = tokenProvider.generateToken(authentication);
            String email = TokenUtility.getEmailFromJwt(token);


            // Tạo phản hồi đăng nhập
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            currentUser.setRememberToken(token);
            userRepository.save(currentUser);
            LoginResponse loginResponse = loginMapper.entityDto(currentUser, token);

            return ResponsePage.builder()
                    .message("Login successfully!")
                    .status(HttpStatus.OK)
                    .data(loginResponse)
                    .build();
        } catch (Exception e) {
            return ResponsePage.builder()
                    .message("Login failed!")
                    .status(HttpStatus.UNAUTHORIZED)
                    .error(e.getMessage())
                    .build();
        }
    }


    @Override
    public ResponsePage logout(HttpServletRequest request) {
        LOGGER.info("LoginServiceImpl -> logout invoked");
        String bearerToken = request.getHeader("Authorization");
        if (Optional.ofNullable(bearerToken).isEmpty()) {
            return ResponsePage.builder()
                    .message("Token is empty!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String token = bearerToken.substring(TokenConstant.STARTING_LETTER_IN_BEARER_TOKEN);
        String username = TokenUtility.getEmailFromJwt(token);
        User currentUser = userRepository.findByEmail(username).orElse(null);
        if (Optional.ofNullable(currentUser).isPresent()) {
            currentUser.setRememberToken(null);
            userRepository.save(currentUser);
            SecurityContextHolder.clearContext();
            return ResponsePage.builder()
                    .message("Logout successfully!!!")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponsePage.builder()
                    .message("User not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
