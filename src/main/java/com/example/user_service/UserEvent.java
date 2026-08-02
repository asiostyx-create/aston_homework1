package com.example.user_service;

public record UserEvent(
    String email,
    String username
) {}
