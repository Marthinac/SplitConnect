package com.marthina.splitconnect.security.auth;

import com.marthina.splitconnect.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    @Getter
    private final Long id;
    private final String email;
    private final String password;

    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    @Override public String getUsername() {
        return email;
    }
    @Override public String getPassword() {
        return password;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
