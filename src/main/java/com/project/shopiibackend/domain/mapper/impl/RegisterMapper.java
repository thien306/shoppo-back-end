package com.project.shopiibackend.domain.mapper.impl;

import com.project.shopiibackend.domain.dto.request.RegisterRequest;
import com.project.shopiibackend.domain.dto.response.RegisterResponse;
import com.project.shopiibackend.domain.entity.User.Role;
import com.project.shopiibackend.domain.entity.User.User;
import com.project.shopiibackend.domain.mapper.IRegisterMapper;
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
public class RegisterMapper implements IRegisterMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMapper.class);

    private final ModelMapper modelMapper;

    @Override
    public RegisterResponse entityDto(User user) {
        LOGGER.info("RegisterMapper -> entityDto invoked");
        RegisterResponse registerResponse = modelMapper.map(user, RegisterResponse.class);
        Set<Role> roleSet = new HashSet<>(Collections.singletonList(user.getRole()));
        registerResponse.setRoles(roleSet);
        return registerResponse;
    }

    @Override
    public User dtoToEntity(RegisterRequest registerRequest) {
        LOGGER.info("RegisterMapper -> dtoToEntity invoked");
        return modelMapper.map(registerRequest, User.class);
    }
}
