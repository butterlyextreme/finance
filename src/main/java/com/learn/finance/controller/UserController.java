package com.learn.finance.controller;


import com.learn.finance.data.entity.UsersEntity;
import com.learn.finance.model.FinanceUser;
import com.learn.finance.model.LoginRequest;
import com.learn.finance.model.LoginResponse;
import com.learn.finance.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<UsersEntity> register(@RequestBody FinanceUser financeUser) {
       return ResponseEntity.ok(usersService.signup(financeUser));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        var loginResponse = usersService.authenticate(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
