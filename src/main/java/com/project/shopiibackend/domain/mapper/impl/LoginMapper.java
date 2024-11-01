package com.project.shopiibackend.domain.mapper.impl;

import com.project.shopiibackend.domain.dto.response.LoginResponse;
import com.project.shopiibackend.domain.entity.User.Role;
import com.project.shopiibackend.domain.entity.User.User;
import com.project.shopiibackend.domain.mapper.ILoginMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class LoginMapper implements ILoginMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginMapper.class);

    private final ModelMapper modelMapper;

    @Override
    public LoginResponse entityDto(User user, String token) {
        LOGGER.info("LoginMapper - entityToDto invoked");
        LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
        Set<Role> roleSet = new HashSet<>(Collections.singletonList(user.getRole()));
        loginResponse.setRoles(roleSet);
        return loginResponse;
    }
}
