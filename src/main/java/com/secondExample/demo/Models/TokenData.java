package com.secondExample.demo.Models;

import java.time.LocalDateTime;

public class TokenData {
    private final String token;
    private final LocalDateTime creationTime;
    private boolean isActive;

    public TokenData(String token, LocalDateTime creationTime) {
        this.token = token;
        this.creationTime = creationTime;
        this.isActive = true; // Токен активен сразу после создания
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
