package com.example.user_service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//попытка через record

public record UpdatedUserDto(

        @NotBlank(message = "Имя пользователя не должно быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
        String username,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotNull(message = "Укажите возраст")
        @Min(value = 0, message = "Возраст не может быть отрицательным")
        @Max(value = 150, message = "Укажите реальный возраст")
        Integer age

) {}