package com.example.user_service;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto userDto) {
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("Создание"),
                linkTo(methodOn(UserController.class).updateUser(userDto.getId(), null)).withRel("Обновление"),
                linkTo(methodOn(UserController.class).deleteUser(userDto.getId())).withRel("Удаление"),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("Все пользователи")
        );
    }
}