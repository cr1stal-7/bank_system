package com.demo_bank_v1.rest_controllers;

import com.demo_bank_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RestAuthApiController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public ResponseEntity validateUserEmail(@PathVariable("email")String email){
        // Получение почты
        String userEmail = userRepository.getUserEmail(email);
        String userPassword = null;

        // Проверка почты на валидность
        if(userEmail != null){
            userPassword = userRepository.getUserPassword(email);
            return new ResponseEntity<>(userPassword, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
