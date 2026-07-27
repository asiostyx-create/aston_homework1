package com.example.user_service;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("POST Создание пользователя)")
	void createValidDto() throws Exception {
		var dto = new UpdatedUserDto("Test", "test@test", 20);

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.username").value("Test"))
				.andExpect(jsonPath("$.email").value("test@test"))
				.andExpect(jsonPath("$.age").value(20));

		assertThat(userRepository.existsByEmail("test@test")).isTrue();
	}

	@Test
	@DisplayName("POST Некорректное поле")
	void createInvalidDto() throws Exception {
		long count = userRepository.count();
		var dto = new UpdatedUserDto("   ", "", -5);

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Ошибка валидации"))
				.andExpect(jsonPath("$.errors.username").exists())
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.age").exists());

		assertThat(userRepository.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("POST Дубликат email")
	void createDuplicateEmail() throws Exception {
		User existingUser = new User();
		existingUser.setUsername("Exist");
		existingUser.setEmail("exist@test");
		existingUser.setAge(30);
		userRepository.save(existingUser);
		var duplicateDto = new UpdatedUserDto("Test", "exist@test", 21);

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(duplicateDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", containsString("уже существует")));
	}

	@Test
	@DisplayName("GET Чтение записи из БД")
	void getByIdExistingUser() throws Exception {
		User user = new User();
		user.setUsername("Test");
		user.setEmail("test@test");
		user.setAge(20);
		User savedUser = userRepository.save(user);

		mockMvc.perform(get("/users/{id}", savedUser.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(savedUser.getId()))
				.andExpect(jsonPath("$.username").value("Test"))
				.andExpect(jsonPath("$.email").value("test@test"))
				.andExpect(jsonPath("$.age").value(20));
	}

	@Test
	@DisplayName("GET Не найден")
	void getByIdNotFound() throws Exception {
		mockMvc.perform(get("/users/{id}", 10000))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Не найдено"))
				.andExpect(jsonPath("$.message").value("Пользователя с id 10000 не существует"));
	}

	@Test
	@DisplayName("PUT Обновление")
	void updateValidDto() throws Exception {
		User user = new User();
		user.setUsername("OldTest");
		user.setEmail("old@test");
		user.setAge(25);
		User savedUser = userRepository.save(user);

		var updateDto = new UpdatedUserDto("NewTest", "new@test", 30);

		mockMvc.perform(put("/users/{id}", savedUser.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(savedUser.getId()))
				.andExpect(jsonPath("$.username").value("NewTest"))
				.andExpect(jsonPath("$.email").value("new@test"))
				.andExpect(jsonPath("$.age").value(30));

		User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
		assertThat(updatedUser.getUsername()).isEqualTo("NewTest");
		assertThat(updatedUser.getEmail()).isEqualTo("new@test");
		assertThat(updatedUser.getAge()).isEqualTo(30);
	}

	@Test
	@DisplayName("PUT Ошибка валидации при обновлении")
	void updateInvalidDto() throws Exception {
		User user = new User();
		user.setUsername("Test");
		user.setEmail("test@test");
		user.setAge(20);
		User savedUser = userRepository.save(user);

		var invalidDto = new UpdatedUserDto("   ", "invalid-test", -10);

		mockMvc.perform(put("/users/{id}", savedUser.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Ошибка валидации"))
				.andExpect(jsonPath("$.errors.username").exists())
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.age").exists());
	}

	@Test
	@DisplayName("PUT Обновление несуществующего пользователя")
	void updateNotFound() throws Exception {
		int nonExistentId = 10001;
		var updateDto = new UpdatedUserDto("Test", "test@test", 30);

		mockMvc.perform(put("/users/{id}", nonExistentId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Не найдено"))
				.andExpect(jsonPath("$.message", containsString(String.valueOf(nonExistentId))));
	}

	@Test
	@DisplayName("DELETE Удаление")
	void deleteExistingUser() throws Exception {
		User user = new User();
		user.setUsername("ToDelete");
		user.setEmail("delete@test.com");
		user.setAge(22);
		User savedUser = userRepository.save(user);

		mockMvc.perform(delete("/users/{id}", savedUser.getId()))
				.andExpect(status().isNoContent());

		assertThat(userRepository.existsById(savedUser.getId())).isFalse();
	}

	@Test
	@DisplayName("DELETE Удаление несуществующего пользователя")
	void deleteNotFound() throws Exception {
		int nonExistentId = 10009;
		mockMvc.perform(delete("/users/{id}", nonExistentId))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Не найдено"))
				.andExpect(jsonPath("$.message", containsString(String.valueOf(nonExistentId))));
	}

	@Test
	@DisplayName("GET Получение всех пользователей")
	void getAllUsers() throws Exception {
		long size = userRepository.count();
		User user1 = new User();
		user1.setUsername("Test1");
		user1.setEmail("testiviy@test");
		user1.setAge(25);

		User user2 = new User();
		user2.setUsername("That One Cool Dude");
		user2.setEmail("thatonce@cool.dude");
		user2.setAge(30);

		userRepository.saveAll(List.of(user1, user2));

		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize((int) (size + 2))))
				.andExpect(jsonPath("$[*].email", hasItem("testiviy@test")))
				.andExpect(jsonPath("$[*].email", hasItem("thatonce@cool.dude")));
	}
}