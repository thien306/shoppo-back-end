package com.project.shopiibackend.domain.mapper;

import com.project.shopiibackend.domain.dto.request.RegisterRequest;
import com.project.shopiibackend.domain.dto.response.RegisterResponse;
import com.project.shopiibackend.domain.entity.User.User;

public interface IRegisterMapper {

    RegisterResponse entityDto(User user);

    User dtoToEntity(RegisterRequest registerRequest);
}
