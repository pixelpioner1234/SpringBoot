package com.secondExample.demo.Controllers;

import com.secondExample.demo.Models.TokenData;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class TokenController {

    private final List<TokenData> tokens = new ArrayList<>();

    @PostMapping("/register")
    public TokenData register() {

        String token = UUID.randomUUID().toString();
        LocalDateTime creationTime = LocalDateTime.now();

        TokenData tokenData = new TokenData(token, creationTime);
        tokens.add(tokenData);

        return tokenData;
    }





    @GetMapping("/getAllTokens")
    public List<TokenData> getAllTokens() {

        return tokens;
    }



    @GetMapping("/isTokenValid")
    public boolean isTokenValid(@RequestParam String token) {
        LocalDateTime now = LocalDateTime.now();

        for (TokenData tokenData : tokens) {
            if (tokenData.getToken().equals(token)) {

                return tokenData.getCreationTime().plusMinutes(1).isAfter(now);
            }
        }

        return false;
    }




}