package com.learn.finance.service;

import com.learn.finance.data.entity.UsersEntity;
import com.learn.finance.data.repository.UsersRepository;
import com.learn.finance.model.FinanceUser;
import com.learn.finance.model.LoginRequest;
import com.learn.finance.model.LoginResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UsersService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UsersEntity signup(FinanceUser user) {
      UsersEntity usersEntity = UsersEntity.builder().fullName(user.fullName())
               .email(user.email())
               .password(passwordEncoder.encode(user.password())).build();
        return userRepository.save(usersEntity);
    }

    public LoginResponse authenticate(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )

        );

        var user = (UsersEntity) authentication.getPrincipal();
        String token= tokenService.generateToken(authentication);
        return new LoginResponse(token, user.getFullName(), user.getEmail());
    }
}
