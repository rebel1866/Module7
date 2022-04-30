package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RestControllerException;
import com.epam.esm.logic.UserLogic;
import com.epam.esm.security.provider.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final UserLogic userLogic;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager, UserLogic userLogic, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userLogic = userLogic;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            User user = userLogic.findUserByLogin(request.getLogin());
            if (user == null) {
                throw new UsernameNotFoundException("User doesn't exists");
            }
            String token = jwtTokenProvider.createToken(request.getLogin(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("login", request.getLogin());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid login/password combination", HttpStatus.FORBIDDEN);
        }
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
