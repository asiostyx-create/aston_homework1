package com.example.user_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "user-service API", description = "API управления пользователя")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает коллекцию всех пользователей")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Called getAllUsers");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id", description = "Возвращает пользователя, если он найден в БД")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<UserDto> getUserById(@Parameter(description = "id пользователя")
            @PathVariable Integer id) {
        log.info("Called getUserById: id={}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Содать пользователя", description = "Создает пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UpdatedUserDto dto) {
        log.info("Called createUser");
        UserDto createdUser = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя по id", description = "Обновляет пользователя, если он найден в БД")
    @ApiResponse(responseCode = "200", description = "Пользователь обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "id пользователя")
            @PathVariable Integer id,
            @Valid @RequestBody UpdatedUserDto dto) {
        log.info("Called updateUser: id={}", id);
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Содать пользователя", description = "Создает пользователя")
    @ApiResponse(responseCode = "204", description = "Пользователь удален")
    @ApiResponse(responseCode = "404", description = "Ошибка валидации")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "id пользователя")
            @PathVariable Integer id) {
        log.info("Called deleteUser: id={}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
