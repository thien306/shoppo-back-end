package com.project.shopiibackend.domain.dto.response;

import com.project.shopiibackend.domain.entity.User.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {

    String avatar;

    String username;

    String email;

    String fullName;

    String token;

    Set<Role> roles;

}
