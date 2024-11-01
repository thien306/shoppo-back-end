package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.request.LoginRequest;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import jakarta.servlet.http.HttpServletRequest;

public interface ILoginService {

    ResponsePage login(LoginRequest loginRequest);

    ResponsePage logout(HttpServletRequest request);
}
