package com.project.shopiibackend.security;

import com.project.shopiibackend.domain.entity.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Setter
public class UserPrincipal implements UserDetails {

    private String userName;

    private String password;

    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        String roleName = user.getRole().getName();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (Optional.ofNullable(roleName).isPresent()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleName);
            grantedAuthorities.add(grantedAuthority);
        }
        return new UserPrincipal(user.getUsername(), user.getPassword(), user.getEmail(), grantedAuthorities);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
