package com.project.shopiibackend.domain.dto.response;

import com.project.shopiibackend.domain.entity.User.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {

    Long id;

    String fullName;

    String username;

    String email;

    String avatar;

    Set<Role> roles;
}
