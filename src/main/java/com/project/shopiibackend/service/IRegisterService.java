package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.request.RegisterRequest;
import com.project.shopiibackend.domain.dto.response.ResponsePage;

public interface IRegisterService {

    ResponsePage register(RegisterRequest registerRequest);
}
