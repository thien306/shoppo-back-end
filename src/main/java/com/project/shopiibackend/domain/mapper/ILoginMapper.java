package com.project.shopiibackend.domain.mapper;

import com.project.shopiibackend.domain.dto.response.LoginResponse;
import com.project.shopiibackend.domain.entity.User.User;

public interface ILoginMapper {

    LoginResponse entityDto(User user, String token);
}
