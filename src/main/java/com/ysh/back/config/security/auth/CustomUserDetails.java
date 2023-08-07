package com.ysh.back.config.security.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private User user;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class User{
        private Long idx;
        private String email;
        private String password;
        private List<String> roleList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.roleList
        .stream()
        .map(role -> (GrantedAuthority) () -> role)
        .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
