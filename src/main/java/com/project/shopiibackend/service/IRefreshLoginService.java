package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.response.ResponsePage;
import jakarta.servlet.http.HttpServletRequest;

public interface IRefreshLoginService {

    ResponsePage refreshLogin(HttpServletRequest request);


}
