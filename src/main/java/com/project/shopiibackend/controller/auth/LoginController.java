package com.project.shopiibackend.controller.auth;

import com.project.shopiibackend.domain.dto.request.LoginRequest;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.service.ILoginService;
import com.project.shopiibackend.service.IRefreshLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    private final IRefreshLoginService refreshLoginService;

    @PostMapping("/login")
    public ResponseEntity<ResponsePage> login(HttpServletRequest request, @Valid @RequestBody(required = false) LoginRequest loginRequest) {
        ResponsePage responsePage;
        if (Optional.ofNullable(loginRequest).isEmpty()) {
            responsePage = refreshLoginService.refreshLogin(request);
        } else {
            responsePage = loginService.login(loginRequest);
        }
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    //Chỉ để kiểm tra bởi người đưa thư, tính năng này chỉ sử dụng khi mã thông báo hết hạn
    // => Dịch vụ này sẽ được gọi tại TokenProvider khi bắt mã thông báo hết hạn
    @PostMapping("/refresh-login")
    public ResponseEntity<ResponsePage> refreshLogin(HttpServletRequest request) {
        ResponsePage responsePage = refreshLoginService.refreshLogin(request);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponsePage> logout(HttpServletRequest request) {
        return new ResponseEntity<>(loginService.logout(request), HttpStatus.OK);
    }

}
