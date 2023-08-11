package com.ysh.back.config.security.auth;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ysh.back.common.dto.LoginUserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private LoginUserDTO loginUserDTO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return loginUserDTO.getUser().getRoleList()
                .stream()
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }

    @Override
    public String getPassword() {
        return loginUserDTO.getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return loginUserDTO.getUser().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        if(loginUserDTO.getUser().getDeletedAt() != null){
            return LocalDateTime.now().isAfter(loginUserDTO.getUser().getDeletedAt()) ? true : false;
        }
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(loginUserDTO.getUser().getSuspendUntil() != null){
            if(LocalDateTime.now().isAfter(loginUserDTO.getUser().getSuspendUntil())){
                // 정지일, 정지사유 삭제해야함
                return true;
            }
            return false;
        }
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
