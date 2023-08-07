package com.ysh.back.config.security.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails.User;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(username);

        if(userEntityOptional.isEmpty()){
            throw new BadRequestException("아이디 또는 비밀번호 정확히 입력해주세요.");
        }

        UserEntity userEntity = userEntityOptional.get();

        List<String> roleList = userEntity.getRoleEntityList()
        .stream()
        .map(roleEntity -> roleEntity.getClass().getName())
        .toList();

        User user = CustomUserDetails.User.builder()
        .idx(userEntity.getIdx())
        .email(userEntity.getEmail())
        .password(userEntity.getPassword())
        .roleList(roleList)
        .build();

        return new CustomUserDetails(user);
    }
    
}
