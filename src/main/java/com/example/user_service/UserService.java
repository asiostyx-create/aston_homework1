package com.example.user_service;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserService(UserRepository userRepository, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
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

        UserEvent userEvent = new UserEvent(savedUser.getEmail(), savedUser.getUsername());
        CompletableFuture<SendResult<String, UserEvent>> future = kafkaTemplate
                .send("user-created-events-topic", userEvent);
        future.whenComplete((result, exception) -> {
            if(exception != null) {
                log.error("Сообщение не отправлено: {}", exception.getMessage());
            } else {
                log.info("Сообщение отправлено: {}", userEvent.email());
            }
        });

        return toDto(savedUser);
    }

    @Transactional
    public UserDto update(Integer id, UpdatedUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id " + id + " не существует"));

        if (userRepository.existsByEmail(dto.email()) && !user.getEmail().equals(dto.email())) {
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

        UserEvent userEvent = new UserEvent(findById(id).getEmail(), findById(id).getUsername());

        userRepository.deleteById(id);

        CompletableFuture<SendResult<String, UserEvent>> future = kafkaTemplate
                .send("user-deleted-events-topic", userEvent);
        future.whenComplete((result, exception) -> {
            if(exception != null) {
                log.error("Сообщение не отправлено: {}", exception.getMessage());
            } else {
                log.info("Сообщение отправлено: {}", result.getRecordMetadata());
            }
        });
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