package com.example.user_service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto).toList();
    }

    public UserDto findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id " + id + " не существует"));
        return toDto(user);
    }

    @Transactional
    public UserDto create(UpdatedUserDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Пользователь с email " + dto.email() + " уже существует");
        }
        User user = new User(dto.username(), dto.email(), dto.age());
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Transactional
    public UserDto update(Integer id, UpdatedUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id " + id + " не существует"));

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email " + dto.email() + " уже занят другим пользователем");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setAge(dto.age());

        return toDto(user);
    }

    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAge(),
                        user.getCreatedAt()
                );
    }
}