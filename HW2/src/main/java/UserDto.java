package com.example.user_service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(itemRelation = "user", collectionRelation = "users")
@Schema(description = "Сущность пользователя")
public class UserDto extends RepresentationModel<UserDto> {
    @Schema(description = "Уникальный идентификатор")
    private Integer id;
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "Email пользователя")
    private String email;
    @Schema(description = "Возраст пользователя")
    private Integer age;
    @Schema(description = "Время создания пользователя")
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
