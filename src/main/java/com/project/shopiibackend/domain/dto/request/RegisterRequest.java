package com.project.shopiibackend.domain.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    String fullName;

    String username;

    String password;

    String confirmPassword;

    String email;

    String phone;

    String avatar;
}
