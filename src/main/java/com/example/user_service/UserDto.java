package com.example.user_service;

import java.time.LocalDateTime;

public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;

    public UserDto(Integer id, String username, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
