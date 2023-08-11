package com.ysh.back.config.security.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysh.back.common.dto.LoginUserDTO;
import com.ysh.back.common.exception.BadRequestException;
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
        // TODO : 정지된 유저인지 확인 후
        // 유저 정지일 삭제
        System.out.println("이거다" + username);
        if(userEntityOptional.isEmpty()){
            throw new BadRequestException("아이디 또는 비밀번호 정확히 입력해주세요.");
        }
        return new CustomUserDetails(LoginUserDTO.of(userEntityOptional.get()));
    }
    
}
