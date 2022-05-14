package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RestControllerException;
import com.epam.esm.logic.UserLogic;
import com.epam.esm.security.provider.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    private final UserLogic userLogic;

    public AuthenticationRestController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        Map<String, String> response = userLogic.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> signup(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RestControllerException("messageCode11", "errorCode=3", bindingResult);
        }
        UserDto addedUser = userLogic.signUp(userDto);
        String responseMessage = "Welcome " + addedUser.getUserName() + " " + addedUser.getUserSurname() + " !";
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }
}
